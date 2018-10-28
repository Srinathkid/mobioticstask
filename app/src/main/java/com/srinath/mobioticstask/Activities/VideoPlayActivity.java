package com.srinath.mobioticstask.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.srinath.mobioticstask.Adapters.RelatedListAdapter;
import com.srinath.mobioticstask.Model.VideoModel;
import com.srinath.mobioticstask.R;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class VideoPlayActivity extends AppCompatActivity {
    TextView vp_title, vp_description;
    SimpleExoPlayer player;
    PlayerView video_player;
    String video_url_str, video_title_str, video_desc_str;
    String current_video_id;
    List<VideoModel> video_list;
    List<VideoModel> related_video_list = new ArrayList<>();
    ListView related_list;
    RelatedListAdapter relatedListAdapter;
    long playbackPosition = 0;
    int currentWindow = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.activity_video_play);
        Bundle extras = getIntent().getExtras();

        if (extras == null)
            finish();
        current_video_id = extras.getString("video_id");
        video_list = extras.getParcelableArrayList("video_list");

        video_player = (PlayerView) findViewById(R.id.video_player);
        related_list = (ListView) findViewById(R.id.vp_related_video_list);
        vp_title = (TextView) findViewById(R.id.vp_title);
        vp_description = (TextView) findViewById(R.id.vp_description);


    }

    @Override
    protected void onResume() {
        super.onResume();
        copyList(video_list, current_video_id);
        relatedListAdapter = new RelatedListAdapter(this, R.layout.relatedvideo_list_row, related_video_list);
        related_list.setAdapter(relatedListAdapter);

        related_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                playbackPosition = player.getContentPosition();

                Paper.book(current_video_id).write("playbackPosition", playbackPosition);

                current_video_id = related_video_list.get(position).getId();

                copyList(video_list, current_video_id);
            }
        });
    }

    private void copyList(List<VideoModel> video_list, String current_video_id) {

        int pos = 0;
        for (int i = 0; i < video_list.size(); i++) {
            String id = video_list.get(i).getId();
            video_title_str = video_list.get(i).getTitle();
            video_desc_str = video_list.get(i).getDescription();
            video_url_str = video_list.get(i).getUrl();

            if (id.equalsIgnoreCase(current_video_id)) {

                break;
            }
        }
        related_video_list = new ArrayList<VideoModel>(video_list);

        related_video_list.clear();
        for (int i = 0; i < video_list.size(); i++) {
            String id = video_list.get(i).getId();


            if (!id.equalsIgnoreCase(current_video_id)) {

                related_video_list.add(video_list.get(i));
            }
        }
        relatedListAdapter = new RelatedListAdapter(this, R.layout.relatedvideo_list_row, related_video_list);
        related_list.setAdapter(relatedListAdapter);
        releasePlayer();
        playbackPosition = Paper.book(current_video_id).read("playbackPosition", 0L);
        setPlayer(video_title_str, video_desc_str, video_url_str, playbackPosition, currentWindow);


    }

    private void setPlayer(String video_title_str, String video_desc_str, String video_url_str, long playbackPosition, int currentWindow) {

        vp_title.setText(video_title_str);
        vp_description.setText(video_desc_str);

        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(), new DefaultLoadControl());

        video_player.setPlayer(player);

        player.setPlayWhenReady(true);

        player.seekTo(currentWindow, playbackPosition);

        Uri uri = Uri.parse(video_url_str);
        MediaSource mediaSource = buildMediaSource(uri);
        player.prepare(mediaSource, false, false);

        player.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                Paper.book(current_video_id).write("playbackPosition", 0L);
                if (playbackState == ExoPlayer.STATE_ENDED) {
                    copyList(video_list, related_video_list.get(0).getId());
                }

            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("mobiotics_task")).
                createMediaSource(uri);
    }

    @Override
    protected void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        releasePlayer();
        playbackPosition = player.getCurrentPosition();
        currentWindow = player.getCurrentWindowIndex();
        Paper.book(current_video_id).write("playbackPosition", playbackPosition);
    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.release();
            player = null;
        }
    }
}
