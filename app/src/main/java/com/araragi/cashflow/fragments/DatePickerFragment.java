package com.araragi.cashflow.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;

import com.araragi.cashflow.R;
import com.araragi.cashflow.activities.MainActivity;
import com.araragi.cashflow.entity.CustomDate;

import java.util.Calendar;

import static com.araragi.cashflow.R.id.textView;

/**
 * Created by Araragi on 2017-09-18.
 */

public class DatePickerFragment extends DialogFragment{



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Calendar c2 = Calendar.getInstance();

        int year = c2.get(Calendar.YEAR);
        int month = c2.get(Calendar.MONTH);
        int day = c2.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener)getTargetFragment(), year, month, day);
    }

}
