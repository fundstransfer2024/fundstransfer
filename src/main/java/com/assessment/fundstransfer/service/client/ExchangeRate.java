package com.assessment.fundstransfer.service.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ExchangeRate {

    private boolean success;
    private Long timestamp;
    private String base;
    private Date date;
    private Map<String, BigDecimal> rates;
}