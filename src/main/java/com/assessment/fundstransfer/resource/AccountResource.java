package com.assessment.fundstransfer.resource;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class AccountResource {
    private Long ownerId;
    private String currency;
    private BigDecimal balance;
}
