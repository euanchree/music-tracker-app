package uk.ac.rgu.music_tracker.lists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import uk.ac.rgu.music_tracker.R;
import uk.ac.rgu.music_tracker.data.classes.Playlist;

public class PlaylistRecyclerViewAdapter extends RecyclerView.Adapter<PlaylistRecyclerViewAdapter.PlaylistViewHolder> {
    // Attributes
    private Context context;
    private List<Playlist> playlists;
    public Playlist selectedPlaylist;

    // Constructor
    public PlaylistRecyclerViewAdapter(Context context, List<Playlist> playlists) {
        super();
        this.context = context;
        this.playlists = playlists;
        this.selectedPlaylist = null;
    };

    // Function to set the data being displayed in this adapter
    public void setPlaylists(List<Playlist> playlists) { this.playlists = playlists; };

    // Function to get the item count
    @Override
    public int getItemCount() { return this.playlists.size(); };

    // Function to get the selected playlist
    public Playlist getSelectedPlaylist() {
        if (selectedPlaylist == null) {
            return new Playlist("");
        };

        return  selectedPlaylist;
    };

    // Function to set the selected playlist to null
    public void selectedPlaylistToNull() { selectedPlaylist = null; }

    // Function to check if the selected playlist is null
    public boolean isSelectedPlaylistNull() { return selectedPlaylist == null; };

    // Playlist view holder class
    class PlaylistViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {
        // Attributes
        private View playlistItemView;
        private PlaylistRecyclerViewAdapter adapter;

        // Constructor
        public PlaylistViewHolder(View playlistItemView, PlaylistRecyclerViewAdapter adapter) {
            super(playlistItemView);
            this.playlistItemView = playlistItemView;
            this.adapter = adapter;
            this.playlistItemView.setOnClickListener(this);
        };

        // On click function
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            selectedPlaylist = playlists.get(position);
        };
    };

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.playlist_list_item, parent, false);
        PlaylistViewHolder viewHolder = new PlaylistViewHolder(itemView, this);
        return viewHolder;
    };

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        // Getting the playlist
        Playlist playlist = this.playlists.get(position);

        // Getting and setting the playlist name
        TextView playlistNameView= holder.playlistItemView.findViewById(R.id.playlistListItemNameText);
        playlistNameView.setText(playlist.getName());
    };
};
