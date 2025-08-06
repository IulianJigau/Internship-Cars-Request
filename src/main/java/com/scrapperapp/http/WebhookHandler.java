package com.scrapperapp.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scrapperapp.api.ScrTelegramBot;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class WebhookHandler implements HttpHandler {
    private final ScrTelegramBot bot;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = Logger.getLogger(WebhookHandler.class.getName());

    public WebhookHandler(ScrTelegramBot bot) {
        this.bot = bot;
    }

    private class Response {
        private int code;
        private String text;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        logger.log(Level.INFO, "New request received");

        Response response = new Response();

        if (!"POST".equals(exchange.getRequestMethod())) {
            response.text = "Unsuported request method (Expected POST)";
            response.code = 501;
            logger.log(Level.WARNING, response.text);
        } else {
            response = processRequest(exchange.getRequestBody());
        }
        exchange.sendResponseHeaders(response.code, response.text.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.text.getBytes());
        }
    }

    private Response processRequest(InputStream bodyStream) {
        Response response = new Response();

        String body;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(bodyStream))) {
            body = reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            response.text = "Failed to extract the body";
            response.code = 500;
            logger.log(Level.SEVERE, response.text, e);
            return response;
        }

        Update update;
        if (!body.isEmpty()) {
            try {
                update = objectMapper.readValue(body, Update.class);
            } catch (JsonProcessingException e) {
                response.text = "Failed to deserialize the body";
                response.code = 400;
                logger.log(Level.SEVERE, response.text, e);
                return response;

            }
        } else {
            response.text = "Request body was empty";
            response.code = 204;
            logger.log(Level.WARNING, response.text);
            return response;
        }

        SendMessage message;
        try {
            message = bot.onWebhookUpdateReceived(update);
        } catch (Exception e) {
            response.text = "Server could not process the update";
            response.code = 500;
            logger.log(Level.SEVERE, response.text, e);
            return response;
        }

        try {
            bot.execute(message);
        } catch (Exception e) {
            response.text = "Server could not send the message";
            response.code = 500;
            logger.log(Level.SEVERE, response.text, e);
            return response;
        }

        response.text = "Request succesful";
        response.code = 200;
        return response;
    }
}
