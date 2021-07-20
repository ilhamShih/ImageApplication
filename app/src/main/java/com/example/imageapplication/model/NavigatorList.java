package com.example.imageapplication.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Ilham Shihnazarow  on 9/7/2021
 */

public class NavigatorList {

    @SerializedName("datas")
    private ArrayList<MapNavigatorList> datas;

/** Get*/
    public ArrayList<MapNavigatorList> getDatas(){ return datas; }
/** Set*/
    public void setDatas(ArrayList<MapNavigatorList> datas){ this.datas = datas; }

}
