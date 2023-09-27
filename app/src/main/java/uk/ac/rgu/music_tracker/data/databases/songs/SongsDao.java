package uk.ac.rgu.music_tracker.data.databases.songs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import uk.ac.rgu.music_tracker.data.classes.Song;

// Interface to access the database
@Dao
public interface SongsDao {
    // Function to insert a song to the database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(Song song);

    // Function to remove a song from the database
    @Delete
    public void remove(Song song);

    // Function to get a song using a songId
    @Query("SELECT * FROM songs WHERE songId LIKE :passedSongId")
    public Song findBySongId(String passedSongId);

    // Function to get a song using a song uid
    @Query("SELECT * FROM songs WHERE uid LIKE :passedUid")
    public Song findBySongUid(int passedUid);

    // Functions to return all the songs
    @Query("SELECT * FROM songs")
    public LiveData<List<Song>> getAllSongs();

    @Query("SELECT * FROM songs")
    public List<Song> getAllSongsNonLiveData();

    // Function to remove all the songs from the database
    @Query("DELETE FROM songs")
    public void removeAllSongs();
};
