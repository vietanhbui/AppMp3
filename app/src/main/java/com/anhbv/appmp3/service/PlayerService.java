package com.anhbv.appmp3.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;

import com.anhbv.appmp3.R;
import com.anhbv.appmp3.constant.KeyIntent;
import com.anhbv.appmp3.ui.activities.PlayerActivity;
import com.anhbv.appmp3.ulities.Utilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.anhbv.appmp3.service.MainApp.CHANNEL_ID;

public class PlayerService extends Service implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, MediaPlayer.OnCompletionListener {

    private static final String TAG = "PlayerService";
    private MediaPlayer mediaPlayer;
    private String chanelId;

    private ImageView imvFav;
    private SeekBar seekBar;
    private ImageView imvMore;
    private ImageView imvShuffle;
    private ImageView imvPrev;
    private ImageView imvPlayPause;
    private ImageView imvNext;
    private ImageView imvRepeat;
    private ImageView imvAvatar;
    private RelativeLayout rlPlayer;
    private TextView tvName;
    private TextView tvArtist;
    private TextView tvTimeMax;
    private TextView tvCurrentTime;


    Handler threadHandler = new Handler();
    Utilities utilities = new Utilities();

    public int position;
    private boolean isShuffle;
    private boolean isRepeat = false;
    private int repeat;
    private int shuffle;
    private boolean isFav = false;
    private int indexSong = 0;


    private static final int NOTIF_ID = 1234;


    MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
    private String pathParent = Environment.getExternalStorageDirectory().toString()
            + File.separator + "Zing MP3";

    File fileSong = new File(pathParent);
    File[] listFileSong = fileSong.listFiles();
    List<File> listFile = Arrays.asList(fileSong.listFiles());
    private int index;
    private int pos;

    private NotificationManagerCompat notificationManager;
    private NotificationCompat.Builder builder;


    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        chanelId = getPackageName();
        notificationManager = NotificationManagerCompat.from(this);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(loadUrlSong().get(index)));

        Log.d(TAG, "onCreateaaaa: " + index);

    }

    private List<String> loadUrlSong() {
        List<String> urlSong = new ArrayList<>();
        for (int i = 0; i < listFile.size(); i++) {
            if (listFile.get(i).getName().contains(".mp3")) {
                String song = String.valueOf(listFile.get(i).getAbsoluteFile());
                urlSong.add(new String(song));
            }
        }
        return urlSong;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initView();

        startSong(intent.getIntExtra(KeyIntent.IDPLAY, 0));
        position = intent.getIntExtra(KeyIntent.IDPLAY, 0);

        pos = intent.getIntExtra("idpos", position);
        setUpNotification(position);
        //updateNotification();

        Log.d(TAG, "onStartCommandaaaaa: " + position);

        return START_NOT_STICKY;
    }


    private void setUpNotification(int position) {
        RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.notification_custom);
        mediaMetadataRetriever.setDataSource(loadUrlSong().get(position));
        String title = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        String artist = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        Bitmap bmAvatar;
        if (mediaMetadataRetriever.getEmbeddedPicture() == null) {
            bmAvatar = null;
        } else {
            bmAvatar = BitmapFactory.decodeByteArray(mediaMetadataRetriever.getEmbeddedPicture(), 0, mediaMetadataRetriever.getEmbeddedPicture().length);
        }
        if (bmAvatar == null) {
            notificationLayout.setImageViewResource(R.id.imv_notification_avatar, R.drawable.ic_launcher);
        } else {
            notificationLayout.setImageViewBitmap(R.id.imv_notification_avatar, bmAvatar);
        }
        Log.d(TAG, "createNotifyaaaaa: " + position);

        notificationLayout.setTextViewText(R.id.tv_notification_title, title);
        notificationLayout.setTextViewText(R.id.tv_notification_artist, artist);

