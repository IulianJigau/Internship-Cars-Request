package com.scrapperapp;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
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

        public String id;
        public String title;
        public Price price;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Price {

        public ValueObject value;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ValueObject {

        public int value;
        public String unit;
    }
}
