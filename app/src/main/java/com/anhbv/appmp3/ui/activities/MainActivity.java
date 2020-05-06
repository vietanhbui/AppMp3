package com.anhbv.appmp3.ui.activities;

import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;

import com.anhbv.appmp3.R;
import com.anhbv.appmp3.adapter.TabFragAdapter;
import com.anhbv.appmp3.ui.fragments.TabAccountFrag;
import com.anhbv.appmp3.ui.fragments.TabChartFrag;
import com.anhbv.appmp3.ui.fragments.TabFeedFrag;
import com.anhbv.appmp3.ui.fragments.TabHomeFrag;
import com.anhbv.appmp3.ui.fragments.TabVideoFrag;
import com.anhbv.appmp3.ui.layouts.ItemTabFrag;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ViewPager vpgMain;
    private LinearLayout llMain;
    private List<Fragment> fragments;
    private List<ItemTabFrag> itemTabFrags;

    private MediaPlayer mediaPlayer;
    MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
    private byte[] imvAvatarSong;
    private String pathParent = Environment.getExternalStorageDirectory().toString()
            + File.separator + "Zing MP3";

    File fileSong = new File(pathParent);
    File[] listFileSong = fileSong.listFiles();
    List<File> listFile = Arrays.asList(fileSong.listFiles());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initTab();
        initFrag();
        initViewPager();
        registerListeners();

        vpgMain.setCurrentItem(1);

        //int index = getIntent().getIntExtra(KeyIntent.IDPLAY,0);
        //playSong(index);

    }

    private void playSong(int index){
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(loadUrlSong().get(index));
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> loadUrlSong(){
        List<String> urlSong = new ArrayList<>();
        for (int i =0;i<listFile.size();i++){
            if (listFile.get(i).getName().contains(".mp3")){
                String song = String.valueOf(listFile.get(i).getAbsoluteFile());
                urlSong.add(new String(song));
            }
        }
        return urlSong;
    }

    private void registerListeners() {
        vpgMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                //dang dc vuot
            }

            @Override
            public void onPageSelected(int i) {
                onSelectTab(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                //bi out ra
            }
        });
    }

    private void initViewPager() {
        TabFragAdapter adapter = new TabFragAdapter(getSupportFragmentManager(),fragments);
        vpgMain.setAdapter(adapter);
    }

    private void initFrag() {
        fragments = new ArrayList<>();
        fragments.add(new TabAccountFrag());
        fragments.add(new TabHomeFrag());
        fragments.add(new TabChartFrag());
        fragments.add(new TabVideoFrag());
        fragments.add(new TabFeedFrag());
    }

    private void initTab() {
        int width = widthScreen()/5;
        itemTabFrags = new ArrayList<>();
        itemTabFrags.add(new ItemTabFrag(this,R.drawable.ic_tab_account,"Cá nhân",width,0));
        itemTabFrags.add(new ItemTabFrag(this,R.drawable.ic_tab_home,"Khám phá",width,1));
        itemTabFrags.add(new ItemTabFrag(this,R.drawable.ic_tab_chart,"Chart",width,2));
        itemTabFrags.add(new ItemTabFrag(this,R.drawable.ic_tab_video,"MV",width,3));
        itemTabFrags.add(new ItemTabFrag(this,R.drawable.ic_tab_feed,"Album",width,4));

        llMain.setWeightSum(itemTabFrags.size());

        for (int i =0;i<itemTabFrags.size();i++){
            ItemTabFrag itemTabFrag = itemTabFrags.get(i);
            itemTabFrag.setiOnClickItemTab(new ItemTabFrag.IOnClickItemTab() {
                @Override
                public void OnClickItemTab(int position) {
                    vpgMain.setCurrentItem(position);
                    onSelectTab(position);
                }
            });
            llMain.addView(itemTabFrag);
        }
    }

    private void initView() {
        vpgMain = findViewById(R.id.vpg_main);
        llMain = findViewById(R.id.ll_tab_frag);
    }

    private void onSelectTab(int position){
        for (int i =0; i<itemTabFrags.size();i++){
            itemTabFrags.get(i).selected(false);
        }
        itemTabFrags.get(position).selected(true);
    }

    private int widthScreen(){
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }


}
