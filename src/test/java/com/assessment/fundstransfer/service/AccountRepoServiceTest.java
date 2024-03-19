package com.assessment.fundstransfer.service;


import com.assessment.fundstransfer.exception.AccountNotFoundException;
import com.assessment.fundstransfer.model.Account;
import com.assessment.fundstransfer.repository.AccountRepository;
import com.assessment.fundstransfer.repository.PessimisticWriteAccountRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountRepoServiceTest {

    @Mock
    private PessimisticWriteAccountRepository pessimisticWriteAccountRepositoryMock;
    @Mock
    private AccountRepository accountRepositoryMock;
    @InjectMocks
    private AccountRepositoryService accountRepositoryService;

    private static Account createAccount(Long creditAccountId, String currency, BigDecimal balance) {
        return new Account()
                .setOwnerId(creditAccountId)
                .setCurrency(currency)
                .setBalance(balance);
    }

    @Test
    void addAccount_shouldSave() {
        // GIVEN
        Account account = createAccount(1L, "USD", BigDecimal.valueOf(1000L));

        // WHEN
        accountRepositoryService.addAccount(account);

        // THEN
        verify(accountRepositoryMock).save(any(Account.class));
    }

    @Test
    void getAccounts_shouldInvokeFindAll() {
        // WHEN
        accountRepositoryService.getAccounts();

        // THEN
        verify(accountRepositoryMock).findAll();
    }

    @SneakyThrows
    @Test
    void getAccounts_shouldInvokeFindById() {
        // GIVEN
        Long accountId = 1L;
        Account account = new Account();
        when(accountRepositoryMock.findById(accountId))
                .thenReturn(Optional.of(account));

        // WHEN
        Account returnedAccount = accountRepositoryService.getAccount(accountId);

        // THEN
        verify(accountRepositoryMock).findById(accountId);
        assertEquals(account, returnedAccount);
    }

//    @Test
//    void updateAccount_shouldSaveWithNewBalance() {
//        // GIVEN
//        Account account = createAccount(2L, "USD", BigDecimal.valueOf(1000L));
//
//        // WHEN
//        accountRepositoryService.updateDebitAccountBalance (2L, BigDecimal.valueOf(100L));
//
//        // THEN
//        verify(accountRepositoryMock).save(any(Account.class));
//    }

    @SneakyThrows
    @Test
    void getAccounts_shouldThrowIfAccountNotFound() {
        // GIVEN
        Long accountId = 1L;
        when(accountRepositoryMock.findById(accountId))
                .thenReturn(Optional.empty());

        // WHEN AND THEN
        assertThrows(AccountNotFoundException.class, () -> accountRepositoryService.getAccount(accountId));
        verify(accountRepositoryMock).findById(accountId);
    }
}
