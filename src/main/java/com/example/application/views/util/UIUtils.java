package com.example.application.views.util;

import com.vaadin.flow.component.html.Label;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class UIUtils {

    public static Label createAmountLabel(double amount) {
        Label label = createH5Label(formatAmount(amount));
        label.addClassName(LumoStyles.FontFamily.MONOSPACE);
        return label;
    }

    public static Label createH5Label(String text) {
        Label label = new Label(text);
        label.addClassName(LumoStyles.Heading.H5);
        return label;
    }

    public static String formatAmount(Double amount) {
        return decimalFormat.get().format(amount);
    }

    private static final ThreadLocal<DecimalFormat> decimalFormat = ThreadLocal
            .withInitial(() -> new DecimalFormat("###,###.00", DecimalFormatSymbols.getInstance(Locale.US)));
    private static final ThreadLocal<DateTimeFormatter> dateFormat = ThreadLocal
            .withInitial(() -> DateTimeFormatter.ofPattern("dd MMM, YYYY"));
}
