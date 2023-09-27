package uk.ac.rgu.music_tracker.data.databases.playlists;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import uk.ac.rgu.music_tracker.data.classes.Playlist;

@Database(entities = {Playlist.class}, version = 1, exportSchema = false)
public abstract class PlaylistDatabase extends RoomDatabase {
    //Attributes
    public abstract PlaylistsDao PlaylistsDao();
    private static PlaylistDatabase INSTANCE;

    // Function to get the database
    public static PlaylistDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PlaylistDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PlaylistDatabase.class, "playlists_database")
                            .fallbackToDestructiveMigration()
                            //.allowMainThreadQueries()
                            .build();
                };
            };
        };

        // Returning the database
        return INSTANCE;
    };
};