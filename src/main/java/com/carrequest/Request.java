package com.carrequest;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Request {

    private static final Logger logger = LogConfig.setupLogger(Request.class);

    public static void main(String[] args) throws Exception {
        final String generic_url = "https://999.md/";

        String pageSizeEnv = System.getenv("PAGE_SIZE");
        String singleModeEnv = System.getenv("SINGLE_MODE");

        int pageSize = pageSizeEnv != null ? Integer.parseInt(pageSizeEnv) : 100;
        boolean singleMode = singleModeEnv != null ? Boolean.parseBoolean(singleModeEnv) : false;

        logger.log(Level.INFO, String.format("The pageSize = %s; The singleMode = %s", pageSizeEnv, singleModeEnv));

        InputStream payloadInput = Request.class.getClassLoader().getResourceAsStream("payload.json");
        if (payloadInput == null) {
            logger.log(Level.SEVERE, "No payload found");
            return;
        }
        String basePayload = new String(payloadInput.readAllBytes(), StandardCharsets.UTF_8);

        String request_url = generic_url + "graphql";
        int skip = 0;

        JsonObject carsFilteredData = new JsonObject();
        carsFilteredData = InfoSimplifier.dataInitializator(carsFilteredData);

        while (true) {
            JsonArray carsInfoRaw = HttpService.sendCarRequest(basePayload, request_url, pageSize, skip);

            if (carsInfoRaw == null) {
                break;
            }

            carsFilteredData = InfoSimplifier.simplifier(generic_url, carsInfoRaw, carsFilteredData);

            if (carsInfoRaw.size() != pageSize) {
                break;
            }

            skip += pageSize;
        }

        JsonArray CarsInfo = carsFilteredData.get("CarsInfo").getAsJsonArray();
        JsonObject CarStats = carsFilteredData.get("CarsStats").getAsJsonObject();

        int nrCars = CarsInfo.size();
        if (nrCars == 0) {
            logger.log(Level.WARNING, "No cars were found");
        }

        String defaultStatement = "Nu este setat";

        double min = CarStats.get("minPrice").getAsDouble();
        CarStats.addProperty("minPrice", min == Double.MAX_VALUE ? defaultStatement : String.format("%.2f", min));

        double max = CarStats.get("maxPrice").getAsDouble();
        CarStats.addProperty("maxPrice", max == -1 ? defaultStatement : String.format("%.2f", max));

        double avg = CarStats.get("totalPrice").getAsDouble() / nrCars;
        CarStats.addProperty("avgPrice", String.format("%.2f", avg));

        CarStats.remove("totalPrice");

        if (singleMode && min != Double.MAX_VALUE && max != 0) {

            JsonObject filteredInfo = new JsonObject();

            for (JsonElement carInfo : CarsInfo) {
                JsonObject carJson = carInfo.getAsJsonObject();
                if (carJson.get("price").getAsDouble() == min) {
                    filteredInfo.add("minPricedCars", carInfo);
                } else if (carJson.get("price").getAsDouble() == max) {
                    filteredInfo.add("maxPricedCars", carInfo);
                }
            }
            carsFilteredData.remove("CarsInfo");
            carsFilteredData.add("CarsInfo", filteredInfo);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(carsFilteredData));
    }
}
