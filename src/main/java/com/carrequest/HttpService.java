package com.carrequest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class HttpService {
    private static final HttpClient client = HttpClient.newHttpClient();

    public static JsonArray sendCarRequest(String basePayload, String url, int limit, int skip) throws Exception {
        JsonObject payloadJson = JsonParser.parseString(basePayload).getAsJsonObject();
        JsonObject pagination = payloadJson
                .getAsJsonObject("variables")
                .getAsJsonObject("input")
                .getAsJsonObject("pagination");

        pagination.addProperty("limit", limit);
        pagination.addProperty("skip", skip);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Accept", "*/*")
                .POST(HttpRequest.BodyPublishers.ofString(payloadJson.toString()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonObject responseJson = JsonParser.parseString(response.body()).getAsJsonObject();
        return responseJson
                .getAsJsonObject("data")
                .getAsJsonObject("searchAds")
                .getAsJsonArray("ads");
    }
}
