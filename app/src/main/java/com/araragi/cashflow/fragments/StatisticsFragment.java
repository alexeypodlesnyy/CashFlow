package com.araragi.cashflow.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.araragi.cashflow.CashFlowApp;
import com.araragi.cashflow.R;
import com.araragi.cashflow.activities.MainActivity;
import com.araragi.cashflow.entity.CashTransact;
import com.araragi.cashflow.entity.CashTransact_;
import com.araragi.cashflow.entity.CustomDate;
import com.araragi.cashflow.utilities.DateRange;
import com.araragi.cashflow.utilities.StatisticalCalculations;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.query.Query;

/**
 * Created by Araragi on 2017-09-21.
 */

public class StatisticsFragment extends Fragment implements DatePickerDialog.OnDateSetListener{

    public static final String TAG = "StatisticsFragment";
    private static final String DATE_LAST_CLICKED = "lastDateClicked";
    private static final int DATE_FROM_CLICKED = 1;
    private static final int DATE_TO_CLICKED = 2;
    public static final int STAT_FRAGMENT_REQUEST_CODE = 102;

    private DateRange dateRange;

    private Unbinder unbinder;

    private long dateFrom;
    private long dateTo;



    private int lastDateViewClicked;

    private Box<CashTransact> cashBox;
    private Query<CashTransact> cashMoneyQuery;

    private ArrayList<CashTransact> cashTransactArrayList;

   // private StatisticalCalculations calculations;

