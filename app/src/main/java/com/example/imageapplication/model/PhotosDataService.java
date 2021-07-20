package com.example.imageapplication.model;

import retrofit2.Call;
import retrofit2.http.GET;

import static com.example.imageapplication.Constant.PUT_URL;


/**
 * Created by Ilham Shihnazarow  on 9/7/2021
 */

public interface PhotosDataService {

    @GET(PUT_URL)
    Call<NavigatorList> getPhotoListCall();
}
