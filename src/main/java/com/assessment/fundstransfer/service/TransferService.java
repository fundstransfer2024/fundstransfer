package com.assessment.fundstransfer.service;

import com.assessment.fundstransfer.exception.AccountNotFoundException;
import com.assessment.fundstransfer.exception.ExchangeRateNotFoundException;
import com.assessment.fundstransfer.exception.InsufficientBalanceException;
import com.assessment.fundstransfer.model.Transfer;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class TransferService {
    private static final Logger logger = LoggerFactory.getLogger(TransferService.class);

    private AccountRepoService accountRepoService;
    private TransferRepoService transferRepoService;
    private FundTransferDataService fundTransferDataService;

    @Transactional
    public void transferFunds(Long debitAccountId, Long creditAccountId, BigDecimal amount, String currency)
            throws AccountNotFoundException, ExchangeRateNotFoundException, InsufficientBalanceException {
        FundTransferData fundTransferData = fundTransferDataService.getFundTransferData(debitAccountId, creditAccountId, amount, currency);

        accountRepoService.updateAccountBalance(fundTransferData.getDebitAccount(), fundTransferData
                .getDebitAmount().multiply(BigDecimal.valueOf(-1L)));
        accountRepoService.updateAccountBalance(fundTransferData.getCreditAccount(), fundTransferData
                .getCreditAmount());

        Transfer transfer = transferRepoService.createTransfer(fundTransferData);

        logger.info(String.format("%s %s debited from account %s", transfer.getDebitAmount().toPlainString(),
                transfer.getDebitCurrency(),
                transfer.getDebitAccountId()));
        logger.info(String.format("%s %s credited to account %s", transfer.getCreditAmount().toPlainString(),
                transfer.getCreditCurrency(),
                transfer.getCreditAccountId()));
    }


}
