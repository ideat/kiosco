package com.example.application.backend.entity.dpf;

import lombok.Data;

import java.util.Date;

@Data
public class DpfAccounts {
    private String numberDpf;

    private String numberCertificate;

    private Date expireDate;

    private Date renovationDate;

    private Integer term;

    private Date registerDate;

    private Integer codeClient;

    private String currency;

    private String fullName;

    private Double amount;


}
