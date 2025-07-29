package com.carrequest;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Request {

    private static final Logger logger = Logger.getLogger(Request.class.getName());

    public static void main(String[] args) throws Exception {
        final String generic_url = "https://999.md/";

        FileHandler fileHandler = new FileHandler("RequestApp.log", true);
        fileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(fileHandler);

        String pageSizeEnv = System.getenv("PAGE_SIZE");
        String singleModeEnv = System.getenv("SINGLE_MODE");
        int pageSize = pageSizeEnv != null ? Integer.parseInt(pageSizeEnv) : 100;
        boolean singleMode = singleModeEnv != null ? Boolean.parseBoolean(singleModeEnv) : false;
        logger.log(Level.INFO, String.format("The pageSize = %s; The singleMode = %s", pageSizeEnv, singleModeEnv));

        List<JsonObject> allCars = new ArrayList<>();
        int minPricePos = 0,
                maxPricePos = 0,
                sum = 0,
                nrCars = 0,
                skip = 0;
        double maxPrice = 0,
                minPrice = Double.MAX_VALUE;

        while (true) {
            InputStream in = Request.class.getClassLoader().getResourceAsStream("payload.json");
            if (in == null) {
                logger.log(Level.SEVERE, "No payload found");
                break;
            }

            String basePayload = new String(in.readAllBytes(), StandardCharsets.UTF_8);

            String request_url = generic_url + "graphql";
            JsonArray ads = HttpService.sendCarRequest(basePayload, request_url, pageSize, skip);

            if (ads == null || ads.size() == 0) {
                break;
            }

            for (JsonElement adElement : ads) {
                try {
                    JsonObject ad = adElement.getAsJsonObject();
                    String carId = ad.get("id").getAsString();
                    String carUrl = generic_url + "ro/" + carId;
                    String title = ad.get("title").getAsString();

                    JsonElement priceElement = ad.get("price");
                    if (priceElement == null || priceElement.isJsonNull()) {
                        continue;
                    }
                    JsonObject priceValue = ad
                            .getAsJsonObject("price")
                            .getAsJsonObject("value");

                    int price = priceValue.get("value").getAsInt();
                    String unit = priceValue.get("unit").getAsString();

                    JsonObject car = new JsonObject();
                    car.addProperty("title", title);
                    car.addProperty("price", price);
                    car.addProperty("currency", unit.replace("UNIT_", ""));
                    car.addProperty("url", carUrl);

                    allCars.add(car);

                    if (price < minPrice) {
                        minPrice = price;
                        minPricePos = nrCars;
                    } else if (price > maxPrice) {
                        maxPrice = price;
                        maxPricePos = nrCars;
                    }
                    sum += price;
                    nrCars++;

                } catch (Exception e) {
                    logger.log(Level.SEVERE, String.format("Failed to parse entry: %s", adElement), e);
                }
            }

            if (ads.size() < pageSize) {
                break;
            }

            skip += pageSize;
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        double avgPrice = nrCars > 0 ? sum / nrCars : 0;

        JsonObject result = new JsonObject();
        JsonObject stats = new JsonObject();

        stats.addProperty("minPrice", minPrice == Double.MAX_VALUE ? 0 : minPrice);
        stats.addProperty("avgPrice", avgPrice);
        stats.addProperty("maxPrice", maxPrice);

        if (singleMode) {
            JsonObject minPriceCar = allCars.get(minPricePos);
            JsonObject maxPriceCar = allCars.get(maxPricePos);

            allCars.clear();
            allCars.add(minPriceCar);
            allCars.add(maxPriceCar);
        }

        result.add("cars", gson.toJsonTree(allCars));
        result.add("adInfo", stats);

        System.out.println(gson.toJson(result));
    }
}
