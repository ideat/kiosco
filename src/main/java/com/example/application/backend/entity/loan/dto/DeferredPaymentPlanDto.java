package com.example.application.backend.entity.loan.dto;

import lombok.Data;

import java.util.Date;

@Data
public class DeferredPaymentPlanDto {

    private Date registerDate;

    private Date paymentDate;

    private Double principal;

    private Double interest;

    private Double total;

    private String isPayment;
}
