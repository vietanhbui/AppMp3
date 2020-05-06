package com.anhbv.appmp3.entities;

import android.graphics.Bitmap;

public class ItemSongModel {
    private Bitmap imvAvatarSong;
    private String tvNameSong;
    private String tvArtistSong;

    public ItemSongModel(Bitmap imvAvatarSong, String tvNameSong, String tvArtistSong) {
        this.imvAvatarSong = imvAvatarSong;
        this.tvNameSong = tvNameSong;
        this.tvArtistSong = tvArtistSong;
    }

    public Bitmap getImvAvatarSong() {
        return imvAvatarSong;
    }

    public String getTvNameSong() {
        return tvNameSong;
    }

    public String getTvArtistSong() {
        return tvArtistSong;
    }

    public void setImvAvatarSong(Bitmap imvAvatarSong) {
        this.imvAvatarSong = imvAvatarSong;
    }

    public void setTvNameSong(String tvNameSong) {
        this.tvNameSong = tvNameSong;
    }

    public void setTvArtistSong(String tvArtistSong) {
        this.tvArtistSong = tvArtistSong;
    }
}
