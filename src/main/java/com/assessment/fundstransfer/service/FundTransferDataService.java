package com.assessment.fundstransfer.service;

import com.assessment.fundstransfer.exception.AccountNotFoundException;
import com.assessment.fundstransfer.exception.ExchangeRateNotFoundException;
import com.assessment.fundstransfer.exception.InsufficientBalanceException;
import com.assessment.fundstransfer.model.Account;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class FundTransferDataService {

    private static final Logger logger = LoggerFactory.getLogger(TransferOrchestratorService.class);

    private AccountRepositoryService accountRepoService;
    private ExchangeRateService exchangeRateService;

    public FundTransferData getFundTransferData(Long debitAccountId, Long creditAccountId, BigDecimal amount, String currency)
            throws AccountNotFoundException, ExchangeRateNotFoundException, InsufficientBalanceException {
        Account debitAccount = accountRepoService.getAccount(debitAccountId);
        Account creditAccount = accountRepoService.getAccount(creditAccountId);

        BigDecimal creditAmount = amount;
        BigDecimal debitAmount = amount;
        if (!debitAccount.getCurrency().equals(creditAccount.getCurrency())) {

            BigDecimal exchangeRate;

            if (!currency.equals(debitAccount.getCurrency())) {
                exchangeRate = exchangeRateService.getExchangeRate(currency, debitAccount.getCurrency());
                debitAmount = exchangeRate.multiply(amount);
            }

            if (!currency.equals(creditAccount.getCurrency())) {
                exchangeRate = exchangeRateService.getExchangeRate(currency, creditAccount.getCurrency());
                creditAmount = exchangeRate.multiply(amount);
            }
        }

        return new FundTransferData()
                .setDebitAccount(debitAccount)
                .setCreditAccount(creditAccount)
                .setDebitAmount(debitAmount)
                .setCreditAmount(creditAmount);
    }
}
