package com.internship.scrapper.controller;

import com.internship.scrapper.service.ScrapperTelegramBot;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.logging.Logger;
import java.util.logging.Level;

@RestController
@RequestMapping("/webhook")
public class WebhookController {
    private static final Logger logger = Logger.getLogger(WebhookController.class.getName());
    private final ScrapperTelegramBot bot;

    public WebhookController(ScrapperTelegramBot bot) {
        this.bot = bot;
    }

    @PostMapping
    public ResponseEntity<String> handleWebhook(@RequestBody Update update) {
        String response;

        SendMessage message;
        try {
            message = bot.onWebhookUpdateReceived(update);
        } catch (Exception e) {
            response = "Server could not process the update";
            logger.log(Level.SEVERE, response, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        if (message != null) {
            try {
                bot.execute(message);
            } catch (TelegramApiException e) {
                response = "Server could not send the message";
                logger.log(Level.SEVERE, response, e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } else {
            response = "The update did not have a message";
            logger.warning(response);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        }

        return ResponseEntity.ok("Request successful");
    }
}
