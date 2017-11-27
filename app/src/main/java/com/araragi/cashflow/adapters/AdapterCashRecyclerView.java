package com.araragi.cashflow.adapters;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.araragi.cashflow.R;
import com.araragi.cashflow.entity.CashTransact;
import com.araragi.cashflow.entity.CustomDate;

import java.util.ArrayList;

/**
 * Created by Araragi on 2017-09-20.
 */

public class AdapterCashRecyclerView extends RecyclerView.Adapter<AdapterCashRecyclerView.ViewHolder> {

    private static final String TAG = "CustomAdapter";
    protected ArrayList<CashTransact> mDataSet;

    public interface OnItemClickListener {
        void onItemClicked(View v, int position);
    }

    private OnItemClickListener listener;



    public AdapterCashRecyclerView(ArrayList<CashTransact> cashTransacts, OnItemClickListener listener) {

        mDataSet = cashTransacts;
        this.listener = listener;
    }

    FragmentManager fragmentManager;

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView textDescription;
        private final TextView textCategory;
        private final TextView textAmount;
        private final ImageView image;
        private final TextView textDate;



        public ViewHolder(View v) {

            super(v);


            textCategory = (TextView) v.findViewById(R.id.txt_item_category);
            textDescription = (TextView)v.findViewById(R.id.text_item_description);
            textAmount = (TextView)v.findViewById(R.id.txt_item_amount);
            textDate = (TextView)v.findViewById(R.id.txt_item_date);
            image = (ImageView)v.findViewById(R.id.item_image_plus);


//            v.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    int position = getAdapterPosition();
//                    CashTransact cashTransaction = mDataSet.get(position);
//                    Log.v("adapter","--- Transaction clicked: " + cashTransaction.customToString());
//                    Toast.makeText(v.getContext(), cashTransaction.customToString(), Toast.LENGTH_SHORT).show();
//
//                }
//            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClicked(v, getAdapterPosition());
                }
            });
        }



        public TextView getTextViewDescription() {
            return textDescription;
        }
        public TextView getTextCategory(){
            return textCategory;
        }
        public TextView getTextAmount(){
            return textAmount;
        }
        public TextView getTextDate(){return textDate;}
        public ImageView getImage(){
            return image;
        }
    }




    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_recycler_view, viewGroup, false);

        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
       // Log.d(TAG, "Element " + position + " set.");
        CashTransact cashTransact = mDataSet.get(position);
        viewHolder.getTextCategory().setText(cashTransact.getCategory());
        long millis = cashTransact.getDate();

        viewHolder.getTextDate().setText(CustomDate.toCustomDateFromMillis(millis));
        viewHolder.getTextViewDescription().setText(cashTransact.getDescription());
        viewHolder.getTextAmount().setText(cashTransact.getAmount());
        viewHolder.getImage()
                .setImageResource(cashTransact.getType()== CashTransact.TYPE_INCOME?
                R.mipmap.income_pict:R.mipmap.expense_pict);

    }


    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}

