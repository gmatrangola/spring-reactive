package com.matrangola.indicator.data.repository;

import com.matrangola.indicator.data.model.Indicator;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import reactor.core.publisher.Flux;

public interface IndicatorRepository extends ReactiveCassandraRepository<Indicator, String> {
    Flux<Indicator> findAllByCountryCode(String countryCode);
}