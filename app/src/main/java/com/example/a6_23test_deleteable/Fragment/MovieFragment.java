package com.example.a6_23test_deleteable.Fragment;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.a6_23test_deleteable.Adapter.MyAdapter;
import com.example.a6_23test_deleteable.Eneity.Music;
import com.example.a6_23test_deleteable.PlayActivity;
import com.example.a6_23test_deleteable.R;
import com.example.a6_23test_deleteable.Service.NetPlayService;
import com.example.a6_23test_deleteable.Service.PlayService;
import com.example.a6_23test_deleteable.Utils.MusicUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MovieFragment  extends Fragment  {
    private ListView mListView;
    private List<Music> list;
    MyAdapter adapter;
    private MediaPlayer mplayer=new MediaPlayer();
    private int currentposition;
    private View rootView;
    public static List<Map<String, String>> searchResults = new ArrayList<Map<String, String>>();
    public static final String CTL_ACTION="com.example.action.CTL_ACTION";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.select,container,false);
        mListView=rootView.findViewById(R.id.main_listview);
        if (mListView.equals(""))
        {
            System.out.println("空空公开");
        }
        setListView();
        initAdapter();
        Intent intent=new Intent(MovieFragment.this.getActivity(), PlayService.class);
        getActivity().startService(intent);
        return rootView;
    }
    public void setListView()
    {
        list=new ArrayList<Music>();
        list= MusicUtils.getMusicData(MovieFragment.this.getActivity());
        if(list.equals(""))
        {
            System.out.println("为空");
        }
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent stop_intent=new Intent();
                stop_intent.setClass(MovieFragment.this.getActivity(), NetPlayService.class);
                stop_intent.setAction("NEW_MUSIC_SERVICE");
                getActivity().stopService(stop_intent);
                Intent intent =new Intent(MovieFragment.this.getContext(), PlayActivity.class);
                intent.putExtra("position",position);
                startActivity(intent);
                Intent intent1=new Intent(CTL_ACTION);
                intent1.putExtra("position",position);
                getActivity().sendBroadcast(intent);
            }
        });

    }
//
    private void initAdapter()
    {
        adapter=new MyAdapter(getActivity(), list);
        mListView.setAdapter(adapter);
    }

}
