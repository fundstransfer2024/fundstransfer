package com.assessment.fundstransfer.controller;


import com.assessment.fundstransfer.exception.AccountNotFoundException;
import com.assessment.fundstransfer.model.Account;
import com.assessment.fundstransfer.resource.AccountResource;
import com.assessment.fundstransfer.service.AccountRepositoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {
    @MockBean
    private AccountRepositoryService accountRepositoryServiceMock;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static AccountResource createAccountResource(String currency, BigDecimal balance) {
        return new AccountResource()
                .setCurrency(currency)
                .setBalance(balance);
    }

    @SneakyThrows
    @Test
    void addAccount_shouldReturn201WhenAccountCreationSucceeds() {
        // GIVEN
        AccountResource accountResource = createAccountResource("USD", BigDecimal.valueOf(1000L));
        when(accountRepositoryServiceMock.addAccount(accountResource)).thenReturn(new Account());

        // WHEN AND THEN
        mockMvc.perform(post("/api/accounts").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountResource)))
                .andExpect(status().isCreated());
    }

    @SneakyThrows
    @Test
    void updateAccount_shouldReturn200WhenAccountUpdateSucceeds() {
        // GIVEN
        AccountResource accountResource = createAccountResource("USD", BigDecimal.valueOf(1000L));
        when(accountRepositoryServiceMock.updateAccount(accountResource)).thenReturn(new Account());

        // WHEN AND THEN
        mockMvc.perform(put("/api/accounts/1").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountResource)))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void updateAccount_shouldReturn404WhenAccountNotFound() {
        // GIVEN
        AccountResource accountResource = createAccountResource("USD", BigDecimal.valueOf(1000L));
        when(accountRepositoryServiceMock.updateAccount(accountResource))
                .thenThrow(AccountNotFoundException.class);

        // WHEN AND THEN
        mockMvc.perform(put("/api/accounts/1").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountResource)))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void updateAccount_shouldReturn500WhenErrorOccurs() {
        // GIVEN
        AccountResource accountResource = createAccountResource("USD", BigDecimal.valueOf(1000L));
        when(accountRepositoryServiceMock.updateAccount(accountResource))
                .thenThrow(RuntimeException.class);

        // WHEN AND THEN
        mockMvc.perform(put("/api/accounts/1").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountResource)))
                .andExpect(status().isInternalServerError());
    }

    @SneakyThrows
    @Test
    void getAccounts_shouldReturn200WhenAccountsFound() {
        // GIVEN
        List<Account> accounts = new ArrayList<>();
        accounts.add(new Account());
        when(accountRepositoryServiceMock.getAccounts()).thenReturn(accounts);

        // WHEN AND THEN
        mockMvc.perform(get("/api/accounts").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void getAccounts_shouldReturn204WhenAccountListEmpty() {
        // GIVEN
        when(accountRepositoryServiceMock.getAccounts()).thenReturn(new ArrayList<>());


        // WHEN AND THEN
        mockMvc.perform(get("/api/accounts").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @SneakyThrows
    @Test
    void getAccounts_shouldReturn500WhenErrorOccurs() {
        // GIVEN
        when(accountRepositoryServiceMock.getAccounts())
                .thenThrow(RuntimeException.class);

        // WHEN AND THEN
        mockMvc.perform(get("/api/accounts").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @SneakyThrows
    @Test
    void getAccountById_shouldReturn200WhenAccountFound() {
        // GIVEN
        when(accountRepositoryServiceMock.getAccount(1L)).thenReturn(new Account());

        // WHEN AND THEN
        mockMvc.perform(get("/api/accounts/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void getAccountById_shouldReturn404WhenAccountNotFound() {
        // GIVEN
        when(accountRepositoryServiceMock.getAccount(1L))
                .thenThrow(AccountNotFoundException.class);

        // WHEN AND THEN
        mockMvc.perform(get("/api/accounts/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void getAccountById_shouldReturn500WhenErrorOccurs() {
        // GIVEN
        when(accountRepositoryServiceMock.getAccount(1L))
                .thenThrow(RuntimeException.class);

        // WHEN AND THEN
        mockMvc.perform(get("/api/accounts/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @SneakyThrows
    @Test
    void deleteAccount_shouldReturn200WhenAccountFound() {

        // WHEN AND THEN
        mockMvc.perform(delete("/api/accounts/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @SneakyThrows
    @Test
    void deleteAccount_shouldReturn404WhenAccountNotFound() {
        // GIVEN
        doThrow(new AccountNotFoundException())
                .when(accountRepositoryServiceMock).deleteById(1L);


        // WHEN AND THEN
        mockMvc.perform(delete("/api/accounts/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void deleteAccount_shouldReturn500WhenErrorOccurs() {
        // GIVEN
        doThrow(new RuntimeException())
                .when(accountRepositoryServiceMock).deleteById(1L);

        // WHEN AND THEN
        mockMvc.perform(delete("/api/accounts/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }


}
