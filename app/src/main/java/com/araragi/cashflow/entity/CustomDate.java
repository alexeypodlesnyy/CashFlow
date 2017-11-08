package com.araragi.cashflow.entity;

import java.util.Calendar;

/**
 * Created by Araragi on 2017-11-02.
 */

public class CustomDate {

    public static String toCustomDate(int day, int month, int year){

        StringBuilder result = new StringBuilder("");

        result.append(day);
        result.append("-");

        switch(month){
            case 0:
                result.append("Jan");
                break;
            case 1:
                result.append("Feb");
                break;
            case 2:
                result.append("Mar");
                break;
            case 3:
                result.append("Apr");
                break;
            case 4:
                result.append("May");
                break;
            case 5:
                result.append("Jun");
                break;
            case 6:
                result.append("Jul");
                break;
            case 7:
                result.append("Aug");
                break;
            case 8:
                result.append("Sep");
                break;
            case 9:
                result.append("Oct");
                break;
            case 10:
                result.append("Nov");
                break;
            case 11:
                result.append("Dec");
                break;
        }

        result.append("-");
        result.append(year);

        return result.toString();
    }
    public static String toCustomDateFromMillis(long millis){

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        StringBuilder result = new StringBuilder("");

        result.append(day);
        result.append("-");

        switch(month){
            case 0:
                result.append("Jan");
                break;
            case 1:
                result.append("Feb");
                break;
            case 2:
                result.append("Mar");
                break;
            case 3:
                result.append("Apr");
                break;
            case 4:
                result.append("May");
                break;
            case 5:
                result.append("Jun");
                break;
            case 6:
                result.append("Jul");
                break;
            case 7:
                result.append("Aug");
                break;
            case 8:
                result.append("Sep");
                break;
            case 9:
                result.append("Oct");
                break;
            case 10:
                result.append("Nov");
                break;
            case 11:
                result.append("Dec");
                break;
        }

        result.append("-");
        result.append(year);

        return result.toString();
    }


}
