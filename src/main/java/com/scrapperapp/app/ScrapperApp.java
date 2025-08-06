package com.scrapperapp.app;

import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;

import com.scrapperapp.api.ScrTelegramBot;
import com.scrapperapp.cronjob.CronJobManager;
import com.scrapperapp.http.LocalServer;
import com.scrapperapp.http.WebhookHandler;
import com.scrapperapp.logic.ChatDataSender;

public class ScrapperApp {

    public static void main(String[] args) throws Exception {

        Defaults defaults = new Defaults();

        ScrTelegramBot scrappingBot = new ScrTelegramBot(defaults);

        LocalServer localServer = new LocalServer();
        localServer.server.createContext("/webhook", new WebhookHandler(scrappingBot));
        localServer.Initialize();

        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        ChatDataSender.configure(scrappingBot, defaults);
        CronJobManager cronJobManager = new CronJobManager(scheduler);
        cronJobManager.addJob(ChatDataSender.class, "sendUpdates", "0/10 * * * * ?");
    }
}
