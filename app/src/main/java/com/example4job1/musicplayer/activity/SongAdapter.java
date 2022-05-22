package com.example4job1.musicplayer.activity;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example4job1.R;

import java.util.List;

public class SongAdapter extends ArrayAdapter<Song> {
    private int resourceId;

    public SongAdapter(Context context, int textViewResourceId,
                        List<Song> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Song song = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.coverImg = (ImageView) view.findViewById (R.id.img_cover);
            viewHolder.songNameTxt = (TextView) view.findViewById (R.id.txt_song_name);
            viewHolder.singerTxt = (TextView) view.findViewById (R.id.txt_singer);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.songNameTxt.setText(song.getSongName());
        viewHolder.singerTxt.setText(song.getSinger());
        viewHolder.songPath=song.getSongPath();
        return view;
    }

    class ViewHolder {
        ImageView coverImg;
        TextView songNameTxt;
        TextView singerTxt;
        String songPath;
    }

}

