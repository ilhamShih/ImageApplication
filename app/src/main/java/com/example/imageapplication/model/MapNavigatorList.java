package com.example.imageapplication.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ilham Shihnazarow  on 9/7/2021
 */

public class MapNavigatorList {
    @SerializedName("image")
    private String image;
    @SerializedName("id")
    public long id;

    public MapNavigatorList(
            Long id,
            String image) {
        this.id = id;
        this.image = image;
    }

    public String getImage() { return image; }
    public long imageId() {
        return id;
    }

}
