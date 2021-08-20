package com.example.application.views.debitCard;

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
public class DebitCardView {

    @Autowired
    private AutoFormService autoFormService;

    public VerticalLayout getLayoutDebitCard(Integer codeClient){

        Button btnCreateRequest = new Button(new Image("/buttons/Botones-10.png","Solicitar Tarjeta de Debito"));
        btnCreateRequest.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnCreateRequest.setWidthFull();
        VerticalLayout layout = new VerticalLayout();
        layout.getElement().getStyle().set("background-image","url('/backgrounds/debitcard.png')" );

        VerticalLayout space = new VerticalLayout();
        space.setHeight("300px");

        HorizontalLayout layout2 = new HorizontalLayout();
        HorizontalLayout spaceH = new HorizontalLayout();
        spaceH.setWidth("300px");
        layout2.add(spaceH,btnCreateRequest);
        layout2.setAlignItems(FlexComponent.Alignment.CENTER);
//        layout2.setWidth("400px");
        layout.add(space,layout2);
        layout.setHorizontalComponentAlignment(FlexComponent.Alignment.END,layout2);
        layout.setSizeFull();

        btnCreateRequest.addClickListener(click -> {
//            Forms forms = autoFormService.findFromKioscoByIdClientAndTypeFormAndCategoryTypeForm(codeClient,"SERVICIOS TD","VARIOS");
//            if (forms.getId()==null){
                Forms newForm = new Forms();
                newForm.setIdClient(codeClient);
                newForm.setNameTypeForm("SERVICIOS TD");
                newForm.setCategoryTypeForm("VARIOS");
                newForm.setAccountServiceOperation(createAccountServiceOperation());
                autoFormService.create(newForm);
                Notification notification = new Notification();
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                Span label = new Span("Solicitud creada, pase por plataforma para firmar");
                notification.add(label);
                notification.setDuration(6000);
                notification.setPosition(Notification.Position.MIDDLE);
                notification.open();
//            }
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
        accountServiceOperation.setServices(fillNewServices());
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

    private String fillNewServices()  {
        List<Parameter> parameterList= autoFormService.findFromKioscoAllActive();
        List<Service> serviceList = new ArrayList<>();
        for(Parameter parameter : parameterList){
            Service s = new Service();
            if(parameter.getCategory().equals("TARJETA DEBITO, SERVICIOS") ){
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