    @BindView(R.id.txt_from_date)public TextView textDateFrom;
    @BindView(R.id.txt_to_date)public TextView textDateTo;
    @BindView(R.id.txt_stat_income_total_amount) TextView textIncomeTotalAmount;
    @BindView(R.id.txt_stat_categories_income)TextView textCategoriesIncome;
    @BindView(R.id.txt_stat_categories_income_amounts)TextView textCategoriesIncomeAmounts;
    @BindView(R.id.txt_stat_expence_total_amount)TextView textExpenseTotalAmount;
    @BindView(R.id.txt_stat_category_expense)TextView textCategoriesExpense;
    @BindView(R.id.txt_stat_category_expense_amount)TextView textCategoriesExpenseAmounts;
    @BindView(R.id.txt_stat_balance_amount)TextView textBalanceAmount;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dateRange = new DateRange();


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.frgmt_statistics_updated, container, false);
        unbinder = ButterKnife.bind(this, view);
        BoxStore boxStore =((CashFlowApp)getActivity().getApplication()).getBoxStore();
        cashBox = boxStore.boxFor(CashTransact.class);

        if(savedInstanceState != null){
            lastDateViewClicked = savedInstanceState.getInt(DATE_LAST_CLICKED);
            dateTo = savedInstanceState.getLong("dateTo");
            dateFrom = savedInstanceState.getLong("dateFrom");
        }else{
            setDatesRangeCurrentMonth();
        }

        updateView(getTransactCustomDates(dateFrom, dateTo));



        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

        ((MainActivity)getActivity()).setToolbarTitle("Current month");
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        menu.clear();
        inflater.inflate(R.menu.statistics_frgm_menu,menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.current_month) {
            setDatesRangeCurrentMonth();
            updateView(getTransactCustomDates(dateFrom, dateTo));
            return true;
        }
        if (id == R.id.current_year) {
            setDatesRangeCurrentYear();
            updateView(getTransactCustomDates(dateFrom, dateTo));
            return true;
        }
        if (id == R.id.previous_month) {
            setDatesRangePreviousMonth();
            updateView(getTransactCustomDates(dateFrom, dateTo));
            return true;
        }
        if (id == R.id.all_transactions) {
            updateView(getAllTransactions());
            return true;
        }
        if (id == R.id.custom_dates) {
            Toast.makeText(getActivity(), "Click on date field to set custom dates", Toast.LENGTH_LONG).show();
            return true;
        }



        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(DATE_LAST_CLICKED, lastDateViewClicked);
        savedInstanceState.putLong("dateFrom", dateFrom);
        savedInstanceState.putLong("dateTo", dateTo);

        super.onSaveInstanceState(savedInstanceState);
    }



    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private void updateView(ArrayList<CashTransact> transactions){

        textDateFrom.setText(CustomDate.toCustomDateFromMillis(dateFrom));
        textDateTo.setText(CustomDate.toCustomDateFromMillis(dateTo));


        if(transactions.size() == 0){
            setViewIfDbEmpty();
        }else {

            StatisticalCalculations calculations = new StatisticalCalculations(transactions);
            calculations.calculate();

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
    }

    private void setViewIfDbEmpty(){

        textIncomeTotalAmount.setText("0");
        textExpenseTotalAmount.setText("0");
        textBalanceAmount.setText("0");
        textCategoriesIncome.setText("");
        textCategoriesIncomeAmounts.setText("");

        textCategoriesExpense.setText("");
        textCategoriesExpenseAmounts.setText("");
        Toast.makeText(getActivity(), "No transactions for this time period", Toast.LENGTH_LONG).show();
    }

    @OnClick({R.id.txt_from_date, R.id.txt_to_date})
    public void onDateClicked(View view) {

        switch(view.getId()) {
            case R.id.txt_from_date:
                lastDateViewClicked = DATE_FROM_CLICKED;
                break;
            case R.id.txt_to_date:
                lastDateViewClicked = DATE_TO_CLICKED;
                break;
        }

        DialogFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setTargetFragment(this,STAT_FRAGMENT_REQUEST_CODE);
        datePickerFragment.show(getActivity().getSupportFragmentManager(), "datePicker");

    }

    public void onDateSet(DatePicker view, int year, int month, int day) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month,day,12, 12,12);

        long dateInMillis = calendar.getTimeInMillis();

        switch (lastDateViewClicked){
            case DATE_FROM_CLICKED:
                dateFrom = dateInMillis;
                textDateFrom.setText(CustomDate.toCustomDateFromMillis(dateInMillis));
                updateView(getTransactCustomDates(dateFrom, dateTo));
                break;
            case DATE_TO_CLICKED:
                dateTo = dateInMillis;
                textDateTo.setText(CustomDate.toCustomDateFromMillis(dateInMillis));
                updateView(getTransactCustomDates(dateFrom, dateTo));
                break;
        }

    }

    private ArrayList<CashTransact> getTransactCustomDates(long dateFrom, long dateTo){

        cashMoneyQuery = ((MainActivity) getActivity()).cashBox
                .query()
                .greater(CashTransact_.date, dateFrom)
                .less(CashTransact_.date, dateTo)
                .build();
        ArrayList<CashTransact> cashTransacts = new ArrayList<>(cashMoneyQuery.find());
        return cashTransacts;

    }

    private void setDatesRangeCurrentMonth(){

        long [] dates = dateRange.getDateRangeCurrentMonth();
        dateFrom =  dates[0];
        dateTo = dates[1];

    }
    private void setDatesRangeCurrentYear(){

        long [] dates = dateRange.getDateRangeCurrentYear();
        dateFrom =  dates[0];
        dateTo = dates[1];

    }
    private void setDatesRangePreviousMonth(){

        long [] dates = dateRange.getDateRangePreviousMonth();
        dateFrom =  dates[0];
        dateTo = dates[1];

    }
    private ArrayList<CashTransact> getAllTransactions(){


        ArrayList<CashTransact> cashTransacts = new ArrayList<CashTransact> (cashBox.getAll());

        CashTransact transactionMin = Collections.min(cashTransacts);
        CashTransact transactionMax = Collections.max(cashTransacts);

        dateFrom = transactionMin.getDate();
        Log.e("stat", "-----------" + CustomDate.toCustomDateFromMillis(dateFrom));
        dateTo = transactionMax.getDate();
        Log.e("stat", "----------" + CustomDate.toCustomDateFromMillis(dateTo));

        return cashTransacts;

    }





}
