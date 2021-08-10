package com.example.application.backend.entity.loan.dto;

import lombok.Data;

import java.util.Date;

@Data
public class DetailPaymentPlanDto {

    private Date paymentDate;

    private Integer installmentNumber;

    private Double principal;

    private Double interest;

    private Double general;

    private Double secure;

    private Double other;

    private Double charge;

    private Double total;

    private Double balance;
}
