package com.scrapperapp;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CarDataSimplifier {

    private static final Logger logger = Logger.getLogger(RequestLoop.class.getName());

    public static void simplify(String BASE_URL, String carsDataRaw, FilteredData data) {
        String adsArray = JsonUtility.extractObject(carsDataRaw, "\"ads\"", new char[]{'[', ']'});
        if(adsArray.isEmpty()){
            logger.log(Level.WARNING, "No ads data was retrieved");
            return;
        }

        int idx = 0;
        while ((idx = adsArray.indexOf("{", idx)) != -1) {
            int objEnd = JsonUtility.findMatchingDelimiter(adsArray, idx, new char[]{'{', '}'});
            if (objEnd == -1) {
                break;
            }

            String ad = adsArray.substring(idx, objEnd + 1);

            String priceObject = JsonUtility.extractObject(ad, "\"price\"", new char[]{'{', '}'});
            if(priceObject.equals("")){
                continue;
            }
            String valueObject = JsonUtility.extractObject(priceObject, "\"value\"", new char[]{'{', '}'});

            FilteredData.Info carInfo = data.new Info();

            carInfo.id = JsonUtility.removeQuotes(JsonUtility.extractValue(ad, "\"id\""));
            carInfo.title = JsonUtility.removeQuotes(JsonUtility.extractValue(ad, "\"title\""));
            carInfo.price = Double.parseDouble(JsonUtility.extractValue(valueObject, "\"value\"").replaceAll("[^0-9.]", ""));
            carInfo.unit = JsonUtility.removeQuotes(JsonUtility.extractValue(valueObject, "\"unit\"")).replace("UNIT_", "");

            data.info.add(carInfo);
            data.compareMinMaxPrice(carInfo.price);
            data.stats.totalPrice += carInfo.price;
            data.stats.totalCars++;

            idx = objEnd + 1;
        }
    }
}
