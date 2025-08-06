package com.scrapperapp.logic;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scrapperapp.app.Defaults;
import com.scrapperapp.http.HttpService;
import com.scrapperapp.model.CarsData;
import com.scrapperapp.model.Payload;
import com.scrapperapp.model.RootResponse;
import com.scrapperapp.transform.CarDataParser;
import com.scrapperapp.transform.PayloadInitializer;

public class CarDataRetriever {

    private static final Logger logger = Logger.getLogger(CarDataRetriever.class.getName());
    private static final ObjectMapper mapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    public static CarsData retrieve(Defaults defaults) {

        Payload payload = new Payload();
        PayloadInitializer.initialize(payload);
        payload.pageSizeSet(defaults.PAGE_SIZE);

        HttpService httpService = new HttpService(defaults.TARGET_BASE_URL + defaults.TARGET_API_PATH);
        CarsData carsData = new CarsData();

        boolean dataExists;
        String responseString;
        RootResponse rootResponse;

        do {
            try {
                responseString = httpService.sendRequest(mapper.writeValueAsString(payload));
                try {
                    rootResponse = mapper.readValue(responseString, RootResponse.class);
                    dataExists = carsData.merge(CarDataParser.ParseCarsData(rootResponse, defaults.TARGET_BASE_URL));
                } catch (JsonMappingException e) {
                    logger.log(Level.SEVERE, "The api response could not be deserialized", e);
                    break;
                }
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Http request to the api failed", e);
                break;
            }

            payload.pageIncrement();
        } while (dataExists);

        return carsData;
    }

}
