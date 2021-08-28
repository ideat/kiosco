package com.example.application.views.digitalbank;

import com.example.application.backend.entity.autoform.AccountServiceOperation;
import com.example.application.backend.entity.autoform.Forms;
import com.example.application.backend.entity.autoform.Parameter;
import com.example.application.backend.entity.autoform.Service;
import com.example.application.backend.rest.AutoFormService;
import com.example.application.views.util.Util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
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

        Button btnDigitalBank = new Button(new Image("/buttons/Botones-12.png","Solicitar Banca Digital"));
        btnDigitalBank.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnDigitalBank.setWidthFull();

        VerticalLayout layout = new VerticalLayout();
        layout.getElement().getStyle().set("background-image","url('/backgrounds/digitalbank.png')" );

        VerticalLayout spaceV = new VerticalLayout();
        spaceV.setHeight("250px");
        VerticalLayout space2 = new VerticalLayout();
        space2.setHeight("50px");

        VerticalLayout layout2 = new VerticalLayout();
        HorizontalLayout spaceH = new HorizontalLayout();
        spaceH.setWidth("200px");
        layout2.add(btnCreateRequest, space2, btnDigitalBank);
//        layout2.setAlignItems(FlexComponent.Alignment.START);
//        layout2.setHorizontalComponentAlignment(FlexComponent.Alignment.START,btnCreateRequest,btnDigitalBank);
        layout2.setSpacing(true);

        HorizontalLayout layout3 = new HorizontalLayout();
        layout3.add(spaceH,layout2);

        layout.add(spaceV,layout3);
//        layout.setHorizontalComponentAlignment(FlexComponent.Alignment.START,layout2);
        layout.setSizeFull();

        btnDigitalBank.addClickListener(click -> {
//            String url = "https://web.bankingly.com/Administration.WebUI/Pages/General/Login.aspx?ID=LaPromotora";
//            UI.getCurrent().getPage().open(url,"_blank, width=200,height=100");
//            UI.getCurrent().getPage().executeJs("window.location.href = 'https://web.bankingly.com/Administration.WebUI/Pages/General/Login.aspx?ID=LaPromotora'");
//            DialogBrowserDigitalBank dialog = new DialogBrowserDigitalBank();
//            dialog.open();

            UI.getCurrent().getPage().executeJs(" window.open(\"https://web.bankingly.com/Administration.WebUI/Pages/General/Login.aspx?ID=LaPromotora\", \"Banca Digital\", \"top=200,left=500,width=950,height=700\")");
//            FirefoxBinary binary = new FirefoxBinary(new File("C:\\Program Files\\Mozilla Firefox\\firefox.exe"));
//            FirefoxProfile profile = new FirefoxProfile();
//            System.setProperty("webdriver.firefox.bin", "C:\\auto-form\\driver\\geckodriver.exe");
//            System.setProperty("webdriver.gecko.driver","C:\\auto-form\\driver\\geckodriver.exe");
//            WebDriver driver = new FirefoxDriver();
//            driver.get("https://web.bankingly.com/Administration.WebUI/Pages/General/Login.aspx?ID=LaPromotora");
        });

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
