package com.carrequest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
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
import com.google.gson.JsonParser;

public class Request {

    private static final Logger logger = Logger.getLogger(Request.class.getName());

    public static void main(String[] args) throws Exception {

        FileHandler fileHandler = new FileHandler("RequestApp.log", true);
        fileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(fileHandler);

        final String url = "https://999.md/graphql";
        final int PAGE_SIZE = 100;

        HttpClient client = HttpClient.newHttpClient();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        List<JsonObject> allCars = new ArrayList<>();
        double minPrice = Double.MAX_VALUE, maxPrice = 0, sum = 0;
        int nrCars = 0;
        int skip = 0;

        while (true) {
            String basePayload = Files.readString(Paths.get("payload.json"));
            JsonObject payloadJson = JsonParser.parseString(basePayload).getAsJsonObject();
            JsonObject pagination = payloadJson
                    .getAsJsonObject("variables")
                    .getAsJsonObject("input")
                    .getAsJsonObject("pagination");
            pagination.addProperty("limit", PAGE_SIZE);
            pagination.addProperty("skip", skip);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .header("Accept", "*/*")
                    .POST(HttpRequest.BodyPublishers.ofString(payloadJson.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonObject responseJson = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonArray ads = responseJson
                    .getAsJsonObject("data")
                    .getAsJsonObject("searchAds")
                    .getAsJsonArray("ads");

            if (ads == null || ads.size() == 0) {
                break;
            }

            for (JsonElement adElement : ads) {
                try {
                    JsonObject ad = adElement.getAsJsonObject();
                    String carId = ad.get("id").getAsString();
                    String carUrl = "https://999.md/ro/" + carId;
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

                    minPrice = Math.min(minPrice, price);
                    maxPrice = Math.max(maxPrice, price);
                    sum += price;
                    nrCars++;

                } catch (Exception e) {
                    logger.severe("Failed to parse entry: " + adElement);
                    logger.log(Level.SEVERE, "Stack trace:", e);
                }
            }

            if (ads.size() < PAGE_SIZE) {
                break;
            }

            skip += PAGE_SIZE;
        }

        double avgPrice = nrCars > 0 ? sum / nrCars : 0;

        JsonObject result = new JsonObject();
        JsonObject stats = new JsonObject();
        stats.addProperty("minPrice", minPrice == Double.MAX_VALUE ? 0 : minPrice);
        stats.addProperty("avgPrice", avgPrice);
        stats.addProperty("maxPrice", maxPrice);
        result.add("cars", gson.toJsonTree(allCars));
        result.add("adInfo", stats);

        System.out.println(gson.toJson(result));
    }
}
