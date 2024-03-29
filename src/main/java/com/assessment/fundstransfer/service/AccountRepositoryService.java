package com.assessment.fundstransfer.service;

import com.assessment.fundstransfer.exception.AccountNotFoundException;
import com.assessment.fundstransfer.exception.InsufficientBalanceException;
import com.assessment.fundstransfer.model.Account;
import com.assessment.fundstransfer.repository.AccountRepository;
import com.assessment.fundstransfer.repository.PessimisticWriteAccountRepository;
import com.assessment.fundstransfer.resource.AccountResource;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountRepositoryService {

    private static final Logger logger = LoggerFactory.getLogger(AccountRepositoryService.class);

    private PessimisticWriteAccountRepository pessimisticWriteAccountRepository;
    private AccountRepository accountRepository;

    @Transactional
    public Account addAccount(AccountResource accountResource) {
        Account account = new Account()
                .setCurrency(accountResource.getCurrency())
                .setBalance(accountResource.getBalance());
        return accountRepository.save(account);
    }

    @Transactional
    public Account updateAccount(AccountResource accountResource) throws AccountNotFoundException {
        Long accountId = accountResource.getOwnerId();
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        if (accountOptional.isEmpty()) {
            logger.error(String.format("Account %s does not exist.", accountId));
            throw new AccountNotFoundException();
        }
        Account account = accountOptional.get();

        account.setCurrency(accountResource.getCurrency())
                .setBalance(accountResource.getBalance());
        return accountRepository.save(account);
    }

    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    public Account getAccount(Long accountId) throws AccountNotFoundException {
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        if (accountOptional.isPresent()) {
            return accountOptional.get();
        }
        logger.error(String.format("Account %s does not exist.", accountId));
        throw new AccountNotFoundException();
    }

    public void updateDebitAccountBalance(Long accountId, BigDecimal amount) throws AccountNotFoundException, InsufficientBalanceException {
        Account account = getAccountForPessimisticWrite(accountId);
        BigDecimal currentBalance = account.getBalance();

        if (currentBalance.compareTo(amount) < 0) {
            logger.error(String.format("Debit account %s does not have sufficient balance.", accountId));
            throw new InsufficientBalanceException();
        }
        saveUpdatedAccount(amount.multiply(BigDecimal.valueOf(-1L)), account);
    }

    public void updateCreditAccountBalance(Long accountId, BigDecimal amount) throws AccountNotFoundException {
        Account account = getAccountForPessimisticWrite(accountId);
        saveUpdatedAccount(amount, account);
    }

    public void deleteById(Long accountId) throws AccountNotFoundException {
        if (!accountRepository.existsById(accountId)) {
            throw new AccountNotFoundException();
        }
        accountRepository.deleteById(accountId);
    }

    private void saveUpdatedAccount(BigDecimal amount, Account account) {
        BigDecimal newBalance = account.getBalance().add(amount);

        account.setBalance(newBalance);
        pessimisticWriteAccountRepository.save(account);
    }

    private Account getAccountForPessimisticWrite(Long accountId) throws AccountNotFoundException {
        Optional<Account> accountOptional = pessimisticWriteAccountRepository.findById(accountId);
        if (accountOptional.isEmpty()) {
            logger.error(String.format("Account %s does not exist.", accountId));
            throw new AccountNotFoundException();
        }
        return accountOptional.get();
    }


}
