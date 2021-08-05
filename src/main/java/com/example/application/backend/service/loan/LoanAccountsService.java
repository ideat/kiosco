package com.example.application.backend.service.loan;

import com.example.application.backend.entity.loan.LoanAccounts;
import com.example.application.backend.repository.loan.LoanAccountsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanAccountsService {

    @Autowired
    private LoanAccountsMapper mapper;

    public List<LoanAccounts> findByCodeClient(Integer codeClient){
        return mapper.findByCodeClient(codeClient);
    }


}
