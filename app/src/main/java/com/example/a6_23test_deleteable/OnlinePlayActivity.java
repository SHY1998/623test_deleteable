package com.example.a6_23test_deleteable;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.a6_23test_deleteable.Service.NetPlayService;
import com.example.a6_23test_deleteable.Service.PlayService;
import com.example.a6_23test_deleteable.Utils.AppConstantUtil;

public class OnlinePlayActivity extends Activity {
    String name;
    String artist;
    String id;
    TextView online_TV_songName,online_TV_singer;
    private ImageButton front,pause,next;
    public static final String UPDATE_ACTION = "action.ONLINE_UPDATE_ACTION";
    public static final String CTL_ACTION = "action.ONLINE_CTL_ACTION";
    public static final String MUSIC_CURRENT = "action.ONLINE_MUSIC_CURRENT";
    public static final String MUSIC_DURATION = "action.ONLINE_MUSIC_DURATION";
    public static final String MUSIC_PERCENT="action.ONLINE_MUSIC_PERCENT";
    private PlayerReceiver playerReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.online_play_layout);
        Intent intent=getIntent();
        id=intent.getStringExtra("music_id");
        online_TV_singer=findViewById(R.id.Online_TV_singer);
        online_TV_songName=findViewById(R.id.Online_TV_songName);
        front=findViewById(R.id.Online_IB_front);
        pause=findViewById(R.id.Online_IB_pause);
        next=findViewById(R.id.Online_IB_next);
        name=intent.getStringExtra("name");
        artist=intent.getStringExtra("artist");

        online_TV_songName.setText(name);
        online_TV_singer.setText(artist);

        Intent intent1=new Intent(this, NetPlayService.class);
        intent1.putExtra("MSG", AppConstantUtil.PlayerMsg.PLAY_MSG);
        System.out.println(id);
        intent1.putExtra("id",id);
        startService(intent1);
        System.out.println("启动intent1");

    }
    private class PlayerReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent) {
        }
    }
    @Override
    public void onDestroy()
    {
        //unregisterReceiver(pactivityReceiver);
        super.onDestroy();
    }
}
