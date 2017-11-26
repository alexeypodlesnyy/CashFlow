package com.araragi.cashflow.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.araragi.cashflow.CashFlowApp;
import com.araragi.cashflow.R;
import com.araragi.cashflow.activities.MainActivity;
import com.araragi.cashflow.entity.CashTransact;
import com.araragi.cashflow.entity.CustomDate;
import com.araragi.cashflow.utilities.StatisticalCalculations;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.query.Query;

/**
 * Created by Araragi on 2017-09-21.
 */

public class StatisticsFragment extends Fragment {

    public static final String TAG = "StatisticsFragment";

    private Unbinder unbinder;

    private Box<CashTransact> cashBox;
    private Query<CashTransact> cashMoneyQuery;

    private ArrayList<CashTransact> cashTransactArrayList;

    private StatisticalCalculations calculations;

    @BindView(R.id.txt_from_date)public TextView textDateFrom;
    @BindView(R.id.txt_to_date)public TextView textDateTo;
    @BindView(R.id.txt_stat_income_total_amount) TextView textIncomeTotalAmount;
    @BindView(R.id.txt_stat_categories_income)TextView textCategoriesIncome;
    @BindView(R.id.txt_stat_categories_income_amounts)TextView textCategoriesIncomeAmounts;
    @BindView(R.id.txt_stat_expence_total_amount)TextView textExpenseTotalAmount;
    @BindView(R.id.txt_stat_category_expense)TextView textCategoriesExpense;
    @BindView(R.id.txt_stat_category_expense_amount)TextView textCategoriesExpenseAmounts;
    @BindView(R.id.txt_stat_balance_amount)TextView textBalanceAmount;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.frgmt_statistics_updated, container, false);
        unbinder = ButterKnife.bind(this, view);
        BoxStore boxStore =((CashFlowApp)getActivity().getApplication()).getBoxStore();
        cashBox = boxStore.boxFor(CashTransact.class);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Override
    public void onStart() {
        super.onStart();



        cashMoneyQuery = ((MainActivity) getActivity()).cashBox.query().build();
        cashTransactArrayList = new ArrayList<>(cashMoneyQuery.find());

        if(cashTransactArrayList.size() > 0) {
            CashTransact transactionMin = Collections.min(cashTransactArrayList);
            CashTransact transactionMax = Collections.max(cashTransactArrayList);

            textDateFrom.setText("From: " + CustomDate.toCustomDateFromMillis(transactionMin.getDate()));
            textDateTo.setText("To: " + CustomDate.toCustomDateFromMillis(transactionMax.getDate()));

            calculations = new StatisticalCalculations(cashTransactArrayList);
            calculations.calculate();

            setViewWithAmounts(calculations);

        }else{
            setViewIfDbEmpty();
        }




    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private void setViewWithAmounts(StatisticalCalculations calculations){

        textIncomeTotalAmount.setText(calculations.getTotalIncome().toString());
        textExpenseTotalAmount.setText(calculations.getTotalExpense().toString());
        textBalanceAmount.setText(calculations.getBalance().toString());
        String[] categoriesIncome = calculations.getIncomeByCategoryAsStringArray();
        String[] categoriesExpense = calculations.getExpenseByCategoryAsStringArray();

        textCategoriesIncome.setText(categoriesIncome[0]);
        textCategoriesIncomeAmounts.setText(categoriesIncome[1]);

        textCategoriesExpense.setText(categoriesExpense[0]);
        textCategoriesExpenseAmounts.setText(categoriesExpense[1]);

    }

    private void setViewIfDbEmpty(){
        Calendar c = Calendar.getInstance();
        String date = CustomDate.toCustomDateFromMillis(c.getTimeInMillis());
        textDateFrom.setText(date);
        textDateTo.setText(date);

        textIncomeTotalAmount.setText("0");
        textExpenseTotalAmount.setText("0");
        textBalanceAmount.setText("0");
    }
//
//    public void onDateSet(DatePicker view, int year, int month, int day) {
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(year,month,day,12, 12,12);
//        long dateInMillis = calendar.getTimeInMillis();
//
//        //editDate.setText(CustomDate.toCustomDateFromMillis(dateInMillis));
//
//        Log.i("onDateSet", "----long millis set manually = " + dateInMillis);
//
//
//    }


}
