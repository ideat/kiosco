package com.example.application.views.loan;

import com.example.application.backend.entity.loan.LoanAccounts;
import com.example.application.backend.entity.loan.dto.BalanceLoanDto;
import com.example.application.backend.entity.savingBank.SavingBankClient;
import com.example.application.backend.service.loan.BalanceLoanService;
import com.example.application.backend.service.loan.LoanAccountsService;
import com.example.application.views.report.FormReportView;
import com.example.application.views.util.PrinterReportJasper;
import com.example.application.views.util.UIUtils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
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
public class LoanView {

    @Autowired
    private LoanAccountsService loanAccountService;

    @Autowired
    private BalanceLoanService balanceLoanService;

    private List<LoanAccounts> loanAccountsList;

    public HorizontalLayout getLayoutLoanAccounts(Integer codeClient){
        HorizontalLayout layout = new HorizontalLayout();

        loanAccountsList = loanAccountService.findByCodeClient(codeClient);

        layout.add(createGridLayout());
        layout.setSpacing(true);
        layout.setWidthFull();
        return layout;
    }

    private VerticalLayout createGridLayout(){
        VerticalLayout layoutGrid = new VerticalLayout();
        layoutGrid.setSizeUndefined();

        H2 title = new H2("CREDITOS ACTIVOS");
        title.addClassName("title-header");

        Grid<LoanAccounts> grid = new Grid<>();
        grid.setItems(loanAccountsList);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COMPACT);
        grid.addColumn(LoanAccounts::getNumberLoan)
                .setHeader("Nro. de Crédito")
                .setFlexGrow(0)
                .setAutoWidth(true);
        grid.addColumn(LoanAccounts::getCurrency)
                .setHeader("Moneda")
                .setFlexGrow(0)
                .setAutoWidth(true);
        grid.addColumn(new ComponentRenderer<>(this::createBalance))
                .setHeader("Saldo")
                .setFlexGrow(0)
                .setAutoWidth(true);
        grid.addColumn(LoanAccounts::getState)
                .setHeader("Estado")
                .setFlexGrow(0)
                .setAutoWidth(true);
        grid.addColumn(new ComponentRenderer<>(this::createButtonLoanAccounts))
                .setFlexGrow(0)
                .setAutoWidth(true);
        grid.setWidth("70%");

        layoutGrid.add(title,grid);
        layoutGrid.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        layoutGrid.setWidth("70%");
        layoutGrid.setHeight("50%");
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

        HorizontalLayout layout = new HorizontalLayout();
        layout.add(btnBalance,btnPaymentPlan);
        layout.setSpacing(true);

        return layout;
    }
}
