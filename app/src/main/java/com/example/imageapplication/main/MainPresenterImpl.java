package com.example.imageapplication.main;



import com.example.imageapplication.helper.MainContract;
import com.example.imageapplication.model.MapNavigatorList;

import java.util.ArrayList;

/**
 * Created by Ilham Shihnazarow  on 9/7/2021
 */

public class MainPresenterImpl implements MainContract.Presenter, MainContract.Intractor.OnFinishedListener {

    private MainContract.MainView mainView;
    private MainContract.Intractor intractor;

    public MainPresenterImpl(MainContract.MainView mainView, MainContract.Intractor intractor){
        this.mainView = mainView;
        this.intractor = intractor;
    }


    @Override
    public void onDestroy() {
        this.mainView = null;
    }

    @Override
    public void requestDataFromServer(int i) {
        if(i==1){
        intractor.getPhotosArrayList(1,this);
    }
    }

    @Override
    public void onFinished(ArrayList<MapNavigatorList> mapNavigatorListArrayList) {
        if(this.mainView != null){
            mainView.setDataToRecyclerView(mapNavigatorListArrayList);
            mainView.hideProgress();
        }
    }

    @Override
    public void onFailure(Throwable t) {
        if(this.mainView != null){
            mainView.onResponseFailure(t);
            mainView.hideProgress();
        }
    }
}
