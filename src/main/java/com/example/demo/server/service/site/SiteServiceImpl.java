package com.example.demo.server.service.site;

import com.example.demo.api.config.BotConfig;
import com.example.demo.model.ApiCredential;
import com.example.demo.model.dto.ItemDto;
import com.example.demo.repository.ApiCredentialRepository;
import com.example.demo.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
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
    public void setBank(String clientId, String clientSecret, String idName, String memberId, String idQr) {
        ApiCredential apiCredential = new ApiCredential();
        apiCredential.setBankName(idName);
        apiCredential.setClientId(clientId);
        apiCredential.setClientSecret(clientSecret);
        apiCredential.setMemberId(memberId);
        apiCredential.setIdQr(idQr);
        credentialRepository.save(apiCredential);
    }

    @Override
    public String generateQr(int sum, String bankName) {
        ApiCredential creds = credentialRepository.findByBankName(bankName)
                .orElseThrow(() -> new RuntimeException("Банк не найден: " + bankName));

        return requestQrFromBank(sum, creds);
    }

    private String requestQrFromBank(int sum, ApiCredential creds) {
        RestTemplate rest = new RestTemplate();
        String tokenUrl = "https://mc.api.sberbank.ru:443/prod/tokens/v3/oauth";
        HttpHeaders tokenHeaders = new HttpHeaders();
        tokenHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        tokenHeaders.setBasicAuth(creds.getClientId(), creds.getClientSecret());
        tokenHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));

        MultiValueMap<String, String> tokenBody = new LinkedMultiValueMap<>();
        tokenBody.add("grant_type", "client_credentials");
        tokenBody.add("scope", "https://api.sberbank.ru/qr/order.create");

        HttpEntity<MultiValueMap<String, String>> tokenReq =
                new HttpEntity<>(tokenBody, tokenHeaders);
        ResponseEntity<Map> tokenRes = rest.exchange(
                tokenUrl, HttpMethod.POST, tokenReq, Map.class);

        if (!tokenRes.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Ошибка получения токена: " + tokenRes.getStatusCode());
        }

        String accessToken = (String) tokenRes.getBody().get("access_token");

        String orderUrl = "https://mc.api.sberbank.ru:443/prod/qr/order/v3/creation";
        HttpHeaders orderHeaders = new HttpHeaders();
        orderHeaders.setContentType(MediaType.APPLICATION_JSON);
        orderHeaders.setBearerAuth(accessToken);
        orderHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Map<String, Object>> orderReq = getMapHttpEntity(sum, creds, orderHeaders);
        ResponseEntity<Map> orderRes = rest.exchange(
                orderUrl, HttpMethod.POST, orderReq, Map.class);

        if (!orderRes.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Ошибка создания заказа: " + orderRes.getStatusCode());
        }

        String qrUrl = (String) orderRes.getBody().get("qrUrl");
        if (qrUrl == null) {
            throw new RuntimeException("QR ссылка отсутствует в ответе.");
        }

        return qrUrl;
    }

    private static @NotNull HttpEntity<Map<String, Object>> getMapHttpEntity(int sum, ApiCredential creds, HttpHeaders orderHeaders) {
        Map<String, Object> orderBody = new HashMap<>();
        orderBody.put("amount", sum * 100);
        orderBody.put("currency", "RUB");
        orderBody.put("description", "Оплата заказа на профуршет.рф");
        orderBody.put("returnUrl", "https://профуршет.рф/payment/success");

        // важные параметры из базы
        orderBody.put("member_id", creds.getMemberId());
        orderBody.put("id_qr", creds.getIdQr());

        return new HttpEntity<>(orderBody, orderHeaders);
    }

}
