package com.assessment.fundstransfer.controller;


import com.assessment.fundstransfer.exception.AccountNotFoundException;
import com.assessment.fundstransfer.exception.ExchangeRateNotFoundException;
import com.assessment.fundstransfer.exception.InsufficientBalanceException;
import com.assessment.fundstransfer.model.Account;
import com.assessment.fundstransfer.resource.FundsTransferResource;
import com.assessment.fundstransfer.service.FundTransferData;
import com.assessment.fundstransfer.service.TransferOrchestratorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransferController.class)
public class TransferControllerTest {

    @MockBean
    private TransferOrchestratorService transferOrchestratorServiceMock;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static Account createAccount(Long accountId, String currency, BigDecimal balance) {
        return new Account()
                .setOwnerId(accountId)
                .setCurrency(currency)
                .setBalance(balance);
    }

    @SneakyThrows
    @Test
    void transferFunds_shouldReturn201WhenTransferSucceeds() {
        // GIVEN
        Long debitAccountId = 1L;
        Long creditAccountId = 2L;
        String debitAccountCurrency = "USD";
        String creditAccountCurrency = "EUR";
        BigDecimal amount = BigDecimal.valueOf(1L);
        String currency = "USD";
        Account debitAccount = createAccount(debitAccountId, debitAccountCurrency, BigDecimal.valueOf(1000L));
        Account creditAccount = createAccount(creditAccountId, creditAccountCurrency, BigDecimal.valueOf(1000L));
        BigDecimal debitAmount = BigDecimal.valueOf(100L);
        BigDecimal creditAmount = BigDecimal.valueOf(91.310500d);

        FundsTransferResource fundsTransferResource = new FundsTransferResource()
                .setDebitAccount(debitAccountId)
                .setCreditAccount(creditAccountId)
                .setAmount(amount)
                .setCurrency(currency);


        FundTransferData fundTransferData = new FundTransferData()
                .setDebitAccount(debitAccount)
                .setCreditAccount(creditAccount)
                .setDebitAmount(debitAmount)
                .setCreditAmount(creditAmount);

        when(transferOrchestratorServiceMock.triggerTransfer(debitAccountId, creditAccountId, amount, currency))
                .thenReturn(fundTransferData);

        // WHEN AND THEN
        mockMvc.perform(post("/api/transfers").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fundsTransferResource)))
                .andExpect(status().isCreated());
    }

    @SneakyThrows
    @Test
    void transferFunds_shouldReturn400WhenCurrencyInvalid() {
        // GIVEN
        Long debitAccountId = 1L;
        Long creditAccountId = 2L;
        BigDecimal amount = BigDecimal.valueOf(1L);
        String currency = "USDA";

        FundsTransferResource fundsTransferResource = new FundsTransferResource()
                .setDebitAccount(debitAccountId)
                .setCreditAccount(creditAccountId)
                .setAmount(amount)
                .setCurrency(currency);

        when(transferOrchestratorServiceMock.triggerTransfer(debitAccountId, creditAccountId, amount, currency))
                .thenThrow(ExchangeRateNotFoundException.class);

        // WHEN AND THEN
        mockMvc.perform(post("/api/transfers").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fundsTransferResource)))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void transferFunds_shouldReturn400WhenAccountIdInvalid() {
        // GIVEN
        Long debitAccountId = 1L;
        Long creditAccountId = 2L;
        BigDecimal amount = BigDecimal.valueOf(1L);
        String currency = "USDA";

        FundsTransferResource fundsTransferResource = new FundsTransferResource()
                .setDebitAccount(debitAccountId)
                .setCreditAccount(creditAccountId)
                .setAmount(amount)
                .setCurrency(currency);

        when(transferOrchestratorServiceMock.triggerTransfer(debitAccountId, creditAccountId, amount, currency))
                .thenThrow(AccountNotFoundException.class);

        // WHEN AND THEN
        mockMvc.perform(post("/api/transfers").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fundsTransferResource)))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void transferFunds_shouldReturn400WhenAccountBalanceInsufficient() {
        // GIVEN
        Long debitAccountId = 1L;
        Long creditAccountId = 2L;
        BigDecimal amount = BigDecimal.valueOf(1L);
        String currency = "USDA";

        FundsTransferResource fundsTransferResource = new FundsTransferResource()
                .setDebitAccount(debitAccountId)
                .setCreditAccount(creditAccountId)
                .setAmount(amount)
                .setCurrency(currency);

        when(transferOrchestratorServiceMock.triggerTransfer(debitAccountId, creditAccountId, amount, currency))
                .thenThrow(InsufficientBalanceException.class);

        // WHEN AND THEN
        mockMvc.perform(post("/api/transfers").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fundsTransferResource)))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void transferFunds_shouldReturn500WhenTransferFails() {
        // GIVEN
        Long debitAccountId = 1L;
        Long creditAccountId = 2L;
        BigDecimal amount = BigDecimal.valueOf(1L);
        String currency = "USDA";

        FundsTransferResource fundsTransferResource = new FundsTransferResource()
                .setDebitAccount(debitAccountId)
                .setCreditAccount(creditAccountId)
                .setAmount(amount)
                .setCurrency(currency);

        when(transferOrchestratorServiceMock.triggerTransfer(debitAccountId, creditAccountId, amount, currency))
                .thenThrow(RuntimeException.class);

        // WHEN AND THEN
        mockMvc.perform(post("/api/transfers").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fundsTransferResource)))
                .andExpect(status().isInternalServerError());
    }

}
