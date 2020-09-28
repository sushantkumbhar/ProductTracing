package com.bct.hackathon.producttracker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bct.hackathon.producttracker.datamodel.transactions.DisplayTransactions;
import com.bct.hackathon.producttracker.datamodel.transactions.Parent;
import com.bct.hackathon.producttracker.datamodel.transactions.Transaction;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DisplayBlocks extends AppCompatActivity {
    Gson gson = new Gson();
    TextView tv_qr_readTxt,prodSerial,prodId;
    private String applicationURL = "https://immense-peak-11930.herokuapp.com/getblocks?hash=";
    ListView listView;
    ProgressBar progressBar;
    CustomAdapter customAdapter;
String hash;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hash=getIntent().getStringExtra("hash");
        listView = (ListView) findViewById(R.id.listView);
        progressBar= (ProgressBar) findViewById(R.id.progressBar);
        tv_qr_readTxt = (TextView) findViewById(R.id.name);
        prodSerial = (TextView) findViewById(R.id.prodSer);
        prodId = (TextView) findViewById(R.id.prodId);
        tv_qr_readTxt = (TextView) findViewById(R.id.name);

        final List<DisplayTransactions> transactions = new ArrayList<>();
        customAdapter = new CustomAdapter(DisplayBlocks.this, transactions);
        tv_qr_readTxt.setText("");
        prodSerial.setText("");
        prodId.setText("");

        String url = null;
        listView.invalidateViews();
        url = applicationURL +hash;
        new GetOrigins(new GetOrigins.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                Type userListType = new TypeToken<ArrayList<Parent>>(){}.getType();
                ArrayList<Parent> parentArray =gson.fromJson(output.replaceAll("\"",""),userListType);
                for(Parent p:parentArray) {
                    Transaction t=p.getCurrentTransction().getTransactions().get(0);
                    transactions.add(new DisplayTransactions(t.getFrom(),t.getTo(),t.getTimestamp(),t.getType(),p.getHash().substring(0,6),p.getPreviousHash().substring(0,6)));
                }
                listView.setAdapter(customAdapter);
                tv_qr_readTxt.setText(parentArray.get(0).getCurrentTransction().getTransactions().get(0).getName());
                prodSerial.setText("Product Serial: "+parentArray.get(0).getCurrentTransction().getTransactions().get(0).getProductSerial());
                prodId.setText("Product Id: "+parentArray.get(0).getCurrentTransction().getTransactions().get(0).getProductId());
            }

            @Override
            public void onProgress(Integer value) {
                progressBar.setProgress(value);
            }
        }).execute(url);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_favorite:
                /*Intent intent = new Intent(this, FeedBackActivity.class);
                startActivity(intent);*/
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
                View view2 = layoutInflaterAndroid.inflate(R.layout.rating, null);
                builder.setView(view2);
                builder.setCancelable(false);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                //view2.findViewById(R.id.button).setOnClickListener(v1 -> onBackPressed());
                view2.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Thank you for your feedback",Toast.LENGTH_LONG).show();
                    }
                });
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}
