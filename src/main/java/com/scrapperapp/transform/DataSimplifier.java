package com.scrapperapp.transform;

import java.util.List;

import com.scrapperapp.model.FilteredData;
import com.scrapperapp.model.RootResponse;

public class DataSimplifier {

    FilteredData data;

    public DataSimplifier(FilteredData data){
        this.data = data;
    }

    public boolean simplify(List<RootResponse.Ads> cars) {

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
