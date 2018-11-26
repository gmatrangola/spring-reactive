package com.matrangola.indicator.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Indicator {
    private String indicatorCode;
    private String countryName;
    private String countryCode;
    private String indicatorName;
    private Double year1970;
    private Double year1980;
    private Double year1990;
    private Double year2000;
    private Double year2010;
    private Double year2017;
    private Double year2018;
}