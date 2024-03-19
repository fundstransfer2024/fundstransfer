package com.assessment.fundstransfer.service;

import com.assessment.fundstransfer.exception.AccountNotFoundException;
import com.assessment.fundstransfer.exception.ExchangeRateNotFoundException;
import com.assessment.fundstransfer.exception.InsufficientBalanceException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class TransferOrchestratorService {

    private static final Logger logger = LoggerFactory.getLogger(TransferOrchestratorService.class);

    private FundTransferDataService fundTransferDataService;
    private TransferService transferService;

    public FundTransferData triggerTransfer(Long debitAccountId, Long creditAccountId, BigDecimal amount, String currency)
            throws AccountNotFoundException, ExchangeRateNotFoundException, InsufficientBalanceException {

        FundTransferData fundTransferData = fundTransferDataService.getFundTransferData(debitAccountId, creditAccountId, amount, currency);

        transferService.performTransfer(debitAccountId, creditAccountId, fundTransferData);

        return fundTransferData;
    }


}
