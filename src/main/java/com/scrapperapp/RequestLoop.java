package com.scrapperapp;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class RequestLoop {

    private static final Logger logger = Logger.getLogger(RequestLoop.class.getName());
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void loop(FilteredData data, Payload payload, String baseUrl, String apiPath) throws Exception {
        boolean dataExists = true;

        while (dataExists) {
            String carsDataRaw = HttpService.sendCarRequest(gson.toJson(payload), baseUrl + apiPath);

            if (carsDataRaw == null) {
                logger.log(Level.WARNING, "No car data retrieved; API returned 0 results.");
                return;
            }

            dataExists = CarDataSimplifier.simplify(baseUrl, carsDataRaw, data);

            payload.pageIncrement();
        }

        double avg = data.stats.totalPrice / data.stats.totalCars;
        data.stats.avgPrice = avg;
    }

}
