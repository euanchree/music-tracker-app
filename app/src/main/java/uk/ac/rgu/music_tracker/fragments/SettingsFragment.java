package uk.ac.rgu.music_tracker.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import uk.ac.rgu.music_tracker.R;
import uk.ac.rgu.music_tracker.data.databases.playlists.PlaylistRepository;
import uk.ac.rgu.music_tracker.data.databases.songs.SongRepository;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener {
    public SettingsFragment() {
        // Required empty public constructor
    };

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Setting the title
        getActivity().setTitle("Settings");

        // Clear all songs button
        Button clearSongs = view.findViewById(R.id.settingsFragmentClearSavedButton);
        clearSongs.setOnClickListener(this);

        // Clear all playlists button
        Button clearPlaylists = view.findViewById(R.id.settingsFragmentClearPlaylistsButton);
        clearPlaylists.setOnClickListener(this);
    };

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.settingsFragmentClearSavedButton) {
            // Clear songs button pressed
            clearSongs();
        } else if (view.getId() == R.id.settingsFragmentClearPlaylistsButton) {
            // Clear playlists button pressed
            clearPlaylists();
        };
    };

    // Function to clear the songs database
    public void clearSongs() {
        // Getting the song repo
        SongRepository repo = SongRepository.getRepository(getContext());
        repo.removeAllSongs();
    };

    // Function to clear the playlists database
    public void clearPlaylists() {
        // Getting the playlists repo
        PlaylistRepository repo = PlaylistRepository.getRepository(getContext());
        repo.removeAllPlaylist();
    };
};