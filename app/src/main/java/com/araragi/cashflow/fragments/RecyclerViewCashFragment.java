package com.araragi.cashflow.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.araragi.cashflow.CashFlowApp;
import com.araragi.cashflow.R;
import com.araragi.cashflow.adapters.AdapterCashRecyclerView;
import com.araragi.cashflow.entity.CashTransact;
import com.araragi.cashflow.entity.CashTransact_;

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

    protected RecyclerView mRecyclerView;
    protected AdapterCashRecyclerView mAdapter;
    protected LinearLayoutManager mLayoutManager;
    protected ArrayList<CashTransact> dataSet;

    private CashTransact cashTransactDeleted;
    private int positionTransactDeleted;

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

        View rootView = inflater.inflate(R.layout.fragment_cash_trans_list, container, false);
        rootView.setTag(TAG);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_for_list_fragment);
        mLayoutManager = new LinearLayoutManager(getActivity());


        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new AdapterCashRecyclerView(dataSet, this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();


        TouchHelperCallback touchHelperCallback = new TouchHelperCallback();
        ItemTouchHelper touchHelper = new ItemTouchHelper(touchHelperCallback);
        touchHelper.attachToRecyclerView(mRecyclerView);

        Toast.makeText(getActivity(), "Swipe to delete", Toast.LENGTH_SHORT).show();

        return rootView;
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
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




    }



    private class TouchHelperCallback extends ItemTouchHelper.SimpleCallback {

        TouchHelperCallback() {
            super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return true;
        }

        @Override
        public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            positionTransactDeleted = position;
            CashTransact transaction = dataSet.get(position);
            cashTransactDeleted = transaction;


            dataSet.remove(position);
            cashBox.remove(transaction);
            mAdapter.notifyItemRemoved(position);

            Snackbar.make(mRecyclerView, "Transaction deleted", Snackbar.LENGTH_LONG)
                    .setAction("UNDO", new SnackBarUndoListener()).show();

        }

        @Override
        public boolean isLongPressDragEnabled() {
            return false;
        }
    }

    public class SnackBarUndoListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {


            if(cashTransactDeleted != null){
                cashTransactDeleted.setId(0);
                long idAdded = cashBox.put(cashTransactDeleted);
                dataSet.add(positionTransactDeleted, cashTransactDeleted);

                mAdapter.notifyDataSetChanged();

                Toast.makeText(getActivity(), "Deletion undone", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getActivity(), "Transaction object null", Toast.LENGTH_SHORT).show();}


        }
    }



    @Override
    public void onItemClicked(View v, int position) {

        CashTransact transaction = dataSet.get(position);
        Log.i("recycler on click", "----on click: -----" + transaction.customToString());

        DialogFragment transactionDetailsFragment = new TransactionDetailsDialogFragment();
        transactionDetailsFragment.setTargetFragment(this,1234);
        transactionDetailsFragment.show(getActivity().getSupportFragmentManager(), "TransactionDetailsDialogFragment");

        Bundle bundle = new Bundle();
        bundle.putLong("transactionId", transaction.getId());
        transactionDetailsFragment.setArguments(bundle);
    }





}




