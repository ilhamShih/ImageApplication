package com.example.imageapplication.main;

import android.util.Log;

import com.example.imageapplication.network.RetrofitInstance;
import com.example.imageapplication.helper.MainContract;
import com.example.imageapplication.model.PhotosDataService;
import com.example.imageapplication.model.NavigatorList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class IntractorImpl implements MainContract.Intractor {
    @Override
    public void getPhotosArrayList(int i ,final OnFinishedListener onFinishedListener) {


        PhotosDataService dataService = RetrofitInstance.getRetrofitInstance().create(PhotosDataService.class);
        Call<NavigatorList> marsPhotosListCall = dataService.getPhotoListCall();

        marsPhotosListCall.enqueue(new Callback<NavigatorList>() {
            @Override
            public void onResponse(Call<NavigatorList> call, Response<NavigatorList> response) {

                if(i==1){
                    /** BazeActivity  */
                    onFinishedListener.onFinished(response.body().getDatas());

        }
            }
            @Override
            public void onFailure(Call<NavigatorList> call, Throwable error) {
               Log.d("Retrofit: ", "Something");
                onFinishedListener.onFailure(error);

            }
        });
    }

}
