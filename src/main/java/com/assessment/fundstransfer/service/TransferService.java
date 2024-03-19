package com.assessment.fundstransfer.service;

import com.assessment.fundstransfer.exception.AccountNotFoundException;
import com.assessment.fundstransfer.exception.InsufficientBalanceException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class TransferService {
    private static final Logger logger = LoggerFactory.getLogger(TransferService.class);

    private AccountRepositoryService accountRepoService;


    @Retryable(maxAttempts = 10,
            backoff = @Backoff(random = true, delay = 1000, maxDelay = 5000, multiplier = 2)
            , retryFor = RuntimeException.class)
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
    public void performTransfer(Long debitAccountId, Long creditAccountId, FundTransferData fundTransferData)
            throws InsufficientBalanceException, AccountNotFoundException {

        accountRepoService.updateDebitAccountBalance(debitAccountId, fundTransferData
                .getDebitAmount());
        accountRepoService.updateCreditAccountBalance(creditAccountId, fundTransferData
                .getCreditAmount());

    }

    @Recover
    public void recover(RuntimeException e, Long debitAccountId,
                        Long creditAccountId, FundTransferData fundTransferData) {
        logger.info(String.format("All attempts to make a transfer from %s to %s failed",
                debitAccountId, creditAccountId));
        throw e;
    }

}
