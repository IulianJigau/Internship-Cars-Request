package com.scrapperapp.transform;

import java.util.List;

import com.scrapperapp.model.Payload;

public class PayloadInitializer {

    public static void initialize(Payload payload) {
        List<Payload.Filter> filters = payload.variables.input.filters;

        // Marca - Renault Megane III
        filters.add(payload.new Filter(1, List.of(
                payload.new Feature(2095, List.of(36188), null, null))));

        // Tip oferta - vand
        filters.add(payload.new Filter(16, List.of(
                payload.new Feature(1, List.of(776), null, null))));

        // Cutie - Mecanica
        filters.add(payload.new Filter(5, List.of(
                payload.new Feature(101, List.of(4), null, null))));

        // Rulaj 100k - 350k
        filters.add(payload.new Filter(1081, List.of(
                payload.new Feature(104, null, payload.new Range(100000, 350000), "UNIT_KILOMETER"))));

        // Cap Cilindrica 0 - 1600 cm^3
        filters.add(payload.new Filter(2, List.of(
                payload.new Feature(103, null, payload.new Range(0, 1600), null))));

        // Tip combustibil - Diesel
        filters.add(payload.new Filter(4, List.of(
                payload.new Feature(151, List.of(24), null, null))));

        // An 2008 - 2016
        filters.add(payload.new Filter(7, List.of(
                payload.new Feature(19, null, payload.new Range(2008, 2011), null))));
    }

}