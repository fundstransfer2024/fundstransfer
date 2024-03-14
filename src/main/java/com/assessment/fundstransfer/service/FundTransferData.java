package com.assessment.fundstransfer.service;

import com.assessment.fundstransfer.model.Account;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class FundTransferData {
    private Account debitAccount;
    private Account creditAccount;
    private BigDecimal debitAmount;
    private BigDecimal creditAmount;

}