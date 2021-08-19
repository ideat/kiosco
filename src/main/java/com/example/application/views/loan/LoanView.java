package com.example.application.views.loan;

import com.example.application.backend.entity.loan.LoanAccounts;
import com.example.application.backend.entity.loan.dto.BalanceLoanDto;
import com.example.application.backend.entity.loan.dto.PaymentPlanDto;
import com.example.application.backend.entity.savingBank.SavingBankClient;
import com.example.application.backend.entity.sec.StageHistoryCreditRequestDto;
import com.example.application.backend.entity.sec.SummaryCreditRequestStage;
import com.example.application.backend.rest.SecService;
import com.example.application.backend.service.loan.BalanceLoanService;
import com.example.application.backend.service.loan.LoanAccountsService;
import com.example.application.backend.service.loan.PaymentPlanService;
import com.example.application.views.dpf.DialogSimulation;
import com.example.application.views.report.FormReportView;
import com.example.application.views.util.PrinterReportJasper;
import com.example.application.views.util.UIUtils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import net.sf.jasperreports.engine.JRException;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

@Component
public class LoanView {

    @Autowired
    private LoanAccountsService loanAccountService;

    @Autowired
    private BalanceLoanService balanceLoanService;

    @Autowired
    private PaymentPlanService paymentPlanService;

    @Autowired
    private SecService secService;

    private List<LoanAccounts> loanAccountsList;
    private List<SummaryCreditRequestStage> summaryCreditRequestStageList;
    private List<StageHistoryCreditRequestDto> stageHistoryCreditRequestDtoList;

    public VerticalLayout getLayoutLoanAccounts(Integer codeClient){
        VerticalLayout layout = new VerticalLayout();
        layout.getElement().getStyle().set("background-image","url('/backgrounds/loan.png')" );
        /////
        Button btnTariff = new Button(new Image("/buttons/Boton-07.png","Tarifario"));
        btnTariff.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnTariff.addClickListener(click -> {

        });

        Button btnSimulation = new Button(new Image("/buttons/Botones-08.png","Simulacion"));
        btnSimulation.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnSimulation.addClickListener(click ->{

        });

        HorizontalLayout header = new HorizontalLayout();
        header.add(btnTariff, btnSimulation);
        header.setSpacing(true);
//        header.setAlignContent(FlexLayout.ContentAlignment.SPACE_AROUND);
//        header.setFlexDirection(FlexLayout.FlexDirection.ROW);
//        header.setWidthFull();

        ///
        loanAccountsList = loanAccountService.findByCodeClient(codeClient);
        summaryCreditRequestStageList = new ArrayList<>();
        List<SummaryCreditRequestStage> auxList = secService.getSummaryByIdCard("5627580BE");
        for(SummaryCreditRequestStage s: auxList){

            if(s.getAssignedUser()==null) s.setAssignedUser("NO ASIGNADO");
            summaryCreditRequestStageList.add(s);
        }

        VerticalLayout space = new VerticalLayout();
        space.setHeight("120px");

        layout.add(space,header,createGridLayout(), createGridRequest());

        layout.setAlignItems(FlexComponent.Alignment.END);
        layout.setSpacing(true);
        layout.setSizeFull();
        return layout;
    }

