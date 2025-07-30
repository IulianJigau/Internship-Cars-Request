package com.scrapperapp;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class CarDataSimplifier {

    public static void simplify(String BASE_URL, JsonArray carsDataRaw, FilteredData data) {
        FilteredData.Stats stats = data.stats;

        for (JsonElement car : carsDataRaw) {
            try {
                JsonObject carJsonRaw = car.getAsJsonObject();

                String carId = carJsonRaw.get("id").getAsString();
                String title = carJsonRaw.get("title").getAsString();

                JsonElement priceElement = carJsonRaw.get("price");
                if (priceElement == null || priceElement.isJsonNull()) {
                    continue;
                }

                JsonObject priceValue = priceElement.getAsJsonObject().getAsJsonObject("value");
                int price = priceValue.get("value").getAsInt();
                String unit = priceValue.get("unit").getAsString();

                FilteredData.Info carInfo = data.new Info();
                carInfo.id = carId;
                carInfo.title = title;
                carInfo.price = price;
                carInfo.unit = unit.replace("UNIT_", "");

                data.info.add(carInfo);

                data.compareMinMaxPrice(price);
                stats.totalPrice += price;
                stats.totalCars++;

            } catch (Exception e) {
                // logger.log(Level.SEVERE, String.format("Failed to parse entry: %s", car), e);
            }
        }
    }
}
