package com.example.demo.server.service.site;

import com.example.demo.api.config.BotConfig;
import com.example.demo.model.ApiCredential;
import com.example.demo.model.dto.ItemDto;
import com.example.demo.repository.ApiCredentialRepository;
import com.example.demo.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import java.util.HashMap;
import java.util.Map;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SiteServiceImpl implements SiteService{
    private final BotConfig botConfig;
    private final ApiCredentialRepository credentialRepository;
    private final ItemRepository repository;

    @Override
    public List<ItemDto> getAll() {
        return repository.findAll().stream()
                .map(ItemDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public void saveOptionsBot(String botName, String botToken) {
        botConfig.updateBotConfig(botName, botToken);
    }

    @Override
    public List<String> getAllCategory() {
        return repository.findDistinctCategories();
    }

    @Override
    public void setBank(String clientId, String clientSecret, String id_name) {
        ApiCredential apiCredential = new ApiCredential();
        apiCredential.setBankName(id_name);
        apiCredential.setClientId(clientId);
        apiCredential.setClientSecret(clientSecret);
        credentialRepository.save(apiCredential);
    }

    @Override
    public String generateQr(int sum, String name) {
        ApiCredential creds = credentialRepository.findByBankName(name)
                .orElseThrow(() -> new RuntimeException("Банк не найден: " + "SBER"));

        String clientId = creds.getClientId();
        String clientSecret = creds.getClientSecret();

        String qrLink = requestQrFromBank(sum, clientId, clientSecret);

        return qrLink;
    }

    private String requestQrFromBank(int sum, String clientId, String clientSecret) {
        RestTemplate restTemplate = new RestTemplate();
        String tokenUrl = "https://api.sberbank.ru/ru/prod/tokens/v2/oauth";

        HttpHeaders tokenHeaders = new HttpHeaders();
        tokenHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        tokenHeaders.setBasicAuth(clientId, clientSecret);

        MultiValueMap<String, String> tokenBody = new LinkedMultiValueMap<>();
        tokenBody.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(tokenBody, tokenHeaders);
        ResponseEntity<Map> tokenResponse = restTemplate.exchange(tokenUrl, HttpMethod.POST, tokenRequest, Map.class);

        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Ошибка получения токена от Сбера: " + tokenResponse.getStatusCode());
        }

        String accessToken = (String) tokenResponse.getBody().get("access_token");
        String orderUrl = "https://api.sberbank.ru/ru/prod/order/v1/creation";

        HttpHeaders orderHeaders = new HttpHeaders();
        orderHeaders.setContentType(MediaType.APPLICATION_JSON);
        orderHeaders.setBearerAuth(accessToken);

        Map<String, Object> orderBody = new HashMap<>();
        orderBody.put("amount", sum * 100);
        orderBody.put("currency", "RUB");
        orderBody.put("description", "Оплата заказа на профуршет.рф");
        orderBody.put("returnUrl", "https://профуршет.рф/payment/success");

        HttpEntity<Map<String, Object>> orderRequest = new HttpEntity<>(orderBody, orderHeaders);
        ResponseEntity<Map> orderResponse = restTemplate.exchange(orderUrl, HttpMethod.POST, orderRequest, Map.class);

        if (!orderResponse.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Ошибка создания заказа: " + orderResponse.getStatusCode());
        }

        Map responseBody = orderResponse.getBody();
        String qrUrl = (String) responseBody.get("qrUrl");

        if (qrUrl == null) {
            throw new RuntimeException("QR ссылка отсутствует в ответе от банка.");
        }

        return qrUrl;
    }



}
