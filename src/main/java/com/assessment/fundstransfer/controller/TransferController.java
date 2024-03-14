package com.assessment.fundstransfer.controller;

import com.assessment.fundstransfer.exception.AccountNotFoundException;
import com.assessment.fundstransfer.exception.ExchangeRateNotFoundException;
import com.assessment.fundstransfer.exception.InsufficientBalanceException;
import com.assessment.fundstransfer.resource.FundsTransferResource;
import com.assessment.fundstransfer.service.TransferService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transfers")
@AllArgsConstructor
public class TransferController {

    private TransferService transferService;

    @PostMapping
    public ResponseEntity<Void> transferFunds(@RequestBody FundsTransferResource fundsTransferResource) {
        try {
            transferService.transferFunds(fundsTransferResource.getDebitAccount(), fundsTransferResource.getCreditAccount()
            , fundsTransferResource.getAmount(), fundsTransferResource.getCurrency());
        } catch (AccountNotFoundException | ExchangeRateNotFoundException | InsufficientBalanceException ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
