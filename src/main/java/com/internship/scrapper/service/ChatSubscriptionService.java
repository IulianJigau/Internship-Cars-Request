package com.internship.scrapper.service;

import com.internship.scrapper.model.CarsData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Slf4j
@Component
public class ChatSubscriptionService {
    private static final Logger logger = Logger.getLogger(ChatSubscriptionService.class.getName());

    private static final List<String> chatIds = new ArrayList<>();
    private final CarDataService carDataService;
    private final ScrapperTelegramBot bot;

    public ChatSubscriptionService(CarDataService carDataService, ScrapperTelegramBot bot) {
        this.carDataService = carDataService;
        this.bot = bot;
    }

    public static int getCount() {
        return chatIds.size();
    }

    public static void addChat(String chatId) {
        if (!chatIds.contains(chatId)) {
            chatIds.add(chatId);
            log.info("A new chat was added to cronjob");
        }
    }

    public static void removeChat(String chatId) {
        chatIds.remove(chatId);
        log.info("A chat was removed from the cronjob");
    }

    @Scheduled(fixedRate = 5000)
    public void execute() {
        logger.log(Level.INFO, "Cronjob execution started");

        for (String chatId : chatIds) {
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
            SendMessage message = new SendMessage(chatId, body);

            try {
                bot.execute(message);
            } catch (TelegramApiException e) {
                logger.log(Level.SEVERE, "Cronjob could not send a message", e);
            }
        }
    }
}
