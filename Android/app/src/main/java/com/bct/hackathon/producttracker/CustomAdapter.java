package com.bct.hackathon.producttracker;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.bct.hackathon.producttracker.datamodel.transactions.DisplayTransactions;

import java.util.List;

class CustomAdapter implements ListAdapter {
    List<DisplayTransactions> arrayList;
    Context context;
    public CustomAdapter(Context context, List<DisplayTransactions> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
    }
    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }
    @Override
    public boolean isEnabled(int position) {
        return true;
    }
    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
    }
    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }
    @Override
    public Object getItem(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public boolean hasStableIds() {
        return false;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DisplayTransactions subjectData=arrayList.get(position);
        if(convertView==null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView=layoutInflater.inflate(R.layout.list_row, null);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            TextView tittle2=convertView.findViewById(R.id.from);
            tittle2.setText("From: "+subjectData.getFrom());
            TextView tittle3=convertView.findViewById(R.id.to);
            tittle3.setText("To: "+subjectData.getTo());
            TextView tittle4=convertView.findViewById(R.id.timestamp);
            tittle4.setText("Timestamp: "+subjectData.getTimestamp());
            ImageView img=convertView.findViewById(R.id.imageView);
         //   Log.e("type",subjectData.getType());
            if(subjectData.getType().equalsIgnoreCase("retailer")) {
             //   Log.e("type","retailer");
                img.setImageDrawable(context.getResources().getDrawable(R.drawable.retail));
            }
            if(subjectData.getType().equalsIgnoreCase("transportation")) {
             //   Log.e("type","transportation");
                img.setImageDrawable(context.getResources().getDrawable(R.drawable.distribution));
            }
            if(subjectData.getType().equalsIgnoreCase("manufacturer")) {
               // Log.e("type","manufacturer");
                img.setImageDrawable(context.getResources().getDrawable(R.drawable.manufacturing));
            }

        }
        return convertView;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getViewTypeCount() {
        return 1;
    }
    @Override
    public boolean isEmpty() {
        return false;
    }
}