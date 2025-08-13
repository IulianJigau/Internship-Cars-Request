package com.internship.scrapper.model;

public class CarInfo {

    private String id;
    private String title;
    private double price;
    private String baseUrl;

    @Override
    public String toString() {
        String result = "";
        result += String.format("Title: %s %n", getTitle());
        result += String.format("Price: %.2f %s %n", getPrice(), "EUR");
        result += String.format("URL: %s %n", getUrl());

        return result;
    }

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

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}

