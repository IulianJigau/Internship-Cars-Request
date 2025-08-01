package com.scrapperapp.payload;

import java.util.ArrayList;
import java.util.List;

public class Payload {
    public int pageSizeGet() {
        return this.variables.input.pagination.limit;
    }

    public void pageSizeSet(int pageSize) {
        this.variables.input.pagination.limit = pageSize;
    }

    public void pageIncrement() {
        this.variables.input.pagination.skip += this.variables.input.pagination.limit;
    }

    public String query = "query SearchAds($input: Ads_SearchInput!, $isWorkCategory: Boolean = false, $includeCarsFeatures: Boolean = false, $includeBody: Boolean = false, $includeOwner: Boolean = false, $includeBoost: Boolean = false, $locale: Common_Locale) {\n  searchAds(input: $input) {\n    ads {\n      ...AdsSearchFragment\n      __typename\n    }\n    count\n    __typename\n  }\n}\n\nfragment AdsSearchFragment on Advert {\n  ...AdListFragment\n  ...WorkCategoryFeatures @include(if: $isWorkCategory)\n  reseted(\n    input: {format: \"2 Jan. 2006, 15:04\", locale: $locale, timezone: \"Europe/Chisinau\", getDiff: false}\n  )\n  __typename\n}\n\nfragment AdListFragment on Advert {\n  id\n  title\n  subCategory {\n    ...CategoryAdFragment\n    __typename\n  }\n  ...PriceAndImages\n  ...CarsFeatures @include(if: $includeCarsFeatures)\n  ...AdvertOwner @include(if: $includeOwner)\n  transportYear: feature(id: 19) {\n    ...FeatureValueFragment\n    __typename\n  }\n  realEstate: feature(id: 795) {\n    ...FeatureValueFragment\n    __typename\n  }\n  body: feature(id: 13) @include(if: $includeBody) {\n    ...FeatureValueFragment\n    __typename\n  }\n  ...AdvertBooster @include(if: $includeBoost)\n  label: displayProduct(alias: LABEL) {\n    ... on DisplayLabel {\n      enable\n      ...DisplayLabelFragment\n      __typename\n    }\n    __typename\n  }\n  frame: displayProduct(alias: FRAME) {\n    ... on DisplayFrame {\n      enable\n      __typename\n    }\n    __typename\n  }\n  animation: displayProduct(alias: ANIMATION) {\n    ... on DisplayAnimation {\n      enable\n      __typename\n    }\n    __typename\n  }\n  animationAndFrame: displayProduct(alias: ANIMATION_AND_FRAME) {\n    ... on DisplayAnimationAndFrame {\n      enable\n      __typename\n    }\n    __typename\n  }\n  __typename\n}\n\nfragment CategoryAdFragment on Category {\n  id\n  title {\n    ...TranslationFragment\n    __typename\n  }\n  parent {\n    id\n    title {\n      ...TranslationFragment\n      __typename\n    }\n    parent {\n      id\n      title {\n        ...TranslationFragment\n        __typename\n      }\n      __typename\n    }\n    __typename\n  }\n  __typename\n}\n\nfragment TranslationFragment on I18NTr {\n  translated\n  __typename\n}\n\nfragment PriceAndImages on Advert {\n  price: feature(id: 2) {\n    ...FeatureValueFragment\n    __typename\n  }\n  pricePerMeter: feature(id: 1385) {\n    ...FeatureValueFragment\n    __typename\n  }\n  images: feature(id: 14) {\n    ...FeatureValueFragment\n    __typename\n  }\n  __typename\n}\n\nfragment FeatureValueFragment on FeatureValue {\n  id\n  type\n  value\n  __typename\n}\n\nfragment CarsFeatures on Advert {\n  carFuel: feature(id: 151) {\n    ...FeatureValueFragment\n    __typename\n  }\n  carDrive: feature(id: 108) {\n    ...FeatureValueFragment\n    __typename\n  }\n  carTransmission: feature(id: 101) {\n    ...FeatureValueFragment\n    __typename\n  }\n  mileage: feature(id: 104) {\n    ...FeatureValueFragment\n    __typename\n  }\n  engineVolume: feature(id: 103) {\n    ...FeatureValueFragment\n    __typename\n  }\n  __typename\n}\n\nfragment AdvertOwner on Advert {\n  owner {\n    business {\n      plan\n      __typename\n    }\n    __typename\n  }\n  __typename\n}\n\nfragment AdvertBooster on Advert {\n  booster: product(alias: BOOSTER) {\n    enable\n    __typename\n  }\n  __typename\n}\n\nfragment DisplayLabelFragment on DisplayLabel {\n  title\n  color {\n    ...ColorFragment\n    __typename\n  }\n  gradient {\n    ...GradientFragment\n    __typename\n  }\n  __typename\n}\n\nfragment ColorFragment on Common_Color {\n  r\n  g\n  b\n  a\n  __typename\n}\n\nfragment GradientFragment on Gradient {\n  from {\n    ...ColorFragment\n    __typename\n  }\n  to {\n    ...ColorFragment\n    __typename\n  }\n  position\n  rotation\n  __typename\n}\n\nfragment WorkCategoryFeatures on Advert {\n  salary: feature(id: 266) {\n    ...FeatureValueFragment\n    __typename\n  }\n  workSchedule: feature(id: 260) {\n    ...FeatureValueFragment\n    __typename\n  }\n  workExperience: feature(id: 263) {\n    ...FeatureValueFragment\n    __typename\n  }\n  education: feature(id: 261) {\n    ...FeatureValueFragment\n    __typename\n  }\n  __typename\n}";
    public String operationName = "SearchAds";
    public Variables variables = new Variables();


    public class Variables {

        public boolean isWorkCategory = false;
        public boolean includeCarsFeatures = true;
        public boolean includeBody = false;
        public boolean includeOwner = true;
        public boolean includeBoost = false;
        public Input input = new Input();
        public String locale = "ro_RO";
    }

    public class Input {

        public int subCategoryId = 659;
        public String source = "AD_SOURCE_DESKTOP";
        public Pagination pagination = new Pagination();
        public List<Filter> filters = new ArrayList<>();
    }

    public class Pagination {

        public int limit = 100;
        public int skip = 0;
    }

    public class Filter {

        public int filterId;
        public List<Feature> features;

        public Filter(int filterId, List<Feature> features) {
            this.filterId = filterId;
            this.features = features;
        }
    }

    public class Feature {

        public int featureId;
        public List<Integer> optionIds;
        public Range range;
        public String unit;

        public Feature(int featureId, List<Integer> optionIds, Range range, String unit) {
            this.featureId = featureId;
            this.optionIds = optionIds;
            this.range = range;
            this.unit = unit;
        }
    }

    public class Range {

        public int min;
        public int max;

        public Range(int min, int max) {
            this.min = min;
            this.max = max;
        }
    }
}
