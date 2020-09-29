package com.out.activitymusic;


import java.io.Serializable;

public class Song implements Serializable {

    private int ID;
    private String Title;
    private String File;
    private String Artist;
    private String Duration;
    private String Album;



    public Song(int ID, String title, String file, String album, String artist, String duration) {
        this.ID = ID;
        Title = title;
        File = file;
        Artist = artist;
        Duration = duration;
        Album=album;
    }
    public Song(int index, String title, String file, String album, String artist, int duration){}

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getFile() {
        return File;
    }

    public void setFile(String file) {
        File = file;
    }

    public String getArtist() {
        return Artist;
    }

    public void setArtist(String artist) {
        Artist = artist;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public String getAlbum() {
        return Album;
    }

    public void setAlbum(String album) {
        Album = album;
    }
}



