package com.example.application.views.savingbank;

import com.vaadin.componentfactory.EnhancedDatePicker;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.theme.lumo.Lumo;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Locale;

@CssImport("./styles/my-dialog.css")
public class DiaglogExtract extends Dialog {
    public String DOCK = "dock";
    public String FULLSCREEN = "fullscreen";

    private boolean isDocked = false;
    private boolean isFullScreen = false;

    private Header header;
    private Button min;
    private Button max;

    private VerticalLayout content;
    public Footer footer;

    public DiaglogExtract(){
        setDraggable(true);
        setModal(false);
        setResizable(true);


        // Dialog theming
        getElement().getThemeList().add("my-dialog");
        setWidth("800px");

        // Accessibility
        getElement().setAttribute("aria-labelledby", "dialog-title");

        // Header
        H2 title = new H2("Consulta Extracto de Caja de Ahorro");
        title.addClassName("dialog-title");

        min = new Button(VaadinIcon.DOWNLOAD_ALT.create());
        min.addClickListener(event -> minimise());

        max = new Button(VaadinIcon.EXPAND_SQUARE.create());
        max.addClickListener(event -> maximise());

        //////////////////

        Button close = new Button(VaadinIcon.CLOSE_BIG.create());
        close.addClickListener(event -> close());

        header = new Header(title,  close);
        header.getElement().getThemeList().add(Lumo.DARK);
        add(header);

        content = new VerticalLayout(layoutExtract());
        content.addClassName("dialog-content");
        content.setAlignItems(FlexComponent.Alignment.STRETCH);
        add(content);

        // Footer
        Button btnClose = new Button("Cerrar");
        btnClose.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_ERROR);
        btnClose.addClickListener(event -> close());
        footer = new Footer(btnClose);
        add(footer);

        // Button theming
        for (Button button : new Button[] { min, max, close }) {
            button.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_TERTIARY);
        }


    }

    private VerticalLayout layoutExtract(){

        HorizontalLayout layout = new HorizontalLayout();
        EnhancedDatePicker startDate = new EnhancedDatePicker ("Seleccione la fecha de inicio");
        startDate.setLocale(Locale.GERMANY);
        startDate.setClearButtonVisible(true);
        LocalDate now = LocalDate.now();
        startDate.setValue(now);
        startDate.setPattern("dd-MM-yyyy");
        startDate.setI18n(new EnhancedDatePicker.DatePickerI18n().setWeek("Semana")
                .setCalendar("Calendario").setClear("Limpiar").setToday("Hoy")
                .setCancel("Cancelar").setFirstDayOfWeek(1)
                .setMonthNames(Arrays.asList( "Enero", "Febrero", "Marzo",
                        "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre",
                        "Octubre", "Noviembre", "Diciembre"))
                .setWeekdays(Arrays.asList("Domingo", "Lunes", "Martes",
                        "Miercoles", "Jueves", "Viernes", "Sabado"))
                .setWeekdaysShort(Arrays.asList("Dom", "Lun", "Mar", "Mie",
                        "Jue", "Vie", "Sab")));


        EnhancedDatePicker endDate = new EnhancedDatePicker("Seleccione la fecha final");
        endDate.setLocale(new Locale("bo"));
        endDate.setClearButtonVisible(true);

        endDate.setValue(now);
        endDate.setPattern("dd-MM-yyyy");
        endDate.setI18n(new EnhancedDatePicker.DatePickerI18n().setWeek("Semana")
                .setCalendar("Calendario").setClear("Limpiar").setToday("Hoy")
                .setCancel("Cancelar").setFirstDayOfWeek(1)
                .setMonthNames(Arrays.asList( "Enero", "Febrero", "Marzo",
                        "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre",
                        "Octubre", "Noviembre", "Diciembre"))
                .setWeekdays(Arrays.asList("Domingo", "Lunes", "Martes",
                        "Miercoles", "Jueves", "Viernes", "Sabado"))
                .setWeekdaysShort(Arrays.asList("Dom", "Lun", "Mar", "Mie",
                        "Jue", "Vie", "Sab")));

        Button btnPrint = new Button("Imprimir");
        btnPrint.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnPrint.setIcon(VaadinIcon.PRINT.create());

        layout.add(startDate,endDate,btnPrint);
        layout.setAlignItems(FlexComponent.Alignment.BASELINE);
        layout.setSpacing(true);

        HorizontalLayout layout2 = new HorizontalLayout();
        NumberField lastMovement = new NumberField();
        lastMovement.setHasControls(true);
        lastMovement.setMin(1);
        lastMovement.setValue(5.0);
        lastMovement.setHelperText("Nro. de movimientos a imprimir");

        Button btnPrint2 = new Button("Imprimir");
        btnPrint2.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnPrint2.setIcon(VaadinIcon.PRINT.create());

        layout2.add(lastMovement,btnPrint2);

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.add(layout,layout2);
        mainLayout.setSpacing(true);

        btnPrint.addClickListener(click -> {
            if(startDate.isEmpty()){
                startDate.setErrorMessage("Seleccione una fecha");
                startDate.setInvalid(true);

                return;
            }
            if(endDate.isEmpty()){
                endDate.setErrorMessage("Seleccione una fecha");
                endDate.setInvalid(true);
                return;
            }

           if(startDate.getValue().isAfter(endDate.getValue())){
               startDate.setErrorMessage("Fecha inicial no puede ser posterior a la final");
               startDate.setInvalid(true);
               return;
           }


        });


        return mainLayout;
    }

    private void minimise() {
        if (isDocked) {
            initialSize();
        } else {
            if (isFullScreen) {
                initialSize();
            }
            min.setIcon(VaadinIcon.UPLOAD_ALT.create());
            getElement().getThemeList().add(DOCK);
            setWidth("620px");
        }
        isDocked = !isDocked;
        isFullScreen = false;
        content.setVisible(!isDocked);
//        footer.setVisible(!isDocked);
    }

    private void initialSize() {
        min.setIcon(VaadinIcon.DOWNLOAD_ALT.create());
        getElement().getThemeList().remove(DOCK);
        max.setIcon(VaadinIcon.EXPAND_SQUARE.create());
        getElement().getThemeList().remove(FULLSCREEN);
        setHeight("auto");
        setWidth("600px");
    }

    private void maximise() {
        if (isFullScreen) {
            initialSize();
        } else {
            if (isDocked) {
                initialSize();
            }
            max.setIcon(VaadinIcon.COMPRESS_SQUARE.create());
            getElement().getThemeList().add(FULLSCREEN);
            setSizeFull();
            content.setVisible(true);
//            footer.setVisible(true);
        }
        isFullScreen = !isFullScreen;
        isDocked = false;
    }
}
