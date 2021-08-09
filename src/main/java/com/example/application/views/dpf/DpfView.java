package com.example.application.views.dpf;

import com.example.application.backend.entity.dpf.DpfAccounts;
import com.example.application.backend.entity.dpf.dto.BalanceDpfDto;
import com.example.application.backend.service.dpf.BalanceDpfService;
import com.example.application.backend.service.dpf.DpfAccountService;
import com.example.application.views.report.FormReportView;
import com.example.application.views.util.PrinterReportJasper;
import com.example.application.views.util.UIUtils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import net.sf.jasperreports.engine.JRException;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

@Component
public class DpfView {

    @Autowired
    private DpfAccountService dpfAccountService;

    @Autowired
    private BalanceDpfService balanceDpfService;

    private List<DpfAccounts> dpfAccountsList;

    public VerticalLayout getLayoutDpfAccount(Integer codeClient){
        VerticalLayout layout = new VerticalLayout();

        Button btnTariff = new Button("Tarifario");
        btnTariff.addClassName("button-font-trf");
        btnTariff.addClickListener(click -> {

        });

        Button btnSimulation = new Button("Simulación");
        btnSimulation.addClassName("button-font-trf");
        btnSimulation.addClickListener(click ->{
            DialogSimulation dialogSimulation = new DialogSimulation();
            dialogSimulation.open();
        });

        HorizontalLayout header = new HorizontalLayout();
        header.add(btnTariff, btnSimulation);
        header.setSpacing(true);
//        header.setAlignContent(FlexLayout.ContentAlignment.SPACE_AROUND);
//        header.setFlexDirection(FlexLayout.FlexDirection.ROW);
        header.setWidthFull();


        dpfAccountsList = dpfAccountService.getDpfsByClient(codeClient);
        layout.add(header,createDpfAccountClient());

        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setSpacing(true);
        layout.setWidth("70%");


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
            List<BalanceDpfDto> collection = new ArrayList<>();
            BalanceDpfDto balanceDpfDto = balanceDpfService.getBalanceDpf(dpfAccounts.getNumberDpf());
            collection.add(balanceDpfDto);

            InputStream stream = getClass().getResourceAsStream("/template-report/dpf/dpfBalance.jrxml");
            String pathLogo =  getClass().getResource("/template-report/img/logo.png").getPath();
            String pathSubreport ="template-report/dpf/";
            Map<String,Object> params = new WeakHashMap<>();
            params.put("logo",pathLogo);
            params.put("path_subreport", pathSubreport);

            byte[] b = new byte[0];
            try {
                b = PrinterReportJasper.imprimirComoPdf(stream,collection,params);
            } catch (JRException e) {
                e.printStackTrace();
            }
            InputStream is = new ByteArrayInputStream(b);
            try {
                byte[] p = IOUtils.toByteArray(is);
                FormReportView contentReport = new FormReportView("EXTRACTO", p);
                contentReport.open();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        return btnStateDpf;
    }

}
