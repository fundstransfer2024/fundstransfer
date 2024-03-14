package com.assessment.fundstransfer.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table
@Accessors(chain = true)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ownerId;

    @Column
    private String currency;

    @Column
    private BigDecimal balance;

}
