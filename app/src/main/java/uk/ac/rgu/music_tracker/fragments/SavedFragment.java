package uk.ac.rgu.music_tracker.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import uk.ac.rgu.music_tracker.R;
import uk.ac.rgu.music_tracker.data.classes.Playlist;
import uk.ac.rgu.music_tracker.data.classes.Song;
import uk.ac.rgu.music_tracker.data.databases.playlists.PlaylistRepository;
import uk.ac.rgu.music_tracker.data.databases.songs.SongRepository;
import uk.ac.rgu.music_tracker.lists.SongRecyclerViewAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SavedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SavedFragment extends Fragment implements View.OnClickListener {
    // Attributes
    List<Song> songs;
    SongRecyclerViewAdapter adapter;

    public SavedFragment() {
        // Required empty public constructor
    };

    public static SavedFragment newInstance() {
        //SavedFragment fragment = new SavedFragment();
        //return fragment;
        return new SavedFragment();
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        songs = new ArrayList<>();
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved, container, false);
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Setting the title
        getActivity().setTitle("Saved");

        // Unsave button
        Button unSaveButton = view.findViewById(R.id.savedFragmentUnsaveButton);
        unSaveButton.setOnClickListener(this);

        // View button
        Button viewButton = view.findViewById(R.id.savedFragmentViewSongButton);
        viewButton.setOnClickListener(this);

        // Add to playlist button
        Button addToPlaylistButton = view.findViewById(R.id.savedFragmentAddToPlaylistButton);
        addToPlaylistButton.setOnClickListener(this);

        // Recycler view
        RecyclerView songRecyclerView = view.findViewById(R.id.savedFragmentRecyclerView);
        // Create a new adapter for the songs
        adapter = new SongRecyclerViewAdapter(getContext(), songs);
        // Setting the recycler view's adapter
        songRecyclerView.setAdapter(adapter);
        // Setup the layout manager on the recycler view
        songRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        SongRepository.getRepository(getContext()).getAllSongs()
                .observe(getViewLifecycleOwner(), new Observer<List<Song>>() {
                    @Override
                    public void onChanged(List<Song> songs) {
                        adapter.setSongs(songs);
                        adapter.notifyDataSetChanged();
                    };
                });

        PlaylistRepository.getRepository(getContext()).getAllPlaylists()
                .observe(getViewLifecycleOwner(), new Observer<List<Playlist>>() {
                    @Override
                    public void onChanged(List<Playlist> playlists) {
                        playlists.forEach(current -> {
                            Log.d(getResources().getString(R.string.debug_tag), current.toString());
                        });
                    };
                });
    };

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.savedFragmentUnsaveButton) {
            // unsave button pressed
            unSaveSong();
        } else if (v.getId() == R.id.savedFragmentViewSongButton) {
            // view button pressed
            viewSong();
        } else if (v.getId() == R.id.savedFragmentAddToPlaylistButton) {
            // add to playlist button
            //Navigation.findNavController(v).navigate(R.id.action_savedFragment_to_savedAddToPlaylistFragment);
            addToPlaylist();
        };
    };

    // Function to unsave the song selected in the recycler view
    public void unSaveSong() {
        // Checking if a song is selected
        if (adapter.isSelectedSongNull()) {
            // Telling the user to first select a song
            Toast.makeText(getContext(), "Please select a song!", Toast.LENGTH_SHORT).show();
            return;
        };
        // Adding the song to the database
        SongRepository repo = SongRepository.getRepository(getContext());
        repo.removeSong(adapter.getSelectedSong());
        adapter.selectedSongToNull();
    };

    // Function to view a song selected from the recycler view
    public void viewSong() {
        // Checking if a song is selected
        if (adapter.isSelectedSongNull()) {
            // Telling the user to first select a song
            Toast.makeText(getContext(), "Please select a song!", Toast.LENGTH_SHORT).show();
            return;
        };
        // Creating the bundle to pass to the song view fragment
        Bundle args = new Bundle();
        // Adding the song id to the bundle
        args.putString("songId", adapter.getSelectedSong().getSongId());
        // Navigating to the song view fragment with the information
        Navigation.findNavController(getView()).navigate(R.id.action_savedFragment_to_songViewFragment,args);
        adapter.selectedSongToNull();
    };

    // Function to add a song to a playlist
    public void addToPlaylist() {
        // Checking if a song is selected
        if (adapter.isSelectedSongNull()) {
            // Telling the user to first select a song
            Toast.makeText(getContext(), "Please select a song!", Toast.LENGTH_SHORT).show();
            return;
        };
        //Log.d(getResources().getString(R.string.debug_tag), String.valueOf(adapter.getSelectedSong().getUid()));
        // Creating the bundle to pass to the add to playlist fragment
        Bundle args = new Bundle();
        // Adding the songId to the bundle
        args.putString("songUid", String.valueOf(adapter.getSelectedSong().getUid()));
        // Navigating to the add to playlist fragment with the information
        Navigation.findNavController(getView()).navigate(R.id.action_savedFragment_to_savedAddToPlaylistFragment, args);
    };
};