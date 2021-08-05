package com.example.application.backend.repository.loan;

import com.example.application.backend.entity.loan.MasterTransaction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MasterTransactionMapper {

    @Select("select prhtrntra, prhtrnpre, prhtrftra, prhtrttrn, prhtrimpt " +
            " from prhtr " +
            " where prhtrnpre = #{loanNumber} " +
            " order by prhtrftra ")
    List<MasterTransaction> findMasterTransactionByLoanNumber(@Param("loanNumber") Integer loanNumber);
}
