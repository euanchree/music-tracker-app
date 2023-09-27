package uk.ac.rgu.music_tracker.data.databases.songs;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import uk.ac.rgu.music_tracker.data.classes.Song;

@Database(entities = {Song.class}, version = 1, exportSchema = false)
public abstract class SongDatabase extends RoomDatabase {
    // Attributes
    public abstract SongsDao songsDao();
    private static SongDatabase INSTANCE;

    // Function to get the database
    public static SongDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (SongDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            SongDatabase.class, "songs_database")
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