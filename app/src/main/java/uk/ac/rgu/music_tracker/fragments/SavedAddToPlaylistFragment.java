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
import java.util.concurrent.Executors;

import uk.ac.rgu.music_tracker.R;
import uk.ac.rgu.music_tracker.data.classes.Playlist;
import uk.ac.rgu.music_tracker.data.classes.Song;
import uk.ac.rgu.music_tracker.data.databases.playlists.PlaylistRepository;
import uk.ac.rgu.music_tracker.lists.PlaylistRecyclerViewAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SavedAddToPlaylistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SavedAddToPlaylistFragment extends Fragment implements View.OnClickListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SONG_UID = "songUid";

    private String passedSongUid;

    // Attributes
    List<Playlist> playlists;
    PlaylistRecyclerViewAdapter adapter;

    public SavedAddToPlaylistFragment() {
        // Required empty public constructor
    };

    public static SavedAddToPlaylistFragment newInstance(String songUid) {
        SavedAddToPlaylistFragment fragment = new SavedAddToPlaylistFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SONG_UID, songUid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            passedSongUid = getArguments().getString(ARG_SONG_UID);
        };
        playlists = new ArrayList<>();
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved_add_to_playlist, container, false);
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Setting the title
        getActivity().setTitle("Save To Playlist");

        // Add button
        Button addButton = view.findViewById(R.id.savedAddToFragmentAddButton);
        addButton.setOnClickListener(this);

        // Cancel button
        Button cancelButton = view.findViewById(R.id.savedAddToFragmentCancelButton);
        cancelButton.setOnClickListener(this);

        // Recycler view
        RecyclerView playlistRecyclerView = view.findViewById(R.id.savedAddToFragmentRecyclerView);
        // Create a new adapter for the playlists
        adapter = new PlaylistRecyclerViewAdapter(getContext(), playlists);
        // Setting the recycler view's adapter
        playlistRecyclerView.setAdapter(adapter);
        // Setup the layout manager on the recycler view
        playlistRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        PlaylistRepository.getRepository(getContext()).getAllPlaylists()
                .observe(getViewLifecycleOwner(), new Observer<List<Playlist>>() {
                    @Override
                    public void onChanged(List<Playlist> playlists) {
                        adapter.setPlaylists(playlists);
                        adapter.notifyDataSetChanged();
                    };
                });
    };

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.savedAddToFragmentAddButton) {
            // On add button pressed
            addSongToPlaylist();
        } else if (v.getId() == R.id.savedAddToFragmentCancelButton) {
            // On return button pressed
            returnToSavedFragment();
        };
    };

    // Function to add the passed song to the selected playlist
    public void addSongToPlaylist() {
        // Checking if a playlist is selected
        if (adapter.isSelectedPlaylistNull()) {
            // Telling the user to first select a playlist
            Toast.makeText(getContext(), "Please select a playlist!", Toast.LENGTH_SHORT).show();
            return;
        };

        // Adding the song to the playlist
        // Getting the repo
        PlaylistRepository repo = PlaylistRepository.getRepository(getContext());
        // Querying the database on a separate thread
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                // Getting the selected playlist
                Playlist selectedPlaylist = repo.getPlaylistByUid(adapter.getSelectedPlaylist().getUid());
                //Log.d(getResources().getString(R.string.debug_tag), "Adding song " + passedSongUid + " to the playlist.");
                // Getting the playlists list of songs
                ArrayList<Integer> currentSelectedPlaylistSongs = selectedPlaylist.getSongs();
                // Adding the song to the songs list
                currentSelectedPlaylistSongs.add(Integer.parseInt(passedSongUid));
                // Setting the playlists songs list
                selectedPlaylist.setSongs(currentSelectedPlaylistSongs);
                // Updating the database with the modified playlist
                repo.updatePlaylist(selectedPlaylist);
            };
        });
        // Returning to the saved song fragment
        returnToSavedFragment();
    };

    // Function to return to the saved fragment
    public void returnToSavedFragment() {
        Navigation.findNavController(getView()).navigate(R.id.action_savedAddToPlaylistFragment_to_savedFragment);
    };
};