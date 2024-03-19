package com.assessment.fundstransfer.service.client;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@AllArgsConstructor
public class ExchangeRateClient {

    private static final String API_ACCESS_KEY = "55c5fd6f42e7bc6d817be51ee030b0f2";
    private RestTemplate restTemplate;

    public ExchangeRate getExchangeRate(String baseCurrency, String targetCurrency) {
        // Call external API
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://data.fixer.io/api/latest")
                .queryParam("base", baseCurrency)
                .queryParam("access_key", API_ACCESS_KEY)
                .queryParam("symbols", targetCurrency);

        return restTemplate.getForObject(builder.toUriString(), ExchangeRate.class);
    }
}
