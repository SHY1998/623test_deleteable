package com.example.a6_23test_deleteable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class OnlinePlayActivity extends Activity {
    String name;
    String artist;
    TextView online_TV_songName,online_TV_singer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.online_play_layout);
        Intent intent=getIntent();
        name=intent.getStringExtra("name");
        artist=intent.getStringExtra("artist");
        online_TV_singer=findViewById(R.id.Online_TV_singer);
        online_TV_songName=findViewById(R.id.Online_TV_songName);
        online_TV_songName.setText(name);
        online_TV_singer.setText(artist);

    }
}
