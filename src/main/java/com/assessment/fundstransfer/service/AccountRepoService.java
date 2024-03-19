package com.assessment.fundstransfer.service;

import com.assessment.fundstransfer.model.Account;
import com.assessment.fundstransfer.repository.PessimisticWriteAccountRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class AccountRepoService {

    private static final Logger logger = LoggerFactory.getLogger(TransferOrchestratorService.class);

    private PessimisticWriteAccountRepository accountRepository;

    @Transactional
    public Account addAccount(Account account) {
        return accountRepository.save(account);
    }

    public List<Account> getAccounts() {
//        return accountRepository.findAll();
        return new ArrayList<>();
    }

    /*
    @Transactional(readOnly = true)
    public Account getAccount(Long accountId) throws AccountNotFoundException {
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        if (accountOptional.isPresent()) {
            return accountOptional.get();
        }
        logger.error(String.format("Account %s does not exist.", accountId));
        throw new AccountNotFoundException();
    }

    public void updateDebitAccountBalance(Long accountId, BigDecimal amount) throws AccountNotFoundException, InsufficientBalanceException {
        Account account = getAccount(accountId);
        BigDecimal currentBalance = account.getBalance();

        if (currentBalance.compareTo(amount) < 0) {
            logger.error(String.format("Debit account %s does not have sufficient balance.", accountId));
            throw new InsufficientBalanceException();
        }
        saveUpdatedAccount(amount.multiply(BigDecimal.valueOf(-1L)), account);
    }

    public void updateCreditAccountBalance(Long accountId, BigDecimal amount) throws AccountNotFoundException {
        Account account = getAccount(accountId);
        saveUpdatedAccount(amount, account);
    }

    private void saveUpdatedAccount(BigDecimal amount, Account account) {
        BigDecimal newBalance = account.getBalance().add(amount);

        account.setBalance(newBalance);
        accountRepository.save(account);
    }*/

}
