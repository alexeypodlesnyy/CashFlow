package com.araragi.cashflow.utilities;



import android.util.Log;

import com.araragi.cashflow.entity.CustomDate;

import java.util.Calendar;
import java.util.Date;

public class DateRange {

    public long[] getDateRangeCurrentMonth() {
        long begining, end;

        {
            Calendar calendar = getCalendarForNow();
            calendar.set(Calendar.DAY_OF_MONTH,
                    calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            setTimeToBeginningOfDay(calendar);
            begining = calendar.getTimeInMillis();
        }

        {
            Calendar calendar = getCalendarForNow();
            calendar.set(Calendar.DAY_OF_MONTH,
                    calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            setTimeToEndofDay(calendar);
            end = calendar.getTimeInMillis();
        }

        long [] dateRangeCurrentMonth = new long[2];
        dateRangeCurrentMonth[0] = begining;
        dateRangeCurrentMonth[1] = end;
        Log.e("DateRange", "---Current month: beginning = " + CustomDate.toCustomDateFromMillis(begining) +
        "; end = " + CustomDate.toCustomDateFromMillis(end));

        return dateRangeCurrentMonth;
    }

    public long[] getDateRangeCurrentYear() {

        long begining, end;

        {
            Calendar calendar = getCalendarForNow();
            calendar.set(Calendar.DAY_OF_YEAR,
                    calendar.getActualMinimum(Calendar.DAY_OF_YEAR));
            setTimeToBeginningOfDay(calendar);
            begining = calendar.getTimeInMillis();
        }

        {
            Calendar calendar = getCalendarForNow();
            calendar.set(Calendar.DAY_OF_YEAR,
                    calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
            setTimeToEndofDay(calendar);
            end = calendar.getTimeInMillis();
        }

        long [] dateRangeCurrentMonth = new long[2];
        dateRangeCurrentMonth[0] = begining;
        dateRangeCurrentMonth[1] = end;
        Log.e("DateRange", "---Current year: beginning = " + CustomDate.toCustomDateFromMillis(begining) +
                "; end = " + CustomDate.toCustomDateFromMillis(end));

        return dateRangeCurrentMonth;

    }

    public long[] getDateRangePreviousMonth() {
        long begining, end;

        {
            Calendar calendar = getCalendarForNow();
            int month  = calendar.get(Calendar.MONTH);
            if(month - 1 != -1){
                calendar.set(Calendar.MONTH, month - 1);
            }else{
                calendar.set(Calendar.MONTH, Calendar.DECEMBER);
            }

            calendar.set(Calendar.DAY_OF_MONTH,
                    calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            setTimeToBeginningOfDay(calendar);
            begining = calendar.getTimeInMillis();

            calendar.set(Calendar.DAY_OF_MONTH,
                    calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            setTimeToEndofDay(calendar);
            end = calendar.getTimeInMillis();
        }

        long [] dateRangeCurrentMonth = new long[2];
        dateRangeCurrentMonth[0] = begining;
        dateRangeCurrentMonth[1] = end;
        Log.e("DateRange", "---Previous month: beginning = " + CustomDate.toCustomDateFromMillis(begining) +
                "; end = " + CustomDate.toCustomDateFromMillis(end));

        return dateRangeCurrentMonth;
    }










    private static Calendar getCalendarForNow() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar;
    }

    private static void setTimeToBeginningOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    private static void setTimeToEndofDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
    }





}
