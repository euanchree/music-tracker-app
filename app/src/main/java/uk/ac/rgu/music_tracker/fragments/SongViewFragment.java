package uk.ac.rgu.music_tracker.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

import java.io.InputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import uk.ac.rgu.music_tracker.R;
import uk.ac.rgu.music_tracker.data.classes.Song;
import uk.ac.rgu.music_tracker.data.databases.songs.SongRepository;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SongViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SongViewFragment extends Fragment implements View.OnClickListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SONG_ID = "songId";
    private static final String ARG_PREV_PLAYLIST_UID = "playlistUid";

    private String passedSongId;
    private String passedPrevPlaylistUid;

    public SongViewFragment() {
        // Required empty public constructor
    }

    public static SongViewFragment newInstance(String songId, String prevPlaylistUid) {
        SongViewFragment fragment = new SongViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SONG_ID, songId);
        args.putString(ARG_PREV_PLAYLIST_UID, prevPlaylistUid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            passedSongId = getArguments().getString(ARG_SONG_ID);
            passedPrevPlaylistUid = getArguments().getString(ARG_PREV_PLAYLIST_UID);
        };
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_song_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Setting the title
        getActivity().setTitle("Song View");

        // Return button
        Button returnButton = getView().findViewById(R.id.songViewFragmentReturnButton);
        returnButton.setOnClickListener(this);

        // Getting song information from the database (on a separate thread) and setting the ui
        SongRepository repo = SongRepository.getRepository(getContext());
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                // Getting the song
                Song returnedSong = repo.getSongById(passedSongId);
                // Setting the ui
                // Song name
                TextView songNameView = view.findViewById(R.id.songViewFragmentSongNameText);
                songNameView.setText(returnedSong.getName());
                // Album name
                TextView songAlbumView = view.findViewById(R.id.songViewFragmentAlbumNameText);
                songAlbumView.setText(returnedSong.getAlbum());
                // Artist name
                TextView songArtistView = view.findViewById(R.id.songViewFragmentArtistNameText);
                songArtistView.setText(returnedSong.getArtistName());

                // Getting the imageUrl
                String requestUrl = "https://theaudiodb.com/api/v1/json/" +
                        getResources().getString(R.string.search_frag_api_key) +
                        "/album.php?m=" +
                        returnedSong.getAlbumId();

                StringRequest searchRequest = new StringRequest(Request.Method.GET, requestUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    // Creating the response json
                                    JSONObject jsonResponse = new JSONObject(response);
                                    //Creating the album json from the response
                                    JSONObject almBumJson = jsonResponse.getJSONArray("album").getJSONObject(0);
                                    // Saving the image url
                                    String songThumbnailUrl = almBumJson.getString("strAlbumThumb");
                                    // Setting the image view to the image
                                    ImageView imageView = getView().findViewById(R.id.songViewFragmentImageView);
                                    // Setting the image to the album art. TAKEN FROM "https://www.tutorialspoint.com/how-do-i-load-an-imageview-by-url-on-android"
                                    new DownloadImageFromInternet(imageView).execute(songThumbnailUrl);
                                    // END OF CODE TAKEN FROM REFERENCE
                                } catch (JSONException e) { onJsonError(e); };
                            };
                        }, new Response.ErrorListener() { @Override public void onErrorResponse(VolleyError error) { onVolleyError(error); }; }
                );
                // Create a new event queue
                RequestQueue volleyQueue = Volley.newRequestQueue(requireActivity().getApplicationContext());
                // Adding the request to the queue
                volleyQueue.add(searchRequest);
            };
        });
    };

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.songViewFragmentReturnButton) {
            // On return button pressed
            if (Navigation.findNavController(v).getPreviousBackStackEntry().getDestination().getId() == R.id.savedFragment) {
                Navigation.findNavController(v).navigate(R.id.action_songViewFragment_to_savedFragment);
            } else if (Navigation.findNavController(v).getPreviousBackStackEntry().getDestination().getId() == R.id.playlistsViewFragment) {
                // Creating the bundle to pass to the playlist view fragment
                Bundle args = new Bundle();
                // Adding the playlist uid to the bundle
                args.putString(ARG_PREV_PLAYLIST_UID, passedPrevPlaylistUid);
                //Log.d(getResources().getString(R.string.debug_tag), passedPrevPlaylistUid);
                Navigation.findNavController(getView()).navigate(R.id.action_songViewFragment_to_playlistsViewFragment, args);
            };
        };
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

    // Class to download image from the internet. TAKEN FROM "https://www.tutorialspoint.com/how-do-i-load-an-imageview-by-url-on-android" more information in the report!
    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        // Image view to populate with the image
        ImageView imageView;

        // Constructor
        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView=imageView;
            // Telling the user that it while take a second to download the image
            //Toast.makeText(getContext(), "Please wait, it may take a few seconds...",Toast.LENGTH_SHORT).show();
        }

        // Function to download the image in the background
        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage=null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            };
            return bimage;
        };

        // Setting the image view after the downloading is done
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        };
    }
    // END OF CODE TAKEN FROM REFERENCE
};