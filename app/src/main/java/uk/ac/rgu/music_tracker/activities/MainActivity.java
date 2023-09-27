package uk.ac.rgu.music_tracker.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import uk.ac.rgu.music_tracker.R;

public class MainActivity extends AppCompatActivity {
    // Tag for the log.d messages
    String TAG = "Music_Tracker";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.nav_menu, menu);
        return true;
    }

    // Function for handling the menu events (navigation)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int buttonItemId = item.getItemId();

        switch (Navigation.findNavController(findViewById(R.id.fragmentContainerView)).getCurrentDestination().getId()) {

            // From the saved songs fragment
            case (R.id.savedFragment):
                switch (buttonItemId) {
                    case (R.id.navBarPlaylists):
                        Navigation.findNavController(findViewById(R.id.fragmentContainerView)).navigate(R.id.action_savedFragment_to_playlistsFragment);
                        break;
                    case (R.id.navBarSearch):
                        Navigation.findNavController(findViewById(R.id.fragmentContainerView)).navigate(R.id.action_savedFragment_to_searchFragment);
                        break;
                    case (R.id.navBarSettings):
                        Navigation.findNavController(findViewById(R.id.fragmentContainerView)).navigate(R.id.action_savedFragment_to_settingsFragment);
                        break;
                };
                break;

            // From the playlists fragment
            case (R.id.playlistsFragment):
                switch (buttonItemId) {
                    case (R.id.navBarSaved):
                        Navigation.findNavController(findViewById(R.id.fragmentContainerView)).navigate(R.id.action_playlistsFragment_to_savedFragment);
                        break;
                    case (R.id.navBarSearch):
                        Navigation.findNavController(findViewById(R.id.fragmentContainerView)).navigate(R.id.action_playlistsFragment_to_searchFragment);
                        break;
                    case (R.id.navBarSettings):
                        Navigation.findNavController(findViewById(R.id.fragmentContainerView)).navigate(R.id.action_playlistsFragment_to_settingsFragment);
                        break;
                };
                break;

            // From the search fragment
            case (R.id.searchFragment):
                switch (buttonItemId) {
                    case (R.id.navBarSaved):
                        Navigation.findNavController(findViewById(R.id.fragmentContainerView)).navigate(R.id.action_searchFragment_to_savedFragment);
                        break;
                    case (R.id.navBarPlaylists):
                        Navigation.findNavController(findViewById(R.id.fragmentContainerView)).navigate(R.id.action_searchFragment_to_playlistsFragment);
                        break;
                    case (R.id.navBarSettings):
                        Navigation.findNavController(findViewById(R.id.fragmentContainerView)).navigate(R.id.action_searchFragment_to_settingsFragment);
                        break;
                };
                break;

            // From the settings fragment
            case (R.id.settingsFragment):
                switch (buttonItemId) {
                    case (R.id.navBarSaved):
                        Navigation.findNavController(findViewById(R.id.fragmentContainerView)).navigate(R.id.action_settingsFragment_to_savedFragment);
                        break;
                    case (R.id.navBarPlaylists):
                        Navigation.findNavController(findViewById(R.id.fragmentContainerView)).navigate(R.id.action_settingsFragment_to_playlistsFragment);
                        break;
                    case (R.id.navBarSearch):
                        Navigation.findNavController(findViewById(R.id.fragmentContainerView)).navigate(R.id.action_settingsFragment_to_searchFragment);
                        break;
                };
                break;

            // For the song add to playlist view fragment
            case (R.id.savedAddToPlaylistFragment):
                Toast savedAddToViewToast = Toast.makeText(getApplicationContext(), "Use the cancel button first.", Toast.LENGTH_SHORT);
                savedAddToViewToast.show();

            // For the song view fragment
            case (R.id.songViewFragment):
                Toast songViewToast = Toast.makeText(getApplicationContext(), "Use the return button first.", Toast.LENGTH_SHORT);
                songViewToast.show();

            // For the playlist view fragment
            case (R.id.playlistsViewFragment):
                Toast playlistViewToast = Toast.makeText(getApplicationContext(), "Use the return button first.", Toast.LENGTH_SHORT);
                playlistViewToast.show();
        };

        return true;
    }
}