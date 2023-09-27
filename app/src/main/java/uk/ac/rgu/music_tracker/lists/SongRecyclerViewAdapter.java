package uk.ac.rgu.music_tracker.lists;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import uk.ac.rgu.music_tracker.R;
import uk.ac.rgu.music_tracker.data.classes.Song;
import uk.ac.rgu.music_tracker.fragments.SavedFragment;
import uk.ac.rgu.music_tracker.fragments.SearchFragment;

public class SongRecyclerViewAdapter extends RecyclerView.Adapter<SongRecyclerViewAdapter.SongViewHolder> {
    // Attributes
    private Context context;
    private List<Song> songs;
    public Song selectedSong;

    // Constructor
    public SongRecyclerViewAdapter(Context context, List<Song> songs) {
        super();
        this.context = context;
        this.songs = songs;
        this.selectedSong = null;
    };

    // Function to set the data being displayed in this adapter
    public void setSongs(List<Song> songs) { this.songs = songs; }

    // Function to get item count
    @Override
    public int getItemCount() { return this.songs.size(); };

    // Function to get the selected song
    public Song getSelectedSong() {
        if (selectedSong == null) {
            return new Song("", "", "", "", "", "", "", "");
        };

        return selectedSong;
    };

    // Function to set selected song to null
    public void selectedSongToNull() {
        selectedSong = null;
    };

    // Function to check if the selected song is null
    public boolean isSelectedSongNull() {
        return selectedSong == null;
    };

    // Song view holder class
    class SongViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Attributes
        private View songItemView;
        private SongRecyclerViewAdapter adapter;

        // Constructor
        public SongViewHolder(View songItemView, SongRecyclerViewAdapter adapter) {
            super(songItemView);
            this.songItemView = songItemView;
            this.adapter = adapter;
            this.songItemView.setOnClickListener(this);
        };

        // On click function
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            selectedSong = songs.get(position);;
        };
    };

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.song_list_item, parent, false);
        SongViewHolder viewHolder = new SongViewHolder(itemView, this);
        return viewHolder;
    };

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        // Getting the song
        Song song = this.songs.get(position);

        // Checking if song is null
        if (song == null) {
            //song is null
            // Getting and setting the song name
            TextView songNameView = holder.songItemView.findViewById(R.id.songListItemSongName);
            songNameView.setText("Song null!");

            // Getting and setting the songs artist
            TextView songArtistView = holder.songItemView.findViewById(R.id.songListItemArtistName);
            songArtistView.setText("Song null!");

            return;
        };
        // Getting and setting the song name
        TextView songNameView = holder.songItemView.findViewById(R.id.songListItemSongName);
        songNameView.setText(song.getName());

        // Getting and setting the songs artist
        TextView songArtistView = holder.songItemView.findViewById(R.id.songListItemArtistName);
        songArtistView.setText(song.getArtistName());
    };
};
