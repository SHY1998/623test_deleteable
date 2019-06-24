package com.example.a6_23test_deleteable.Service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.example.a6_23test_deleteable.Utils.AppConstantUtil;

import java.io.IOException;

public class NetPlayService extends Service implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener {
    MediaPlayer mediaPlayer;
    private String uri;
    String id;
    private int msg;
    private int percent;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0) {
                if(mediaPlayer != null) {
                    int currentTime = mediaPlayer.getCurrentPosition();
                    Intent intent = new Intent();
                    intent.setAction("MUSIC_CURRENT");
                    intent.putExtra("currentTime", currentTime);
                    sendBroadcast(intent);
                    handler.sendEmptyMessageDelayed(0, 1000);
                }
            }
            if(msg.what==1){
                int duration = mediaPlayer.getDuration();
                if (duration > 0) {
                    Intent intent = new Intent();
                    intent.setAction("MUSIC_DURATION");
                    int mCurrentPosition=0;
                    intent.putExtra("pos", mCurrentPosition);
                    Log.v("DUA", duration+"");
                    intent.putExtra("duration", duration);
                    sendBroadcast(intent);
                }
            }
            if(msg.what==2){
                Intent intent=new Intent();
                intent.setAction("MUSIC_PERCENT");
                intent.putExtra("percent", percent);
                sendBroadcast(intent);
            }
        };
    };
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        System.out.println("serviceCreate");
        super.onCreate();
        try {
            mediaPlayer=new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnPreparedListener(this);
        } catch (Exception  e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onStart(Intent intent,int flags)
    {
        System.out.println("serviceStart");
        if(intent==null)
        {
            stopSelf();
            System.out.println("停止运行");
        }
        id=intent.getStringExtra("id");
        msg=intent.getIntExtra("MSG",-1);
        System.out.println("id为："+id);
        System.out.println(msg);
        if (msg== AppConstantUtil.PlayerMsg.PLAY_MSG)
        {
            System.out.println("msg正常");
            play(id);
            System.out.println();
        }
        return;

    }
    private void play(String id)
    {
        try
        {
            System.out.println("play运行");
            mediaPlayer.reset();
            uri="https://v1.itooi.cn/netease/url?id="+id+"&quality=flac";
            mediaPlayer.setDataSource(uri);
            mediaPlayer.prepare();
//            mediaPlayer.start();
            mediaPlayer.setOnPreparedListener(this);
            handler.sendEmptyMessage(0);
            handler.sendEmptyMessage(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();
        Intent intent = new Intent();
//        intent.setAction(MUSIC_DURATION);
//        duration = mediaPlayer.getDuration();
//        intent.putExtra("duration", duration);
//        Log.v("duran", duration+"");
        sendBroadcast(intent);
    }


    @Override
    public void onBufferingUpdate(MediaPlayer mp, int perc) {
        percent=perc;
        handler.sendEmptyMessage(2);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.e("mediaPlayer", "onCompletion");
    }
}
