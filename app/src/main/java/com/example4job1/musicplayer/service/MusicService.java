package com.example4job1.musicplayer.service;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.provider.MediaStore;

/*下列代码完成了Service各种状态下所进行的操作，目的是在Service运行时保证MediaPlayer的存在，
而在service被停止时使MediaPlayer销毁。Service在后台持续运行
**/
public class MusicService extends Service {

    private MediaPlayer mediaPlayer = new MediaPlayer();
   private MusicBinder mBinder = new MusicBinder(); //获取MusicBinder的实例
    /*创建一个MusicBinder类，让它继承自Binder，然后在它的内部提供了initMediaPlayer方法**/
    public class MusicBinder extends Binder {

        public void initMediaPlayer(){
            Cursor cursor = null;
            try{
                cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        null, null, null, null);
                if (cursor!=null) {
                    while(cursor.moveToNext()) {

                        mediaPlayer.setDataSource(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                        mediaPlayer.prepare();

                    }
                }

            } catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    //在onBinder方法中返回这个实例
    public IBinder onBind(Intent intent){
        return mBinder;
   }

    @Override
    public void onCreate()
    {
        super.onCreate();
        mBinder.initMediaPlayer();

    }


    public int onStartCommand(Intent intent, int flags, int id)
    {
        //mediaPlayer.reset();
        if (!mediaPlayer.isPlaying()){
            mediaPlayer.start();
        }
        return super.onStartCommand(intent, flags, id);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

    }

}
