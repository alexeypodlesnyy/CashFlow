package com.araragi.cashflow.utilities;

import com.araragi.cashflow.entity.CashTransact;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Araragi on 2017-09-22.
 */

public class StatisticalCalculations {



    private BigDecimal totalExpense;
    private BigDecimal totalIncome;
    private BigDecimal balance;



    private HashMap<String, BigDecimal> categoriesTotalExpense;
    private HashMap<String, BigDecimal> categoriesTotalIncome;



    private ArrayList<CashTransact> cashTransacts;

    public StatisticalCalculations(ArrayList<CashTransact> cashTransacts){
        this.cashTransacts = cashTransacts;
    }

    public ArrayList<CashTransact> getCashTransacts() {
        return cashTransacts;
    }

//    public void setCashTransactions(ArrayList<CashTransact> cashTransacts) {
//        this.cashTransacts = cashTransacts;
//    }

    public boolean calculate(){

        totalExpense = new BigDecimal("0");
        totalIncome = new BigDecimal("0");
        balance = new BigDecimal("0");

        categoriesTotalExpense = new HashMap<>();
        categoriesTotalIncome = new HashMap<>();

        ArrayList<CashTransact> localList = getCashTransacts();

        for(CashTransact cashTransact :localList){
            BigDecimal localAmount = new BigDecimal(cashTransact.getAmount());
            String category = cashTransact.getCategory();
            int typeOfCashTransaction = cashTransact.getType();
            switch(typeOfCashTransaction){
                case CashTransact.TYPE_EXPENSE:
                    totalExpense = totalExpense.add(localAmount);
                    if(categoriesTotalExpense.get(category)==null){
                        categoriesTotalExpense.put(category, localAmount);
                    }else{
                        BigDecimal totalByCategory = categoriesTotalExpense.get(category);
                        totalByCategory = totalByCategory.add(localAmount);
                        categoriesTotalExpense.put(category, totalByCategory);
                    }
                    break;
                case CashTransact.TYPE_INCOME:
                    totalIncome = totalIncome.add(localAmount);
                    if(categoriesTotalIncome.get(category)==null){
                        categoriesTotalIncome.put(category, localAmount);
                    }else{
                        BigDecimal totalByCategory = categoriesTotalIncome.get(category);
                        totalByCategory = totalByCategory.add(localAmount);
                        categoriesTotalIncome.put(category, totalByCategory);
                    }
                    break;


            }
        }

        balance = totalIncome.subtract(totalExpense);
        return true;
    }

    public BigDecimal getTotalExpense() {
        return totalExpense;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public HashMap<String, BigDecimal> getCategoriesTotalExpense() {
        return categoriesTotalExpense;
    }

    public HashMap<String, BigDecimal> getCategoriesTotalIncome() {
        return categoriesTotalIncome;
    }

    public String[] getIncomeByCategoryAsStringArray(){
        StringBuilder categories = new StringBuilder("");
        StringBuilder amounts = new StringBuilder("");

        for(String key: categoriesTotalIncome.keySet()){
            categories.append(key + "\n");
            amounts.append(categoriesTotalIncome.get(key) + "\n");
        }
        String[] results = {categories.toString(), amounts.toString()};
        return results;
    }
    public String[] getExpenseByCategoryAsStringArray(){
        StringBuilder categories = new StringBuilder("");
        StringBuilder amounts = new StringBuilder("");

        for(String key: categoriesTotalExpense.keySet()){
            categories.append(key + "\n");
            amounts.append(categoriesTotalExpense.get(key) + "\n");
        }
        String[] results = {categories.toString(), amounts.toString()};
        return results;
    }
}






