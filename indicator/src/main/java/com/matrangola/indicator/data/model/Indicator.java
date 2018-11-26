package com.matrangola.indicator.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Indexed;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("indicator")
public class Indicator {
    @PrimaryKeyColumn(ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String indicatorCode;
    @Indexed
    private String countryName;
    @Indexed
    @PrimaryKeyColumn(ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    private String countryCode;
    @Indexed
    private String indicatorName;

    private Double year1970;
    private Double year1980;
    private Double year1990;
    private Double year2000;
    private Double year2010;
    private Double year2017;
    private Double year2018;
}