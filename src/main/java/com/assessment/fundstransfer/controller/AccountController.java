package com.assessment.fundstransfer.controller;

import com.assessment.fundstransfer.model.Account;
import com.assessment.fundstransfer.service.AccountRepoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@AllArgsConstructor
public class AccountController {

    private AccountRepoService accountRepoService;

    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        try {
            List<Account> accountList = accountRepoService.getAccounts();

            if (accountList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(accountList, HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<Account> addAccount(@RequestBody Account account) {
        try {
            Account savedAccount = accountRepoService.addAccount(account);
            return new ResponseEntity<>(savedAccount, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
