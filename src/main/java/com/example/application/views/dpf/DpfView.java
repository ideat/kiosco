package com.example.application.views.dpf;

import com.example.application.backend.entity.dpf.DpfAccounts;
import com.example.application.backend.service.dpf.DpfAccountService;
import com.example.application.views.util.UIUtils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DpfView {

    @Autowired
    private DpfAccountService dpfAccountService;

    private List<DpfAccounts> dpfAccountsList;

    public HorizontalLayout getLayoutDpfAccount(Integer codeClient){
        HorizontalLayout layout = new HorizontalLayout();

        Button btnTariff = new Button("Tarifario");
        btnTariff.addClassName("button-font-trf");
        btnTariff.addClickListener(click -> {

        });

        dpfAccountsList = dpfAccountService.getDpfsByClient(codeClient);

        layout.add(createDpfAccountClient(), btnTariff);
        layout.setSpacing(true);
        layout.setWidthFull();


        return layout;
    }

    private VerticalLayout createDpfAccountClient(){
        VerticalLayout layoutGrid = new VerticalLayout();

        H2 title = new H2("DEPOPSITOS A PLAZO FIJO");
        title.addClassName("title-header");

        Grid<DpfAccounts> grid = new Grid<>();

        grid.setItems(dpfAccountsList);
        grid.addColumn(DpfAccounts::getNumberDpf)
                .setHeader("Numero de DPF")
                .setFlexGrow(0)
                .setAutoWidth(true);
        grid.addColumn(DpfAccounts::getCurrency)
                .setHeader("Moneda")
                .setFlexGrow(0)
                .setAutoWidth(true);
        grid.addColumn(DpfAccounts::getTerm)
                .setHeader("Plazo(dias)")
                .setFlexGrow(0)
                .setAutoWidth(true);
        grid.addColumn(new ComponentRenderer<>(this::createAmount))
                .setHeader("Capital")
                .setFlexGrow(0)
                .setAutoWidth(true);
        grid.addColumn(new ComponentRenderer<>(this::createButtonState))
                .setFlexGrow(0)
                .setAutoWidth(true);

        layoutGrid.add(title,grid);
        layoutGrid.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        layoutGrid.setWidth("80%");
        return layoutGrid;
    }

    private com.vaadin.flow.component.Component createAmount(DpfAccounts dpfAccounts){
        Double balance = dpfAccounts.getAmount();
        return UIUtils.createAmountLabel(balance);
    }

    private com.vaadin.flow.component.Component createButtonState(DpfAccounts dpfAccounts){
        Button btnStateDpf = new Button("Estado DPF");
        btnStateDpf.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        btnStateDpf.addClickListener(click ->{

        });

        return btnStateDpf;
    }

}
