package com.internship.scrapper.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.internship.scrapper.config.WebRequestConfig;
import com.internship.scrapper.model.CarsData;
import com.internship.scrapper.model.RootResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class CarDataService {
    private static final Logger logger = Logger.getLogger(CarDataService.class.getName());

    private final RestTemplate restTemplate = new RestTemplate();
    private final WebRequestPayload payload;
    private final WebRequestConfig webRequestConfig;
    private final ObjectMapper objectMapper;

    public CarDataService(WebRequestConfig webRequestConfig, WebRequestPayload payload) {
        this.webRequestConfig = webRequestConfig;
        this.payload = payload;

        this.objectMapper = new ObjectMapper();
        this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public CarsData retrieve() {
        CarsData carsData = new CarsData();
        boolean dataExists;

        do {
            try {
                RootResponse rootResponse = sendRequest(payload);
                dataExists = carsData.merge(CarsData.ParseCarsData(rootResponse, webRequestConfig.getBaseUrl()));
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Failed to retrieve or parse API response", e);
                break;
            }

            payload.pageIncrement();
        } while (dataExists);

        payload.pageReset();

        return carsData;
    }

    private RootResponse sendRequest(WebRequestPayload payload) throws Exception {
        String jsonPayload = objectMapper.writeValueAsString(payload);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);

        String url = webRequestConfig.getBaseUrl() + webRequestConfig.getApiPath();
        ResponseEntity<RootResponse> response = restTemplate.postForEntity(url, request, RootResponse.class);

        return response.getBody();
    }
}
