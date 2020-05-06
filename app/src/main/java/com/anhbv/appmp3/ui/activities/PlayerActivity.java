package com.anhbv.appmp3.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.anhbv.appmp3.R;
import com.anhbv.appmp3.constant.KeyIntent;
import com.anhbv.appmp3.dialog.TimerDiaLog;
import com.anhbv.appmp3.service.PlayerService;


public class PlayerActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "PlayerActivity";


    public static TimerDiaLog timerDiaLog;
    public static ImageView imvBack;
    public static ImageView imvTime;
    public static ImageView imvFav;
    public static SeekBar seekBar;
    public static ImageView imvMore;
    public static ImageView imvShuffle;
    public static ImageView imvPrev;
    public static ImageView imvPlayPause;
    public static ImageView imvNext;
    public static ImageView imvRepeat;
    public static ImageView imvAvatar;
    public static RelativeLayout rlPlayer;
    public static TextView tvName;
    public static TextView tvArtist;
    public static TextView tvTimeMax;
    public static TextView tvCurrentTime;


    public int position = 0;
    public int index =0;
    public static final String EXTRA_DATA = "EXTRA_DATA";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initView();

        registrationView();
        position = getIntent().getIntExtra(KeyIntent.IDSONG, 0);


    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(PlayerActivity.this, PlayerService.class);
        intent.putExtra(KeyIntent.IDPLAY, position);
        startService(intent);
        //startServiceSong(position);
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Intent intent = new Intent(PlayerActivity.this,PlayerService.class);
//        intent.putExtra("playsong", String.valueOf(loadUrlSong()));
//        intent.putExtra("idsong",position);
//        startService(intent);
//    }

    private void registrationView() {
        imvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerDiaLog = new TimerDiaLog(PlayerActivity.this);
                timerDiaLog.show();
            }
        });
    }

    private void initView() {
        imvBack = findViewById(R.id.imv_player_back);
        imvTime = findViewById(R.id.imv_player_time);
        imvFav = findViewById(R.id.imv_player_fav);
        seekBar = findViewById(R.id.sb_player);
        imvMore = findViewById(R.id.imv_player_more);
        imvShuffle = findViewById(R.id.imv_player_shuffle);
        imvPrev = findViewById(R.id.imv_player_prev);
        imvPlayPause = findViewById(R.id.imv_player_play_pause);
        imvNext = findViewById(R.id.imv_player_next);
        imvRepeat = findViewById(R.id.imv_player_repeat);
        imvAvatar = findViewById(R.id.imv_player_avatar);
        rlPlayer = findViewById(R.id.rl_player);
        tvName = findViewById(R.id.tv_player_name);
        tvArtist = findViewById(R.id.tv_player_artist);
        tvTimeMax = findViewById(R.id.tv_player_time_finish);
        tvCurrentTime = findViewById(R.id.tv_player_time_start);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_player_time:
                timerDiaLog = new TimerDiaLog(PlayerActivity.this);
                timerDiaLog.show();
                break;
            case R.id.imv_player_back:
                finish();
                break;
        }

    }
}
