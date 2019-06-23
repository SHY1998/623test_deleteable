package com.example.a6_23test_deleteable.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.a6_23test_deleteable.Adapter.MyAdapter;
import com.example.a6_23test_deleteable.Eneity.Music;
import com.example.a6_23test_deleteable.MainActivity;
import com.example.a6_23test_deleteable.PlayActivity;
import com.example.a6_23test_deleteable.R;
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
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.select,container,false);
       // musicList=rootView.findViewById(R.id.main_listview);
        mListView=rootView.findViewById(R.id.main_listview);
        if (mListView.equals(""))
        {
            System.out.println("空空公开");
        }
    //    musicList.setAdapter(getAdapter());
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
      //  musicList.setAdapter(getAdapter());
      //    adapter=new MyAdapter(MovieFragment.this,list);
      //  mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // musicplay(position);
                Intent intent =new Intent(MovieFragment.this.getContext(), PlayActivity.class);
                intent.putExtra("position",position);
                startActivity(intent);
                Intent intent1=new Intent(CTL_ACTION);
                intent1.putExtra("position",position);
                getActivity().sendBroadcast(intent);
            }
        });

    }
//    private BaseAdapter getAdapter()
//    {
//        BaseAdapter adapter=new BaseAdapter() {
//            @Override
//            public int getCount() {
//                return list.size();
//            }
//
//            @Override
//            public Object getItem(int position) {
//                return list.get(position);
//            }
//
//            @Override
//            public long getItemId(int position) {
//                return position;
//            }
//
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//         //       Map<String,String> map= (Map<String, String>) list.get(position);
//                LinearLayout line =new LinearLayout(MovieFragment.this.getActivity());
//                line.setOrientation(LinearLayout.HORIZONTAL);
//                line.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100));
//                TextView songname =new TextView(MovieFragment.this.getActivity());
//                TextView singers =new TextView(MovieFragment.this.getActivity());
//                String name=list.get(position).getSong();
//                String singer=list.get(position).getSinger();
//                songname.setHeight(100);
//                songname.setMaxLines(1);
//                songname.setTextColor(Color.MAGENTA);
//                songname.setTextSize(18);
//                songname.setText(name);
//                songname.setGravity(TextView.TEXT_ALIGNMENT_CENTER);
//                songname.setGravity(Gravity.CENTER_VERTICAL);
//                singers.setGravity(TextView.TEXT_ALIGNMENT_CENTER);
//                //  artist.setWidth(MainActivity.screenWidth/3);
//                singers.setHeight(100);
//                singers.setTextColor(Color.MAGENTA);
//                singers.setText(singer);
//                singers.setTextSize(15);
//                singers.setMaxLines(1);
//                singers.setPadding(50, 0, 0, 10);
//                singers.setGravity(Gravity.RIGHT);
//                line.addView(songname);
//                line.addView(singers);
//                return line;
//            }
//        };
//        return adapter;
//    }
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                System.out.println("点击歌曲");
//                // musicplay(position);
//                Intent intent =new Intent(MovieFragment.this.getContext(), PlayActivity.class);
//                intent.putExtra("position",position);
//                startActivity(intent);
//                Intent intent1=new Intent(CTL_ACTION);
//                intent1.putExtra("position",position);
//                getActivity().sendBroadcast(intent);
//            }
    private void initAdapter()
    {
        adapter=new MyAdapter(getActivity(), list);
        mListView.setAdapter(adapter);
    }

}
