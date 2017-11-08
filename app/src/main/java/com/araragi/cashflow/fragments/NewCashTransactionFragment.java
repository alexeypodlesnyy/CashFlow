package com.araragi.cashflow.fragments;

import android.app.DatePickerDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.araragi.cashflow.CashFlowApp;
import com.araragi.cashflow.R;
import com.araragi.cashflow.activities.MainActivity;
import com.araragi.cashflow.entity.CashTransaction;
import com.araragi.cashflow.entity.CustomDate;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.query.Query;

/**
 * Created by Araragi on 2017-09-14.
 */

public class NewCashTransactionFragment extends Fragment implements DatePickerDialog.OnDateSetListener{

    public final  static String TAG = "NewCashTransactionFragment";

    private long dateInMillis;


    @BindView(R.id.edit_date)public TextView editDate;
    @BindView(R.id.txt_date)public TextView txtDate;
    @BindView(R.id.edit_amount) public EditText amount;
    @BindView(R.id.spinner_category)public Spinner spinnerCategory;
    @BindView(R.id.radio_btn_expense)public RadioButton expenseRadioBtn;
    @BindView(R.id.radio_btn_income)public RadioButton incomeRadioBtn;
    @BindView(R.id.edit_description)public EditText description;


    private ArrayAdapter<CharSequence> adapterExpense;
    private ArrayAdapter<CharSequence> adapterIncome;

    private Unbinder unbinder;


    private static final int MAX_LENGTH_DESCRIPTION = 500;

    private Box<CashTransaction> cashBox;
    private Query<CashTransaction> cashMoneyQuery;

    private long timeInMillis;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.frgmt_new_transaction, container, false);
        unbinder = ButterKnife.bind(this, view);

        BoxStore boxStore =((CashFlowApp)getActivity().getApplication()).getBoxStore();
        cashBox = boxStore.boxFor(CashTransaction.class);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Calendar c = Calendar.getInstance();

        timeInMillis = c.getTimeInMillis();

        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        editDate.setText(CustomDate.toCustomDate(mDay,mMonth, mYear));


        adapterExpense = ArrayAdapter.createFromResource(getActivity(), R.array.categories_expense,
                android.R.layout.simple_spinner_item);

        adapterIncome = ArrayAdapter.createFromResource(getActivity(), R.array.categories_income,
                android.R.layout.simple_spinner_item);

        adapterExpense.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterIncome.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCategory.setAdapter(adapterExpense);


    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        menu.clear();
        inflater.inflate(R.menu.new_transaction_fragment_menu,menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.save_btn_actionbar) {
            saveBtnClicked();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @OnClick({R.id.radio_btn_expense, R.id.radio_btn_income})
    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radio_btn_expense:
                if (checked)
                    spinnerCategory.setAdapter(adapterExpense);
                break;
            case R.id.radio_btn_income:
                if (checked)
                    spinnerCategory.setAdapter(adapterIncome);
                break;
        }
    }




    public void saveBtnClicked() {

        CashTransaction cashTransaction = viewToCashTransaction();
        if(cashTransaction != null) {
            try {
                ((MainActivity) getActivity()).cashBox.put(cashTransaction);
                Toast.makeText(getActivity(), "Transaction saved", Toast.LENGTH_SHORT).show();

            }catch (Exception ex){
                ex.printStackTrace();
                Toast.makeText(getActivity(), "Database error", Toast.LENGTH_SHORT).show();
            }
            amount.setText("");
            description.setText("");

            Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);
            dateInMillis = c.getTimeInMillis();

            editDate.setText(CustomDate.toCustomDate(mDay, mMonth, mYear));

        }
        cashMoneyQuery = ((MainActivity) getActivity()).cashBox.query().build();
        List<CashTransaction> moneys = cashMoneyQuery.find();
        for (CashTransaction money : moneys) {
            Log.i("main", money.getDescription() + " " + money.getAmount() + " " + money.getId());
        }

    }

    private CashTransaction viewToCashTransaction(){

            CashTransaction cashMoneyTransaction;
            boolean isExpense = expenseRadioBtn.isChecked();
            BigDecimal amountBigDecimal;
            String s = "";
            try {
                s = amount.getText().toString();
                if(s.equals("") || s.equals(null)){
                        Toast.makeText(getActivity(), "Amount can not be empty", Toast.LENGTH_SHORT).show();
                    return null;
                }else{
                    amountBigDecimal = new BigDecimal(s);
                    amountBigDecimal = amountBigDecimal.setScale(2, BigDecimal.ROUND_DOWN);
                }

        }catch (Exception e){
            Toast.makeText(getActivity(), "Don't put " + s + " as amount", Toast.LENGTH_SHORT).show();
            return null;

        }

        String descr = description.getText().toString();
        long date = dateInMillis;
        if(descr.length()>= MAX_LENGTH_DESCRIPTION){
            Toast.makeText(getActivity(), "Description is too long", Toast.LENGTH_SHORT).show();
            return null;
        }

        String category = spinnerCategory.getSelectedItem().toString();


        if(isExpense){
            cashMoneyTransaction = new CashTransaction(amountBigDecimal.toString(),
                    CashTransaction.TYPE_EXPENSE, date, category, descr);
        }
        else {
            cashMoneyTransaction = new CashTransaction(amountBigDecimal.toString(),
                    CashTransaction.TYPE_INCOME, date, category, descr);

        }
        return cashMoneyTransaction;

    }

    @OnClick(R.id.edit_date)
    public void showDatePickerDialog(View v) {

        DialogFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setTargetFragment(this,1234);
        datePickerFragment.show(getActivity().getSupportFragmentManager(), "datePicker");

    }




    public void onDateSet(DatePicker view, int year, int month, int day) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month,day,12, 12,12);

        dateInMillis = calendar.getTimeInMillis();

        editDate.setText(CustomDate.toCustomDateFromMillis(dateInMillis));

        Log.i("onDateSet", "----long millis set manually = " + dateInMillis);


    }



}




