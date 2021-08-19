package com.example.application.views.digitalbank;

import com.example.application.backend.entity.autoform.AccountServiceOperation;
import com.example.application.backend.entity.autoform.Forms;
import com.example.application.backend.entity.autoform.Service;
import com.example.application.backend.rest.AutoFormService;
import com.example.application.views.util.UIUtils;
import com.example.application.views.util.Util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class DigitalBankView {

    @Autowired
    private AutoFormService autoFormService;

    public VerticalLayout getLayoutDigitalBank(Integer codeClient){

        Button btnCreateRequest = new Button(new Image("/buttons/Botones-09.png","Tarifario"));
        btnCreateRequest.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnCreateRequest.setWidthFull();
        VerticalLayout layout = new VerticalLayout();
        layout.getElement().getStyle().set("background-image","url('/backgrounds/digitalbank.png')" );

        VerticalLayout space = new VerticalLayout();
        space.setHeight("300px");

        HorizontalLayout layout2 = new HorizontalLayout();
        HorizontalLayout spaceH = new HorizontalLayout();
        spaceH.setWidth("300px");
        layout2.add(spaceH,btnCreateRequest);
        layout2.setAlignItems(FlexComponent.Alignment.CENTER);
//        layout2.setWidth("400px");
        layout.add(space,layout2);
        layout.setHorizontalComponentAlignment(FlexComponent.Alignment.START,layout2);
        layout.setSizeFull();

        btnCreateRequest.addClickListener(click -> {
           Forms forms = autoFormService.findFromKioscoByIdClientAndTypeFormAndCategoryTypeForm(codeClient,"BANCA DIGITAL","VARIOS");
           if (forms.getId()==null){
               Forms newForm = new Forms();
               newForm.setIdClient(codeClient);
               newForm.setNameTypeForm("BANCA DIGITAL");
               newForm.setCategoryTypeForm("VARIOS");
               newForm.setAccountServiceOperation("");
               autoFormService.create(newForm);
               Notification notification = new Notification();
               notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
               Span label = new Span("Solicitud creada, pase por plataforma para firmar");
               notification.add(label);
               notification.setDuration(6000);
               notification.setPosition(Notification.Position.MIDDLE);
               notification.open();
           }
        });

        return layout;
    }

    private String createAccountServiceOperation(){
        AccountServiceOperation accountServiceOperation = new AccountServiceOperation();
        accountServiceOperation.setId(UUID.randomUUID().toString());
        Date currentDate = new Date();
        accountServiceOperation.setCreateDate(Util.formatDate(currentDate, "dd/MM/yyyy"));

        return "";

    }

    public String getServices()  {
        List<Service> serviceState = new ArrayList<>();
        List<Service> auxService = new ArrayList<>();
        List<Service> auxOperation = new ArrayList<>();
//
//        for(Service s: serviceListGlobal){
//            s.setChecked("NO");
//            auxService.add(s);
//        }
//        for(Service s: operationListGlobal){
//            s.setChecked("NO");
//            auxOperation.add(s);
//        }
//
//        for(String s:servicesSelected){
//            Service service = auxService.stream()
//                    .filter(f -> f.getName().equals(s))
//                    .collect(Collectors.toList()).get(0);
//            service.setChecked("SI");
//            auxService.removeIf(d -> d.getName().equals(s));
//            auxService.add(service);
//        }
//
//        for(String s:operationsSelected){
//            Service service = operationListGlobal.stream()
//                    .filter(f -> f.getName().equals(s))
//                    .collect(Collectors.toList()).get(0);
//            service.setChecked("SI");
//            auxOperation.removeIf(d -> d.getName().equals(s));
//            auxOperation.add(service);
//        }
//        serviceListGlobal = auxService;
//        operationListGlobal = auxOperation;
//
//        serviceState.addAll(serviceListGlobal);
//        serviceState.addAll(operationListGlobal);
//
//        ObjectMapper mapper = new ObjectMapper();
//        String result = "";
//        try {
//            result = mapper.writeValueAsString(serviceState);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }

        return  null;//result;
    }
}
