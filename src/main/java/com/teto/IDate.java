package com.teto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public interface IDate extends IProperties {

    default String formatDate(Date dte) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(dte);
    }
    default Date convertFromLocalDate(LocalDate ld) {
        return convertFromSql(java.sql.Date.valueOf(ld));
    }

    default Integer year(Date dte) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dte);
        return cal.get(Calendar.YEAR);
    }
    default Integer day(Date dte) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dte);
        return cal.get(Calendar.DAY_OF_MONTH);
    }
    default Integer month(Date dte) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dte);
        return cal.get(Calendar.DAY_OF_MONTH)+1;
    }

    default LocalDate convertToLocalDate(Date date)  {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH)+1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return LocalDate.of(year, month, day);
    }

    default Date convertFromSql(java.sql.Date sql) {
        return new Date(sql.getTime());
    }

    default String toDate(Date dte, String fmt) {
        final ThreadLocal<SimpleDateFormat> formatter = ThreadLocal
                .withInitial(() -> new SimpleDateFormat(fmt));
        return formatter.get().format(dte);
    }


    default boolean dateIsValid(String text, String fmt) {
        final ThreadLocal<SimpleDateFormat> format = new ThreadLocal<SimpleDateFormat>() {
            @Override
            protected SimpleDateFormat initialValue() {
                SimpleDateFormat df = new SimpleDateFormat(fmt);
                df.setLenient(false);
                return df;
            }
        };
        if (text == null || !text.matches("\\d{4}-[01]\\d-[0-3]\\d"))
            return false;
        try {
            format.get().parse(text);
            return true;
        } catch (ParseException ex) {
            return false;
        }
    }

    default String toDate(Date d) {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat fmt = new SimpleDateFormat(pattern);
        return fmt.format(d);
    }
    default String toDate(long ms) {
        return toDate(new Date(ms));
    }
}
