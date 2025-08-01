package com.scrapperapp.output;

import java.util.ArrayList;
import java.util.List;

import com.scrapperapp.model.FilteredData;

public class FilteredDataPrinter {

    public static void printSummary(FilteredData.Stats stats) {
        System.out.println("Summary");
        System.out.printf("Min Price:  %.2f %s %n", stats.getMinPrice(), "EUR");
        System.out.printf("Max Price:  %.2f %s %n", stats.getMaxPrice(), "EUR");
        System.out.printf("Avg Price:  %.2f %s %n", stats.getAvgPrice(), "EUR");
        System.out.printf("total Cars:  %d", stats.getTotalCars());
        System.out.println();
    }

    public static void printListings(List<FilteredData.Info> dataInfo) {
        System.out.println("Listings");
        for (FilteredData.Info car : dataInfo) {
            System.out.printf("Title: %s %n", car.getTitle());
            System.out.printf("Price: %.2f %s %n", car.getPrice(), "EUR");
            System.out.printf("URL: %s %n", car.getUrl());
            System.out.println();
        }
    }

    public static void printShort(FilteredData data) {
        FilteredData.Stats stats = data.stats;

        List<FilteredData.Info> maxPriceCars = new ArrayList<>();
        List<FilteredData.Info> minPriceCars = new ArrayList<>();

        for (FilteredData.Info car : data.info) {
            if (stats.getMaxPrice() == car.getPrice()) {
                maxPriceCars.add(car);
            } else if (stats.getMinPrice() == car.getPrice()) {
                minPriceCars.add(car);
            }
        }

        System.out.print("Max Price ");
        printListings(maxPriceCars);
        System.out.print("Min Price ");
        printListings(minPriceCars);

        printSummary(stats);
    }

    public static void printLong(FilteredData data) {
        printListings(data.info);
        printSummary(data.stats);
    }
}
