package com.example.imageapplication.helper;


import com.example.imageapplication.model.MapNavigatorList;

import java.util.ArrayList;

/**
 * Created by Ilham Shihnazarow  on 9/7/2021
 */

public interface MainContract {

    interface Presenter{

        void onDestroy();

        void requestDataFromServer(int i);

    }

    interface MainView  {

        void showProgress();

        void hideProgress();

        void setDataToRecyclerView(ArrayList<MapNavigatorList> noticeArrayList);

        void onResponseFailure(Throwable throwable);

    }

    interface Intractor {

        interface OnFinishedListener {
            void onFinished(ArrayList<MapNavigatorList> mapNavigatorListArrayList);
            void onFailure(Throwable t);
        }

        void getPhotosArrayList(int i, OnFinishedListener onFinishedListener);

    }
}
