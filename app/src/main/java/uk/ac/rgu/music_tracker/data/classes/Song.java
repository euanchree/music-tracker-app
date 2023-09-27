package uk.ac.rgu.music_tracker.data.classes;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.PrimaryKey;
import androidx.room.Query;

import java.util.List;

@Entity(tableName = "songs")
public class Song {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int uid;

    // Attributes
    private String songId;
    private String albumId;
    private String name;
    private String album;
    private String artistName;
    private String songUrl;
    private String lyrics;
    private String thumbnailUrl;

    // Constructor
    public Song(String songId, String albumId, String name, String album, String artistName, String songUrl, String lyrics, String thumbnailUrl) {
        this.songId = songId;
        this.albumId = albumId;
        this.name = name;
        this.album = album;
        this.artistName = artistName;
        this.songUrl = songUrl;
        this.lyrics = lyrics;
        this.thumbnailUrl = thumbnailUrl;
    };

    // Getters and setters
    public int getUid() { return uid; };

    public void setUid(int uid) { this.uid = uid; };

    public String getAlbumId() { return this.albumId; }

    public void setAlbumId(String albumId) { this.albumId = albumId; };

    public String getSongId() { return songId; };

    public void setSongId(String songId) { this.songId = songId; };

    public String getName() {
        return name;
    };

    public void setName(String name) {
        this.name = name;
    };

    public String getAlbum() {
        return album;
    };

    public void setAlbum(String album) {
        this.album = album;
    };

    public String getArtistName() {
        return artistName;
    };

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    };

    public String getSongUrl() {
        return songUrl;
    };

    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl;
    };

    public String getLyrics() {
        return lyrics;
    };

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    };

    public String getThumbnailUrl() {
        return thumbnailUrl;
    };

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    };
};