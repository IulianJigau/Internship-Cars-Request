package com.scrapperapp.app;

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
    }
}
