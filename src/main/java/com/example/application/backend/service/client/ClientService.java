package com.example.application.backend.service.client;

import com.example.application.backend.entity.client.Client;
import com.example.application.backend.repository.client.ClientMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    @Autowired
    ClientMapper mapper;

    public Client getClientByCode(Integer codeClient){
        return mapper.getClientByCode(codeClient);
    }

    public Client getClientByIdCard(String idCard){
        return mapper.getClientByIdCard(idCard);
    }
}
