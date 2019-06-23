package com.example.a6_23test_deleteable.Fragment;

import android.app.Activity;
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
import com.example.a6_23test_deleteable.R;

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
    public static final String CLOUD_MUSIC_API="https://v1.itooi.cn/netease/search?";
    private static final String TAG = "MainActivity";
    public static  String getResponse;
    public static JSONObject jo;
    public static List<Map<String, String>> searchResults = new ArrayList<Map<String, String>>();
    private static int screenWidth;
    private SearchView searchView;
    ListView musicList;
    Handler handler;
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
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(MusicFragment.this.getActivity(),"搜索内容为"+query,Toast.LENGTH_SHORT);
                musicList.setAdapter(null);
                searchResults.clear();
                getSong(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return rootView;
    }
    public void getSong(String search)
    {
        if (search.equals(""))
            return;
        Log.d("TAG","connecting");
        Toast.makeText(MusicFragment.this.getActivity(),"connectURL",Toast.LENGTH_SHORT).show();
        String s=null;
        if (search!=null)
        {
            try {
                s= URLEncoder.encode(search, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        final String url=CLOUD_MUSIC_API+"keyword="+s+"&type=song&pageSize=20&page=0";
        System.out.println(url);
        Log.d(TAG,"url="+url);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                getResponse = response;
                showList();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG,error.getMessage(),error);

                Toast.makeText(MusicFragment.this.getActivity(), "请检查网络连接", Toast.LENGTH_SHORT).show();
//                NetworkResponse response = error.networkResponse;
//                if (error instanceof ServerError && response != null) {
//                    try {
//                        String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
//                        JSONObject obj = new JSONObject(res);
//                    } catch (UnsupportedEncodingException e1) {
//                        e1.printStackTrace();
//                    } catch (JSONException e2) {
//                        e2.printStackTrace();
//                    }
//                }
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
                String artist=artists.getJSONObject(0).getString("name");
                String name=child.getString("name");
                System.out.println(name);
                Map<String, String> item = new HashMap<String, String>();
                item.put("name",name);
                item.put("artist",artist);
                searchResults.add(item);
                Log.d(TAG, "name = " + name + "---artists:" + artist);
            }
            handler.sendEmptyMessage(0);
            Toast.makeText(MusicFragment.this.getActivity(),"下载完成",Toast.LENGTH_SHORT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private BaseAdapter getAdapter()
    {
        BaseAdapter adapter=new BaseAdapter() {
            @Override
            public int getCount() {
                return searchResults.size();
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
                Map<String, String> map = searchResults.get(position);
                LinearLayout line=new LinearLayout(MusicFragment.this.getActivity());
                line.setOrientation(LinearLayout.HORIZONTAL);
                line.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100));
                TextView songTitle=new TextView(MusicFragment.this.getActivity());
                String musicname=map.get("name");
            //    songTitle.setWidth(MainActivity.screenWidth/2);
                songTitle.setHeight(100);
                songTitle.setMaxLines(1);
                songTitle.setTextColor(Color.MAGENTA);
                songTitle.setTextSize(18);
                songTitle.setText(musicname);
                songTitle.setGravity(TextView.TEXT_ALIGNMENT_CENTER);
                songTitle.setGravity(Gravity.CENTER_VERTICAL);

                TextView artist=new TextView(MusicFragment.this.getActivity());
                artist.setGravity(TextView.TEXT_ALIGNMENT_CENTER);
              //  artist.setWidth(MainActivity.screenWidth/3);
                artist.setHeight(100);
                artist.setTextColor(Color.MAGENTA);
                artist.setText(map.get("artist"));
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
    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

}
