package com.internship.scrapper.service;

import com.internship.scrapper.config.BotConfig;
import com.internship.scrapper.model.CarsData;

import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ScrapperTelegramBot extends TelegramWebhookBot {

    private final BotConfig botConfig;
    private final CarDataService carDataService;

    public ScrapperTelegramBot(
            BotConfig botConfig,
            CarDataService carDataService
    ) {
        super(botConfig.getToken());
        this.botConfig = botConfig;
        this.carDataService = carDataService;
    }

    @Override public String getBotUsername() { return botConfig.getName(); }
    @Override public String getBotPath()     { return botConfig.getPath(); }

    @Override
    public SendMessage onWebhookUpdateReceived(Update update) {
        if (update == null || !update.hasMessage()) return null;
        Message message = update.getMessage();
        if (!message.hasText()) return null;

        String chatId = message.getChatId().toString();
        String text = message.getText().trim().toLowerCase();

        return switch (text) {
            case "/data" -> {
                String body;
                CarsData carsData = carDataService.retrieve();
                if(carsData != null) {
                    body = !carsData.info.isEmpty()
                            ? carsData.toString()
                            : "No car data available to print";
                }else{
                    body = "There has been an error on extracting data";
                    log.error("There has been an error on extracting data");
                }
                yield new SendMessage(chatId, body);
            }
            case "/run" -> {
                ChatSubscriptionService.addChat(chatId);
                yield new SendMessage(chatId, "Cronjob was set");
            }
            case "/stop" -> {
                ChatSubscriptionService.removeChat(chatId);
                yield new SendMessage(chatId, "Cronjob was removed");
            }
            default -> new SendMessage(chatId, "Unrecognized command. Try /data to get the latest cars.");
        };
    }
}