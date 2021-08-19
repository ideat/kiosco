package com.example.application.backend.rest;

import com.example.application.backend.entity.autoform.Forms;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AutoFormService {
    @Value("${url_auto_form}")
    private String url;

    RestTemplate restTemplate = new RestTemplate();

    public Forms create(Forms forms){
        final String uri = url + "/form/create";
        forms.setOriginModule("KIOSCO");
        HttpEntity<Forms> entity = new HttpEntity<>(forms);
        ResponseEntity<Forms> response = restTemplate.postForEntity(uri,entity,Forms.class);
        return response.getBody();
    }

    public Forms findFromKioscoByIdClientAndTypeFormAndCategoryTypeForm(Integer idClient, String nameTypeForm, String categoryTypeForm ){
        final String uri = url + "/form/findFromKioscoByIdClientAndTypeFormAndCategoryTypeForm";

        HttpHeaders headers = new HttpHeaders();
        headers.add("id_client",idClient.toString());
        headers.add("name_type_form",nameTypeForm);
        headers.add("category_type_form",categoryTypeForm);
        HttpEntity<Forms> entity = new HttpEntity<>(headers);

        ResponseEntity<Forms> response = restTemplate.exchange(uri, HttpMethod.GET,entity,Forms.class);

        return response.getBody();

    }
}
