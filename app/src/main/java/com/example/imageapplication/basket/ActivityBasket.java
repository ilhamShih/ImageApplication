package com.example.imageapplication.basket;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.recyclerview.widget.RecyclerView;

import com.example.imageapplication.R;
import com.example.imageapplication.helper.DBHelper;

import java.util.ArrayList;
import java.util.List;


public class ActivityBasket extends AppCompatActivity {

    RecyclerView recyclerView;
    View lyt_empty_cart;
    RelativeLayout lyt_order;
    DBHelper dbhelper;
    AdapterBasket adapterBasket;
    double globalVarValue;

    final int CLEAR_ONE_ORDER = 0;
    int FLAG;
    int ID;
    Button  btn_continue;
    private ArrayList<ArrayList<Object>> data;
    public static ArrayList<Integer> product_id = new ArrayList<Integer>();
    public static ArrayList<String> product_image = new ArrayList<String>();

    List<Basket> arrayBasket;
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        view = findViewById(android.R.id.content);

        Intent intent = getIntent();

        recyclerView = findViewById(R.id.recycler_view);
        lyt_empty_cart = findViewById(R.id.lyt_empty_history);

        btn_continue = findViewById(R.id.btn_continue);
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        lyt_order = (RelativeLayout) findViewById(R.id.lyt_history);
        adapterBasket = new AdapterBasket(this, arrayBasket);
        dbhelper = new DBHelper(this);

        try {
            dbhelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showClearDialog(CLEAR_ONE_ORDER, product_id.get(position));
                    }
                }, 400);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        new getDataTask().execute();
    }

    public void showClearDialog(int flag, int id) {
        FLAG = flag;
        ID = id;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.confirm);
        switch (FLAG) {
            case 0:
                builder.setMessage(getString(R.string.clear_one_order));
                break;

        }
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getString(R.string.dialog_option_yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (FLAG) {
                    case 0:
                        dbhelper.deleteData(ID);
                        clearData();
                        new getDataTask().execute();
                        break;
                }
            }
        });

        builder.setNegativeButton(getResources().getString(R.string.dialog_option_no), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    public void clearData() {
        product_id.clear();
        product_image.clear();
    }

    public class getDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            getDataFromDatabase();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            if (product_id.size() > 0) {
                lyt_order.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(adapterBasket);

            } else {
                lyt_empty_cart.setVisibility(View.VISIBLE);
            }
        }
    }

    public void getDataFromDatabase() {
        clearData();
        data = dbhelper.getAllData();

        for (int i = 0; i < data.size(); i++) {
            ArrayList<Object> row = data.get(i);
            product_id.add(Integer.parseInt(row.get(0).toString()));
            product_image.add(row.get(1).toString());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {

            this.clickListener = clickListener;

            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }

    public interface ClickListener {
        public void onClick(View view, int position);
        public void onLongClick(View view, int position);
    }

}