package com.example4job1.musicplayer.activity;

public class Song {

    private String songName;
    private String singer;
    public String songPath;

    public Song(String songName,String singer,String songPath){
        this.singer=singer;
        this.songName=songName;
        this.songPath=songPath;
    }

    public String getSongName(){return songName;}
    public String getSinger(){return singer;}
    public String getSongPath(){return songPath;}
}
