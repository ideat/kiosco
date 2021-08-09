package com.example.application.backend.repository.dpf;

import com.example.application.backend.entity.dpf.HeaderBalanceDpf;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface HeaderBalanceDpfMapper {

    @Select(" select pfmdpndep, pfmdpfvto, pfmdpplzo, pfmdpfreg, gbagecage,  " +
            " pfmdpcmon, gbagenomb, pfmdpcapi, pftdpdesc, pfmdpcorr, pfmdpfror, pfmdpcren, " +
            " pfmdpfpro, pfmdpppgi " +
            " from gbage  " +
            " inner join pfmdp on gbagecage = pfmdpcage  " +
            " inner join pftdp on pftdptdep = pfmdptdep " +
            " where pfmdpndep = #{account}" +
            " and pfmdpstat = 1 ")
    HeaderBalanceDpf findByAccount(@Param("account") String account);


}
