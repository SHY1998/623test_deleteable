package com.example.a6_23test_deleteable;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.a6_23test_deleteable.Service.NetPlayService;
import com.example.a6_23test_deleteable.Service.PlayService;
import com.example.a6_23test_deleteable.Utils.AppConstantUtil;

public class OnlinePlayActivity extends Activity implements View.OnClickListener {
    String name;
    String artist;
    private static int nId=-1;
    private boolean isPlaying;
    private boolean isPasue;
    private boolean flag;
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
        int  id= Integer.parseInt(intent.getStringExtra("music_id"));
        String stringId=intent.getStringExtra("music_id");
        System.out.println("OPA"+id);
        if (id!=nId)
        {
            nId=id;
            flag=true;
            isPasue=false;
            isPlaying=true;
        }
        else
        {
            flag=false;
            isPlaying=false;
            isPasue=true;
        }
        online_TV_singer=findViewById(R.id.Online_TV_singer);
        online_TV_songName=findViewById(R.id.Online_TV_songName);

        front=findViewById(R.id.Online_IB_front);
        pause=findViewById(R.id.Online_IB_pause);
        next=findViewById(R.id.Online_IB_next);
        front.setOnClickListener(this);
        pause.setOnClickListener(this);
        next.setOnClickListener(this);

        name=intent.getStringExtra("name");
        artist=intent.getStringExtra("artist");

        online_TV_songName.setText(name);
        online_TV_singer.setText(artist);

        if(flag)
        {
            play();
            isPlaying=true;
            isPasue=false;
        }
        else
        {
            Intent intent1=new Intent(this, NetPlayService.class);
            intent1.putExtra("MSG", AppConstantUtil.PlayerMsg.PLAY_MSG);
            System.out.println(id);
            intent1.putExtra("id",stringId);
            startService(intent1);
            System.out.println("启动intent1");
            isPlaying=true;
            isPlaying=false;
        }



    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.Online_IB_pause:
                if (isPlaying)
                {
                    pause();
                }
                else if(isPasue)
                {
                    resume();
                }
                else
                {
                    play();
                }
                break;
        }

    }

    private class PlayerReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent) {
        }
    }
    private void pause()
    {
        Intent intent=new Intent();
        intent.setClass(OnlinePlayActivity.this,NetPlayService.class);
        intent.setAction("com.example.action.MUSIC_SERVICE");
        intent.putExtra("MSG", AppConstantUtil.PlayerMsg.PAUSE_MSG);
        startService(intent);
        isPlaying=false;
        isPasue=true;
    }
    private void resume()
    {
        Intent intent=new Intent();
        intent.setAction("com.example.action.MUSIC_SERVICE");
        intent.setClass(OnlinePlayActivity.this,NetPlayService.class);
        intent.putExtra("MSG",AppConstantUtil.PlayerMsg.CONTINUE_MSG);
        startService(intent);
        isPlaying=true;
        isPasue=false;
    }
    private void play()
    {
        Intent intent=new Intent();
        intent.setAction("com.example.action.MUSIC_SERVICE");
        intent.setClass(this,NetPlayService.class);
//        intent.putExtra("path",musicEntity.getPath());
//        intent.putExtra("position",mCurrentPosition);
        intent.putExtra("id",nId);
        intent.putExtra("MSG",AppConstantUtil.PlayerMsg.PLAY_MSG);
        startService(intent);
        isPlaying=true;
        isPasue=false;
    }
    @Override
    public void onDestroy()
    {
        //unregisterReceiver(pactivityReceiver);
        super.onDestroy();
    }
}
