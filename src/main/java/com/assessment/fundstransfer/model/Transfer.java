package com.assessment.fundstransfer.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table
@Accessors(chain = true)
/*
  Class for audit purposes
 */
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private Long debitAccountId;

    @Column
    private Long creditAccountId;

    @Column
    private BigDecimal debitAmount;

    @Column
    private BigDecimal creditAmount;

    @Column
    private String debitCurrency;

    @Column
    private String creditCurrency;

    @Column
    private LocalDateTime timeStamp;
}

