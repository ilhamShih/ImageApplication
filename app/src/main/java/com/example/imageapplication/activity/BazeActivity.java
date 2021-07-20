package com.example.imageapplication.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.imageapplication.basket.ActivityBasket;
import com.example.imageapplication.helper.ActivHelper;
import com.example.imageapplication.helper.DBHelper;
import com.example.imageapplication.R;
import com.example.imageapplication.adapter.AdapterBaze;
import com.example.imageapplication.helper.MainContract;
import com.example.imageapplication.helper.MyNotificationManager;
import com.example.imageapplication.helper.RecyclerItemClickListener;
import com.example.imageapplication.helper.SessionManager;
import com.example.imageapplication.main.IntractorImpl;
import com.example.imageapplication.main.MainPresenterImpl;
import com.example.imageapplication.model.MapNavigatorList;
import com.example.imageapplication.model.PhotosDataService;
import com.example.imageapplication.model.NavigatorList;
import com.example.imageapplication.network.RetrofitInstance;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BazeActivity extends AppCompatActivity implements MainContract.MainView{
    DBHelper dbhelper;
    MainContract.Presenter presenter;
    private ProgressBar progressBar;
    RecyclerView recyclerView;
    ArrayList<MapNavigatorList> marsPhotoArrayList;
    AdapterBaze adapterBaze;
    SwipeRefreshLayout swipeRefreshLayout = null;
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 10000;
    public static int size;
    public static int size2;
    private int SIZE3 = 0;
    private int SIZE4 = 0;
    private SessionManager session;
    public String appName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragments);

        SIZE3 = CheckByServer();
        /**--------сессия-------*/
        session = new SessionManager(getApplicationContext());

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        /**--------кнопка избранных-------*/
        FloatingActionButton fab = findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityBasket.class);
                startActivity(intent);
            }
        });
        /**--------кнопка настройки-------*/
        FloatingActionButton fab2 = findViewById(R.id.floatingActionButton3);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivitySettings.class);
                startActivity(intent);
            }
        });
        /**--------адаптер-------*/
        adapterBaze = new AdapterBaze(marsPhotoArrayList, recyclerItemClickListener);
        /**--------пресет-------*/
        presenter = new MainPresenterImpl(this, new IntractorImpl());
        presenter.requestDataFromServer(1);
        /**--------база-------*/
        dbhelper = new DBHelper(getApplicationContext());
        try {
            dbhelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        try {
            dbhelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }
        /**--------прогресс бар если нужен-------*/
        progressBar = new ProgressBar(getApplicationContext(), null, android.R.attr.progressBarStyleLarge);
        progressBar.setIndeterminate(true);
        /**--------ресайкл-------*/
        recyclerView = findViewById(R.id.recyclerViewMarsPhoto);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        RelativeLayout relativeLayout = new RelativeLayout(getApplicationContext());
        relativeLayout.setGravity(Gravity.CENTER);
        relativeLayout.addView(progressBar);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterBaze);
        appName = BazeActivity.this.getPackageName();


    }
    /**--------Уведомления без firebase-------*/
    public void createNotifications(){
        /**--------условие если подписка включена-------*/
        if(session.isLoggedIn()){
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel("CHANNEL_ID", "CHANNEL_NAME", importance);
                mChannel.setDescription("Description");
                mChannel.enableLights(true);
                mChannel.setLightColor(Color.RED);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                mNotificationManager.createNotificationChannel(mChannel);

            }

            MyNotificationManager.getInstance(this).displayNotification("Доступно", "Добавлено новое изображение : ");
            /**--------Подписка выключена не уведомляем-------*/
        }else if(!session.isLoggedIn()){

        }

    }

    /**--------Опрос сервера с заданным темпом-------*/
    @Override
    protected void onResume() {
        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                if (SIZE3 < SIZE4){
                    adapterBaze.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(true);
                    presenter.requestDataFromServer(1);
                    SIZE3 = CheckByServer();
                    /**--------Проверяем запущено ли приложение-------*/
                    if (ActivHelper.isAppRunning(BazeActivity.this, appName)) {
                        Toast.makeText(getApplicationContext(), "приложение активно", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "приложение не активно", Toast.LENGTH_SHORT).show();
                        createNotifications();
                    }

                    handler.postDelayed(runnable, delay);
                    Toast.makeText(getApplicationContext(), String.valueOf(SIZE3)+"  Опрос сервера для size3 < size4", Toast.LENGTH_LONG).show();

                }else if (SIZE3 > SIZE4){
                    handler.postDelayed(runnable, delay);
                    SIZE4 = CheckByServer();
                    Toast.makeText(getApplicationContext(), String.valueOf(SIZE4)+" Опрос сервера для size3 > size4", Toast.LENGTH_LONG).show();
                }else if (SIZE3 == SIZE4){
                    handler.postDelayed(runnable, delay);
                    SIZE4 = CheckByServer();
                    Toast.makeText(getApplicationContext(), String.valueOf(SIZE4)+" Опрос сервера для size3 == size4", Toast.LENGTH_LONG).show();
                }

            }
        }, delay);
        super.onResume();
    }
    /**--------Пауза опроса сервера-------*/
    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    /**--------получаем размер массива с сервера-------*/
    public int CheckByServer(){

        PhotosDataService dataService = RetrofitInstance.getRetrofitInstance().create(PhotosDataService.class);
        Call<NavigatorList> getPhotoListCall = dataService.getPhotoListCall();
        getPhotoListCall.enqueue(new Callback<NavigatorList>() {

            @Override
            public void onResponse(Call<NavigatorList> call, Response<NavigatorList> response) {

                ArrayList<MapNavigatorList> listOfPersons = response.body().getDatas();
                size = listOfPersons.size();
            }
            @Override
            public void onFailure(Call<NavigatorList> call, Throwable t) {
            }
        });
    return size;
    }

    /**--------добавляем в избранное db-------*/
    private RecyclerItemClickListener recyclerItemClickListener = new RecyclerItemClickListener() {
        @Override
        public void onItemClick(String url, long id) {

            if (dbhelper.isDataExist(id)) {
                dbhelper.updateData(id, url);
            } else {
                dbhelper.addData( id, url);
            }
        }
    };
    /**--------прогресс бар-------*/
    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {

        swipeRefreshLayout.setRefreshing(false);
        progressBar.setVisibility(View.INVISIBLE);
    }


    @Override
    public void setDataToRecyclerView(ArrayList<MapNavigatorList> PhotoArrayList) {
        AdapterBaze adapterBaze = new AdapterBaze(PhotoArrayList,
                recyclerItemClickListener);
        recyclerView.setAdapter(adapterBaze);
    }

    @Override
    public void onResponseFailure(Throwable throwable) {
        Toast.makeText(getApplicationContext(),
                "Connection Error" + throwable.getMessage(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        presenter.onDestroy();
    }

}
