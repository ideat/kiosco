package com.example.application.views.savingbank;


import com.example.application.backend.entity.savingBank.SavingBankClient;
import com.example.application.backend.service.savingBank.SavingBankService;
import com.example.application.views.util.UIUtils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SavingBankView {
    @Autowired
    private SavingBankService savingBankService;

    private List<SavingBankClient> savingBankClientList;


    public VerticalLayout getLayoutSavingBank(Integer codeClient){
        VerticalLayout layout = new VerticalLayout();


        Button btnTariff = new Button("Tarifario");
        btnTariff.addClassName("button-font-trf");

        FlexLayout header = new FlexLayout();
        header.add(btnTariff);
        header.setAlignContent(FlexLayout.ContentAlignment.SPACE_BETWEEN);
        header.setFlexDirection(FlexLayout.FlexDirection.COLUMN);
        header.setSizeFull();

        btnTariff.addClickListener(click -> {

        });


        savingBankClientList = savingBankService.getSavingBankClient(codeClient);
        VerticalLayout lSavingBank = createSavingBankClient();
        layout.add(header, lSavingBank);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setSpacing(true);
        layout.setWidth("60%");


        return layout;
    }



    private VerticalLayout createSavingBankClient(){
        VerticalLayout layoutGrid = new VerticalLayout();

        H2 title = new H2("CAJAS DE AHORRO");
        title.addClassName("title-header");

        Grid<SavingBankClient> grid = new Grid<>();
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COMPACT);

        grid.setItems(savingBankClientList);
        grid.addColumn(SavingBankClient::getAccount)
                .setHeader("Numero de Cuenta")
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
//        grid.setWidth("70%");

        layoutGrid.add(title,grid);
        layoutGrid.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
//        layoutGrid.setWidth("70%");
        return layoutGrid;
    }

    private com.vaadin.flow.component.Component createBalance(SavingBankClient savingBankClient){
        Double balance = savingBankClient.getBalance();
        return UIUtils.createAmountLabel(balance);
    }

    private com.vaadin.flow.component.Component createButtonSavingBank(SavingBankClient savingBankClient){
        Button btnSavingBank = new Button("Extracto");
        btnSavingBank.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button btnBalance = new Button("Saldo");
        btnBalance.addThemeVariants(ButtonVariant.LUMO_SUCCESS,ButtonVariant.LUMO_PRIMARY);
        btnBalance.setIcon(VaadinIcon.PRINT.create());

        HorizontalLayout layout = new HorizontalLayout();
        layout.add(btnBalance,btnSavingBank);
        layout.setSpacing(true);

        btnSavingBank.addClickListener(click -> {
           DiaglogExtract diaglogExtract = new DiaglogExtract();
           diaglogExtract.open();
        });

        btnBalance.addClickListener(click -> {

        });

        return layout;
    }



}
