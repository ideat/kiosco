package com.example.application.backend.repository.client;

import com.example.application.backend.entity.client.Client;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ClientMapper {

    @Select("select gbagenomb full_name, gbagecage code_client, trim(gbagendid) id_card, gbagetlfd " +
            " from gbage " +
            " where gbagecage = #{codeClient}")
    Client getClientByCode(@Param("codeClient") Integer codeClient);

    @Select("select gbagenomb full_name, gbagecage code_client, trim(gbagendid) id_card, gbagetlfd " +
            " from gbage " +
            " where trim(gbagendid) = #{idCard}")
    Client getClientByIdCard(@Param("idCard") String idCard);
}
