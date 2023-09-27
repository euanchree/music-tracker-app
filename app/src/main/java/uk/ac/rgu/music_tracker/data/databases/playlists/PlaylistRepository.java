package uk.ac.rgu.music_tracker.data.databases.playlists;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import uk.ac.rgu.music_tracker.data.classes.Playlist;

public class PlaylistRepository {
    private PlaylistsDao playlistsDao;
    private static PlaylistRepository INSTANCE;
    private Context context;

    private PlaylistRepository(Context context) {
        playlistsDao = PlaylistDatabase.getDatabase(context).PlaylistsDao();
    };

    public static PlaylistRepository getRepository(Context context) {
        if (INSTANCE == null) {
            synchronized (PlaylistRepository.class) {
                INSTANCE = new PlaylistRepository(context);
            };
        };

        return INSTANCE;
    };

    // Function to add a playlist
    public void addPlaylist(Playlist playlist) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() { playlistsDao.insert(playlist); };
        });
    };

    // Function to remove a playlist
    public void removePlaylist(Playlist playlist) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() { playlistsDao.remove(playlist); };
        });
    };

    // Function to update a playlist
    public void updatePlaylist(Playlist playlist) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() { playlistsDao.update(playlist); };
        });
    };

    // Function to get a playlist using a uid
    public Playlist getPlaylistByUid(int passedUid) {
        return playlistsDao.getPlaylistByUid(passedUid);
    };

    // Function to get a playlist using a uid using live data
    public LiveData<Playlist> getPlaylistByUidLiveData(int passedUid) {
        return playlistsDao.getPlaylistByUidLiveData(passedUid);
    };

    // Function to get all the playlists with live data
    public LiveData<List<Playlist>> getAllPlaylists() { return playlistsDao.getAllPlaylists(); };

    // Function to get all the playlist with non live data
    public List<Playlist> getAllPlaylistsNonLiveData() { return playlistsDao.getAllPlaylistsNonLiveData(); };

    // Function to remove all the playlists from the database
    public void removeAllPlaylist() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() { playlistsDao.removeAllPlaylists(); };
        });
    };
};
