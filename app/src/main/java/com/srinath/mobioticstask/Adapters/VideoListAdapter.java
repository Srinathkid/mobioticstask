package com.srinath.mobioticstask.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.srinath.mobioticstask.Activities.VideoListActivity;
import com.srinath.mobioticstask.Activities.VideoPlayActivity;
import com.srinath.mobioticstask.Model.VideoModel;
import com.srinath.mobioticstask.R;

import java.util.ArrayList;
import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> {

    Context context;
    List<VideoModel> video_list;

    public VideoListAdapter(Context context, List<VideoModel> video_list) {
        this.context = context;
        this.video_list = video_list;

    }

    @NonNull
    @Override
    public VideoListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.video_list_row, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull VideoListAdapter.ViewHolder viewHolder, final int i) {
        VideoModel videoModel = video_list.get(i);

        viewHolder.video_thumb_img.layout(0, 0, 0, 0);
        Glide.with(context)
                .load(videoModel.getThumb())
                .asBitmap()
                .placeholder(R.drawable.thumb_image)
                .error(R.drawable.thumb_image)
                .into(viewHolder.video_thumb_img);

        viewHolder.title.setText(video_list.get(i).getTitle());
        viewHolder.desc.setText(video_list.get(i).getDescription());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoPlayActivity.class);
                intent.putExtra("video_id", video_list.get(i).getId());
                intent.putParcelableArrayListExtra("video_list", (ArrayList<? extends Parcelable>) video_list);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return video_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView video_thumb_img;
        TextView title, desc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            video_thumb_img = (ImageView) itemView.findViewById(R.id.vl_row_img);
            title = (TextView) itemView.findViewById(R.id.vl_row_title);
            desc = (TextView) itemView.findViewById(R.id.vl_row_des);

        }
    }
}
