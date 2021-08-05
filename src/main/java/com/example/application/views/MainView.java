package com.example.application.views;

import com.example.application.views.dpf.DpfView;
import com.example.application.views.loan.LoanView;
import com.example.application.views.savingbank.SavingBankView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import de.mekaso.vaadin.addons.Carousel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@PageTitle("Opciones")
@Route(value = "main")
@CssImport("./styles/styles.css")
public class MainView extends FlexLayout implements RouterLayout, HasUrlParameter<String> {

    private static final String BACKGROUND = "hsla(%s, 100%%, 50%%, 0.8)";

    @Autowired
    private SavingBankView savingBankView;

    @Autowired
    private DpfView dpfView;

    @Autowired
    private LoanView loanView;

    private Button btnSavingBank;
    private Button btnDpf;
    private Button btnLoan;
    private Button btnDebitCard;
    private Button btnDigitalBank;
    private Button btnPersonalData;

    private Carousel carousel;

    private Map<String, List<String>> param;


    public MainView() {
       
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent,  @OptionalParameter String s) {


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
        Div layoutSavingBank = createSimpleDiv(12);
        layoutSavingBank.add(savingBankView.getLayoutSavingBank(26907));


        //Layout DPF
        Div layoutDpf = createSimpleDiv(4);
        layoutDpf.add(dpfView.getLayoutDpfAccount(26907));

        //Layout Loan
        Div layoutLoan = createSimpleDiv(4);
        layoutLoan.add(loanView.getLayoutLoanAccounts(33262));

        Div layoutMenu = createSimpleDiv(5);

        layoutMenu.add(menuLayout());
        carousel.add(layoutMenu);
        carousel.add(layoutSavingBank);
        carousel.add(layoutDpf);
        carousel.add(layoutLoan);
        carousel.add(new Button("CANCELAR"));


        Button home = new Button("Menu Principal");
        home.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_LARGE,ButtonVariant.LUMO_CONTRAST);
        home.addClickListener(click -> {
//            Component targetComponent = carousel.getChildren().collect(Collectors.toList()).get(0);
//            carousel.show(targetComponent);
            carousel.show(0);
            if(carousel.getSelectedIndex()!=0){
                carousel.show(0);
            }
        });
        
        add(home,carousel);

        super.getStyle().set("justify-content", "space-evenly");
        super.getStyle().set("flex-direction", "column");
    }

    private HorizontalLayout menuLayout(){
        HorizontalLayout layout = new HorizontalLayout();
        layout.add(layoutInfoClient(),layoutOptions());
        layout.setSpacing(true);
        layout.setAlignItems(Alignment.CENTER);
        return layout;
    }

    private VerticalLayout layoutOptions(){

        btnSavingBank = new Button("CAJAS DE AHORRO");
        btnSavingBank.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_LARGE, ButtonVariant.LUMO_CONTRAST);
        btnSavingBank.setWidth("380px");
        btnSavingBank.setHeight("100px");
        btnSavingBank.addClassName("button-font");
        btnSavingBank.addClickListener(click -> carousel.show(1));


        btnDpf = new Button("DPF");
        btnDpf.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_LARGE, ButtonVariant.LUMO_CONTRAST);
        btnDpf.setWidth("380px");
        btnDpf.setHeight("100px");
        btnDpf.addClassName("button-font");
        btnDpf.addClickListener(click -> carousel.show(2));
        
        btnLoan = new Button("PRESTAMOS");
        btnLoan.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_LARGE, ButtonVariant.LUMO_CONTRAST);
        btnLoan.setWidth("380px");
        btnLoan.setHeight("100px");
        btnLoan.addClassName("button-font");
        btnLoan.addClickListener(click -> carousel.show(3));

        btnDebitCard = new Button("TARJETAS DE DEBITO");
        btnDebitCard.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_LARGE, ButtonVariant.LUMO_CONTRAST);
        btnDebitCard.setWidth("380px");
        btnDebitCard.setHeight("100px");
        btnDebitCard.addClassName("button-font");
        
        btnDigitalBank = new Button("BANCA DIGITAL");
        btnDigitalBank.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_LARGE, ButtonVariant.LUMO_CONTRAST);
        btnDigitalBank.setWidth("380px");
        btnDigitalBank.setHeight("100px");
        btnDigitalBank.addClassName("button-font");
        
        btnPersonalData = new Button("DATOS PERSONALES");
        btnPersonalData.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_LARGE, ButtonVariant.LUMO_CONTRAST);
        btnPersonalData.setWidth("380px");
        btnPersonalData.setHeight("100px");
        btnPersonalData.addClassName("button-font");

        VerticalLayout layout1 = new VerticalLayout();
        layout1.add(btnSavingBank, btnDpf, btnLoan, btnDebitCard, btnDigitalBank, btnPersonalData);
        layout1.setAlignItems(Alignment.CENTER);

        return layout1;
        
    }

    private Component layoutInfoClient(){
        String name = "MARIA JOSE QUINTANILLA";
        Html intro = new Html(" <H1>  Bienvenido </H1> ");
        Html client = new Html( String.format( " <H2> <b>%s </b> </H2>" ,name));

        Html productivity = new Html("<p> <b>Kiosco una manera fácil y conveniente de realizar tus consultas de saldo y estado de cuenta, " +
                "efectuar transferencias entre cuentas nacionales, " +
                "pagar tus facturas y obtener información sobre nuestros productos y servicios." +
                "En el Kiosco encontrarás la calculadora financiera para modelar tu próximo crédito y calcular el rendimiento de tus inversiones futuras.</b></p>");

        FlexLayout layout = new FlexLayout();
        layout.setFlexDirection(FlexDirection.COLUMN);
        layout.add(intro,client, productivity);
        layout.setMaxWidth("840px");
        layout.setAlignContent(ContentAlignment.CENTER);;

        return layout;
    }

    private Div createSimpleDiv(Integer value) {
        Div content = new Div();
        int tempcolor = value * 40;
        String background = String.format(BACKGROUND, tempcolor);
        content.getStyle().set("background", background);
//        content.add(new H1(String.valueOf(value)));
        return content;
    }
}
