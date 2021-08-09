package com.example.application.backend.repository.dpf;

import com.example.application.backend.entity.dpf.DpfAccounts;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DpfAccountsMapper {

    @Select(" select pfmdpndep number_dpf, pfmdpncrt number_certificate, pfmdpfvto expire_date, " +
            " pfmdpfern renovation_date, pfmdpplzo term, pfmdpfreg register_date, gbagecage code_client, " +
            " gbconabre currency, gbagenomb full_name, pfmdpcapi amount " +
            " from gbage " +
            " inner join pfmdp on gbagecage = pfmdpcage " +
            " inner join gbcon on gbconpfij = 5 and gbconcorr = pfmdpcmon " +
            " where pfmdpstat = 1 " +
            " and pfmdpcage = #{codeClient} ")
    List<DpfAccounts> getDpfsByClient(@Param("codeClient") Integer codeClient);



}
