package com.araragi.cashflow.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.araragi.cashflow.CashFlowApp;
import com.araragi.cashflow.R;
import com.araragi.cashflow.entity.CashTransact;
import com.araragi.cashflow.entity.CashTransact_;
import com.araragi.cashflow.utilities.FileSaver;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.query.Query;

/**
 * Created by Araragi on 2017-11-13.
 */

public class DataManagerFragment extends Fragment {

    public static final String TAG = "DtaManagerFragment";

    @BindView(R.id.btn_save_to_file)
    public Button saveToFile;

    private Unbinder unbinder;

    private Box<CashTransact> cashBox;
    private Query<CashTransact> cashMoneyQuery;
    private ArrayList<CashTransact> dataSet;

    private final static int PERMISSION_TO_WRITE_TO_SD = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        initDataSet();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_data_mngr, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

    @OnClick(R.id.btn_save_to_file)
    public void saveToFile(View view) {
        writeToSdBlyad();
    }

    private void initDataSet() {

        BoxStore boxStore = ((CashFlowApp) getActivity().getApplication()).getBoxStore();
        cashBox = boxStore.boxFor(CashTransact.class);
        cashMoneyQuery = cashBox.query().orderDesc(CashTransact_.date).build();
        List<CashTransact> transactionList = cashMoneyQuery.find();
        dataSet = new ArrayList<CashTransact>(transactionList);

    }

    private void checkPermission() {

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale( getActivity() ,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//
//
//            } else {


            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_TO_WRITE_TO_SD);
//
//            }
        }


        }

        @Override
        public void onRequestPermissionsResult ( int requestCode,
        String permissions[], int[] grantResults){
            switch (requestCode) {
                case PERMISSION_TO_WRITE_TO_SD: {
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    } else {

                    }
                    return;
                }


            }
        }

    private void writeToSdBlyad() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {

            FileSaver fileSaver = new FileSaver(dataSet);

            String filePath = fileSaver.writeCashTransactionsToSD();
            if(filePath.equals("")){
                Toast.makeText(getActivity(), "File was not created", Toast.LENGTH_LONG).show();

            }else{
                Toast.makeText(getActivity(), "Transactions saved: " + filePath, Toast.LENGTH_LONG).show();
            }


        } else {

            checkPermission();
        }

    }


    }


