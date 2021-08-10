package com.example.application.backend.service.dpf;

import com.example.application.backend.entity.dpf.dto.ProductRateTermAmount;
import com.example.application.backend.repository.loan.ProductRateTermAmountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductRateTermAmountService {

    @Autowired
    private ProductRateTermAmountMapper mapper;

    public List<ProductRateTermAmount> findAll() {
        return mapper.findAll();
    }
}
