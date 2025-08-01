package com.scrapperapp;

import java.util.List;

public class InitializePayload{

    public static void initialize(Payload payload){
        payload.pageSizeSet(Defaults.pageSize); 

        //Tip oferta - vand
        payload.addFilters(new Payload.Filter(16, List.of(new Payload.Feature(1, List.of(776), null, null))));
        //An 2008 - 2016
        payload.addFilters(new Payload.Filter(7, List.of(new Payload.Feature(19, null, new Payload.Range(2008, 2011), null))));
        //Marca - Renault Megane III
        payload.addFilters(new Payload.Filter(1, List.of(new Payload.Feature(2095, List.of(36188), null, null))));
        //Rulaj 100k - 350k
        payload.addFilters(new Payload.Filter(1081, List.of(new Payload.Feature(104, null, new Payload.Range(100000, 350000), "UNIT_KILOMETER"))));
        //Cap Cilindrica 0 - 1600 cm^3
        payload.addFilters(new Payload.Filter(2, List.of(new Payload.Feature(103, null, new Payload.Range(0, 1600), null))));
        //Cutie - Mecanica
        payload.addFilters(new Payload.Filter(5, List.of(new Payload.Feature(101, List.of(4), null, null))));
        //Tip combustibil - Diesel
        payload.addFilters(new Payload.Filter(4, List.of(new Payload.Feature(151, List.of(24), null, null))));
    }
}