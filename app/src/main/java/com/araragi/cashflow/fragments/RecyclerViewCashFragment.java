package com.araragi.cashflow.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.araragi.cashflow.CashFlowApp;
import com.araragi.cashflow.R;
import com.araragi.cashflow.adapters.AdapterCashRecyclerView;
import com.araragi.cashflow.entity.CashTransaction;
import com.araragi.cashflow.entity.CashTransaction_;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.query.Query;

/**
 * Created by Araragi on 2017-09-20.
 */

public class RecyclerViewCashFragment extends Fragment {

    public static final String TAG = "RecyclerViewFragment";

    protected RecyclerView mRecyclerView;
    protected AdapterCashRecyclerView mAdapter;
    protected LinearLayoutManager mLayoutManager;
    protected ArrayList<CashTransaction> dataSet;

    private Box<CashTransaction> cashBox;
    private Query<CashTransaction> cashMoneyQuery;

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
        mAdapter = new AdapterCashRecyclerView(dataSet);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();


        TouchHelperCallback touchHelperCallback = new TouchHelperCallback();
        ItemTouchHelper touchHelper = new ItemTouchHelper(touchHelperCallback);
        touchHelper.attachToRecyclerView(mRecyclerView);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);


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

    //
//    @Override
//    public void onResume() {
//
//        super.onResume();
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        initDataSet();
//        mAdapter.notifyDataSetChanged();
//        super.onAttach(context);
//    }

    private void initDataSet() {

        BoxStore boxStore =((CashFlowApp)getActivity().getApplication()).getBoxStore();
        cashBox = boxStore.boxFor(CashTransaction.class);
        cashMoneyQuery = cashBox.query().orderDesc(CashTransaction_.date).build();
        List<CashTransaction> transactionList = cashMoneyQuery.find();
        dataSet = new ArrayList<CashTransaction>(transactionList);



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
            CashTransaction transaction = dataSet.get(position);
            dataSet.remove(position);
            cashBox.remove(transaction);
            mAdapter.notifyItemRemoved(position);

            Snackbar.make(mRecyclerView, "Element deleted", Snackbar.LENGTH_SHORT)
                    .setAction("UNDO", null).show();

        }

        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }
    }



}




