package com.example4job1.musicplayer.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example4job1.R;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private IntentFilter intentFilter;
    private HeadsetPlugReceiver headsetPlugReceiver;
    private List<Song> songArray = new ArrayList<Song>();
    public String Songpath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.HEADSET_PLUG");
        headsetPlugReceiver = new HeadsetPlugReceiver();
        registerReceiver(headsetPlugReceiver, intentFilter);
        TextView welcomeTxt=(TextView)findViewById(R.id.txt_welcome);
        Button backBtn=(Button)findViewById(R.id.btn_back);
        String uname=this.getIntent().getStringExtra("uname");
        welcomeTxt.setText("欢迎您，"+uname+"!");

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ListActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE }, 1);
        } else {
            initSongs();
        }

        ListView songList=(ListView)findViewById(R.id.list_song);
        SongAdapter myAdapter=new SongAdapter(ListActivity.this,R.layout.song_item,songArray);
        songList.setAdapter(myAdapter);
        songList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Song song = songArray.get(position);
                Songpath = song.getSongPath();
                Toast.makeText(ListActivity.this, Songpath, Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(ListActivity.this, PlayActivity.class);
                intent.putExtra("position",Songpath);
                startActivity(intent);
            }
        });

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initSongs();
                } else {
                    Toast.makeText(this, "未授权，功能无法实现", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }


    public void initSongs() {

        Cursor cursor = null;
        cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                String disName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                Song song = new Song(disName, artist, url);
                songArray.add(song);
            }
        }
    }


    class HeadsetPlugReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equalsIgnoreCase("android.intent.action.HEADSET_PLUG")){
                if(intent.hasExtra("state")){
                    if (intent.getIntExtra("state", 0) == 1){
                        Toast.makeText(context, "耳机已连接", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context, "耳机已断开", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(headsetPlugReceiver);
    }

}
