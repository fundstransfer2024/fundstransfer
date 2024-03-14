package com.assessment.fundstransfer.service;

import com.assessment.fundstransfer.model.Account;
import com.assessment.fundstransfer.model.Transfer;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransferServiceTest {

    @Mock
    private AccountRepoService accountRepoServiceMock;
    @Mock
    private TransferRepoService transferRepoServiceMock;
    @Mock
    private FundTransferDataService fundTransferDataServiceMock;
    @InjectMocks
    private TransferService transferService;

    @SneakyThrows
    @Test
    void transferFunds_shouldTransferAndCreateRecord() {
        // GIVEN
        Long debitAccountId = 1L;
        Long creditAccountId = 2L;
        String debitAccountCurrency = "USD";
        String creditAccountCurrency = "EUR";
        BigDecimal amount = BigDecimal.valueOf(100L);
        String transferCurrency = "USD";
        Account debitAccount = createAccount(debitAccountId, debitAccountCurrency, BigDecimal.valueOf(1000L));
        Account creditAccount = createAccount(creditAccountId, creditAccountCurrency, BigDecimal.valueOf(1000L));
        BigDecimal debitAmount = BigDecimal.valueOf(100L);
        BigDecimal creditAmount = BigDecimal.valueOf(91.310500d);

        FundTransferData fundTransferData = new FundTransferData()
                .setDebitAccount(debitAccount)
                .setCreditAccount(creditAccount)
                .setDebitAmount(debitAmount)
                .setCreditAmount(creditAmount);
        when(fundTransferDataServiceMock.getFundTransferData(debitAccountId, creditAccountId, amount, transferCurrency))
                .thenReturn(fundTransferData);
        when(transferRepoServiceMock.createTransfer(fundTransferData))
                .thenReturn(new Transfer()
                        .setDebitAccountId(debitAccountId)
                        .setDebitCurrency(debitAccountCurrency)
                        .setDebitAmount(debitAmount)
                        .setCreditAccountId(creditAccountId)
                        .setCreditCurrency(creditAccountCurrency)
                        .setCreditAmount(creditAmount));

        // WHEN
        transferService.transferFunds(debitAccountId, creditAccountId, amount, transferCurrency);

        // THEN
        verify(fundTransferDataServiceMock).getFundTransferData(debitAccountId, creditAccountId, amount, transferCurrency);
        verify(accountRepoServiceMock).updateAccountBalance(debitAccount, debitAmount.multiply(BigDecimal.valueOf(-1L)));
        verify(accountRepoServiceMock).updateAccountBalance(creditAccount, creditAmount);
    }

    private static Account createAccount(Long accountId, String currency, BigDecimal balance) {
        return new Account()
                .setOwnerId(accountId)
                .setCurrency(currency)
                .setBalance(balance);
    }
}
