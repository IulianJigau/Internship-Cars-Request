package com.scrapperapp.logic;

import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    private static final ObjectMapper mapper = new ObjectMapper();

    public static CarsData retrieve(Defaults defaults) {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

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
                rootResponse = mapper.readValue(responseString, RootResponse.class);
                dataExists = carsData.merge(CarDataParser.ParseCarsData(rootResponse, defaults.TARGET_BASE_URL));

                payload.pageIncrement();
            } catch (Exception e) {
                logger.severe(e.toString());
                break;
            }
        } while (dataExists);

        return carsData;
    }

}
