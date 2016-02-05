package com.byteshaft.psl_live;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.byteshaft.psl_live.utils.Config;
import com.byteshaft.psl_live.utils.YouTubeFailureRecoveryActivity;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class MainActivity extends YouTubeFailureRecoveryActivity implements
        View.OnClickListener,
        YouTubePlayer.OnFullscreenListener {

    private LinearLayout baseLayout;
    private YouTubePlayerView playerView;
    private YouTubePlayer player;
    private boolean fullscreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        baseLayout = (LinearLayout) findViewById(R.id.layout);
        playerView = (YouTubePlayerView) findViewById(R.id.player);
        playerView.initialize(Config.DEVELOPER_KEY, this);
        doLayout();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                        boolean wasRestored) {
        this.player = player;
        player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
        player.setOnFullscreenListener(this);
        if (!wasRestored) {
            player.cueVideo(Config.YOUTUBE_VIDEO_CODE);
        }
        int controlFlags = player.getFullscreenControlFlags();
        controlFlags &= ~YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE;
        player.setFullscreenControlFlags(controlFlags);
    }

    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return playerView;
    }

    @Override
    public void onClick(View v) {
        player.setFullscreen(!fullscreen);
    }

    private void doLayout() {
        LinearLayout.LayoutParams playerParams =
                (LinearLayout.LayoutParams) playerView.getLayoutParams();
        if (fullscreen) {
            playerParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
            playerParams.height = LinearLayout.LayoutParams.MATCH_PARENT;

        } else {
            playerParams.height = WRAP_CONTENT;
            playerParams.weight = 1;
            baseLayout.setOrientation(LinearLayout.HORIZONTAL);
        }
    }

    @Override
    public void onFullscreen(boolean isFullscreen) {
        fullscreen = isFullscreen;
        doLayout();
    }
}
