package com.scrapperapp;

import java.util.ArrayList;
import java.util.List;

public class FilteredDataPrinter {

    public static void printSummary(FilteredData.Stats stats) {
        System.out.println("Summary");
        System.out.printf("Min Price:  %.2f%n", stats.minPrice);
        System.out.printf("Max Price:  %.2f%n", stats.maxPrice);
        System.out.printf("Avg Price:  %.2f%n", stats.avgPrice);
        System.out.println("total Cars:  " + stats.totalCars);
        System.out.println();
    }

    public static void printListings(List<FilteredData.Info> dataInfo) {
        System.out.println("Listings");
        for (FilteredData.Info car : dataInfo) {
            System.out.println("Title: " + car.title);
            System.out.println("Price: " + car.price + " " + car.unit);
            System.out.println("URL: " + car.url());
            System.out.println();
        }
    }

    public static void printShort(FilteredData data) {
        FilteredData.Stats stats = data.stats;

        List<FilteredData.Info> maxPriceCars = new ArrayList<>();
        List<FilteredData.Info> minPriceCars = new ArrayList<>();

        for (FilteredData.Info car : data.info) {
            if (stats.maxPrice == car.price) {
                maxPriceCars.add(car);
            } else if (stats.minPrice == car.price) {
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
