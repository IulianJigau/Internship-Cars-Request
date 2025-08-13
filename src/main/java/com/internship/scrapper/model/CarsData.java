package com.internship.scrapper.model;

import java.util.ArrayList;
import java.util.List;

public class CarsData {

    public final Stats stats = new Stats();
    public final List<CarInfo> info = new ArrayList<>();

    @Override
    public String toString() {
        String result = "";

        List<CarInfo> maxPriceCars = new ArrayList<>();
        List<CarInfo> minPriceCars = new ArrayList<>();

        for (CarInfo car : info) {
            if (stats.getMaxPrice() == car.getPrice()) {
                maxPriceCars.add(car);
            } else if (stats.getMinPrice() == car.getPrice()) {
                minPriceCars.add(car);
            }
        }

        result += String.format("Max Price ");
        result += String.format("%n");
        for (CarInfo car : maxPriceCars) {
            result += String.format(car.toString());
            result += String.format("%n");
        }
        result += String.format("Min Price ");
        result += String.format("%n");
        for (CarInfo car : minPriceCars) {
            result += String.format(car.toString());
            result += String.format("%n");
        }

        result += String.format(stats.toString());

        return result;
    }

    public void add(CarInfo info) {
        this.info.add(info);

        stats.compareMinMaxPrice(info.getPrice());
        stats.addPriceTotal(info.getPrice());
    }

    public boolean merge(CarsData data) {
        if (!data.info.isEmpty()) {
            info.addAll(data.info);
            stats.compareMinMaxPrice(data.stats.minPrice);
            stats.compareMinMaxPrice(data.stats.maxPrice);
            stats.totalPrice += data.stats.totalPrice;
            return true;
        }
        return false;
    }

    public class Stats {

        private double minPrice = Double.MAX_VALUE;
        private double maxPrice = -1;
        private double totalPrice = 0;

        @Override
        public String toString() {
            String result = "";
            result += String.format("Summary %n");
            result += String.format("Min Price:  %.2f %s %n", getMinPrice(), "EUR");
            result += String.format("Max Price:  %.2f %s %n", getMaxPrice(), "EUR");
            result += String.format("Avg Price:  %.2f %s %n", getAvgPrice(), "EUR");
            result += String.format("total Cars:  %d %n", info.size());

            return result;
        }

        public void compareMinMaxPrice(double price) {
            if (price < minPrice) {
                minPrice = price;
            } else if (price > maxPrice) {
                maxPrice = price;
            }
        }

        public double getMinPrice() {
            return minPrice;
        }

        public double getMaxPrice() {
            return maxPrice;
        }

        public void addPriceTotal(double price) {
            totalPrice += price;
        }

        public double getAvgPrice() {
            if (!info.isEmpty()) {
                return totalPrice / info.size();
            } else {
                return 0;
            }
        }
    }

    public static CarsData ParseCarsData(RootResponse rootResponse, String baseUrl) throws NullPointerException{
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

