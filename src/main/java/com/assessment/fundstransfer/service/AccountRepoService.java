package com.assessment.fundstransfer.service;

import com.assessment.fundstransfer.exception.AccountNotFoundException;
import com.assessment.fundstransfer.model.Account;
import com.assessment.fundstransfer.repository.AccountRepository;
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
public class AccountRepoService {

    private static final Logger logger = LoggerFactory.getLogger(TransferService.class);

    private AccountRepository accountRepository;

    @Transactional
    public Account addAccount(Account account) {
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

    public void updateAccount(Account account, BigDecimal amount) {
        BigDecimal currentBalance = account.getBalance();
        BigDecimal newBalance = currentBalance.add(amount);
        account.setBalance(newBalance);
        accountRepository.save(account);
    }
}
