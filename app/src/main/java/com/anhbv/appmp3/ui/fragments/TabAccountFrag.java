package com.anhbv.appmp3.ui.fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anhbv.appmp3.R;
import com.anhbv.appmp3.adapter.SongAdapter;
import com.anhbv.appmp3.constant.KeyIntent;
import com.anhbv.appmp3.entities.ItemSongModel;
import com.anhbv.appmp3.ui.activities.PlayerActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;

public class TabAccountFrag extends Fragment{
    private static final String TAG = "TabAccountFrag";
    private View view;

    private List<ItemSongModel> itemSongModels = new ArrayList<>();
    private RecyclerView rcvSong;
    private MediaPlayer mediaPlayer;
    MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
    private byte[] imvAvatarSong;
    private String pathParent = Environment.getExternalStorageDirectory().toString()
            + File.separator + "Zing MP3";

    File fileSong = new File(pathParent);
    File[] listFileSong = fileSong.listFiles();
    List<File> listFile = Arrays.asList(fileSong.listFiles());

    private static final int REQUEST_CODE_EXAMPLE = 0x9345;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_tab_account, container, false);

        initView();
        loadDataSong();


        return view;
    }

    private void loadDataSong(){
        itemSongModels = loadSong();
        SongAdapter adapter = new SongAdapter(itemSongModels);
        rcvSong.setAdapter(adapter);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        rcvSong.setLayoutManager(manager);

        adapter.setiOnClickItemSong(new SongAdapter.IOnClickItemSong() {
            @Override
            public void OnClickItemSong(int position) {
                Intent intent = new Intent(getContext(),PlayerActivity.class);
                List url = loadUrlSong();
                String urlSong = (String) url.get(position);
                //intent.putCharSequenceArrayListExtra(KeyIntent.URLSONG, (ArrayList<CharSequence>) url);
                intent.putExtra(KeyIntent.IDSONG,position);
                intent.putExtra(KeyIntent.URLSONG,urlSong);
                startActivityForResult(intent,2);
            }
        });
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

    private List<ItemSongModel> loadSong() {
        List<ItemSongModel> pathSong = new ArrayList<>();
        Bitmap imvavatar;
        for (int i = 0; i < listFileSong.length; i++) {
            if (listFileSong[i].getName().contains(".mp3")) {
                mediaMetadataRetriever.setDataSource(pathParent + File.separator + listFileSong[i].getName());
                imvAvatarSong = mediaMetadataRetriever.getEmbeddedPicture();
                if (imvAvatarSong == null) {
                    Resources resources = getContext().getResources();
                    int image = R.drawable.ic_launcher;
                    imvavatar = BitmapFactory.decodeResource(resources, image);
                } else {
                    imvavatar = BitmapFactory.decodeByteArray(imvAvatarSong, 0, imvAvatarSong.length);
                }

                String nameSong = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                String artistSong = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                pathSong.add(new ItemSongModel(imvavatar, nameSong, artistSong));
            }
        }
        return pathSong;
    }

    private void initView() {
        rcvSong = view.findViewById(R.id.rcv_song_account);
        SongAdapter adapter = new SongAdapter(itemSongModels);
        rcvSong.setAdapter(adapter);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcvSong.setLayoutManager(manager);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_CANCELED) {
            if (requestCode == 2) {
                int index = data.getIntExtra("MESSAGE", 0);

                mediaMetadataRetriever.setDataSource(loadUrlSong().get(index));

                Log.d(TAG, "onActivityResultpppp: " + loadUrlSong().get(index));
            }
        }
    }
}
