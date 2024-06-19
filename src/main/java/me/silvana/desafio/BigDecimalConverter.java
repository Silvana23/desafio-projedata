package me.silvana.desafio;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class BigDecimalConverter {
    public static final Locale BRAZIL_LOCALE = new Locale.Builder()
            .setLanguage("pt")
            .setRegion("BR")
            .build();

    private static final NumberFormat FORMAT = new DecimalFormat("#,###.00", new DecimalFormatSymbols(BRAZIL_LOCALE));

    public static String convertToString(BigDecimal bigDecimal) {
        return FORMAT.format(bigDecimal);
    }


}
