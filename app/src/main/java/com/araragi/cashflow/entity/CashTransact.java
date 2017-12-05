package com.araragi.cashflow.entity;

import java.util.Comparator;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by Araragi on 2017-09-14.
 */
@Entity
public class CashTransact implements Comparable<CashTransact>{

    public static final int TYPE_EXPENSE = 1;
    public static final int TYPE_INCOME = 2;

    @Id
    private long id;

    private String amount;
    private int type;
    private long date;
    private String category;
    private String description;

    public CashTransact(){}

    public CashTransact(String amount, int type, long date, String category, String description) {

        this.amount = amount;
        this.type = type;
        this.date = date;
        this.category = category;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String customToString() {


        String type = (this.getType()==TYPE_EXPENSE?"Expense":"Income");
        String s = ">> " + type + "; " + this.getAmount()  +
                "; " + CustomDate.toCustomDateFromMillis(this.getDate()) + "; " +
                this.getCategory() + "; " + this.getDescription() + "<<";

        return s;
    }



    @Override
    public int compareTo(CashTransact cashTransact) {
        if(this.getDate() - cashTransact.getDate() > 0){
            return 1;
        }if (this.getDate() - cashTransact.getDate() < 0) {
            return -1;
        }else {
            return 0;
        }
    }

    public static Comparator<CashTransact> CashComparator = new Comparator<CashTransact>() {

        @Override
        public int compare(CashTransact cashTransactOne, CashTransact cashTransactTwo) {
            if(cashTransactOne.getDate() - cashTransactTwo.getDate() > 0){
                return 1;
            }if (cashTransactOne.getDate() - cashTransactTwo.getDate() < 0) {
                return -1;
            }else {
                return 0;
            }
        }
    };


}
