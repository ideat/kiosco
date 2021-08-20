package com.example.application.views;

import com.example.application.views.debitCard.DebitCardView;
import com.example.application.views.digitalbank.DigitalBankView;
import com.example.application.views.dpf.DpfView;
import com.example.application.views.loan.LoanView;
import com.example.application.views.savingbank.SavingBankView;
import com.helger.commons.state.ICloseable;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.InitialPageSettings;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.PageConfigurator;
import com.vaadin.flow.server.VaadinSession;
import de.mekaso.vaadin.addons.Carousel;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@PageTitle("Opciones")
@Route("")
@RouteAlias("kiosco")
@CssImport("./styles/styles.css")
@PWA(name = "Kiosco UI", shortName = "Kiosco UI", iconPath = "images/logo-18.png", backgroundColor = "#5074a4", themeColor = "#5074a4")
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
public class MainView extends VerticalLayout implements RouterLayout, HasUrlParameter<String> {

    private static final String BACKGROUND = "hsla(%s, 100%%, 50%%, 0.8)";

    @Autowired
    private SavingBankView savingBankView;

    @Autowired
    private DpfView dpfView;

    @Autowired
    private LoanView loanView;

    @Autowired
    private DigitalBankView digitalBankView;

    @Autowired
    private DebitCardView debitCardView;

    private Button btnSavingBank;
    private Button btnDpf;
    private Button btnLoan;
    private Button btnDebitCard;
    private Button btnDigitalBank;
    private Button btnPersonalData;

    private Carousel carousel;

    private Map<String, List<String>> param;


    public MainView() {

//       this.getElement().getStyle().set("background-image","url('background.jpg')");
        this.getElement().getStyle().set("background","whitesmoke");
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent,  @OptionalParameter String s) {
        VaadinSession.getCurrent()
                .getSession()
                .setMaxInactiveInterval(30000);

        Location location = beforeEvent.getLocation();
        QueryParameters qp =  location.getQueryParameters();
        param = qp.getParameters();


    }
    
    @Override
    protected void onAttach(AttachEvent attachEvent){
        super.setAlignItems(FlexComponent.Alignment.CENTER);
        super.setHeightFull();
        carousel = Carousel.create();
        carousel.setWidth("1500px");
        carousel.setHeight("800px");

        //Layout SavingBank
        VerticalLayout layoutSavingBank = createSimpleDiv(5);
        layoutSavingBank.add(savingBankView.getLayoutSavingBank(26907));
        layoutSavingBank.setSizeFull();
        layoutSavingBank.setAlignItems(Alignment.BASELINE);


        //Layout DPF
        VerticalLayout layoutDpf = createSimpleDiv(5);
        layoutDpf.add(dpfView.getLayoutDpfAccount(3155));
        layoutDpf.setAlignItems(Alignment.END);
        layoutDpf.setSizeFull();

        //Layout Loan
        VerticalLayout layoutLoan = createSimpleDiv(5);
        layoutLoan.setSizeFull();
        layoutLoan.add(loanView.getLayoutLoanAccounts(27459));
        layoutLoan.setAlignItems(Alignment.END);

        VerticalLayout layoutMenu = createSimpleDiv(5);

        //Digital Bank
        VerticalLayout layoutDigitalBank = createSimpleDiv(5);
        layoutDigitalBank.add(digitalBankView.getLayoutDigitalBank(63718));
        layoutDigitalBank.setSizeFull();
        layoutDigitalBank.setAlignItems(Alignment.START);

        //Debit Card
        VerticalLayout layoutDebitCard = createSimpleDiv(5);
        layoutDebitCard.add(debitCardView.getLayoutDebitCard(63718));
        layoutDebitCard.setSizeFull();
        layoutDebitCard.setAlignItems(Alignment.END);

        layoutMenu.add(menuLayout());
        carousel.add(layoutMenu);
        carousel.add(layoutSavingBank);
        carousel.add(layoutDpf);
        carousel.add(layoutLoan);
        carousel.add(layoutDigitalBank);
        carousel.add(layoutDebitCard);

//        carousel.add(new Button("CANCELAR"));


        Button home = new Button(new Image("/buttons/Botones-00.png","Menu Principal"));
        home.setWidth("380px");
        home.setHeight("100px");
        home.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        home.addClickListener(click -> {
//            Component targetComponent = carousel.getChildren().collect(Collectors.toList()).get(0);
//            carousel.show(targetComponent);
            carousel.show(0);
            if(carousel.getSelectedIndex()!=0){
                carousel.show(0);
            }
        });

        Button exit = new Button(new Image("/buttons/Botones-20.png","Menu Principal"));
        exit.setWidth("380px");
        exit.setHeight("100px");
        exit.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        exit.addClickListener(click -> {
//            Page.getCurrent().getJavaScript().execute("window.onbeforeunload = function (e) { var e = e || window.event; closeMyApplication(); return; };");
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec("taskkill /IM chrome.exe /F");
            } catch (IOException e) {
                e.printStackTrace();
            }


        });

        HorizontalLayout header = new HorizontalLayout();
        header.add(home,exit);

