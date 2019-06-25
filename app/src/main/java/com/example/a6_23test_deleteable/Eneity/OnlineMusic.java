package com.example.a6_23test_deleteable.Eneity;

import java.io.Serializable;

public class OnlineMusic implements Serializable {
    private String music_id;
    private String music_cover_url;
    private String music_url;
    private String music_singer;
    private String music_length;
    private String music_name;

    public String getMusic_id() {
        return music_id;
    }

    public void setMusic_id(String music_id) {
        this.music_id = music_id;
    }

    public String getMusic_cover_url() {
        return music_cover_url;
    }

    public void setMusic_cover_url(String music_cover_url) {
        this.music_cover_url = music_cover_url;
    }

    public String getMusic_url() {
        return music_url;
    }

    public void setMusic_url(String music_url) {
        this.music_url = music_url;
    }

    public String getMusic_singer() {
        return music_singer;
    }

    public void setMusic_singer(String music_singer) {
        this.music_singer = music_singer;
    }

    public String getMusic_length() {
        return music_length;
    }

    public void setMusic_length(String music_length) {
        this.music_length = music_length;
    }

    public String getMusic_name() {
        return music_name;
    }

    public void setMusic_name(String music_name) {
        this.music_name = music_name;
    }
}
