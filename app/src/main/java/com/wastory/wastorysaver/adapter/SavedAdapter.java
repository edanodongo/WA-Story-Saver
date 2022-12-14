package com.wastory.wastorysaver.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.wastory.wastorysaver.R;
import com.wastory.wastorysaver.model.Status;
import com.wastory.wastorysaver.ui.ImageView;
import com.wastory.wastorysaver.ui.VideoView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SavedAdapter extends RecyclerView.Adapter<ItemViewHolder> {
    private final List<Status> imagesList;
    private Context context;
    private RewardedAd rewardedAd;

    public SavedAdapter(List<Status> imagesList) {
        this.imagesList = imagesList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_view, parent, false);
        return new ItemViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        holder.save.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_delete_24));
        holder.share.setVisibility(View.VISIBLE);
        holder.save.setVisibility(View.VISIBLE);

        final Status status = imagesList.get(position);

        if (status.isVideo())
            Glide.with(context).asBitmap().load(status.getFile()).into(holder.imageView);

        else
            Picasso.get().load(status.getFile()).into(holder.imageView);

        holder.save.setOnClickListener(view -> {
            if (status.getFile().delete()) {
                imagesList.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context, "File Deleted", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(context, "Unable to Delete File", Toast.LENGTH_SHORT).show();
        });

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String path = status.getPath();
                Intent intent;
                if (!status.isVideo()) {
                    intent = new Intent(context, ImageView.class);


                } else {
                    intent = new Intent(context, VideoView.class);

                }
                intent.putExtra("file", path);
                context.startActivity(intent);
            }
        });

        holder.share.setOnClickListener(v -> {

            Intent shareIntent = new Intent(Intent.ACTION_SEND);

            if (status.isVideo())
                shareIntent.setType("image/mp4");
            else
                shareIntent.setType("image/jpg");

            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + status.getFile().getAbsolutePath()));
            context.startActivity(Intent.createChooser(shareIntent, "Share image"));

        });
    }

    //Code with status keyword error
    /*@Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, int position) {

        holder.save.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_delete_24));
        holder.share.setVisibility(View.VISIBLE);
        holder.save.setVisibility(View.VISIBLE);

        final Status status = imagesList.get(position);
        //holder.getAdapterPosition();
        //holder.imagesList.get(position);

        if (status.isVideo())
            Glide.with(context).asBitmap().load(status.getFile()).into(holder.imageView);

        else
            Picasso.get().load(status.getFile()).into(holder.imageView);

        holder.save.setOnClickListener(view -> {
            if (status.getFile().delete()) {
                imagesList.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context, "File Deleted", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(context, "Unable to Delete File", Toast.LENGTH_SHORT).show();
        });
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String path = imagesList.get(position).getPath();
                Intent intent;
                if (!imagesList.get(position).isVideo()) {
                    intent = new Intent(context, ImageView.class);


                } else {
                    intent = new Intent(context, VideoView.class);

                }
                intent.putExtra("file", path);
                context.startActivity(intent);

            }
        });


        holder.share.setOnClickListener(v -> {

            Intent shareIntent = new Intent(Intent.ACTION_SEND);

            if (status.isVideo())
                shareIntent.setType("image/mp4");
            else
                shareIntent.setType("image/jpg");

            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + status.getFile().getAbsolutePath()));
            context.startActivity(Intent.createChooser(shareIntent, "Share image"));

        });


    }*/



    @Override
    public int getItemCount() {
        return imagesList.size();
    }

}
