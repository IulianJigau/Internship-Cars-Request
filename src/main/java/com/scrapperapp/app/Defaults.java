package com.scrapperapp.app;

public class Defaults {

    public final int PAGE_SIZE;
    public final String SERVER_BASE_URL;
    
    public final String BOT_PATH = "/webhook";
    public final String TARGET_BASE_URL = "https://999.md/";
    public final String TARGET_API_PATH = "graphql";

    public final String BOT_TOKEN = "8444077982:AAGr40Dk7vnqPYDmpo2YfZgjjpo_Z7l9Ee8";
    public final String BOT_USERNAME = "CScrapperBot";

    public Defaults() {
        String pageSizeEnv = System.getenv("PAGE_SIZE");
        PAGE_SIZE = pageSizeEnv != null ? Integer.parseInt(pageSizeEnv) : 100;

        String serverUrlEnv = System.getenv("SERVER_BASE_URL");
        SERVER_BASE_URL = serverUrlEnv != null ? serverUrlEnv : "localhost";
    }
}