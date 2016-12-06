package software.level.udacity.popularmovies1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
    }

    /**
     * Inflates the menu resource for this activity
     *
     * @param menu Interface that manages the menu
     * @return True if the menu is to be displayed
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movies, menu);
        return true;
    }

    /**
     * Handles selection of items in the menu
     *
     * @param item The menu item that was selected
     * @return Return false to allow normal menu processing, true to consume it here
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Popular sort method selected
        if(id == R.id.action_popular) {
            Log.i(TAG, "Selected popular.");
            return true;
        }

        // Top rated sort method selected
        if(id == R.id.action_toprated) {
            Log.i(TAG, "Selected top rated.");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
