package uk.ac.rgu.music_tracker.data.databases.playlists;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

import uk.ac.rgu.music_tracker.data.classes.Playlist;

// Interface to access the database
@Dao
public interface PlaylistsDao {
    // Function to insert a playlist
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(Playlist playlist);

    // Function to remove a playlist from the database
    @Delete
    public void remove(Playlist playlist);

    // Function to update a playlist in the database
    @Update
    public void update(Playlist playlist);

    // Function to get a playlist using uid
    @Query("SELECT * FROM playlists WHERE uid LIKE :passedUid")
    public Playlist getPlaylistByUid(int passedUid);

    // Function to get a playlist using uid using Live Data
    @Query("SELECT * FROM playlists WHERE uid like :passedUid")
    public LiveData<Playlist> getPlaylistByUidLiveData(int passedUid);

    // Functions to get all the playlists
    @Query("SELECT * FROM playlists")
    public LiveData<List<Playlist>> getAllPlaylists();

    @Query("SELECT * FROM playlists")
    public List<Playlist> getAllPlaylistsNonLiveData();

    // Function to remove all the playlists from the database
    @Query("DELETE FROM playlists")
    public void removeAllPlaylists();
};
