package com.scrapperapp.app;

import com.scrapperapp.payload.Payload;
import com.scrapperapp.payload.PayloadInitializer;
import com.scrapperapp.logic.DataRetriever;
import com.scrapperapp.model.FilteredData;
import com.scrapperapp.output.FilteredDataPrinter;

import java.util.logging.Logger;

public class ScrapperApp {

    private static final Logger logger = Logger.getLogger(ScrapperApp.class.getName());

    private static final String BASE_URL = "https://999.md/";
    private static final String API_PATH = "graphql";

    public static void main(String[] args) throws Exception {
        Defaults.initialize();
        Payload payload = new Payload();
        PayloadInitializer payloadInitializer = new PayloadInitializer(payload);
        payloadInitializer.initialize();

        DataRetriever dataRetriever = new DataRetriever(payload, BASE_URL, API_PATH);
        FilteredData data = dataRetriever.retrieve();

        if (data != null && data.stats.totalCars != 0) {
            if (Defaults.shortMode) {
                FilteredDataPrinter.printShort(data);
            } else {
                FilteredDataPrinter.printLong(data);
            }
        } else {
            logger.warning("No data available to print");
        }
    }
}
