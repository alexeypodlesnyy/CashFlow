package com.araragi.cashflow;

import android.app.Application;

import com.araragi.cashflow.entity.MyObjectBox;

import io.objectbox.BoxStore;
import io.objectbox.BoxStoreBuilder;

/**
 * Created by Araragi on 2017-09-14.
 */

public class CashFlowApp extends Application{

    private BoxStore boxStore;

    @Override
    public void onCreate() {


        boxStore = MyObjectBox.builder().androidContext(this).build();
        super.onCreate();


    }

    public BoxStore getBoxStore() {
        return boxStore;
    }

}
