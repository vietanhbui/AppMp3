package com.anhbv.appmp3.adapter;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anhbv.appmp3.R;
import com.anhbv.appmp3.entities.ItemSongModel;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {



    private List<ItemSongModel> itemSongModels;
    private IOnClickItemSong iOnClickItemSong;

    public SongAdapter(List<ItemSongModel> itemSongModels) {
        this.itemSongModels = itemSongModels;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_song_rcv,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.imvAvatarSong.setImageBitmap(itemSongModels.get(i).getImvAvatarSong());
        viewHolder.tvNameSong.setText(itemSongModels.get(i).getTvNameSong());
        viewHolder.tvArtistSong.setText(itemSongModels.get(i).getTvArtistSong());

        viewHolder.imvAvatarSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iOnClickItemSong.OnClickItemSong(i);
            }
        });

        viewHolder.llInfoSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iOnClickItemSong.OnClickItemSong(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemSongModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout llSong;
        private LinearLayout llInfoSong;
        private ImageView imvAvatarSong;
        private TextView tvNameSong;
        private TextView tvArtistSong;
        private ImageView imvPlayNext;
        private ImageView imvMore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            llSong = itemView.findViewById(R.id.ll_song);
            llInfoSong = itemView.findViewById(R.id.ll_info_song);
            imvAvatarSong = itemView.findViewById(R.id.imv_avatar_song);
            tvNameSong = itemView.findViewById(R.id.tv_name_song);
            tvArtistSong = itemView.findViewById(R.id.tv_artist_song);
            imvPlayNext = itemView.findViewById(R.id.imv_play_next_song);
            imvMore = itemView.findViewById(R.id.imv_more_song);

            tvNameSong.setTypeface(null,Typeface.BOLD);
            tvNameSong.setTextColor(Color.parseColor("#151515"));
            imvPlayNext.setColorFilter(Color.parseColor("#151515"));
            imvMore.setColorFilter(Color.parseColor("#151515"));
        }
    }

    public interface IOnClickItemSong{
        void OnClickItemSong(int position);
    }
    public void setiOnClickItemSong(IOnClickItemSong iOnClickItemSong){
        this.iOnClickItemSong = iOnClickItemSong;
    }
}
