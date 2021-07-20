package com.example.imageapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;


import com.example.imageapplication.R;
import com.example.imageapplication.helper.RecyclerItemClickListener;
import com.example.imageapplication.model.MapNavigatorList;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Ilham Shihnazarow  on 9/7/2021
 */

public class AdapterBaze extends RecyclerView.Adapter<AdapterBaze.PhotosViewHolder>{

    private ArrayList<MapNavigatorList> mapNavigatorListArrayList;
    private RecyclerItemClickListener recyclerItemClickListener;
    private List<MapNavigatorList> productListFiltered;

    public AdapterBaze(ArrayList<MapNavigatorList> mapNavigatorListArrayList, RecyclerItemClickListener recyclerItemClickListener ){

        this.mapNavigatorListArrayList = mapNavigatorListArrayList;
        this.recyclerItemClickListener = recyclerItemClickListener;
        this.productListFiltered = mapNavigatorListArrayList;
    }

    @Override
    public PhotosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_mars_photo, parent, false);
        return new PhotosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PhotosViewHolder holder, int position) {
        MapNavigatorList product = mapNavigatorListArrayList.get(position);
        product   = productListFiltered.get(mapNavigatorListArrayList.size() - 1);

        final String logoPhotoURL = product.getImage();
        final long id = product.imageId();

        Picasso.get()
                .load(logoPhotoURL)
                .into(holder.imageUrl);


        holder.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerItemClickListener.onItemClick(logoPhotoURL, id);

            }
        });
    }

    @Override
    public int getItemCount() {
        if(mapNavigatorListArrayList != null)
            return productListFiltered.size()-productListFiltered.size()+1;
        else
            return 0;
    }

    class PhotosViewHolder extends RecyclerView.ViewHolder{
        ImageView imageUrl;
        FloatingActionButton fab;
        PhotosViewHolder(View view){
            super(view);
            fab= view.findViewById(R.id.floats);
            imageUrl = view.findViewById(R.id.images);

        }
    }

}
