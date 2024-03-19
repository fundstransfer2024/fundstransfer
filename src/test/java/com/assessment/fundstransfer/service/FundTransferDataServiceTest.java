package com.assessment.fundstransfer.service;

import com.assessment.fundstransfer.model.Account;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FundTransferDataServiceTest {

    @Mock
    private AccountRepositoryService accountRepoServiceMock;
    @Mock
    private ExchangeRateService exchangeRateServiceMock;
    @InjectMocks
    private FundTransferDataService fundTransferDataService;

    private static Account createAccount(Long creditAccountId, String currency, BigDecimal balance) {
        return new Account()
                .setOwnerId(creditAccountId)
                .setCurrency(currency)
                .setBalance(balance);
    }

    @SneakyThrows
    @Test
    void getFundTransferData_shouldGetAccountsAndExchangeRates() {
        // GIVEN
        Long debitAccountId = 1L;
        Long creditAccountId = 2L;
        String baseCurrency = "EUR";
        BigDecimal amount = BigDecimal.valueOf(100L);
        Account debitAccount = createAccount(debitAccountId, "EUR", BigDecimal.valueOf(1000L));
        when(accountRepoServiceMock.getAccount(debitAccountId))
                .thenReturn(debitAccount);
        Account creditAccount = createAccount(creditAccountId, "USD", BigDecimal.valueOf(1000L));
        when(accountRepoServiceMock.getAccount(creditAccountId))
                .thenReturn(creditAccount);
        BigDecimal exchangeRate = BigDecimal.valueOf(1.094272d);

        when(exchangeRateServiceMock.getExchangeRate(baseCurrency, creditAccount.getCurrency()))
                .thenReturn(exchangeRate);

        // WHEN
        FundTransferData fundTransferData = fundTransferDataService.getFundTransferData(debitAccountId, creditAccountId,
                amount, baseCurrency);


        // THEN
        assertEquals(debitAccount, fundTransferData.getDebitAccount());
        assertEquals(creditAccount, fundTransferData.getCreditAccount());
        assertEquals(amount, fundTransferData.getDebitAmount());
        assertEquals(amount.multiply(exchangeRate), fundTransferData.getCreditAmount());

        verify(accountRepoServiceMock).getAccount(debitAccountId);
        verify(accountRepoServiceMock).getAccount(creditAccountId);
        verify(exchangeRateServiceMock).getExchangeRate(baseCurrency, creditAccount.getCurrency());
    }

    @SneakyThrows
    @Test
    void getFundTransferData_shouldGetAccountsAndExchangeRatesTwice() {
        // GIVEN
        Long debitAccountId = 1L;
        Long creditAccountId = 2L;
        String baseCurrency = "GBP";
        BigDecimal amount = BigDecimal.valueOf(100L);
        Account debitAccount = createAccount(debitAccountId, "EUR", BigDecimal.valueOf(1000L));
        when(accountRepoServiceMock.getAccount(debitAccountId))
                .thenReturn(debitAccount);
        Account creditAccount = createAccount(creditAccountId, "USD", BigDecimal.valueOf(1000L));
        when(accountRepoServiceMock.getAccount(creditAccountId))
                .thenReturn(creditAccount);
        BigDecimal gbpToEurExchangeRate = BigDecimal.valueOf(1.171003d);
        BigDecimal gbpToUsdExchangeRate = BigDecimal.valueOf(1.281304d);

        when(exchangeRateServiceMock.getExchangeRate(baseCurrency, debitAccount.getCurrency()))
                .thenReturn(gbpToEurExchangeRate);
        when(exchangeRateServiceMock.getExchangeRate(baseCurrency, creditAccount.getCurrency()))
                .thenReturn(gbpToUsdExchangeRate);

        // WHEN
        FundTransferData fundTransferData = fundTransferDataService.getFundTransferData(debitAccountId, creditAccountId,
                amount, baseCurrency);


        // THEN
        assertEquals(debitAccount, fundTransferData.getDebitAccount());
        assertEquals(creditAccount, fundTransferData.getCreditAccount());
        assertEquals(amount.multiply(gbpToEurExchangeRate), fundTransferData.getDebitAmount());
        assertEquals(amount.multiply(gbpToUsdExchangeRate), fundTransferData.getCreditAmount());

        verify(accountRepoServiceMock).getAccount(debitAccountId);
        verify(accountRepoServiceMock).getAccount(creditAccountId);
        verify(exchangeRateServiceMock).getExchangeRate(baseCurrency, debitAccount.getCurrency());
        verify(exchangeRateServiceMock).getExchangeRate(baseCurrency, creditAccount.getCurrency());
    }


}
