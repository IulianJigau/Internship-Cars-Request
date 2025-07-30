package com.scrapperapp;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

public class RequestLoop {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final Logger logger = Logger.getLogger(RequestLoop.class.getName());

    public static void loop(FilteredData data, Payload payload, String baseUrl, String apiPath) {
        try {
            while (true) {
                JsonArray carsDataRaw = HttpService.sendCarRequest(gson.toJson(payload), baseUrl + apiPath)
                        .getAsJsonObject("data")
                        .getAsJsonObject("searchAds")
                        .getAsJsonArray("ads");

                if (carsDataRaw == null) {
                    logger.log(Level.WARNING, "No car data retrieved; API returned 0 results.");
                    return;
                }

                CarDataSimplifier.simplify(baseUrl, carsDataRaw, data);

                if (carsDataRaw.size() != payload.pageSize()) {
                    break;
                }

                payload.pageIncrement();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception during car data request loop", e);
            return;
        }

        double avg = data.stats.totalPrice / data.stats.totalCars;
        data.stats.avgPrice = avg;
    }

}
