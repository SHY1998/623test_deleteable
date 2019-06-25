package com.example.a6_23test_deleteable.Fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.a6_23test_deleteable.Eneity.OnlineMusic;
import com.example.a6_23test_deleteable.OnlinePlayActivity;
import com.example.a6_23test_deleteable.PlayActivity;
import com.example.a6_23test_deleteable.R;
import com.example.a6_23test_deleteable.Service.PlayService;
import com.example.xlistviewapi22.XListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MusicFragment extends Fragment {
    public static final String CTL_ACTION="com.example.action.CTL_ACTION";
    public static final String CLOUD_MUSIC_API="https://v1.itooi.cn/netease/search?";
    public static final String CLOUD_MUSIC_PLAY="https://v1.itooi.cn/netease/url?";
    private static final String TAG = "MainActivity";
    public static  String getResponse;
    public static JSONObject jo;
    public static int npositon=-1;
    String artist;
    String name;
    String music_id;
    List<OnlineMusic> list=new ArrayList<>();
    public static List<Map<String, String>> searchResults = new ArrayList<Map<String, String>>();
    private static int screenWidth;
    private SearchView searchView;
    private OLPActivityReceiver olpActivityReceiver;
    XListView musicList;
    public static String searchInfo;
    Handler handler;
    int page=0;
    RequestQueue mQueue = null;
    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState)
    {
        View rootView=inflater.inflate(R.layout.fragment_getmusic,container,false);
        searchView=rootView.findViewById(R.id.searchView);
        musicList=rootView.findViewById(R.id.netmusiclist);
        musicList.setPullLoadEnable(true);
        musicList.setPullRefreshEnable(true);
        olpActivityReceiver=new OLPActivityReceiver();
        IntentFilter filter= new IntentFilter();
        filter.addAction("ONLINE_NEXT");
        filter.addAction("ONLINE_FRONT");
        getActivity().getApplicationContext().registerReceiver(olpActivityReceiver, filter);


        mQueue= Volley.newRequestQueue(MusicFragment.this.getActivity());
        handler=new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                if(msg.what==0)
                {
                    musicList.setAdapter(getAdapter());

                } else if (msg.what==1)
                {
                    Toast.makeText(MusicFragment.this.getActivity(),"完成",Toast.LENGTH_SHORT);
                }
            }
        };
        musicList.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                page++;
                getSong(searchInfo,page);
            }
            @Override
            public void onLoadMore() {
                page++;
                getSong(searchInfo,page);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(MusicFragment.this.getActivity(),"搜索内容为"+query,Toast.LENGTH_SHORT);
                searchInfo=query;
                page=0;
                musicList.setAdapter(null);
                list.clear();
                getSong(query,page);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return rootView;
    }
    public void getSong(String search,int page)
    {
        if (search.equals(""))
            return;
        Log.d("TAG","connecting");
        String s=null;
        if (search!=null)
        {
            try {
                s= URLEncoder.encode(search, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        final String url=CLOUD_MUSIC_API+"keyword="+s+"&type=song&pageSize=20&"+"page="+page;
        Log.d(TAG,"url="+url);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                getResponse = response;
                showList();
                onLoad();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG,error.getMessage(),error);
                Toast.makeText(MusicFragment.this.getActivity(), "请检查网络连接", Toast.LENGTH_SHORT).show();
            }
        });
        mQueue.add(stringRequest);
    }
    public void showList()
    {
        Log.d(TAG,"showList");
        try
        {
            jo=new JSONObject(getResponse);
            String result=jo.getString("data");
            String songs=new JSONObject(result).getString("songs");
            JSONArray js=new JSONObject(result).getJSONArray("songs");
            for(int i=0;i<js.length();i++)
            {
                JSONObject child=js.getJSONObject(i);
                JSONArray artists=child.getJSONArray("ar");
                JSONObject musicId=child.getJSONObject("privilege");
                artist=artists.getJSONObject(0).getString("name");
                name=child.getString("name");
                music_id=musicId.getString("id");
                OnlineMusic onlineMusic=new OnlineMusic();
                onlineMusic.setMusic_name(name);
                onlineMusic.setMusic_singer(artist);
                onlineMusic.setMusic_id(music_id);
                list.add(onlineMusic);
                Log.d(TAG, "name = " + name + "---artists:" + artist);
            }
            handler.sendEmptyMessage(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        musicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                npositon=position-1;
                OnlineMusic result=list.get(position-1);
                String songname,playername;
                songname=result.getMusic_name();
                playername=result.getMusic_singer();
                music_id=result.getMusic_id();
                Intent stop_loc=new Intent();
                stop_loc.setClass(MusicFragment.this.getActivity(),PlayService.class);
                stop_loc.setAction("com.lzw.media.MUSIC_SERVICE");
                getActivity().stopService(stop_loc);
                Intent start_online=new Intent(MusicFragment.this.getActivity(), OnlinePlayActivity.class);
                start_online.putExtra("name",songname);
                start_online.putExtra("artist",playername);
                start_online.putExtra("music_id",music_id);
//                start_online.putExtra("netmusicitem",list.get(position-1));
                getActivity().startActivity(start_online);
            }
        });
    }

    private BaseAdapter getAdapter()
    {
        BaseAdapter adapter=new BaseAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
//                Map<String, String> map = searchResults.get(position);
                LinearLayout line=new LinearLayout(MusicFragment.this.getActivity());
                line.setOrientation(LinearLayout.HORIZONTAL);
                line.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100));
                TextView songTitle=new TextView(MusicFragment.this.getActivity());
                String musicname=list.get(position).getMusic_name();

                songTitle.setHeight(100);
                songTitle.setMaxLines(1);
                songTitle.setTextColor(Color.MAGENTA);
                songTitle.setTextSize(18);
                songTitle.setText(musicname);
                songTitle.setGravity(TextView.TEXT_ALIGNMENT_CENTER);
                songTitle.setGravity(Gravity.CENTER_VERTICAL);
                TextView artist=new TextView(MusicFragment.this.getActivity());
                artist.setGravity(TextView.TEXT_ALIGNMENT_CENTER);
                artist.setHeight(100);
                artist.setTextColor(Color.MAGENTA);
                artist.setText(list.get(position).getMusic_singer());
                artist.setTextSize(15);
                artist.setMaxLines(1);
                artist.setPadding(50, 0, 0, 10);
                artist.setGravity(Gravity.RIGHT);
                line.addView(songTitle);
                line.addView(artist);
                return line;
            }
        };
        return adapter;
    }
    private void onLoad()
    {
        musicList.stopRefresh();
        musicList.stopLoadMore();
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
    public class OLPActivityReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            System.out.println("action为"+action);
            if (action.equals("ONLINE_NEXT"))
            {
                npositon++;
                System.out.println("点击next");
                String songname,playername,id;
                playername=list.get(npositon).getMusic_singer();
                songname=list.get(npositon).getMusic_name();
                id=list.get(npositon).getMusic_id();
                System.out.println("当前"+list.get(npositon+1).getMusic_id());
                System.out.println("下一个"+list.get(npositon+2).getMusic_id());
                System.out.println("F获得id"+id);

                Intent intent1=new Intent();
                intent1.setAction("MF_PLAY_NEXT");
                intent1.putExtra("name",songname);
                intent1.putExtra("artist",playername);
                intent1.putExtra("music_id",id);
                getActivity().sendBroadcast(intent1);
                System.out.println("Fagment发送信息");
            }
            else if (action.equals("ONLINE_FRONT"))
            {
                if (npositon--<0)
                {
                    Toast.makeText(getActivity(),"这已经是第一首了",Toast.LENGTH_SHORT);
                }
                else {
                    npositon--;
                    System.out.println("点击next");
                    String songname, playername, id;
                    playername = list.get(npositon).getMusic_singer();
                    songname = list.get(npositon).getMusic_name();
                    id = list.get(npositon).getMusic_id();
                    System.out.println("当前" + list.get(npositon + 1).getMusic_id());
                    System.out.println("下一个" + list.get(npositon + 2).getMusic_id());
                    System.out.println("F获得id" + id);

                    Intent intent1 = new Intent();
                    intent1.setAction("MF_PLAY_NEXT");
                    intent1.putExtra("name", songname);
                    intent1.putExtra("artist", playername);
                    intent1.putExtra("music_id", id);
                    getActivity().sendBroadcast(intent1);
                    System.out.println("Fagment发送信息");
                }
            }
        }
    }
}
