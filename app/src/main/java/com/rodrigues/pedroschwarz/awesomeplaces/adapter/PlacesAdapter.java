package com.rodrigues.pedroschwarz.awesomeplaces.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rodrigues.pedroschwarz.awesomeplaces.R;
import com.rodrigues.pedroschwarz.awesomeplaces.activity.PlaceActivity;
import com.rodrigues.pedroschwarz.awesomeplaces.model.Place;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolder> {

    private List<Place> places;
    private Context context;

    public PlacesAdapter(List<Place> places, Context context) {
        this.places = places;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Place place = places.get(position);
        Picasso.get().load(place.getImage()).placeholder(R.drawable.place_image).into(holder.itemPlaceImage);
        holder.itemPlaceTitle.setText(place.getTitle());
        holder.itemPlaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PlaceActivity.class);
                intent.putExtra("placeId", place.getId());
                intent.putExtra("placeTitle", place.getTitle());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView itemPlaceImage;
        TextView itemPlaceTitle;
        Button itemPlaceBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            itemPlaceImage = itemView.findViewById(R.id.item_place_image);
            itemPlaceTitle = itemView.findViewById(R.id.item_place_title);
            itemPlaceBtn = itemView.findViewById(R.id.item_place_btn);
        }
    }
}
