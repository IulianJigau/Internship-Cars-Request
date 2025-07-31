package com.scrapperapp;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CarScrapperApp {

    private static final Logger logger = Logger.getLogger(RequestLoop.class.getName());

    private static final String BASE_URL = "https://999.md/";
    private static final String API_PATH = "graphql";

    public static void main(String[] args) throws Exception {
        Defaults.initialize();
        Payload payload = new Payload();
        InitializePayload.initialize(payload);

        FilteredData data = new FilteredData(BASE_URL);

        try {
            RequestLoop.loop(data, payload, BASE_URL, API_PATH);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception during car data request loop", e);
            return;
        }

        if (data.stats.totalCars != 0) {
            if (Defaults.shortMode) {
                FilteredDataPrinter.printShort(data);
            } else {
                FilteredDataPrinter.printLong(data);
            }
        }
    }
}