    private VerticalLayout createGridLayout(){
        VerticalLayout layoutGrid = new VerticalLayout();
        layoutGrid.setSizeUndefined();

        H2 title = new H2("Créditos Activos");
        title.addClassName("title-header");

        Grid<LoanAccounts> grid = new Grid<>();
        grid.setItems(loanAccountsList);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COMPACT);
        grid.addColumn(LoanAccounts::getNumberLoan)
                .setHeader("Nro. Crédito")
                .setFlexGrow(1)
                .setAutoWidth(true);
        grid.addColumn(LoanAccounts::getCurrency)
                .setHeader("Moneda")
                .setFlexGrow(1)
                .setAutoWidth(true);
        grid.addColumn(new ComponentRenderer<>(this::createBalance))
                .setHeader("Saldo")
                .setFlexGrow(1)
                .setAutoWidth(true);
        grid.addColumn(LoanAccounts::getState)
                .setHeader("Estado")
                .setFlexGrow(1)
                .setAutoWidth(true);
        grid.addColumn(new LocalDateRenderer<>(LoanAccounts::getExpiredDateConvert, DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .setHeader("Fecha Venc.")
                .setFlexGrow(1)
                .setAutoWidth(true);
        grid.addColumn(LoanAccounts::getRate)
                .setHeader("Tasa")
                .setFlexGrow(1)
                .setAutoWidth(true);
        grid.addColumn(new ComponentRenderer<>(this::createButtonLoanAccounts))
                .setFlexGrow(1)
                .setAutoWidth(true);
        grid.setWidth("70%");
        grid.setHeightByRows(true);


        layoutGrid.add(title,grid);
        layoutGrid.setHorizontalComponentAlignment(FlexComponent.Alignment.END,title,grid);
        layoutGrid.setWidth("75%");
//        layoutGrid.setHeight("200px");
        return layoutGrid;
    }

    private com.vaadin.flow.component.Component createBalance(LoanAccounts loanAccounts){
        Double balance = loanAccounts.getBalance();
        return UIUtils.createAmountLabel(balance);
    }

    private com.vaadin.flow.component.Component createButtonLoanAccounts(LoanAccounts loanAccounts){
        Button btnPaymentPlan = new Button("Plan Pagos");
        btnPaymentPlan.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnPaymentPlan.setIcon(VaadinIcon.PRINT.create());

        Button btnBalance = new Button("Extracto");
        btnBalance.addThemeVariants(ButtonVariant.LUMO_SUCCESS,ButtonVariant.LUMO_PRIMARY);

        btnBalance.addClickListener(click ->{
            List<BalanceLoanDto> collection = new ArrayList<>();
            BalanceLoanDto balanceLoanDto = balanceLoanService.getBalanceLoan(loanAccounts.getNumberLoan());
            collection.add(balanceLoanDto);

            InputStream stream = getClass().getResourceAsStream("/template-report/loan/loanBalance.jrxml");
            String pathLogo =  getClass().getResource("/template-report/img/logo.png").getPath();
            String pathSubreport ="template-report/loan/";
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

        btnPaymentPlan.addClickListener(click -> {
           List<PaymentPlanDto> collection = new ArrayList<>();
            PaymentPlanDto paymentPlanDto = paymentPlanService.getPaymentPlan(loanAccounts.getNumberLoan());
            collection.add(paymentPlanDto);

            InputStream stream = getClass().getResourceAsStream("/template-report/loan/paymentPlan.jrxml");
            String pathLogo =  getClass().getResource("/template-report/img/logo.png").getPath();
            String pathSubreport ="template-report/loan/";
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

        HorizontalLayout layout = new HorizontalLayout();
        layout.add(btnBalance,btnPaymentPlan);
        layout.setSpacing(true);

        return layout;
    }

    private VerticalLayout  createGridRequest(){
        VerticalLayout layoutGrid = new VerticalLayout();
        layoutGrid.setSizeUndefined();

        H2 title = new H2("Solicitudes de Crédito");
        title.addClassName("title-header");

        Grid<SummaryCreditRequestStage> grid = new Grid<>();
        grid.setItems(summaryCreditRequestStageList);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COMPACT);
        grid.addColumn(SummaryCreditRequestStage::getNumberRequest)
                .setHeader("Nro. Solicitud")
                .setFlexGrow(1)
                .setAutoWidth(true);
        grid.addColumn(SummaryCreditRequestStage::getCurrency)
                .setHeader("Moneda")
                .setFlexGrow(1)
                .setAutoWidth(true);
        grid.addColumn(new ComponentRenderer<>(this::createAmount))
                .setHeader("Monto")
                .setFlexGrow(1)
                .setAutoWidth(true);
        grid.addColumn(SummaryCreditRequestStage::getOfficerName)
                .setHeader("Oficial Responsable")
                .setFlexGrow(1)
                .setAutoWidth(true);
        grid.addColumn(SummaryCreditRequestStage::getStage)
                .setHeader("Etapa")
                .setFlexGrow(1)
                .setAutoWidth(true);
        grid.addColumn(SummaryCreditRequestStage::getState)
                .setHeader("Estado")
                .setFlexGrow(1)
                .setAutoWidth(true);
        grid.addColumn(SummaryCreditRequestStage::getAssignedUser)
                .setHeader("Responsable Etapa")
                .setFlexGrow(1)
                .setAutoWidth(true);
        grid.addColumn(new ComponentRenderer<>(this::createDetailCreditRequest))
                .setFlexGrow(1)
                .setAutoWidth(true);

//        grid.setWidth("70%");
        grid.setHeightByRows(true);

        layoutGrid.add(title,grid);
        layoutGrid.setHorizontalComponentAlignment(FlexComponent.Alignment.END,title,grid);
        layoutGrid.setWidth("75%");

        return layoutGrid;
    }

    private com.vaadin.flow.component.Component createAmount(SummaryCreditRequestStage summaryCreditRequestStage){
        Double amount = summaryCreditRequestStage.getAmount();
        return UIUtils.createAmountLabel(amount);
    }

    private  com.vaadin.flow.component.Component createDetailCreditRequest(SummaryCreditRequestStage summaryCreditRequestStage){
        Button btnDetail = new Button("Detalle");
        btnDetail.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);

        btnDetail.addClickListener(click -> {
            stageHistoryCreditRequestDtoList = secService.getDetailByNumberRequest(summaryCreditRequestStage.getNumberRequest());
            DialogTimeLine dialogTimeLine = new DialogTimeLine(stageHistoryCreditRequestDtoList);
            dialogTimeLine.open();
        });

        return btnDetail;
    }
}
