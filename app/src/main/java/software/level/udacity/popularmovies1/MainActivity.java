package software.level.udacity.popularmovies1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import software.level.udacity.popularmovies1.data.Movie;
import software.level.udacity.popularmovies1.utilities.MovieRequestType;
import software.level.udacity.popularmovies1.utilities.NetworkUtils;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieOnClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();

    // Holds a reference to the RecyclerView that holds all the movie posters
    private RecyclerView mRecyclerView;

    // Holds a reference to the RecyclerView adapter
    private MovieAdapter mMovieAdapter;

    // Holds a reference to the loading indicator
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        // Do the initial setup on the RecyclerView
        configureRecyclerView();

        // Testing the url builder
        NetworkUtils.buildURL(this, MovieRequestType.DETAILS, 500);
        NetworkUtils.buildURL(this, MovieRequestType.POPULAR);
        NetworkUtils.buildURL(this, MovieRequestType.TOP_RATED);

        // Create some fake data for testing the RecyclerView
        ArrayList<Movie> fakeData = new ArrayList<>();
        fakeData.add(new Movie("The Shawshank Redemption"));
        fakeData.add(new Movie("Forest Gump"));

        mMovieAdapter.setMovieData(fakeData);

        // Picasso.with(context).load("http://i.imgur.com/DvpvklR.png").into(imageView);

    }


    /**
     * Performs the initial configuration for the RecyclerView
     */
    private void configureRecyclerView() {
        // Get the reference to the RecyclerView from the layout file
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movies);

        // Initialize the layout manager and set the RecyclerView to use it
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        // All the movie posters will be the same size
        mRecyclerView.setHasFixedSize(true);

        // Create the adapter and set it
        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);
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

    /**
     * Handles selection of movie from the RecyclerView
     * @param movie The movie that was selected passed by the ViewHolder
     */
    @Override
    public void onClickMovie(Movie movie) {
        Log.d(TAG, "You selected movie " + movie.getTitle());
    }
}
