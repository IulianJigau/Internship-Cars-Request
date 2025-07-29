package com.carrequest;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class InfoSimplifier {

    private static final Logger logger = LogConfig.setupLogger(Request.class);

    public static JsonObject dataInitializator(JsonObject carsFilteredData) {
        if (carsFilteredData.entrySet().isEmpty()) {
            JsonObject stats = new JsonObject();
            List<JsonObject> carsInfoSimplified = new ArrayList<>();

            double minPrice = Double.MAX_VALUE;
            double maxPrice = -1;
            double avgPrice = 0;
            double totalPrice = 0;

            stats.addProperty("minPrice", minPrice);
            stats.addProperty("maxPrice", maxPrice);
            stats.addProperty("avgPrice", avgPrice);
            stats.addProperty("totalPrice", totalPrice);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            carsFilteredData.add("CarsInfo", gson.toJsonTree(carsInfoSimplified));
            carsFilteredData.add("CarsStats", stats);
        }
        return carsFilteredData;
    }

    public static JsonObject simplifier(String generic_url, JsonArray carsInfoRaw, JsonObject carsFilteredData) {
        JsonObject stats = carsFilteredData.get("CarsStats").getAsJsonObject();
        JsonArray info = carsFilteredData.get("CarsInfo").getAsJsonArray();

        for (JsonElement carInfoRaw : carsInfoRaw) {
            try {
                JsonObject carJsonRaw = carInfoRaw.getAsJsonObject();

                String carId = carJsonRaw.get("id").getAsString();
                String carUrl = generic_url + "ro/" + carId;
                String title = carJsonRaw.get("title").getAsString();
                JsonElement priceElement = carJsonRaw.get("price");
                if (priceElement == null || priceElement.isJsonNull()) {
                    continue;
                }

                JsonObject priceValue = carJsonRaw
                        .getAsJsonObject("price")
                        .getAsJsonObject("value");
                int price = priceValue.get("value").getAsInt();
                String unit = priceValue.get("unit").getAsString();

                JsonObject carJson = new JsonObject();
                carJson.addProperty("title", title);
                carJson.addProperty("price", price);
                carJson.addProperty("currency", unit.replace("UNIT_", ""));
                carJson.addProperty("url", carUrl);

                info.add(carJson);

                if (price < stats.get("minPrice").getAsDouble()) {
                    stats.addProperty("minPrice", price);
                } else if (price > stats.get("maxPrice").getAsDouble()) {
                    stats.addProperty("maxPrice", price);
                }
                stats.addProperty("totalPrice", stats.get("totalPrice").getAsDouble() + price);

            } catch (Exception e) {
                logger.log(Level.SEVERE, String.format("Failed to parse entry: %s", carInfoRaw), e);
            }
        }
        return carsFilteredData;
    }
}
