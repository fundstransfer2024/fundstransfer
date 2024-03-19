package com.assessment.fundstransfer.service;


import com.assessment.fundstransfer.exception.AccountNotFoundException;
import com.assessment.fundstransfer.model.Account;
import com.assessment.fundstransfer.repository.AccountRepository;
import com.assessment.fundstransfer.repository.PessimisticWriteAccountRepository;
import com.assessment.fundstransfer.resource.AccountResource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
public class AccountRepositoryServiceTest {

    @Mock
    private PessimisticWriteAccountRepository pessimisticWriteAccountRepositoryMock;
    @Mock
    private AccountRepository accountRepositoryMock;
    @InjectMocks
    private AccountRepositoryService accountRepositoryService;

    private static Account createAccount(Long accountId, String currency, BigDecimal balance) {
        return new Account()
                .setOwnerId(accountId)
                .setCurrency(currency)
                .setBalance(balance);
    }
    private static AccountResource createAccountResource(long id, String currency, BigDecimal balance) {
        return new AccountResource()
                .setOwnerId(id)
                .setCurrency(currency)
                .setBalance(balance);
    }

    @Test
    void addAccount_shouldSave() {
        // GIVEN
        AccountResource accountResource = createAccountResource(2L, "USD", BigDecimal.valueOf(1000L));

        // WHEN
        accountRepositoryService.addAccount(accountResource);

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

    @SneakyThrows
    @Test
    void updateAccount_shouldSaveWithNewBalance() {
        // GIVEN
        AccountResource accountResource = createAccountResource(2L, "USD", BigDecimal.valueOf(1000L));
        Account account = createAccount(2L, "EUR",  BigDecimal.valueOf(100L));
        when(accountRepositoryMock.findById(2L)).thenReturn(Optional.of(account));

        // WHEN
        accountRepositoryService.updateAccount(accountResource);

        // THEN
        ArgumentCaptor<Account> argumentCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepositoryMock).save(argumentCaptor.capture());
        assertEquals(BigDecimal.valueOf(1000L), argumentCaptor.getValue().getBalance());
    }

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
