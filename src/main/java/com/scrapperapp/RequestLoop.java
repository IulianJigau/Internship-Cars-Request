package com.scrapperapp;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RequestLoop {

    private static final Logger logger = Logger.getLogger(RequestLoop.class.getName());
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void loop(FilteredData data, Payload payload, String baseUrl, String apiPath) throws Exception {
        boolean dataExists = true;

        while (dataExists) {
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            String ResponseDataString = HttpService.sendRequest(mapper.writeValueAsString(payload), baseUrl + apiPath);

            if (ResponseDataString == null) {
                logger.warning("No car data retrieved; API returned 0 results.");
                return;
            }

            RootResponse rootResponse;
            try {
                rootResponse = mapper.readValue(ResponseDataString, RootResponse.class);
            } catch (JsonProcessingException e) {
                logger.log(Level.SEVERE, "Response mapping has returned an error.", e);
                return;
            }

            if(rootResponse.data.searchAds == null){
                logger.log(Level.SEVERE, "No data could be serialized during the mapping process");
                return;
            }
            dataExists = CarDataSimplifier.simplify(baseUrl, rootResponse.data.searchAds.ads, data);

            payload.pageIncrement();
        }

        double avg = data.stats.totalPrice / data.stats.totalCars;
        data.stats.avgPrice = avg;
    }

}
