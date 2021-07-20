package com.example.imageapplication.basket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imageapplication.R;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import java.util.List;


public class AdapterBasket extends RecyclerView.Adapter<AdapterBasket.ViewHolder> {

    private Context context;
    private List<Basket> arrayBasket;

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView product_image;
        public ViewHolder(View view) {
            super(view);
            product_image = view.findViewById(R.id.product_image);
        }
    }

    public AdapterBasket(Context context, List<Basket> arrayBasket) {
        this.context = context;
        this.arrayBasket = arrayBasket;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(8)
                .oval(false)
                .build();

        Picasso.get()
                .load(ActivityBasket.product_image.get(position))
                .placeholder(R.drawable.ic_loading)
                .resize(250, 250)
                .centerCrop()
                .transform(transformation)
                .into(holder.product_image);

    }

    @Override
    public int getItemCount() {
        return ActivityBasket.product_id.size();
    }

}
