package com.example.application.backend.entity.loan;

import lombok.Data;

@Data
public class LoanAccounts {

    private String codeClient;

    private Integer numberLoan;

    private Double balance;

    private String currency;

    private String nameProduct;

    private String state;
}
