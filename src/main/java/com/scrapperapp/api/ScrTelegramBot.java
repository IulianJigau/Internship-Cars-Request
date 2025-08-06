package com.scrapperapp.api;

import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.scrapperapp.app.Defaults;
import com.scrapperapp.logic.CarDataRetriever;
import com.scrapperapp.model.CarsData;

public class ScrTelegramBot extends TelegramWebhookBot {

    private final Defaults defaults;

    public ScrTelegramBot(Defaults defaults) {
        super(defaults.BOT_TOKEN);

        this.defaults = defaults;
    }

    @Override
    public String getBotUsername() {
        return defaults.BOT_USERNAME;
    }

    @Override
    public String getBotPath() {
        return defaults.BOT_PATH;
    }

    @Override
    public SendMessage onWebhookUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message == null) {
            return null;
        }
        String chatId = message.getChatId().toString();
        String messageText = message.getText();
        if (messageText == null) {
            return null;
        }

        String response;
        switch (messageText.trim().toLowerCase()) {
            case "/data":
                CarsData carsData = CarDataRetriever.retrieve(defaults);

                if (!carsData.info.isEmpty()) {
                    response = carsData.toString();
                } else {
                    response = "No car data available to print";
                }
                break;
            default:
                response = "Unrecognized command. Try /data to get the latest cars.";
                break;
        }
        return new SendMessage(chatId, response);
    }
}