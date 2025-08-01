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

            carInfo.setId(car.getId());
            carInfo.setTitle(car.getTitle());
            carInfo.setPrice(car.price.value.getValue());

            data.info.add(carInfo);
            data.compareMinMaxPrice(carInfo.getPrice());
            data.stats.addPriceTotal(carInfo.getPrice());
        }

        return true;
    }
}
