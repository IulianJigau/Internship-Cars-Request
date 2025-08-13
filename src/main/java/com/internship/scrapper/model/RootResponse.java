package com.internship.scrapper.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class RootResponse {
    public Data data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {

        public SearchAds searchAds;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SearchAds {

        public List<Ads> ads;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Ads {

        private String id;
        private String title;
        public Price price;

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Price {

        public ValueObject value;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ValueObject {

        private int value;
        private String unit;

        public int getValue() {
            return value;
        }

        public String getUnit() {
            return unit;
        }
    }
}
