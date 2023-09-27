package uk.ac.rgu.music_tracker.fragments;

import android.app.Activity;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import uk.ac.rgu.music_tracker.R;
import uk.ac.rgu.music_tracker.data.classes.Playlist;
import uk.ac.rgu.music_tracker.data.classes.Song;
import uk.ac.rgu.music_tracker.data.databases.playlists.PlaylistRepository;
import uk.ac.rgu.music_tracker.data.databases.songs.SongRepository;
import uk.ac.rgu.music_tracker.lists.SongRecyclerViewAdapter;

public class PlaylistsViewFragment extends Fragment implements View.OnClickListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PLAYLIST_UID = "playlistUid";

    private String passedPlaylistUid;

    // Attributes
    List<Song> songs;
    SongRecyclerViewAdapter adapter;

    public PlaylistsViewFragment() {
        // Required empty public constructor
    }

    public static PlaylistsViewFragment newInstance(String playlistUid) {
        PlaylistsViewFragment fragment = new PlaylistsViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PLAYLIST_UID, playlistUid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            passedPlaylistUid = getArguments().getString(ARG_PLAYLIST_UID);
        }
        songs = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_playlists_view, container, false);
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Setting the title
        getActivity().setTitle("Playlist View");

        // Rename button
        Button renameButton = view.findViewById(R.id.playlistsViewRenameButton);
        renameButton.setOnClickListener(this);

        // Remove button
        Button removeButton = view.findViewById(R.id.playlistsVeiwFragmentRemoveButton);
        removeButton.setOnClickListener(this);

        // View button
        Button viewButton = view.findViewById(R.id.playlistsViewFragmentViewButton);
        viewButton.setOnClickListener(this);

        // Return button
        Button returnButton = view.findViewById(R.id.playlistsViewFragmentReturnButton);
        returnButton.setOnClickListener(this);

        // Recycler view
        RecyclerView songRecyclerView = view.findViewById(R.id.playlistsVeiwFragmentRecyclerView);
        // Creating a new adapter for the songs
        adapter = new SongRecyclerViewAdapter(getContext(), songs);
        // Setting the recycler view's adapter
        songRecyclerView.setAdapter(adapter);
        // Setup the layout manager on the recycler view
        songRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Getting playlist repo
        SongRepository repo = SongRepository.getRepository(getContext());

        // Filling the recycler view with songs from the playlist updating on change
        PlaylistRepository.getRepository(getContext()).getPlaylistByUidLiveData(Integer.parseInt(passedPlaylistUid))
                .observe(getViewLifecycleOwner(), new Observer<Playlist>() {
                    @Override
                    public void onChanged(Playlist playlist) {
                        Executors.newSingleThreadExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                // Getting the list of song uis
                                List<Integer> songUids = playlist.getSongs();
                                // Checking if null
                                if (songUids == null) {
                                    //Is null
                                    return;
                                };
                                // Creating the list of songs to be used by the adapter
                                List<Song> songs = new ArrayList<>();
                                // Retrieving and adding the songs from the song database to the adapter list
                                for (Integer current : songUids) {
                                    songs.add(repo.getSongByUid(current));
                                };
                                // Updating the recycler view on the ui thread
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Setting adapter
                                        adapter.setSongs(songs);
                                        adapter.notifyDataSetChanged();
                                    };
                                });
                            };
                        });
                    };
                });
    };

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.playlistsViewRenameButton) {
            renamePlaylist();
        } else if (v.getId() == R.id.playlistsVeiwFragmentRemoveButton) {
            removeSong();
        } else if (v.getId() == R.id.playlistsViewFragmentViewButton) {
            viewSong();
        } else if (v.getId() == R.id.playlistsViewFragmentReturnButton) {
            returnToPlaylistFragment();
        };
    };

    // Function to rename the currently viewed playlist
    public void renamePlaylist() {
        // Getting playlist repo
        PlaylistRepository repo = PlaylistRepository.getRepository(getContext());

        // Querying the database on a separate thread
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                // Getting the currently view playlist
                Playlist currentPlaylist = repo.getPlaylistByUid(Integer.parseInt(passedPlaylistUid));
                // Getting the input from the text input
                EditText renameInput = getView().findViewById(R.id.playlistsViewRenameInput);
                String newName = String.valueOf(renameInput.getText());

                // Checking that the newName isn't empty
                if (newName.isEmpty()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Can't be empty.", Toast.LENGTH_SHORT).show();
                        };
                    });
                    return;
                };

                // Updating the playlist
                currentPlaylist.setName(newName);
                // Updating the database
                repo.updatePlaylist(currentPlaylist);
                // Telling the user
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "Renamed playlist to " + newName, Toast.LENGTH_SHORT).show();
                    };
                });
            };
        });
    };

    // Function to remove the selected song from the viewed playlist
    public void removeSong() {
        // Checking if a song is selected
        if (adapter.isSelectedSongNull()) {
            // Telling the user to first select a song
            Toast.makeText(getContext(), "Please select a song!", Toast.LENGTH_SHORT).show();
            return;
        };

        // Getting playlist repo
        PlaylistRepository repo = PlaylistRepository.getRepository(getContext());

        // Querying the database on a separate thread
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                // Getting the selected song
                Integer selectingSongUid = adapter.getSelectedSong().getUid();

                // Getting the currently view playlist
                Playlist currentPlaylist = repo.getPlaylistByUid(Integer.parseInt(passedPlaylistUid));

                // Getting the playlists songs
                ArrayList<Integer> currentPlaylistSongs = currentPlaylist.getSongs();

                // Removing the song from the list
                currentPlaylistSongs.remove(selectingSongUid);

                // Setting the playlists songs list to the new one
                currentPlaylist.setSongs(currentPlaylistSongs);

                // Updating the database
                repo.updatePlaylist(currentPlaylist);

                // Setting the selected song to null
                adapter.selectedSongToNull();

                Log.d(getResources().getString(R.string.debug_tag), "New playlist songs: " + currentPlaylistSongs);
            };
        });
    };

    // Function to view the currently selected song
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
        args.putString("playlistUid", passedPlaylistUid);
        // Navigating to the song view fragment with the information
        Navigation.findNavController(getView()).navigate(R.id.action_playlistsViewFragment_to_songViewFragment,args);
        adapter.selectedSongToNull();

        //Navigation.findNavController(getView()).navigate(R.id.action_playlistsViewFragment_to_songViewFragment);
    };

    // Function return to the playlists fragment
    public void returnToPlaylistFragment() {
        Navigation.findNavController(getView()).navigate(R.id.action_playlistsViewFragment_to_playlistsFragment);
    };
}