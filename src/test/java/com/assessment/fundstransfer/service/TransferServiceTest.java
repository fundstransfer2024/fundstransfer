package com.assessment.fundstransfer.service;

import com.assessment.fundstransfer.model.Account;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TransferServiceTest {

    @Mock
    private AccountRepositoryService accountRepositoryServiceMock;
    @InjectMocks
    private TransferService transferService;

    private static Account createAccount(Long accountId, String currency, BigDecimal balance) {
        return new Account()
                .setOwnerId(accountId)
                .setCurrency(currency)
                .setBalance(balance);
    }

    @SneakyThrows
    @Test
    void transferFunds_shouldTransferAndCreateRecord() {
        // GIVEN
        Long debitAccountId = 1L;
        Long creditAccountId = 2L;
        String debitAccountCurrency = "USD";
        String creditAccountCurrency = "EUR";
        Account debitAccount = createAccount(debitAccountId, debitAccountCurrency, BigDecimal.valueOf(1000L));
        Account creditAccount = createAccount(creditAccountId, creditAccountCurrency, BigDecimal.valueOf(1000L));
        BigDecimal debitAmount = BigDecimal.valueOf(100L);
        BigDecimal creditAmount = BigDecimal.valueOf(91.310500d);

        FundTransferData fundTransferData = new FundTransferData()
                .setDebitAccount(debitAccount)
                .setCreditAccount(creditAccount)
                .setDebitAmount(debitAmount)
                .setCreditAmount(creditAmount);

        // WHEN
        transferService.performTransfer(debitAccountId, creditAccountId, fundTransferData);

        // THEN
        verify(accountRepositoryServiceMock).updateDebitAccountBalance(debitAccountId, fundTransferData.getDebitAmount());
        verify(accountRepositoryServiceMock).updateCreditAccountBalance(creditAccountId, fundTransferData.getCreditAmount());
    }

}
