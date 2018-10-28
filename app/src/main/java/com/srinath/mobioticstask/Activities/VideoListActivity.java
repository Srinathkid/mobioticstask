package com.srinath.mobioticstask.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.srinath.mobioticstask.Adapters.VideoListAdapter;
import com.srinath.mobioticstask.R;
import com.srinath.mobioticstask.Model.VideoModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VideoListActivity extends AppCompatActivity {
    RequestQueue queue;
    Gson gson;
    RecyclerView video_list_rv;
    List<VideoModel> videolist;
    VideoListAdapter videoLIstAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        queue = Volley.newRequestQueue(this);
        gson = new GsonBuilder().create();
        videolist = new ArrayList<>();
        video_list_rv = (RecyclerView) findViewById(R.id.rv_video_list);

        videoLIstAdapter = new VideoListAdapter(VideoListActivity.this, videolist);
        video_list_rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false));
        video_list_rv.setAdapter(videoLIstAdapter);
        getJSONData();


    }

    private void getJSONData() {
        final String url = "https://interview-e18de.firebaseio.com/media.json?print=pretty";

        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                        System.out.println("JSON Response : " + response.toString());
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject object = response.getJSONObject(i);
                                VideoModel model = gson.fromJson(object.toString(), VideoModel.class);
                                videolist.add(model);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        videoLIstAdapter = new VideoListAdapter(VideoListActivity.this, videolist);
                            video_list_rv.setAdapter(videoLIstAdapter);
//                        videoLIstAdapter.notifyDataSetChanged();
                        System.out.println("Video Size : " + videolist.size());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("JSON Error : " + error.getMessage());

                    }
                }
        );

// add it to the RequestQueue
        queue.add(getRequest);
//        addViewPager(viewPager);
    }

}
