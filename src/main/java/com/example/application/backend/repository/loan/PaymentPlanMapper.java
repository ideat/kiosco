package com.example.application.backend.repository.loan;

import com.example.application.backend.entity.loan.PaymentPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PaymentPlanMapper {

    @Select(" select * from prppg where prppgnpre = #{numberLoan}")
    List<PaymentPlan> findByNumberLoan(@Param("numberLoan") Integer numberLoan);
}
