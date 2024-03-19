package com.assessment.fundstransfer.service;

import com.assessment.fundstransfer.exception.ExchangeRateNotFoundException;
import com.assessment.fundstransfer.service.client.ExchangeRate;
import com.assessment.fundstransfer.service.client.ExchangeRateClient;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExchangeRateServiceTest {
    @Mock
    private ExchangeRateClient exchangeRateClientMock;
    @InjectMocks
    private ExchangeRateService exchangeRateService;

    private static ExchangeRate createExchangeRate(String baseCurrency, String targetCurrency, BigDecimal exchangeRate) {
        Map<String, BigDecimal> map = new HashMap<>();
        map.put(targetCurrency, exchangeRate);

        return new ExchangeRate().setSuccess(true)
                .setBase(baseCurrency)
                .setRates(map);
    }

    @SneakyThrows
    @Test
    void getExchangeRate_shouldInvokeClient() {
        // GIVEN
        String baseCurrency = "USD";
        String targetCurrency = "EUR";
        BigDecimal givenExchangeRate = BigDecimal.valueOf(0.913065d);
        ExchangeRate exchangeRateObj = createExchangeRate(baseCurrency, targetCurrency, givenExchangeRate);
        when(exchangeRateClientMock.getExchangeRate(baseCurrency, targetCurrency))
                .thenReturn(exchangeRateObj);

        // WHEN
        BigDecimal exchangeRate = exchangeRateService.getExchangeRate(baseCurrency, targetCurrency);

        // THEN
        assertEquals(givenExchangeRate, exchangeRate);
        verify(exchangeRateClientMock).getExchangeRate(baseCurrency, targetCurrency);
    }

    @SneakyThrows
    @Test
    void getExchangeRate_shouldThrowIfClientReturnsInvalidResponse() {
        // GIVEN
        String baseCurrency = "USD";
        String targetCurrency = "EUR";
        BigDecimal givenExchangeRate = BigDecimal.valueOf(0.913065d);
        ExchangeRate exchangeRateObj = createExchangeRate("EUR", targetCurrency, givenExchangeRate);
        when(exchangeRateClientMock.getExchangeRate(baseCurrency, targetCurrency))
                .thenReturn(exchangeRateObj);

        // WHEN AND THEN
        assertThrows(ExchangeRateNotFoundException.class, () -> exchangeRateService.getExchangeRate(baseCurrency, targetCurrency));
        verify(exchangeRateClientMock).getExchangeRate(baseCurrency, targetCurrency);
    }

    @SneakyThrows
    @Test
    void getExchangeRate_shouldThrowIfClientDoesNotReturnCurrency() {
        // GIVEN
        String baseCurrency = "USD";
        String targetCurrency = "EUR";
        BigDecimal givenExchangeRate = BigDecimal.valueOf(0.913065d);
        ExchangeRate exchangeRateObj = createExchangeRate("EUR", targetCurrency, givenExchangeRate);
        when(exchangeRateClientMock.getExchangeRate(baseCurrency, targetCurrency))
                .thenReturn(exchangeRateObj.setRates(new HashMap<>()));

        // WHEN AND THEN
        assertThrows(ExchangeRateNotFoundException.class, () -> exchangeRateService.getExchangeRate(baseCurrency, targetCurrency));
        verify(exchangeRateClientMock).getExchangeRate(baseCurrency, targetCurrency);
    }

    @SneakyThrows
    @Test
    void getExchangeRate_shouldThrowIfClientReturnsEmptyResponse() {
        // GIVEN
        String baseCurrency = "USD";
        String targetCurrency = "EUR";
        when(exchangeRateClientMock.getExchangeRate(baseCurrency, targetCurrency))
                .thenReturn(null);

        // WHEN AND THEN
        assertThrows(ExchangeRateNotFoundException.class, () -> exchangeRateService.getExchangeRate(baseCurrency, targetCurrency));
        verify(exchangeRateClientMock).getExchangeRate(baseCurrency, targetCurrency);
    }

    @SneakyThrows
    @Test
    void getExchangeRate_shouldThrowIfClientThrows() {
        // GIVEN
        String baseCurrency = "USD";
        String targetCurrency = "EUR";

        when(exchangeRateClientMock.getExchangeRate(baseCurrency, targetCurrency))
                .thenThrow(new RuntimeException("Exception"));

        // WHEN AND THEN
        assertThrows(ExchangeRateNotFoundException.class, () -> exchangeRateService.getExchangeRate(baseCurrency, targetCurrency));
        verify(exchangeRateClientMock).getExchangeRate(baseCurrency, targetCurrency);
    }
}
