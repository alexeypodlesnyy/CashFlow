package com.araragi.cashflow.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.araragi.cashflow.CashFlowApp;
import com.araragi.cashflow.R;
import com.araragi.cashflow.activities.MainActivity;
import com.araragi.cashflow.adapters.AdapterCashRecyclerView;
import com.araragi.cashflow.entity.CashTransact;
import com.araragi.cashflow.entity.CashTransact_;
import com.araragi.cashflow.utilities.StatisticalCalculations;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.query.Query;


/**
 * Created by Araragi on 2017-09-20.
 */

public class RecyclerViewCashFragment extends Fragment implements AdapterCashRecyclerView.OnItemClickListener{

    public static final String TAG = "RecyclerViewFragment";
    private static final String TRANSACTION_POSITION_LAST_CLICKED = "transactionPositionLastClicked";
    public static final int RECYCLER_FRAGMENT_REQUEST_CODE = 101;


    protected RecyclerView mRecyclerView;
    protected AdapterCashRecyclerView mAdapter;
    protected LinearLayoutManager mLayoutManager;
    protected ArrayList<CashTransact> dataSet;

    private CashTransact cashTransactDeleted;
    private int positionTransactClickedLast;
//
//    private String balance;
//
//    public TextView balanceText;

//    private AdapterCashRecyclerView.OnItemClickListener listener;

    private Box<CashTransact> cashBox;
    private Query<CashTransact> cashMoneyQuery;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        initDataSet();

        setHasOptionsMenu(true);

        if(savedInstanceState != null){
            positionTransactClickedLast = savedInstanceState.getInt(TRANSACTION_POSITION_LAST_CLICKED);
        }

        View rootView = inflater.inflate(R.layout.fragment_cash_trans_list, container, false);
        rootView.setTag(TAG);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_for_list_fragment);
        mLayoutManager = new LinearLayoutManager(getActivity());



       // balanceText = (TextView)getActivity().findViewById(R.id.txt_balance_in_recycler_view);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new AdapterCashRecyclerView(dataSet, this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

//
//        balance = "0";
//        if(dataSet.size() > 0) {
//            StatisticalCalculations calculations = new StatisticalCalculations(dataSet);
//            calculations.calculate();
//            balance = calculations.getBalance().toString();
//            balanceText.setText("Balance: " + balance);
//        }




//        TouchHelperCallback touchHelperCallback = new TouchHelperCallback();
//        ItemTouchHelper touchHelper = new ItemTouchHelper(touchHelperCallback);
//        touchHelper.attachToRecyclerView(mRecyclerView);
//
//        Toast.makeText(getActivity(), "Swipe to delete", Toast.LENGTH_SHORT).show();

        return rootView;
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(TRANSACTION_POSITION_LAST_CLICKED, positionTransactClickedLast);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

    }


    private void initDataSet() {

        BoxStore boxStore =((CashFlowApp)getActivity().getApplication()).getBoxStore();
        cashBox = boxStore.boxFor(CashTransact.class);
        cashMoneyQuery = cashBox.query().orderDesc(CashTransact_.date).build();
        List<CashTransact> transactionList = cashMoneyQuery.find();
        dataSet = new ArrayList<CashTransact>(transactionList);
        Collections.sort(dataSet);
        Collections.reverse(dataSet);

    }



//    private class TouchHelperCallback extends ItemTouchHelper.SimpleCallback {
//
//        TouchHelperCallback() {
//            super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
//        }
//
//        @Override
//        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//            return true;
//        }
//
//        @Override
//        public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
//            int position = viewHolder.getAdapterPosition();
//            positionTransactDeleted = position;
//            CashTransact transaction = dataSet.get(position);
//            cashTransactDeleted = transaction;
//
//
//            dataSet.remove(position);
//            cashBox.remove(transaction);
//            mAdapter.notifyItemRemoved(position);
//
//            Snackbar.make(mRecyclerView, "Transaction deleted", Snackbar.LENGTH_LONG)
//                    .setAction("UNDO", new SnackBarUndoListener()).show();
//
//        }
//
//        @Override
//        public boolean isLongPressDragEnabled() {
//            return false;
//        }
//    }

    public class SnackBarUndoListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {


            if(cashTransactDeleted != null){
                cashTransactDeleted.setId(0);
                long idAdded = cashBox.put(cashTransactDeleted);
                dataSet.add(positionTransactClickedLast, cashTransactDeleted);

                mAdapter.notifyDataSetChanged();

                Toast.makeText(getActivity(), "Deletion undone", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getActivity(), "Transaction object null", Toast.LENGTH_SHORT).show();}


        }
    }



    @Override
    public void onItemClicked(View v, int position) {

        CashTransact transaction = dataSet.get(position);
        positionTransactClickedLast = position;

        Log.i("recycler on click", "----on click: -----" + transaction.customToString());

        DialogFragment transactionDetailsFragment = new TransactionDetailsDialogFragment();
        transactionDetailsFragment.setTargetFragment(this,RECYCLER_FRAGMENT_REQUEST_CODE);
        transactionDetailsFragment.show(getActivity().getSupportFragmentManager(), "TransactionDetailsDialogFragment");




        Bundle bundle = new Bundle();
        bundle.putLong("transactionId", transaction.getId());
        bundle.putInt("transactionPositionLastClicked", positionTransactClickedLast);
        transactionDetailsFragment.setArguments(bundle);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
      //  super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case RECYCLER_FRAGMENT_REQUEST_CODE:

                if (resultCode == Activity.RESULT_OK) {
                    //Toast.makeText(getActivity(), "Clicked cancel", Toast.LENGTH_SHORT).show();
                } else if (resultCode == Activity.RESULT_CANCELED){


                    CashTransact transaction = dataSet.get(positionTransactClickedLast);
                    cashTransactDeleted = transaction;


                    dataSet.remove(positionTransactClickedLast);
                    cashBox.remove(transaction);
                    mAdapter.notifyItemRemoved(positionTransactClickedLast);

                    Snackbar.make(mRecyclerView, "Transaction deleted", Snackbar.LENGTH_LONG)
                    .setAction("UNDO", new SnackBarUndoListener()).show();
                }

                break;
        }
    }
}




