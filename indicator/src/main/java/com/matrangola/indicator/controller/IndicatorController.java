package com.matrangola.indicator.controller;

import com.matrangola.indicator.data.model.Indicator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/indicator")
public class IndicatorController {

    @GetMapping("/sample")
    public Mono<Indicator> sample() {
        Indicator indicator = new Indicator();
        indicator.setCountryCode("BGR");
        indicator.setCountryName("Bulgaria");
        indicator.setIndicatorCode("IP.PAT.RESD");
        indicator.setYear2017(230.0);
        return Mono.just(indicator);
    }
}