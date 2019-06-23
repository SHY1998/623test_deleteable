package com.example.a6_23test_deleteable.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.a6_23test_deleteable.Eneity.Music;
import com.example.a6_23test_deleteable.MainActivity;
import com.example.a6_23test_deleteable.R;
import com.example.a6_23test_deleteable.Utils.MusicUtils;


import java.util.List;



public class MyAdapter extends BaseAdapter {
    private Context mcontext;
    private List<Music> mlist;
    private int position_flag=0;
    public  MyAdapter(Context context, List<Music> list)
    {
        mlist=list;
        mcontext=context;
    }
    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    ViewHolder  holder;
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
          holder=new ViewHolder();
        if(view==null)
        {
            holder=new ViewHolder();
            view= LayoutInflater.from(mcontext).inflate(R.layout.selectitem,null);
            holder.song=(TextView) view.findViewById(R.id.item_music_song);
            holder.singer=(TextView) view.findViewById(R.id.it_music_singer);
            holder.songLength=(TextView) view.findViewById(R.id.item_music_songLength);
            holder.position=(TextView) view.findViewById(R.id.item_music_position);
            view.setTag(holder);
        }
        else
        {
            holder= (ViewHolder) view.getTag();
        }
        String string_song =mlist.get(position).getSong();
        if (string_song.length() >= 5
                && string_song.substring(string_song.length() - 4,
                string_song.length()).equals(".mp3")) {
            holder.song.setText(string_song.substring(0,
                    string_song.length() - 4).trim());
        } else {
            holder.song.setText(string_song.trim());
        }
        holder.singer.setText(mlist.get(position).getSinger().toString().trim());

//        holder.song.setText(list.get(position).song.toString());
//        holder.singer.setText(list.get(position).singer.toString());
        int songLength = mlist.get(position_flag).getSonLength();
        String time= MusicUtils.formaTime(songLength);
        holder.songLength.setText(time);
        holder.position.setText(position+1+"");
        return view;
    }

    class ViewHolder
    {
        TextView song;
        TextView singer;
        TextView songLength;
        TextView position;
    }
}
