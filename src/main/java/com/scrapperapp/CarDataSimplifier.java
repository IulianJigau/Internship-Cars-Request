package com.scrapperapp;

import java.util.List;

public class CarDataSimplifier {

    public static boolean simplify(String BASE_URL, List<RootResponse.Ads> cars, FilteredData data) {

        if (cars.isEmpty()) {
            return false;
        }

        for (RootResponse.Ads car : cars) {
            if (car.price == null) {
                continue;
            }

            FilteredData.Info carInfo = data.new Info();

            carInfo.id = car.id;
            carInfo.title = car.title;
            carInfo.price = car.price.value.value;

            data.info.add(carInfo);
            data.compareMinMaxPrice(carInfo.price);
            data.stats.totalPrice += carInfo.price;
            data.stats.totalCars++;
        }

        return true;
    }
}
