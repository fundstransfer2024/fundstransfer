package com.assessment.fundstransfer.resource;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class AccountResource {
    private Long ownerId;
    @Size(min = 3, message = "Currency cannot contain less than 3 characters")
    @Size(max = 3, message = "Currency cannot contain more than 3 characters")
    private String currency;
    @NotNull(message = "Balance cannot be null")
    private BigDecimal balance;
}
