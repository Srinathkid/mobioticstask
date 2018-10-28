package com.srinath.mobioticstask.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.srinath.mobioticstask.Activities.VideoPlayActivity;
import com.srinath.mobioticstask.Model.VideoModel;
import com.srinath.mobioticstask.R;

import java.util.ArrayList;
import java.util.List;

public class RelatedListAdapter extends ArrayAdapter<VideoModel> {
    List<VideoModel> relatedvideo_list = new ArrayList<>();
    Context context;

    public RelatedListAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        this.context = context;
    }

    public RelatedListAdapter(VideoPlayActivity context, int resource, List<VideoModel> related_video_list) {
        super(context, resource, related_video_list);
        this.context = context;
        this.relatedvideo_list = related_video_list;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;
        view = LayoutInflater.from(context).inflate(R.layout.relatedvideo_list_row, parent, false);
        VideoModel relatedvieo = relatedvideo_list.get(position);
        ImageView rv_thumb_image = (ImageView) view.findViewById(R.id.rv_list_thumb);
        TextView rv_list_title = (TextView) view.findViewById(R.id.rv_list_title);
        TextView rv_list_description = (TextView) view.findViewById(R.id.rv_list_description);

        Glide.with(context)
                .load(relatedvieo.getThumb())
                .asBitmap()
                .placeholder(R.drawable.thumb_image)
                .error(R.drawable.thumb_image)
                .into(rv_thumb_image);

        rv_list_title.setText(relatedvieo.getTitle());
        rv_list_description.setText(relatedvieo.getDescription());


        return view;
    }
}
