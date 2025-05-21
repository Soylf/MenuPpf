package com.example.demo.server.service.telegram;

import com.example.demo.model.Item;
import com.example.demo.model.OrderUser;
import com.example.demo.model.dto.FeedbackFormDto;
import com.example.demo.repository.OrderUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TelegramServiceImpl implements TelegramService{
    @Setter
    private List<Item> items = new ArrayList<>();
    @Setter
    private FeedbackFormDto feedbackFormDto;
    private final Integer key = 1;
    private final OrderUserRepository repository;

    private String buildOrderMessage() {
        StringBuilder message = new StringBuilder();

        message.append("Номер заказа: ").append(key).append("\n");
        message.append("Из ").append(feedbackFormDto.getStatus()).append("\n");
        message.append("Имя клиента: ").append(feedbackFormDto.getUserName()).append("\n");
        message.append("Возраст: ").append(feedbackFormDto.getAge()).append("\n");
        message.append("Контактные данные: ").append(feedbackFormDto.getRelations()).append("\n");
        message.append("Комментарии к заказу: ").append(feedbackFormDto.getComment()).append("\n");
        message.append("<------------------------------->\n");

        int index = 1;
        for (Item item : items) {
            try {
                int pieces = Integer.parseInt(item.getPieces());
                int quantity = item.getQuantity();
                int totalPieces = pieces * quantity;

                message.append("-").append(index++).append(" ")
                        .append(item.getName()).append(" (").append(item.getPieces()).append(" шт.)")
                        .append(" — всего = ").append(totalPieces).append("\n");
            } catch (NumberFormatException e) {
                message.append("-").append(index++).append(" ")
                        .append(item.getName()).append(" — ❌ ошибка: неверное значение \"pieces\" (").append(item.getPieces()).append(")\n");
            }
        }

        return message.toString();
    }


    @Override
    public void saveMessage() {
        String message = buildOrderMessage();
        OrderUser orderUser = new OrderUser();
        orderUser.setKey(key);
        orderUser.setMessage(message);
        repository.save(orderUser);
    }

    @Override
    public List<String> getMessages() {
        List<OrderUser> orders = repository.findAll();
        List<String> messages = new ArrayList<>();
        for (OrderUser order : orders) {
            messages.add(order.getMessage());
        }
        return messages;
    }

    @Override
    public void deleteOrderUser(int key) {
        repository.deleteByKey(key);
    }
}