package com.example.a6_23test_deleteable.Service;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import com.example.a6_23test_deleteable.Utils.AppConstantUtil;


public class NetPlayService extends Service implements MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener {
    MediaPlayer mediaPlayer;
    private String uri;
    int id;
    private boolean isPlaying;
    private boolean isPause;
    private int msg;
    private int percent;
    private MyReceiver myReceiver;
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
        System.out.println("serviece建立receiver之前");
        myReceiver=new MyReceiver();
        IntentFilter filter= new IntentFilter();
        filter.addAction("MF_PLAY_NEXT");
        filter.addAction("ONLINE_ACTIVITY_NEXT");
        registerReceiver(myReceiver, filter);
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
        else
        {
//            msg=intent.getIntExtra("MSG",0);
            msg=intent.getIntExtra("MSG",-1);
            System.out.println("MSG="+msg);
            id=intent.getIntExtra("id",-1);
        }
        if (msg== AppConstantUtil.PlayerMsg.PLAY_MSG)
        {
            System.out.println("msg正常");
            play(id);
            System.out.println();
        }
        else if (msg== AppConstantUtil.PlayerMsg.PAUSE_MSG)
        {
            pause();
        }
        else if(msg== AppConstantUtil.PlayerMsg.CONTINUE_MSG)
        {
            resume();
        }
        else if(msg==AppConstantUtil.PlayerMsg.NEXT_MSG);
        {
            System.out.println("======="+id);
//      int id= Integer.parseInt(intent.getStringExtra("id"));
            System.out.println("接收id"+id);
           play(id);
        }
        super.onStart(intent,flags);

    }

    public class MyReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("MF_PLAY_NEXT"))
            {
                int id= Integer.parseInt(intent.getStringExtra("music_id"));
                System.out.println("service获取的id"+id);
            //    play(id);
            }
        }
    }
    private void play(int id)
    {
        try
        {
            System.out.println("play运行");
            mediaPlayer.reset();
            System.out.println(id);
            uri="https://v1.itooi.cn/netease/url?id="+id+"&quality=flac";
            mediaPlayer.setDataSource(uri);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(this);
            handler.sendEmptyMessage(0);
            handler.sendEmptyMessage(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pause()
    {
        if (mediaPlayer != null && mediaPlayer.isPlaying())
        {
            mediaPlayer.pause();
            isPause=true;
        }
    }
    private void resume()
    {
        if(isPause)
        {
            mediaPlayer.start();
            isPause=false;
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
//        Intent intent = new Intent();
//        sendBroadcast(intent);
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
