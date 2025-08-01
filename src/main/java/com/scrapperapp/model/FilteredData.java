package com.scrapperapp.model;

import java.util.ArrayList;
import java.util.List;

public class FilteredData {

    private final String baseUrl;
    public final Stats stats = new Stats();
    public final List<Info> info = new ArrayList<>();

    public FilteredData(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void compareMinMaxPrice(double price) {
        if (price < this.stats.minPrice) {
            this.stats.minPrice = price;
        } else if (price > this.stats.maxPrice) {
            this.stats.maxPrice = price;
        }
    }

    public class Stats {

        private double minPrice = Double.MAX_VALUE;
        private double maxPrice = -1;
        private double totalPrice = 0;
        private int totalCars = 0;

        public double getMinPrice() {
            return minPrice;
        }

        public double getMaxPrice() {
            return maxPrice;
        }

        public int getTotalCars() {
            return totalCars;
        }

        public void addPriceTotal(double price) {
            totalPrice += price;
        }

        public void setTotalCars(int totalCars) {
            this.totalCars = totalCars;
        }

        public double getAvgPrice() {
            return totalPrice / totalCars;
        }
    }

    public class Info {

        private String id;
        private String title;
        private double price;

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public double getPrice() {
            return price;
        }

        public String getUrl() {
            return baseUrl + this.id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setPrice(double price) {
            this.price = price;
        }
    }
}
