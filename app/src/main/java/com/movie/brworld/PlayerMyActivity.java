package com.movie.brworld;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.ExoMediaCrypto;
import com.google.android.exoplayer2.drm.ExoMediaDrm;
import com.google.android.exoplayer2.drm.MediaDrmCallback;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.dash.DashChunkSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.ui.TrackSelectionView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoRendererEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import constants.ConstantCodes;


public class PlayerMyActivity extends AppCompatActivity implements VideoRendererEventListener, helpers.TrackSelection.GetReso {

    private SharedPreferences mSharedPreferences;
    DefaultBandwidthMeter bandwidthMeter;
    ImageView ivSetting;
    private PlayerView playerView;
    private SimpleExoPlayer player;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_my_player);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);


        if (getIntent().getStringExtra("path") != null) {
            if (!getIntent().getStringExtra("path").equals("")) {

                initializePlayer(getIntent().getStringExtra("path"));

                int resId = getResources().getIdentifier("exo_shutter", "id", getPackageName());
                findViewById(resId).setBackgroundColor(ContextCompat.getColor(this, R.color.transparent));

            }
        }
    }


    private void initializePlayer(String path) {

        ivSetting = findViewById(R.id.ivSetting);

        playerView = findViewById(R.id.video_view);

        bandwidthMeter = new DefaultBandwidthMeter();

        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        DefaultTrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

        playerView.setPlayer(player);

        Uri uri = Uri.parse(path);
        MediaSource mediaSource = buildMediaSource(uri);

        player.setPlayWhenReady(true);
        player.seekTo(0, 0);
        player.prepare(mediaSource, false, false);


        ivSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    TrackSelectionDialog trackSelectionDialog =
                            TrackSelectionDialog.createForTrackSelector(
                                    trackSelector, onDismissListener);
                    trackSelectionDialog.show(getSupportFragmentManager(), /* tag= */ null);

                } catch (Exception e) {
                    Toast.makeText(PlayerMyActivity.this, "Please Try Again Later", Toast.LENGTH_SHORT).show();
                }

            /*    List<String> audioLanguages = new ArrayList<>();
                for (int i = 0; i < player.getCurrentTrackGroups().length; i++) {
                    String format = player.getCurrentTrackGroups().get(i).getFormat(0).sampleMimeType;
                    String lang = player.getCurrentTrackGroups().get(i).getFormat(0).language;
                    String id = player.getCurrentTrackGroups().get(i).getFormat(0).id;

                    Log.e(player.getCurrentTrackGroups().get(i).getFormat(0).language, "lan");

                }


                MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
                if (mappedTrackInfo != null) {
                    //CharSequence title = ((Button) view).getText();
                    //int rendererIndex = (int) v.getTag();
                    int rendererType = mappedTrackInfo.getRendererType(0);
                    boolean allowAdaptiveSelections =
                            rendererType == C.TRACK_TYPE_VIDEO
                                    || (rendererType == C.TRACK_TYPE_AUDIO
                                    && mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_VIDEO)
                                    == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_NO_TRACKS);

                    Pair<AlertDialog, helpers.TrackSelection> dialogPair = helpers.TrackSelection.getDialog(PlayerMyActivity.this, "Video Quality", trackSelector,
                            0, PlayerMyActivity.this,
                            bandwidthMeter.toString(), player.getVideoFormat());
                    dialogPair.second.setShowDisableOption(false);
                    dialogPair.second.setAllowAdaptiveSelections(allowAdaptiveSelections);
                    dialogPair.first.show();
                }*/
            }
        });


        //  trackSelector.setParameters(trackSelector.getParameters().buildUpon().setPreferredAudioLanguage("guj"));

       /* List<String> audioLanguages = new ArrayList<>();
        for (int i = 0; i < player.getCurrentTrackGroups().length; i++) {
            String format = player.getCurrentTrackGroups().get(i).getFormat(0).sampleMimeType;
            String lang = player.getCurrentTrackGroups().get(i).getFormat(0).language;
            String id = player.getCurrentTrackGroups().get(i).getFormat(0).id;

            Log.e(player.getCurrentTrackGroups().get(i).getFormat(0).language + "", "lan");

        }*/

    }

    DialogInterface.OnDismissListener onDismissListener = new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialog) {
            dialog.dismiss();
        }
    };

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        Toast.makeText(this, "RES:(WxH):" + width + "X" + height + "\n           " + height + "p", Toast.LENGTH_SHORT).show();
    }

    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(this, "brworld", bandwidthMeter);

        return new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);
    }

    private void releasePlayer() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.release();
            player = null;
        }
    }

    @Override
    protected void onDestroy() {
        releasePlayer();
        super.onDestroy();

    }

    @Override
    protected void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void getResoText(String resoText) {
        Log.e("resotext", "" + resoText);

    }


    private MediaSource createLeafMediaSource(
            Uri uri, String extension) {
        @C.ContentType int type = Util.inferContentType(uri, extension);

        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(this, "brworld", bandwidthMeter);

        DefaultDrmSessionManager drmSessionManager = new DefaultDrmSessionManager.Builder().build(new MediaDrmCallback() {
            @Override
            public byte[] executeProvisionRequest(UUID uuid, ExoMediaDrm.ProvisionRequest request) throws Exception {
                return new byte[0];
            }

            @Override
            public byte[] executeKeyRequest(UUID uuid, ExoMediaDrm.KeyRequest request) throws Exception {
                return new byte[0];
            }
        });

        switch (type) {
            case C.TYPE_DASH:
                return new DashMediaSource.Factory(dataSourceFactory)
                        .setDrmSessionManager(drmSessionManager)
                        .createMediaSource(uri);
            case C.TYPE_SS:
                return new SsMediaSource.Factory(dataSourceFactory)
                        .setDrmSessionManager(drmSessionManager)
                        .createMediaSource(uri);
            case C.TYPE_HLS:
                return new HlsMediaSource.Factory(dataSourceFactory)
                        .setDrmSessionManager(drmSessionManager)
                        .createMediaSource(uri);
            case C.TYPE_OTHER:
                return new ProgressiveMediaSource.Factory(dataSourceFactory)
                        .setDrmSessionManager(drmSessionManager)
                        .createMediaSource(uri);
            default:
                throw new IllegalStateException("Unsupported type: " + type);
        }
    }



}
