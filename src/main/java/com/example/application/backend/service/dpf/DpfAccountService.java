package com.example.application.backend.service.dpf;

import com.example.application.backend.entity.dpf.DpfAccounts;
import com.example.application.backend.repository.dpf.DpfAccountsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DpfAccountService {

    @Autowired
    DpfAccountsMapper dpfaccountsMapper;

    public List<DpfAccounts> getDpfsByClient(Integer codeClient){
        return dpfaccountsMapper.getDpfsByClient(codeClient);
    }

}
