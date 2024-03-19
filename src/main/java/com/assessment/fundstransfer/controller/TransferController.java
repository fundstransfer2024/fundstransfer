package com.assessment.fundstransfer.controller;

import com.assessment.fundstransfer.exception.AccountNotFoundException;
import com.assessment.fundstransfer.exception.ExchangeRateNotFoundException;
import com.assessment.fundstransfer.exception.InsufficientBalanceException;
import com.assessment.fundstransfer.resource.FundsTransferResource;
import com.assessment.fundstransfer.service.FundTransferData;
import com.assessment.fundstransfer.service.TransferOrchestratorService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(TransferController.class);

    private TransferOrchestratorService transferOrchestratorService;

    @PostMapping
    public ResponseEntity<Void> transferFunds(@RequestBody FundsTransferResource fundsTransferResource) {
        try {
            FundTransferData fundsTransferData = transferOrchestratorService.triggerTransfer(fundsTransferResource.getDebitAccount()
                    , fundsTransferResource.getCreditAccount()
                    , fundsTransferResource.getAmount()
                    , fundsTransferResource.getCurrency());
            logger.info(String.format("%s %s debited from account %s",
                    fundsTransferData.getDebitAmount().toPlainString(),
                    fundsTransferData.getDebitAccount().getCurrency(),
                    fundsTransferData.getDebitAccount().getOwnerId()));
            logger.info(String.format("%s %s credited to account %s",
                    fundsTransferData.getCreditAmount().toPlainString(),
                    fundsTransferData.getCreditAccount().getCurrency(),
                    fundsTransferData.getCreditAccount().getOwnerId()));
        } catch (AccountNotFoundException | ExchangeRateNotFoundException | InsufficientBalanceException ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            logger.error(String.format("Error occurred while transferring funds from %s to %s", fundsTransferResource.getDebitAccount()
                    , fundsTransferResource.getCreditAccount()));
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
