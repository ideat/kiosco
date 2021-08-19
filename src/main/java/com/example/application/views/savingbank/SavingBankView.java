package com.example.application.views.savingbank;


import com.example.application.backend.entity.savingBank.SavingBankClient;
import com.example.application.backend.service.savingBank.BalanceSavingBankDtoService;
import com.example.application.backend.service.savingBank.SavingBankService;
import com.example.application.views.util.UIUtils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SavingBankView extends VerticalLayout {
    @Autowired
    private SavingBankService savingBankService;

    @Autowired
    private BalanceSavingBankDtoService balanceSavingBankDtoService;

    private List<SavingBankClient> savingBankClientList;



    public VerticalLayout getLayoutSavingBank(Integer codeClient){
        VerticalLayout layout = new VerticalLayout();
        layout.getElement().getStyle().set("background-image","url('/backgrounds/savingbank.png')" );


        Button btnTariff = new Button(new Image("/buttons/Boton-07.png","Tarifario"));
        btnTariff.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
//        btnTariff.addClassName("button-font-trf");

        VerticalLayout header = new VerticalLayout();
        header.add(btnTariff);
//        header.setAlignContent(FlexLayout.ContentAlignment.START);
//        header.setFlexDirection(FlexLayout.FlexDirection.COLUMN);
        header.setAlignItems(Alignment.BASELINE);
//        header.setSizeFull();

        btnTariff.addClickListener(click -> {

        });

        VerticalLayout space = new VerticalLayout();
        space.setHeight("200px");

        savingBankClientList = savingBankService.getSavingBankClient(codeClient);
        VerticalLayout lSavingBank = createSavingBankClient();
        layout.add(space,header, lSavingBank);
        layout.setAlignItems(FlexComponent.Alignment.BASELINE);
        layout.setHorizontalComponentAlignment(Alignment.BASELINE,lSavingBank);

        layout.setSizeFull();



        return layout;
    }



    private VerticalLayout createSavingBankClient(){
        VerticalLayout layoutGrid = new VerticalLayout();
//
//        H2 title = new H2("CAJAS DE AHORRO");
//        title.addClassName("title-header");

        Grid<SavingBankClient> grid = new Grid<>();
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COMPACT);

        grid.setItems(savingBankClientList);
        grid.addColumn(SavingBankClient::getAccount)
                .setHeader("Numero de Cuenta")
                .setFlexGrow(0)
                .setAutoWidth(true);
        grid.addColumn(SavingBankClient::getProductName)
                .setHeader("Producto")
                .setFlexGrow(0)
                .setAutoWidth(true);
        grid.addColumn(SavingBankClient::getRate)
                .setHeader("Tasa")
                .setFlexGrow(0)
                .setAutoWidth(true);
        grid.addColumn(SavingBankClient::getState)
                .setHeader("Estado")
                .setFlexGrow(0)
                .setAutoWidth(true);
        grid.addColumn(SavingBankClient::getCurrency)
                .setHeader("Moneda")
                .setFlexGrow(0)
                .setAutoWidth(true);
        grid.addColumn(new ComponentRenderer<>(this::createBalance))
                .setHeader("Saldo")
                .setFlexGrow(0)
                .setAutoWidth(true);
        grid.addColumn(new ComponentRenderer<>(this::createButtonSavingBank))
                .setFlexGrow(0)
                .setAutoWidth(true);

        grid.setHeightByRows(true);
        grid.setWidth("65%");

        layoutGrid.add(grid);
        layoutGrid.setHorizontalComponentAlignment(FlexComponent.Alignment.AUTO);
        layoutGrid.setWidth("90%");
        return layoutGrid;
    }

    private com.vaadin.flow.component.Component createBalance(SavingBankClient savingBankClient){
        Double balance = savingBankClient.getBalance();
        return UIUtils.createAmountLabel(balance);
    }

    private com.vaadin.flow.component.Component createButtonSavingBank(SavingBankClient savingBankClient){
        Button btnSavingBank = new Button("Extracto");
        btnSavingBank.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

//        Button btnBalance = new Button("Saldo");
//        btnBalance.addThemeVariants(ButtonVariant.LUMO_SUCCESS,ButtonVariant.LUMO_PRIMARY);
//        btnBalance.setIcon(VaadinIcon.PRINT.create());

        HorizontalLayout layout = new HorizontalLayout();
//        layout.add(btnBalance,btnSavingBank);
        layout.add(btnSavingBank);
        layout.setSpacing(true);

        btnSavingBank.addClickListener(click -> {
           DiaglogExtract diaglogExtract = new DiaglogExtract(savingBankClient.getAccount(), balanceSavingBankDtoService);
           diaglogExtract.open();
        });

//        btnBalance.addClickListener(click -> {
//
//        });

        return layout;
    }



}
