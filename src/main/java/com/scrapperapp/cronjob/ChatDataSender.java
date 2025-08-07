package com.scrapperapp.cronjob;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.scrapperapp.api.ScrTelegramBot;
import com.scrapperapp.app.Defaults;
import com.scrapperapp.logic.CarDataRetriever;
import com.scrapperapp.model.CarsData;

public class ChatDataSender implements Job {
    private static final Logger logger = Logger.getLogger(ChatDataSender.class.getName());

    private static final List<String> chatIds = new ArrayList<>();
    private static ScrTelegramBot bot;
    private static Defaults defaults;

    public static void configure(ScrTelegramBot b, Defaults d) {
        bot = b;
        defaults = d;
    }

    public static int getCount() {
        return chatIds.size();
    }

    public static void addChat(String chatId) {
        if (!chatIds.contains(chatId)) {
            chatIds.add(chatId);
            logger.log(Level.INFO, "A new chat was added to cronjob");
        }
    }

    public static void removeChat(String chatId) {
        chatIds.remove(chatId);
        logger.log(Level.INFO, "A chat was removed from the cronjob");
    }

    @Override
    public void execute(JobExecutionContext context) {
        logger.log(Level.INFO, "Cronjob execution started");

        String response;
        CarsData carsData;
        SendMessage message;

        for (String chatId : chatIds) {
            carsData = CarDataRetriever.retrieve(defaults);

            if (!carsData.info.isEmpty()) {
                response = String.format("Here's your scheduled data display.%n%n%s%nType /stop to unsubscribe",
                        carsData.toString());
            } else {
                logger.log(Level.WARNING, "Car data request returned empty");
                response = "No car data available to print";
            }

            message = new SendMessage(chatId, response);

            try {
                bot.execute(message);
            } catch (TelegramApiException e) {
                logger.log(Level.SEVERE, "Cronjob could not send a message", e);
            }
        }
    }
}
