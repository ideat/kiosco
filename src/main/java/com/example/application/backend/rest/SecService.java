package com.example.application.backend.rest;

import com.example.application.backend.entity.sec.StageHistoryCreditRequestDto;
import com.example.application.backend.entity.sec.SummaryCreditRequestStage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class SecService {
    @Value("${url_sec}")
    private String url;

    RestTemplate restTemplate = new RestTemplate();

    public List<SummaryCreditRequestStage> getSummaryByIdCard(String idCard){
        final String uri = url +"/getSummaryByIdCard/"+idCard.trim();

        HttpEntity<SummaryCreditRequestStage[]> entity = new HttpEntity<>(new HttpHeaders());
        ResponseEntity<SummaryCreditRequestStage[]> response = restTemplate.exchange(uri, HttpMethod.GET,entity,SummaryCreditRequestStage[].class);

        return Arrays.asList(response.getBody());
    }

    public List<StageHistoryCreditRequestDto> getDetailByNumberRequest(Integer numberRequest){
        final String uri = url +"/getDetailByNumberRequest";

        HttpHeaders headers = new HttpHeaders();
        headers.set("number-request", numberRequest.toString());

        HttpEntity<StageHistoryCreditRequestDto[]> entity = new HttpEntity<>(headers);
        ResponseEntity<StageHistoryCreditRequestDto[]> response = restTemplate
                .exchange(uri, HttpMethod.GET,entity,StageHistoryCreditRequestDto[].class);
        return Arrays.asList(response.getBody());
    }
}
