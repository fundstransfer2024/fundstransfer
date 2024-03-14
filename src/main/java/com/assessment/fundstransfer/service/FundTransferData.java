package com.assessment.fundstransfer.service;

import com.assessment.fundstransfer.model.Account;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class FundTransferData {
    private final Account debitAccount;
    private final Account creditAccount;
    private final BigDecimal debitAmount;
    private final BigDecimal creditAmount;

}