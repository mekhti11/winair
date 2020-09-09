package com.hititcs.dcs.util;

import com.hititcs.dcs.domain.model.FlightSummary;
import java.text.DateFormatSymbols;
import java.util.Locale;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

public class DateTimeUtils {

    public static String getDepArrTimes(FlightSummary flightSummary) {
        String date = flightSummary.getDepTime().substring(8, 10) + new DateFormatSymbols().getMonths()[Integer.valueOf(flightSummary.getDepTime().substring(5, 7))-1].substring(0, 3);;
        String depTime = flightSummary.getDepTime().substring(11,16);
        String arrTime = flightSummary.getArrTime().substring(11,16);

        return date + " " + depTime + " - " + arrTime;
    }

    public static String normalizeZonedDateTime(String zoned) {
        String result = "";

        if(zoned == null || zoned.isEmpty()) {
            return result;
        }

        result = zoned.substring(0, 16).replace("T", " ");

        return result;
    }

    public static String getTimeFromZonedDateTime(String zoned) {
        String result = "";

        if(zoned == null || zoned.isEmpty()) {
            return result;
        }

        result = zoned.substring(zoned.indexOf('T') + 1).substring(0,5);

        return result;
    }

    public static String formatDateEnglishName(LocalDate localDate) {
        if (localDate == null) {
            return "";
        }
        return DateTimeFormatter.ofPattern("dd MMM YYYY")
            .withLocale(Locale.ENGLISH)
            .format(localDate);
    }

    public static String formatDateToRequestFormat(LocalDate localDate) {
        if (localDate == null) {
            return "";
        }
        return DateTimeFormatter.ofPattern("YYYY-MM-dd")
            .withLocale(Locale.ENGLISH)
            .format(localDate);
    }

    public static String formatDateToHourFormat(LocalDate localDate) {
        if (localDate == null) {
            return "";
        }
        return DateTimeFormatter.ofPattern("HH:MM")
            .withLocale(Locale.ENGLISH)
            .format(localDate);
    }
}
