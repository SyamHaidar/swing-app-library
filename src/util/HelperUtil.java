package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HelperUtil {

    public static long dayBetween(String start, String end) throws ParseException {
        Calendar startDate;
        Calendar endDate;
        startDate = Calendar.getInstance();
        endDate = Calendar.getInstance();

        Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(start);
        Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(end);

        startDate.setTime(date1);
        endDate.setTime(date2);

        long lama = 0;

        Calendar tgl = (Calendar) startDate.clone();
        while (tgl.before(endDate)) {
            tgl.add(Calendar.DAY_OF_MONTH, 1);
            lama++;
        }

        return lama;
    }

    public static String randomNumber() {
        StringBuilder result = new StringBuilder();
        String chars = "0123456789";
        for (int i = 0; i < 4; i++) {
            result.append(chars.charAt((int) Math.floor(Math.random() * chars.length())));
        }

        return result.toString();
    }

}
