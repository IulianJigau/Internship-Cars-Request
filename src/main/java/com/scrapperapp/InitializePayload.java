package com.scrapperapp;

import java.util.List;

public class InitializePayload{

    public static void initialize(Payload payload){
        payload.variables.input.pagination.limit = Defaults.pageSize;

        payload.addFilters(new Payload.Filter(1, List.of(new Payload.Feature(2095, List.of(36188), null, null))));
        payload.addFilters(new Payload.Filter(16, List.of(new Payload.Feature(1, List.of(776), null, null))));
        payload.addFilters(new Payload.Filter(1081, List.of(new Payload.Feature(104, null, new Payload.Range(100000, 350000), "UNIT_KILOMETER"))));
        payload.addFilters(new Payload.Filter(2, List.of(new Payload.Feature(103, null, new Payload.Range(1000, 2500), null))));
    }
}