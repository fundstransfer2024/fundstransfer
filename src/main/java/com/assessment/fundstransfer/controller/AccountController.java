package com.assessment.fundstransfer.controller;

import com.assessment.fundstransfer.exception.AccountNotFoundException;
import com.assessment.fundstransfer.model.Account;
import com.assessment.fundstransfer.resource.AccountResource;
import com.assessment.fundstransfer.service.AccountRepositoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@AllArgsConstructor
public class AccountController {

    private AccountRepositoryService accountRepositoryService;

    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        try {
            List<Account> accountList = accountRepositoryService.getAccounts();

            if (accountList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(accountList, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<Account> addAccount(@RequestBody AccountResource account) {
        try {
            Account savedAccount = accountRepositoryService.addAccount(account);
            return new ResponseEntity<>(savedAccount, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount(@RequestBody AccountResource account, @PathVariable Long id) {
        try {
            Account savedAccount = accountRepositoryService.updateAccount(account);
            return new ResponseEntity<>(savedAccount, HttpStatus.OK);
        } catch (AccountNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long id) {
        try {
            Account account = accountRepositoryService.getAccount(id);
            return new ResponseEntity<>(account, HttpStatus.OK);
        } catch (AccountNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteAccount(@PathVariable Long id) {
        try {
            accountRepositoryService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (AccountNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
