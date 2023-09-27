package uk.ac.rgu.music_tracker.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import uk.ac.rgu.music_tracker.lists.PlaylistRecyclerViewAdapter;
import uk.ac.rgu.music_tracker.lists.SongRecyclerViewAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlaylistsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlaylistsFragment extends Fragment implements View.OnClickListener {
    // Attributes
    List<Playlist> playlists;
    PlaylistRecyclerViewAdapter adapter;

    public PlaylistsFragment() {
        // Required empty public constructor
    };

    public static PlaylistsFragment newInstance() {
        return new PlaylistsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playlists = new ArrayList<>();
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_playlist, container, false);
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Setting the title
        getActivity().setTitle("Playlists");

        // View button
        Button viewButton = view.findViewById(R.id.playlistFragmentVeiwButton);
        viewButton.setOnClickListener(this);

        // Create button
        Button createButton = view.findViewById(R.id.playlistFragmentCreateButton);
        createButton.setOnClickListener(this);

        // Delete button
        Button deleteButton = view.findViewById(R.id.playlistFragmentDeleteButton);
        deleteButton.setOnClickListener(this);

        // Recycler view
        RecyclerView playlistRecyclerView = view.findViewById(R.id.playlistFragmentRecyclerView);
        // Create a new adapter for the songs
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
        if (v.getId() == R.id.playlistFragmentVeiwButton) {
            // view button pressed
            viewPlaylist();
        } else if (v.getId() == R.id.playlistFragmentCreateButton) {
            // Create button pressed
            createPlaylist();
        } else if (v.getId() == R.id.playlistFragmentDeleteButton) {
            // Delete button pressed
            deletePlaylist();
        };
    };

    // Function to view a playlist
    public void viewPlaylist() {
        // Checking if a playlist is selected
        if (adapter.isSelectedPlaylistNull()) {
            // Telling the user to first select a playlist
            Toast.makeText(getContext(), "Please select a playlist!", Toast.LENGTH_SHORT).show();
            return;
        };
        // Creating the bundle to pass to the playlist view fragment
        Bundle args = new Bundle();
        // Adding the playlist uid to the bundle
        args.putString("playlistUid", String.valueOf(adapter.getSelectedPlaylist().getUid()));
        Navigation.findNavController(getView()).navigate(R.id.action_playlistsFragment_to_playlistsViewFragment, args);
        adapter.selectedPlaylistToNull();
    };

    // Function to create a new playlist
    public void createPlaylist() {
        // Adding the playlist to the database
        PlaylistRepository repo = PlaylistRepository.getRepository(getContext());
        repo.addPlaylist(new Playlist("New playlist"));
        adapter.selectedPlaylistToNull();
    };

    // Function to delete the selected playlist from the database
    public void deletePlaylist() {
        // Checking if a playlist is selected
        if (adapter.isSelectedPlaylistNull()) {
            // Telling the user to first select a playlist
            Toast.makeText(getContext(), "Please select a playlist!", Toast.LENGTH_SHORT).show();
            return;
        };

        // Removing the playlist from the database
        PlaylistRepository repo = PlaylistRepository.getRepository(getContext());
        repo.removePlaylist(adapter.getSelectedPlaylist());
        adapter.selectedPlaylistToNull();
    };
}