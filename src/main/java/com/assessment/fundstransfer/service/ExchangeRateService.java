package com.assessment.fundstransfer.service;

import com.assessment.fundstransfer.exception.ExchangeRateNotFoundException;
import com.assessment.fundstransfer.service.client.ExchangeRate;
import com.assessment.fundstransfer.service.client.ExchangeRateClient;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
@AllArgsConstructor
public class ExchangeRateService {

    private static final Logger logger = LoggerFactory.getLogger(TransferService.class);

    private ExchangeRateClient exchangeRateClient;

    public BigDecimal getExchangeRate(String baseCurrency, String targetCurrency) throws ExchangeRateNotFoundException {
        logger.info(String.format("Fetching exchange rates for base currency %s and target currency %s .."
                , baseCurrency, targetCurrency));
        try {
            ExchangeRate exchangeRate = exchangeRateClient.getExchangeRate(baseCurrency, targetCurrency);
            if (exchangeRate != null && exchangeRate.isSuccess() && baseCurrency.equals(exchangeRate.getBase())) {
                for(Map.Entry<String, BigDecimal> e: exchangeRate.getRates().entrySet()) {
                    if(targetCurrency.equals(e.getKey())) {
                        logger.info(String.format("Obtained exchange rates successfully 1 %s = %s %s", baseCurrency
                                , e.getValue().toPlainString(), targetCurrency));
                        return e.getValue();
                    }
                }
            }
        } catch (Exception ex) {
            logger.error("Exchange rate could not be retrieved.");
            throw new ExchangeRateNotFoundException();
        }
        logger.error("Exchange rate could not be retrieved.");
        throw new ExchangeRateNotFoundException();
    }

}
