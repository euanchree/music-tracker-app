package uk.ac.rgu.music_tracker.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import uk.ac.rgu.music_tracker.R;
import uk.ac.rgu.music_tracker.data.classes.Song;
import uk.ac.rgu.music_tracker.data.databases.songs.SongRepository;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment implements View.OnClickListener {
    // Variable to store the id of the most recently searched song
    private String searchedSongId = null;

    public SearchFragment() {
        // Required empty public constructor
    };

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Setting the title
        getActivity().setTitle("Search");

        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_search, container, false);

        // Search button
        Button searchButton = view.findViewById(R.id.searchFragmentSearchButton);
        searchButton.setOnClickListener(this);

        // Save button
        Button saveButton = view.findViewById(R.id.searchFragmentSaveButton);
        saveButton.setOnClickListener(this);

        // Inflate the layout for this fragment
        return view;
    }

    // Button listener
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.searchFragmentSearchButton) {
            // Search button pressed
            searchSong();
        } else if (view.getId() == R.id.searchFragmentSaveButton) {
            // Save button pressed
            saveSong();
        };
    };

    // Function for searching for a song and updating the ui
    public void searchSong() {
        // Get the search input
        EditText searchTermEt = getView().findViewById(R.id.searchFragmentSearchBarInput);

        // Checking the input for the deliminator ","
        if (!searchTermEt.getText().toString().contains(",")) {
            Toast onErrorToast = Toast.makeText(getContext(), "Don't forget the ,", Toast.LENGTH_SHORT);
            onErrorToast.show();
            return;
        };

        // Getting the Artist name and song name from the search
        String[] searchTerms = searchTermEt.getText()
            .toString()// Getting the string
            .replaceAll(" ", "_") // Removing the white space
            .split(","); // Extracting the Artist and Song names

        // Getting information from the music api
        // Example api usage "https://theaudiodb.com/api/v1/json/{APIKEY}/searchtrack.php?s=coldplay&t=yellow"
        String searchUrl =
            "https://theaudiodb.com/api/v1/json/" +
            getResources().getString(R.string.search_frag_api_key) +
            "/searchtrack.php?s=" +
            searchTerms[0] +
            "&t=" +
            searchTerms[1];

        // Printing request
        Log.d(getResources().getString(R.string.debug_tag), "Searching for '" + searchTerms[0] + "', '" + searchTerms[1] + "'" + ", Using GET:" + searchUrl);

        // Creating the request
        StringRequest searchRequest = new StringRequest(Request.Method.GET, searchUrl,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Creating a json with the results of the string request
                    try {
                        // Creating the response json
                        JSONObject jsonResponse = new JSONObject(response);
                        //Creating the song json from the response
                        JSONObject songJson = jsonResponse.getJSONArray("track").getJSONObject(0);
                        Log.d(getResources().getString(R.string.debug_tag), "Response: " + songJson.toString());
                        // Getting ui text views
                        TextView songNameView = getActivity().findViewById(R.id.searchFragmentSongNameText);
                        TextView songArtistView = getActivity().findViewById(R.id.searchFragmentSongArtistText);
                        TextView songAlbumView = getActivity().findViewById(R.id.searchFragmentSongAlbumText);
                        // Getting song information from the json
                        String songName = songJson.getString("strTrack");
                        String songArtist = songJson.getString("strArtist");
                        String songAlbum = songJson.getString("strAlbum");
                        // Updating ui
                        songNameView.setText(songName);
                        songArtistView.setText(songArtist);
                        songAlbumView.setText(songAlbum);
                        // Setting the searched song id so the save function knows which song to search for
                        searchedSongId = songJson.getString("idTrack");
                    } catch (JSONException e) { onJsonError(e); };
                };
            }, new Response.ErrorListener() { @Override public void onErrorResponse(VolleyError error) { onVolleyError(error); }; }
        );
        // Create a new event queue
        RequestQueue volleyQueue = Volley.newRequestQueue(requireActivity().getApplicationContext());
        // Adding the request to the queue
        volleyQueue.add(searchRequest);
    };

    // Function to save searched song
    public void saveSong() {
        if (searchedSongId == null) {
            Toast onErrorToast = Toast.makeText(getContext(), "Please search for a song first.", Toast.LENGTH_SHORT);
            onErrorToast.show();
            return;
        };

        // Get the information from the api and fill the song
        String requestUrl = "https://theaudiodb.com/api/v1/json/" +
                getResources().getString(R.string.search_frag_api_key) +
                "/track.php?h=" +
                searchedSongId;

        StringRequest searchRequest = new StringRequest(Request.Method.GET, requestUrl,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        // Creating the response json
                        JSONObject jsonResponse = new JSONObject(response);
                        //Creating the song json from the response
                        JSONObject songJson = jsonResponse.getJSONArray("track").getJSONObject(0);
                        //Log.d(getResources().getString(R.string.debug_tag), "Response: " + songJson.toString());

                        // Getting song information from the json
                        String songName = songJson.getString("strTrack");
                        String songArtist = songJson.getString("strArtist");
                        String songAlbum = songJson.getString("strAlbum");

                        // Creating the song object from the information
                        Song songToSave = new Song(
                                songJson.getString("idTrack"),
                                songJson.getString("idAlbum"),
                                songJson.getString("strTrack"),
                                songJson.getString("strAlbum"),
                                songJson.getString("strArtist"),
                                "unknown",
                                "unknown",
                                "unknown"
                                );

                        // Adding the song to the database
                        SongRepository repo = SongRepository.getRepository(getContext());

                        repo.saveSong(songToSave);

                        // Telling the user the song has been saved
                        Toast.makeText(getContext(), "Saved song " + songToSave.getName(), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) { onJsonError(e); };
                };
            }, new Response.ErrorListener() { @Override public void onErrorResponse(VolleyError error) { onVolleyError(error); }; }
        );
        // Create a new event queue
        RequestQueue volleyQueue = Volley.newRequestQueue(requireActivity().getApplicationContext());
        // Adding the request to the queue
        volleyQueue.add(searchRequest);
    };

    // On error functions
    public void onVolleyError(VolleyError error) {
        // Telling the user of the error
        Toast onErrorToast = Toast.makeText(getContext(), "On response error!", Toast.LENGTH_SHORT);
        onErrorToast.show();
    };

    public void onJsonError(JSONException e) {
        // Telling the user of the error
        Toast onErrorToast = Toast.makeText(getContext(), "Try to search another song!", Toast.LENGTH_SHORT);
        onErrorToast.show();
    };
};