package com.example.application.backend.entity.loan;

import lombok.Data;

import java.util.Date;

@Data
public class DetailTransaction {

    private Integer prtdtntra;

    private Integer prtdtttrn;

    private Integer prtdtnpre;

    private Date prtdtftra;

    private Integer prtdtpref;

    private Integer prtdtccon;

    private Double prtdtimpp;

    private String prtdtdesc;
}
