package com.scrapperapp;

public class Defaults {

    static public boolean shortMode;
    static public int pageSize;

    public static void initialize() {
        String pageSizeEnv = System.getenv("PAGE_SIZE");
        String shortModeEnv = System.getenv("SHORT_MODE");

        pageSize = pageSizeEnv != null ? Integer.parseInt(pageSizeEnv) : 100;
        shortMode = shortModeEnv != null ? Boolean.parseBoolean(shortModeEnv) : true;
    }
}