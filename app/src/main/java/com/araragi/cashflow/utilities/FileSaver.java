package com.araragi.cashflow.utilities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.araragi.cashflow.entity.CashTransact;
import com.araragi.cashflow.entity.CustomDate;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Araragi on 2017-11-14.
 */

public class FileSaver {

    private ArrayList<CashTransact> dataSet;


    public FileSaver(ArrayList<CashTransact> cashTransacts){
        this.dataSet = cashTransacts;

    }


    public String writeCashTransactionsToSD() {

        String filePath = "storage";

        if(isExternalStorageWritable()) {

            Log.i("FileSaver", "---- SD writable ----");

            try {

                Calendar calendar = Calendar.getInstance();
                long timeInMillis = calendar.getTimeInMillis();
                int hours = calendar.get(Calendar.HOUR_OF_DAY);
                int minutes = calendar.get(Calendar.MINUTE);

                String textFileName = "batabase_copy_" + CustomDate.toCustomDateFromMillis(timeInMillis) +
                        "_" + hours + "_" + minutes + ".txt";

                File file = new File(getStorageDir("/cashflow_transactions"), textFileName);
                filePath = file.getPath();
                FileOutputStream outputStream = new FileOutputStream(file);

                for (CashTransact transaction : dataSet) {
                    String s = transaction.customToString() + "\n";
                    outputStream.write(s.getBytes());
                    Log.i("FileSaver", "---- FileSaver file was written ----");

                }outputStream.flush();
                outputStream.close();
                return filePath;

            } catch (Exception e) {
                e.printStackTrace();
                Log.i("FileSaver", "---- FileSaver writing file exception ----");
                filePath = "";

            }
        }else{
            Log.i("FileSaver", "---- SD is not writable ----");
        }


        return filePath;

    }


    public File getStorageDir(String filename) {

        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), filename);

        if (!file.mkdirs()) {
            Log.e("FileSaver", "Directory not created");
        }

        return file;
    }


    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }



}