//        Intent intent = new Intent(this, NotificationReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_launcher)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setCustomContentView(notificationLayout)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setOngoing(false)
                .build();
        startForeground(0, builder.build());
        notificationManager.notify(0, builder.build());


    }


    private void updateNotification() {
        startForeground(NOTIF_ID, builder.build());
        notificationManager.notify(NOTIF_ID, builder.build());
    }


    private void repeatSong() {
        if (isRepeat) {
            imvRepeat.setImageResource(R.drawable.ic_player_repeat_1);
        } else {
            imvRepeat.setImageResource(R.drawable.ic_player_repeat);
        }
    }

    private void favSong() {
        if (isFav) {
            imvFav.setImageResource(R.drawable.ic_player_fav_selected);
            imvFav.setColorFilter(Color.parseColor("#660066"));
        } else {
            imvFav.setImageResource(R.drawable.ic_player_fav);
        }
    }

    private void shuffleSong() {
        if (isShuffle) {
            imvShuffle.setImageResource(R.drawable.ic_player_shuffle_selected);
            shuffle = 1;
        } else {
            imvShuffle.setImageResource(R.drawable.ic_player_shuffle);
            shuffle = 0;
        }
    }

    private void nextSong() {
        if (shuffle == 1 && position < (loadUrlSong().size() - 1)) {
            Random random = new Random();
            indexSong = random.nextInt((loadUrlSong().size() - 1) - 0 + 1) - 0;
            startSong(indexSong);
            imvPlayPause.setImageResource(R.drawable.ic_player_pause);
        } else {
            if (position < (loadUrlSong().size() - 1)) {
                startSong(position + 1);
                imvPlayPause.setImageResource(R.drawable.ic_player_pause);
                position = position + 1;

            } else {
                startSong(0);
                position = 0;
            }

        }

    }

    public void prevSong() {

        if (shuffle == 1 && position > 0) {
            Random random = new Random();
            indexSong = random.nextInt((loadUrlSong().size() - 1) - 0 + 1) - 0;
            startSong(indexSong);
            imvPlayPause.setImageResource(R.drawable.ic_player_pause);

        } else {
            if (position > 0) {
                startSong(position - 1);
                imvPlayPause.setImageResource(R.drawable.ic_player_pause);
                position = position - 1;
            } else {
                startSong(loadUrlSong().size() - 1);
                position = loadUrlSong().size() - 1;
            }

        }
    }


    public void startSong(int index) {

        Log.d(TAG, "startSongaaa: " + index);

        List url = loadUrlSong();
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(String.valueOf(url.get(index)));
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(this);

            seekBar.setProgress(0);
            seekBar.setMax(100);
            updateProgressBar();
            setUpNotification(index);
        } catch (IOException e) {
            e.printStackTrace();
        }


        mediaMetadataRetriever.setDataSource(String.valueOf(url.get(index)));
        Bitmap bitmapAvatar;
        String nameSong = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        String artistSong = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        if (nameSong.length() > 30) {
            nameSong = nameSong.substring(0, 30) + " ...";
            tvName.setText(nameSong);
        } else {
            tvName.setText(nameSong);
        }
        tvName.setTextColor(Color.parseColor("#FFFFFF"));
        tvName.setTypeface(null, Typeface.BOLD);
        tvArtist.setText(artistSong);

        if (mediaMetadataRetriever.getEmbeddedPicture() == null) {
            bitmapAvatar = null;
        } else {
            bitmapAvatar = BitmapFactory.decodeByteArray(mediaMetadataRetriever.getEmbeddedPicture(), 0, mediaMetadataRetriever.getEmbeddedPicture().length);
        }
        if (bitmapAvatar == null) {
            imvAvatar.setImageResource(R.drawable.default_discview_onboarding);
        } else {
            imvAvatar.setImageBitmap(bitmapAvatar);
            int pixel = bitmapAvatar.getPixel(5, 5);
            rlPlayer.setBackgroundColor(Color.rgb(100 - pixel, 70, 54));
        }

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.cycle_interpolator);
        imvAvatar.startAnimation(animation);

    }


    private void playPause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            imvPlayPause.setImageResource(R.drawable.ic_player_play);
            imvAvatar.clearAnimation();
        } else {
            mediaPlayer.start();
            imvPlayPause.setImageResource(R.drawable.ic_player_pause);
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.cycle_interpolator);
            imvAvatar.startAnimation(animation);
        }
    }


    @Override
    public void onDestroy() {
        mediaPlayer.release();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_player_play_pause:
                playPause();
                break;
            case R.id.imv_player_next:
                nextSong();
                break;
            case R.id.imv_player_prev:
                prevSong();
                break;
            case R.id.imv_player_shuffle:
                isShuffle = !isShuffle;
                shuffleSong();
                break;
            case R.id.imv_player_repeat:
                isRepeat = !isRepeat;
                repeatSong();
                break;
            case R.id.sb_player:
                break;

        }

    }


    public void updateProgressBar() {
        threadHandler.postDelayed(mUpdateTimeTask, 100);

    }

    private Runnable mUpdateTimeTask = new Runnable() {
        @Override
        public void run() {
            long totalDuration = mediaPlayer.getDuration();
            long currentDuration = mediaPlayer.getCurrentPosition();

            tvTimeMax.setText("" + utilities.milliSecondsToTimer(totalDuration));
            tvCurrentTime.setText("" + utilities.milliSecondsToTimer(currentDuration));

            int progress = utilities.getProgressPercentage(currentDuration, totalDuration);

            Log.d(TAG, "runaaaa: " + progress);
            seekBar.setProgress(progress);

            threadHandler.postDelayed(this, 100);
        }
    };

    private void initView() {
        imvFav = PlayerActivity.imvFav;
        seekBar = PlayerActivity.seekBar;
        imvMore = PlayerActivity.imvMore;
        imvShuffle = PlayerActivity.imvShuffle;
        imvPrev = PlayerActivity.imvPrev;
        imvPlayPause = PlayerActivity.imvPlayPause;
        imvNext = PlayerActivity.imvNext;
        imvRepeat = PlayerActivity.imvRepeat;
        imvAvatar = PlayerActivity.imvAvatar;
        rlPlayer = PlayerActivity.rlPlayer;
        tvName = PlayerActivity.tvName;
        tvArtist = PlayerActivity.tvArtist;
        tvTimeMax = PlayerActivity.tvTimeMax;
        tvCurrentTime = PlayerActivity.tvCurrentTime;


        imvFav.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);
        imvMore.setOnClickListener(this);
        imvShuffle.setOnClickListener(this);
        imvPrev.setOnClickListener(this);
        imvPlayPause.setOnClickListener(this);
        imvNext.setOnClickListener(this);
        imvRepeat.setOnClickListener(this);
        imvAvatar.setOnClickListener(this);
        rlPlayer.setOnClickListener(this);
        tvName.setOnClickListener(this);
        tvArtist.setOnClickListener(this);
        tvTimeMax.setOnClickListener(this);
        tvCurrentTime.setOnClickListener(this);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (isRepeat) {
            startSong(indexSong);
        } else if (isShuffle) {
            Random random = new Random();
            indexSong = random.nextInt((loadUrlSong().size() - 1) - 0 + 1) + 0;
            startSong(indexSong);
        } else {
            if (indexSong < (loadUrlSong().size() - 1)) {
                startSong(indexSong + 1);
                indexSong = indexSong + 1;
            } else {
                startSong(0);
                indexSong = 0;
            }
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        threadHandler.removeCallbacks(mUpdateTimeTask);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

        threadHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = mediaPlayer.getDuration();
        int currentPosition = utilities.progressToTimer(seekBar.getProgress(), totalDuration);

        mediaPlayer.seekTo(currentPosition);
        updateProgressBar();
    }


}
