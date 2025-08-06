package com.scrapperapp.app;

import java.net.HttpURLConnection;
import java.net.URI;

import com.scrapperapp.api.ScrTelegramBot;
import com.scrapperapp.http.LocalServer;
import com.scrapperapp.http.WebhookHandler;

public class ScrapperApp {

    public static void main(String[] args) throws Exception {

        Defaults defaults = new Defaults();

        ScrTelegramBot scrappingBot = new ScrTelegramBot(defaults);

        LocalServer localServer = new LocalServer();
        localServer.server.createContext("/webhook", new WebhookHandler(scrappingBot));
        localServer.Initialize();

        //For Testing Purposes Only
        URI uri = URI.create(String.format("https://api.telegram.org/bot%s/setWebhook?url=%s%s",
                defaults.BOT_TOKEN, defaults.SERVER_BASE_URL, defaults.BOT_PATH));
        HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
        conn.getResponseCode();
        //

        // TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        // SetWebhook setWebhook =
        // SetWebhook.builder().url(defaults.SERVER_BASE_URL + scrappingBot.getBotPath()).build();
        // botsApi.registerBot(scrappingBot, setWebhook);
    }
}
