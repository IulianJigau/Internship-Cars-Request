package com.scrapperapp;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class HttpService {
    private static final HttpClient client = HttpClient.newHttpClient();

    public static JsonObject sendCarRequest(String payload, String url) throws Exception {
        JsonObject payloadJson = JsonParser.parseString(payload).getAsJsonObject();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Accept", "*/*")
                .POST(HttpRequest.BodyPublishers.ofString(payloadJson.toString()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonObject responseJson = JsonParser.parseString(response.body()).getAsJsonObject();
        return responseJson;
    }
}
