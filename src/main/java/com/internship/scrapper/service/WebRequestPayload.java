package com.internship.scrapper.service;

import com.internship.scrapper.config.WebRequestConfig;
import com.internship.scrapper.model.PayloadTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WebRequestPayload extends PayloadTemplate {

    WebRequestPayload(WebRequestConfig webRequestConfig){
        pageSizeSet(webRequestConfig.getPageSize());

        List<Filter> filters = this.variables.input.filters;

        // Marca - Renault Megane III
        filters.add(new Filter(1, List.of(
                new Feature(2095, List.of(36188), null, null))));

        // Tip oferta - vand
        filters.add(new Filter(16, List.of(
                new Feature(1, List.of(776), null, null))));

        // Cutie - Mecanica
        filters.add(new Filter(5, List.of(
                new Feature(101, List.of(4), null, null))));

        // Rulaj 100k - 350k
        filters.add(new Filter(1081, List.of(
                new Feature(104, null, new Range(100000, 350000), "UNIT_KILOMETER"))));

        // Cap Cilindrica 0 - 1600 cm^3
        filters.add(new Filter(2, List.of(
                new Feature(103, null, new Range(0, 1600), null))));

        // Tip combustibil - Diesel
        filters.add(new Filter(4, List.of(
                new Feature(151, List.of(24), null, null))));

        // An 2008 - 2016
        filters.add(new Filter(7, List.of(
                new Feature(19, null, new Range(2008, 2011), null))));
    }
}
