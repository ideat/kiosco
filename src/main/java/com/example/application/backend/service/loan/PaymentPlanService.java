package com.example.application.backend.service.loan;

import com.example.application.backend.entity.loan.DeferredPaymentPlan;
import com.example.application.backend.entity.loan.HeaderPaymentPlan;
import com.example.application.backend.entity.loan.LoanRate;
import com.example.application.backend.entity.loan.PaymentPlan;
import com.example.application.backend.entity.loan.dto.DeferredPaymentPlanDto;
import com.example.application.backend.entity.loan.dto.DetailPaymentPlanDto;
import com.example.application.backend.entity.loan.dto.PaymentPlanDto;
import com.example.application.backend.repository.loan.DeferredPaymentPlanMapper;
import com.example.application.backend.repository.loan.HeaderPaymentPlanMapper;
import com.example.application.backend.repository.loan.LoanRateMapper;
import com.example.application.backend.repository.loan.PaymentPlanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PaymentPlanService {

    @Autowired
    private HeaderPaymentPlanMapper headerPaymentPlanMapper;

    @Autowired
    private PaymentPlanMapper paymentPlanMapper;

    @Autowired
    private LoanRateMapper loanRateMapper;

    @Autowired
    private DeferredPaymentPlanMapper deferredPaymentPlanMapper;

    public PaymentPlanDto getPaymentPlan(Integer loanNumber){

        HeaderPaymentPlan headerPaymentPlan = headerPaymentPlanMapper.findByNumberLoan(loanNumber);
        List<PaymentPlan> paymentPlanList = paymentPlanMapper.findByNumberLoan(loanNumber);
        List<LoanRate> loanRateList = loanRateMapper.findByLoanNumber(loanNumber);
        List<DeferredPaymentPlan> deferredPaymentPlanList = deferredPaymentPlanMapper.findByLoanNumber(loanNumber);

        List<DetailPaymentPlanDto> detailPaymentPlanDtoList = new ArrayList<>();
        Double balance = headerPaymentPlan.getPrmprmapt();

        DetailPaymentPlanDto firstDetail = new DetailPaymentPlanDto();
        firstDetail.setPaymentDate(null);
        firstDetail.setInstallmentNumber(0);
        firstDetail.setPrincipal(0.0);
        firstDetail.setInterest(0.0);
        firstDetail.setGeneral(0.0);
        firstDetail.setSecure(0.0);
        firstDetail.setOther(0.0);
        firstDetail.setCharge(0.0);
        firstDetail.setTotal(0.0);
        firstDetail.setBalance(balance);

        detailPaymentPlanDtoList.add(firstDetail);

        for(PaymentPlan p: paymentPlanList){
            DetailPaymentPlanDto dto = new DetailPaymentPlanDto();

            dto.setPaymentDate(p.getPrppgfech());
            dto.setInstallmentNumber(p.getPrppgnpag());
            dto.setPrincipal(p.getPrppgcapi());
            dto.setInterest(p.getPrppginte());
            dto.setGeneral(p.getPrppggral());
            dto.setSecure(p.getPrppgsegu());
            dto.setOther(p.getPrppgotro());
            dto.setCharge(p.getPrppgcarg());
            dto.setTotal(p.getPrppgtota());
            balance = balance - p.getPrppgcapi();
            dto.setBalance(balance);

            detailPaymentPlanDtoList.add(dto);
        }

        List<DeferredPaymentPlanDto> deferredPaymentPlanDtoList = new ArrayList<>();
        List<DeferredPaymentPlan> auxList = deferredPaymentPlanList.stream()
                .filter(f -> f.getPrdipcarg().equals(420) ||
                        f.getPrdipcarg().equals(421) || f.getPrdipcarg().equals(424) || f.getPrdipcarg().equals(425))
                .collect(Collectors.toCollection( () ->
                        new TreeSet<>(Comparator.comparing(DeferredPaymentPlan::getPrdipfreg))))
                .stream().collect(Collectors.toList());
       auxList.sort(Comparator.comparing(DeferredPaymentPlan::getPrdipfreg));



        for(DeferredPaymentPlan d: auxList){
            DeferredPaymentPlanDto dto = new DeferredPaymentPlanDto();

            dto.setRegisterDate(d.getPrdipfreg());
            dto.setPaymentDate(d.getPrdipfpag());
            Double principal = deferredPaymentPlanList.stream()
                    .filter(f -> f.getPrdipfreg().equals(d.getPrdipfreg()) && (f.getPrdipcarg().equals(420) ||
                            f.getPrdipcarg().equals(421) || f.getPrdipcarg().equals(424) || f.getPrdipcarg().equals(425)))
                    .mapToDouble(DeferredPaymentPlan::getPrdipcuot).sum();
            Double interest = deferredPaymentPlanList.stream()
                    .filter(f -> f.getPrdipfreg().equals(d.getPrdipfreg()) && (f.getPrdipcarg().equals(422) ||
                            f.getPrdipcarg().equals(423) || f.getPrdipcarg().equals(426) || f.getPrdipcarg().equals(427)))
                    .mapToDouble(DeferredPaymentPlan::getPrdipcuot).sum();
            dto.setPrincipal(principal);
            dto.setInterest(interest);
            dto.setTotal(principal + interest);
            dto.setIsPayment(d.getPrdipmpag());

            deferredPaymentPlanDtoList.add(dto);
        }


        PaymentPlanDto paymentPlanDto = new PaymentPlanDto();
        paymentPlanDto.setLoanNumber(headerPaymentPlan.getPrmprnpre());
        paymentPlanDto.setCodeClient(headerPaymentPlan.getPrmprcage());
        paymentPlanDto.setFullName(headerPaymentPlan.getGbagenomb());
        paymentPlanDto.setAmount(headerPaymentPlan.getPrmprmapt());
        paymentPlanDto.setCurrency(headerPaymentPlan.getPrmprcmon().equals(1)?"Bs.":"$us.");
        paymentPlanDto.setCurrencyName(headerPaymentPlan.getPrmprcmon().equals(1)?"BOLIVIANOS.":"DOLARES AMERICANOS");
        paymentPlanDto.setTypeLoan(headerPaymentPlan.getPrtcrdesc());
        paymentPlanDto.setState(headerPaymentPlan.getPrmprstat());
        paymentPlanDto.setRate(loanRateList.get(0).getPrtsatbas());
        Integer term = 0;
        if(headerPaymentPlan.getPrmpruplz().equals(1)){
            term = (headerPaymentPlan.getPrmprplzo()/360)*12;
        }else if(headerPaymentPlan.getPrmpruplz().equals(2)){
            term = headerPaymentPlan.getPrmprplzo()/30;
        }else {
            term = headerPaymentPlan.getPrmprplzo();
        }

        paymentPlanDto.setTerm(term);
        paymentPlanDto.setDisbursementDate(headerPaymentPlan.getPrmprfdes());
        paymentPlanDto.setPaymentPeriodPrincipal(headerPaymentPlan.getPrmprppgk());
        paymentPlanDto.setPaymentPeriodInterest(headerPaymentPlan.getPrmprppgi());
        paymentPlanDto.setFeeType(headerPaymentPlan.getPrcondesc());
        paymentPlanDto.setDetailPaymentPlanDtoList(detailPaymentPlanDtoList);
        paymentPlanDto.setDeferredPaymentPlanDtoList(deferredPaymentPlanDtoList);

        return paymentPlanDto;
    }
}
