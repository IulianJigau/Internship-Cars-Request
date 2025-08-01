package com.scrapperapp.model;

import java.util.ArrayList;
import java.util.List;

public class FilteredData {

    public final String baseUrl;
    public Stats stats = new Stats();
    public List<Info> info = new ArrayList<>();

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

        public double minPrice = Double.MAX_VALUE;
        public double maxPrice = -1;
        public double avgPrice = 0;
        public double totalPrice = 0;
        public int totalCars = 0;
    }

    public class Info {

        public String id;
        public String title;
        public double price;

        public String url() {
            return baseUrl + this.id;
        }
    }
}
