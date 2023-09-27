package uk.ac.rgu.music_tracker.data.databases.songs;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executors;

import uk.ac.rgu.music_tracker.data.classes.Song;

public class SongRepository {
    private SongsDao songsDao;
    private static SongRepository INSTANCE;
    private Context context;

    private SongRepository(Context context) {
        songsDao = SongDatabase.getDatabase(context).songsDao();
    };

    public static SongRepository getRepository(Context context) {
        if (INSTANCE == null) {
            synchronized (SongRepository.class) {
                INSTANCE = new SongRepository(context);
            };
        };

        return INSTANCE;
    };

    // Function to get all the songs
    public LiveData<List<Song>> getAllSongs() {
        return songsDao.getAllSongs();
    };

    // Function to get all the songs in the database with non live data
    public List<Song> getAllSongsNonLiveData() { return this.songsDao.getAllSongsNonLiveData(); };

    // Function to get a song from a song id
    public Song getSongById(String passedSongId) { return songsDao.findBySongId(passedSongId); };

    // Function to get a song from a song uid
    public Song getSongByUid(int passedUid) { return songsDao.findBySongUid(passedUid); };

    // Function to save a song
    public void saveSong(Song song) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() { songsDao.insert(song); };
        });
    };

    // Function to remove a song
    public void removeSong(Song song) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() { songsDao.remove(song); };
        });
    };

    // Function to remove all songs from the database
    public void removeAllSongs() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() { songsDao.removeAllSongs(); };
        });
    };
};