        add(header,carousel);

//        setAlignContent(ContentAlignment.CENTER);

        super.getStyle().set("justify-content", "space-evenly");
        super.getStyle().set("flex-direction", "column");
    }

    private HorizontalLayout menuLayout(){
        HorizontalLayout layout = new HorizontalLayout();
        VerticalLayout optionLayout = layoutOptions();
        layout.add(layoutInfoClient(),optionLayout);
        layout.setSpacing(true);
        layout.setVerticalComponentAlignment(Alignment.END,optionLayout);
        layout.setAlignItems(Alignment.START);
        layout.getStyle().set("background", "whitesmoke");
        layout.getElement().getStyle().set("background-image","url('/backgrounds/menu.png')");

        return layout;
    }

    private VerticalLayout layoutOptions(){

        btnSavingBank = new Button(new Image("/buttons/Botones-01.png","Caja Ahorro"));

//        btnSavingBank.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_LARGE, ButtonVariant.LUMO_CONTRAST);
        btnSavingBank.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnSavingBank.setWidth("380px");
        btnSavingBank.setHeight("100px");
//        btnSavingBank.addClassName("button-font");
        btnSavingBank.addClickListener(click -> carousel.show(1));


        btnDpf = new Button(new Image("/buttons/Botones-02.png","DPF"));
        btnDpf.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnDpf.setWidth("380px");
        btnDpf.setHeight("100px");
//        btnDpf.addClassName("button-font");
        btnDpf.addClickListener(click -> carousel.show(2));
        
        btnLoan = new Button(new Image("/buttons/Botones-03.png","Caja Ahorro"));
        btnLoan.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnLoan.setWidth("380px");
        btnLoan.setHeight("100px");
//        btnLoan.addClassName("button-font");
        btnLoan.addClickListener(click -> carousel.show(3));

        btnDebitCard = new Button(new Image("/buttons/Botones-04.png","Caja Ahorro"));
        btnDebitCard.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnDebitCard.setWidth("380px");
        btnDebitCard.setHeight("100px");
        btnDebitCard.addClickListener(click -> carousel.show(5));
//        btnDebitCard.addClassName("button-font");
        
        btnDigitalBank = new Button(new Image("/buttons/Botones-05.png","Caja Ahorro"));
        btnDigitalBank.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnDigitalBank.setWidth("380px");
        btnDigitalBank.setHeight("100px");
//        btnDigitalBank.addClassName("button-font");
        btnDigitalBank.addClickListener(click -> carousel.show(4));
        
        btnPersonalData = new Button(new Image("/buttons/Botones-06.png","Caja Ahorro"));
        btnPersonalData.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnPersonalData.setWidth("380px");
        btnPersonalData.setHeight("100px");
//        btnPersonalData.addClassName("button-font");

        VerticalLayout layout1 = new VerticalLayout();
//        layout1.getStyle().set("background", "whitesmoke");
        layout1.add(btnSavingBank, btnDpf, btnLoan, btnDebitCard, btnDigitalBank, btnPersonalData);
        layout1.setAlignItems(Alignment.END);

        return layout1;
        
    }

    private Component layoutInfoClient(){
        String name = "MARIA JOSE QUINTANILLA";
        Html intro = new Html(" <H4  style=\"padding-top: 50px; padding-right: 20px; padding-bottom: 20px; padding-left: 500px\"> <p style=\"color:#0D416C;\">Bienvenido </p> </H4> ");
        Html client = new Html( String.format( " <H4 style=\"padding-top: 0px; padding-right: 20px; padding-bottom: 20px; padding-left: 480px; color:#0D416C;\"> <b>  %s </b> </H4>" ,name));
//
//        Html productivity = new Html("<p> <b>Kiosco una manera fácil y conveniente de realizar tus consultas de saldo y estado de cuenta, " +
//                "efectuar transferencias entre cuentas nacionales, " +
//                "pagar tus facturas y obtener información sobre nuestros productos y servicios." +
//                "En el Kiosco encontrarás la calculadora financiera para modelar tu próximo crédito y calcular el rendimiento de tus inversiones futuras.</b></p>");

        VerticalLayout infoLayout = new VerticalLayout();
        infoLayout.add(intro,client);

        infoLayout.setHorizontalComponentAlignment(Alignment.CENTER,intro,client);
//        infoLayout.setHorizontalComponentAlignment(Alignment.CENTER,client);
        infoLayout.setWidth("940px");
        VerticalLayout layout = new VerticalLayout();
//        layout.setFlexDirection(FlexDirection.COLUMN);
        layout.add(infoLayout);
        layout.setMaxWidth("1050px");
        layout.setMaxHeight("840px");
        layout.addClassName("text-color");
//        layout.setAlignContent(ContentAlignment.END);

        return layout;
    }

    private VerticalLayout createSimpleDiv(Integer value) {
        VerticalLayout content = new VerticalLayout();
        int tempcolor = value * 40;
        String background = String.format(BACKGROUND, tempcolor);
        content.getStyle().set("background", "whitesmoke");
//        content.add(new H1(String.valueOf(value)));
        return content;
    }


}
