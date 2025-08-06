package com.scrapperapp.transform;

import java.util.List;

import com.scrapperapp.model.CarInfo;
import com.scrapperapp.model.CarsData;
import com.scrapperapp.model.RootResponse;

public class CarDataParser {

    public static CarsData ParseCarsData(RootResponse rootResponse, String baseUrl) {
        List<RootResponse.Ads> cars = rootResponse.data.searchAds.ads;

        CarsData data = new CarsData();

        if (!cars.isEmpty()) {

            for (RootResponse.Ads car : cars) {
                if (car.price == null) {
                    continue;
                }

                CarInfo info = new CarInfo();

                info.setId(car.getId());
                info.setTitle(car.getTitle());
                info.setPrice(car.price.value.getValue());
                info.setBaseUrl(baseUrl);

                data.add(info);
            }
        }

        return data;
    }
}
