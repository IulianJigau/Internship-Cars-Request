package com.scrapperapp.app;

public class Defaults {

    public final int PAGE_SIZE;
    public final String SERVER_BASE_URL;
    public final String BOT_TOKEN;
    
    public final String BOT_PATH = "/webhook";
    public final String TARGET_BASE_URL = "https://999.md/";
    public final String TARGET_API_PATH = "graphql";

    public final String BOT_USERNAME = "CScrapperBot";

    public Defaults() {
        String botTokenEnv = System.getenv("BOT_TOKEN");
        BOT_TOKEN = botTokenEnv != null ? botTokenEnv : "none";

        String pageSizeEnv = System.getenv("PAGE_SIZE");
        PAGE_SIZE = pageSizeEnv != null ? Integer.parseInt(pageSizeEnv) : 100;

        String serverUrlEnv = System.getenv("SERVER_BASE_URL");
        SERVER_BASE_URL = serverUrlEnv != null ? serverUrlEnv : "localhost";
    }
}