package com.scrapperapp;

import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DataRetriever {

    private static final Logger logger = Logger.getLogger(DataRetriever.class.getName());
    private static final ObjectMapper mapper = new ObjectMapper();

    Payload payload;
    String baseUrl;
    String apiPath;

    public DataRetriever(Payload payload, String baseUrl, String apiPath){
        this.payload = payload;
        this.baseUrl = baseUrl;
        this.apiPath = apiPath;
    }

    public FilteredData retrieve() throws Exception {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        FilteredData data = new FilteredData(baseUrl);

        boolean dataExists;

        do {
            String ResponseDataString = HttpService.sendRequest(mapper.writeValueAsString(payload), baseUrl + apiPath);

            if (ResponseDataString == null) {
                logger.warning("No car data retrieved; API returned 0 results.");
                return null;
            }

            RootResponse rootResponse;
            rootResponse = mapper.readValue(ResponseDataString, RootResponse.class);

            if (rootResponse.data.searchAds == null) {
                logger.severe("No data could be serialized during the mapping process");
                return null;
            }

            CarDataSimplifier simplifier = new CarDataSimplifier(data);
            dataExists = simplifier.simplify( rootResponse.data.searchAds.ads);

            payload.pageIncrement();
        }
        while (dataExists);

        double avg = data.stats.totalPrice / data.stats.totalCars;
        data.stats.avgPrice = avg;

        return data;
    }

}
