package uk.ac.rgu.music_tracker.data.classes;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.ArrayList;

import uk.ac.rgu.music_tracker.data.databases.converters.Converters;

@Entity(tableName = "playlists")
@TypeConverters(Converters.class)
public class Playlist {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int uid;

    // Attributes
    private String name;
    private ArrayList<Integer> songs;

    // Constructor
    public Playlist(String name) {
        this.name = name;
        this.songs = new ArrayList<>();
    };

    // Getters and setters
    public int getUid() { return uid; };

    public void setUid(int uid) { this.uid = uid; };

    public String getName() { return name; };

    public void setName(String name) { this.name = name; };

    public ArrayList<Integer> getSongs() { return songs; };

    public void setSongs(ArrayList<Integer> songs) { this.songs = songs; };

    // Functions to add and remove songs from the songs list
    public void addSong(Integer songuid) { songs.add(songuid); };

    public void removeSong(Integer songuid) { songs.remove(songuid); };

    // To string method
    public String toString() {
        // Creating a string builder
        StringBuilder output = new StringBuilder("");

        // Checking if the input list is empty
        if (songs.isEmpty()) {
            return output.toString();
        };

        // Add each list item to the output string
        for (Integer current : songs) {
            output.append(current.toString()).append(",");
        };

        // Removing the last comma
        output.deleteCharAt(output.lastIndexOf(","));

        return output.toString();
    };
};
