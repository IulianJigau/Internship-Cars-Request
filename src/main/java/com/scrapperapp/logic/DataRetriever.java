package com.scrapperapp.logic;

import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scrapperapp.http.HttpService;
import com.scrapperapp.model.FilteredData;
import com.scrapperapp.model.RootResponse;
import com.scrapperapp.payload.Payload;
import com.scrapperapp.transform.DataSimplifier;

public class DataRetriever {

    private static final Logger logger = Logger.getLogger(DataRetriever.class.getName());
    private static final ObjectMapper mapper = new ObjectMapper();

    private final Payload payload;
    private final String baseUrl;
    private final String apiPath;

    public DataRetriever(Payload payload, String baseUrl, String apiPath){
        this.payload = payload;
        this.baseUrl = baseUrl;
        this.apiPath = apiPath;
    }

    public FilteredData retrieve() throws Exception {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        FilteredData data = new FilteredData(baseUrl);

        HttpService httpService = new HttpService(baseUrl + apiPath);
        boolean dataExists;

        do {
            String ResponseDataString = httpService.sendRequest(mapper.writeValueAsString(payload));

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

            DataSimplifier simplifier = new DataSimplifier(data);
            dataExists = simplifier.simplify( rootResponse.data.searchAds.ads);

            payload.pageIncrement();
        }
        while (dataExists);

        data.stats.setTotalCars(data.info.size());

        return data;
    }

}
