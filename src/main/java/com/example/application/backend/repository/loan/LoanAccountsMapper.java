package com.example.application.backend.repository.loan;

import com.example.application.backend.entity.loan.LoanAccounts;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LoanAccountsMapper {

    @Select(" select prmprcage code_client, prmprnpre number_loan, prmprsald balance,  " +
            "gbconabre currency, prtcrdesc name_product, prcondesc state " +
            "from prmpr " +
            "inner join prtcr on prtcrtcre = prmprtcre " +
            "inner join prcon on prconpref = 4 and prconcorr= prmprstat " +
            "inner join gbcon on (gbconpfij = 10 and gbconcorr = prmprcmon)    " +
            "where prmprcage = #{codeClient} " +
            "and prmprstat in (2,3,4,5,6)")
    List<LoanAccounts> findByCodeClient(@Param("codeClient") Integer codeClient);
}
