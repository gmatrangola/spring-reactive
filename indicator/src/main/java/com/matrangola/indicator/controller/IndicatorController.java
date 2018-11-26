package com.matrangola.indicator.controller;

import com.matrangola.indicator.data.model.Indicator;
import com.matrangola.indicator.data.repository.IndicatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Random;

@RestController
@RequestMapping("/indicator")
public class IndicatorController {

    @Autowired
    IndicatorRepository indicatorRepository;

    @GetMapping("/sample")
    public Mono<Indicator> sample() {
        Indicator indicator = new Indicator();
        indicator.setCountryCode("BGR");
        indicator.setCountryName("Bulgaria");
        indicator.setIndicatorCode("IP.PAT.RESD");
        indicator.setYear2017(230.0);
        return Mono.just(indicator);
    }

    @GetMapping("/")
    public Flux<Indicator> getIndicators() {
        return indicatorRepository.findAll();
    }

    @GetMapping("/{countryCode}")
    public Flux<Indicator> country(@PathVariable String countryCode) {
        return indicatorRepository.findAllByCountryCode(countryCode);
    }

    @GetMapping(value = "/updates/{countryCode}",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Indicator> updates(@PathVariable String countryCode) {
        Random r = new Random(System.currentTimeMillis());
        return Flux.interval(Duration.ofSeconds(3)).map(seq -> {
            Indicator indicator = new Indicator();
            indicator.setCountryCode(countryCode);
            indicator.setYear2018(r.nextDouble());
            return indicator;
        });
    }
}