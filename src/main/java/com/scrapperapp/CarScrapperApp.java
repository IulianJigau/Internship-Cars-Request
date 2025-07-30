package com.scrapperapp;

public class CarScrapperApp {

    private static final String BASE_URL = "https://999.md/";
    private static final String API_PATH = "graphql";

    public static void main(String[] args) throws Exception {
        Defaults.initialize();
        Payload payload = new Payload();
        InitializePayload.initialize(payload);

        FilteredData data = new FilteredData(BASE_URL);

        RequestLoop.loop(data, payload, BASE_URL, API_PATH);

        if (Defaults.shortMode) {
            FilteredDataPrinter.printShort(data);
        } else {
            FilteredDataPrinter.printLong(data);
        }
    }
}
