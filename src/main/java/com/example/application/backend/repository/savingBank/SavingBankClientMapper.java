package com.example.application.backend.repository.savingBank;

import com.example.application.backend.entity.savingBank.SavingBankClient;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SavingBankClientMapper {
    @Select(" select gbagenomb full_name, camcancta account, catcadesc product_name, " +
            " gbconabre currency, (camcasact)*-1 balance, cacondesc state " +
            " from camca " +
            " inner join gbage on gbagecage = camcacage " +
            " inner join catca on catcatpca = camcatpca " +
            " inner join gbcon on gbconpfij = 5 and gbconcorr = camcacmon " +
            " inner join cacon on caconpref = 4 and caconcorr = camcastat" +
            " where camcacage = #{codeClient} " +
            " and camcastat in (1,2) ")
    List<SavingBankClient> getSavingBanksClient(@Param("codeClient") Integer codeClient);


}
