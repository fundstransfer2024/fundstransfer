package com.assessment.fundstransfer.controller;


import com.assessment.fundstransfer.model.Account;
import com.assessment.fundstransfer.service.AccountRepositoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {
    @MockBean
    private AccountRepositoryService accountRepositoryServiceMock;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static Account createAccount(Long creditAccountId, String currency, BigDecimal balance) {
        return new Account()
                .setOwnerId(creditAccountId)
                .setCurrency(currency)
                .setBalance(balance);
    }

    @Test
    void addAccount_shouldReturn201WhenAccountCreationSucceeds() {
        // GIVEN
        Account account = createAccount(1L, "USD", BigDecimal.valueOf(1000L));

        // WHEN
        when(accountRepositoryService.addAccount(account)).thenReturn(a);

        // WHEN AND THEN
        mockMvc.perform(post("/api/accounts").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString()))
                .andExpect(status().isCreated());
    }
}
