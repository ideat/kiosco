package com.example.application.views.digitalbank;

import com.example.application.backend.entity.autoform.AccountServiceOperation;
import com.example.application.backend.entity.autoform.Forms;
import com.example.application.backend.entity.autoform.Parameter;
import com.example.application.backend.entity.autoform.Service;
import com.example.application.backend.rest.AutoFormService;
import com.example.application.views.util.Util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class DigitalBankView {

    @Autowired
    private AutoFormService autoFormService;

    public VerticalLayout getLayoutDigitalBank(Integer codeClient){

        Button btnCreateRequest = new Button(new Image("/buttons/Botones-09.png","Solicitar Banca Digital"));
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
               newForm.setAccountServiceOperation(createAccountServiceOperation());
               autoFormService.create(newForm);
               Notification notification = new Notification();
               notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
               Span label = new Span("Solicitud creada, pase por Plataforma para Firmar");
               notification.add(label);
               notification.setDuration(6000);
               notification.setPosition(Notification.Position.MIDDLE);
               notification.open();
           }else{
               Notification notification = new Notification();
               notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST,NotificationVariant.LUMO_SUCCESS);
               Span label = new Span("Ya cuenta con el servicio de Banca Digital, puede pasar por Plataforma");
               label.addClassName("text-color");
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
        accountServiceOperation.setDeliverDate(null);
        accountServiceOperation.setHourCreate(null);
        accountServiceOperation.setServices(fillNewServicesAndOperations());
        accountServiceOperation.setReasonOpening("");
        accountServiceOperation.setExtensionAmount(0.0);
        accountServiceOperation.setDecreaseAmount(0.0);
        accountServiceOperation.setAccountSavingBank(null);
        accountServiceOperation.setOriginModule("KIOSCO");

        List<AccountServiceOperation> accounts = new ArrayList<>();
        accounts.add(accountServiceOperation);
        ObjectMapper mapper = new ObjectMapper();
        String result = "";
        try {
            result = mapper.writeValueAsString(accounts);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;

    }

    private String fillNewServicesAndOperations()  {
        List<Parameter> parameterList= autoFormService.findFromKioscoAllActive();
        List<Service> serviceList = new ArrayList<>();
        for(Parameter parameter : parameterList){
            Service s = new Service();
            if(parameter.getCategory().equals("BANCA DIGITAL, SERVICIOS") ||
                    parameter.getCategory().equals("BANCA DIGITAL, OPERACIONES")){
                s.setName(parameter.getName());
                s.setChecked("NO");
                s.setCategory(parameter.getCategory());
                serviceList.add(s);
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        String result = null;
        try {
            result = mapper.writeValueAsString(serviceList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
