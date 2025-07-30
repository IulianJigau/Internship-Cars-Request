package com.scrapperapp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

public class RequestLoop {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void loop(FilteredData data, Payload payload, String baseUrl, String apiPath) throws Exception{
        while (true) {
            JsonArray carsDataRaw = HttpService.sendCarRequest(gson.toJson(payload), baseUrl + apiPath)
                    .getAsJsonObject("data")
                    .getAsJsonObject("searchAds")
                    .getAsJsonArray("ads");

            if (carsDataRaw == null) {
                break;
            }

            CarDataSimplifier.simplify(baseUrl, carsDataRaw, data);

            if (carsDataRaw.size() != payload.pageSize()) {
                break;
            }

            payload.pageIncrement();
        }

        if (data.stats.totalCars == 0) {
            return;
        }

        double avg = data.stats.totalPrice / data.stats.totalCars;
        data.stats.avgPrice = avg;
    }

}
