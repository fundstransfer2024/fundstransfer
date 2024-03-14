package com.assessment.fundstransfer.service;

import com.assessment.fundstransfer.model.Transfer;
import com.assessment.fundstransfer.repository.TransferRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class TransferRepoService {

    private TransferRepository transferRepository;

    public Transfer createTransfer(FundTransferData fundTransferData) {
        Transfer transfer = new Transfer()
                .setDebitAccountId(fundTransferData.getDebitAccount().getOwnerId())
                .setCreditAccountId(fundTransferData.getCreditAccount().getOwnerId())
                .setDebitAmount(fundTransferData.getDebitAmount())
                .setCreditAmount(fundTransferData.getCreditAmount())
                .setDebitCurrency(fundTransferData.getDebitAccount().getCurrency())
                .setCreditCurrency(fundTransferData.getCreditAccount().getCurrency())
                .setTimeStamp(LocalDateTime.now());

        return transferRepository.save(transfer);
    }
}
