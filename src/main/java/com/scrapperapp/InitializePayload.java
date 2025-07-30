package com.scrapperapp;

import java.util.List;

public class InitializePayload{

    public static void initialize(Payload payload){
        payload.variables.input.pagination.limit = Defaults.pageSize;

        //Renault Megane III
        payload.addFilters(new Payload.Filter(1, List.of(new Payload.Feature(2095, List.of(36188), null, null))));
        //Tip oferta - vand
        payload.addFilters(new Payload.Filter(16, List.of(new Payload.Feature(1, List.of(776), null, null))));
        //Rulaj 100k - 350k
        payload.addFilters(new Payload.Filter(1081, List.of(new Payload.Feature(104, null, new Payload.Range(100000, 350000), "UNIT_KILOMETER"))));
        //Cap Cilindrica 0 - 1600 cm^3
        payload.addFilters(new Payload.Filter(2, List.of(new Payload.Feature(103, null, new Payload.Range(0, 1600), null))));
    }
}