package software.level.udacity.popularmovies1;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import software.level.udacity.popularmovies1.data.Movie;
import software.level.udacity.popularmovies1.data.MovieParser;
import software.level.udacity.popularmovies1.utilities.MovieRequestType;
import software.level.udacity.popularmovies1.utilities.NetworkUtils;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieOnClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();

    // Reference to the RecyclerView that holds all the movie posters
    private RecyclerView mRecyclerView;

    // Reference to the RecyclerView adapter
    private MovieAdapter mMovieAdapter;

    // Reference to the loading indicator
    private ProgressBar mProgressBar;

    // Reference to the menu for keeping track of what is selected
    private Menu mMenu;

    // Set the default request type
    private final MovieRequestType mDefaultMovieRequestType = MovieRequestType.POPULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        // Do the initial setup on the RecyclerView
        configureRecyclerView();

        // ProgressBar is shown when data is loading
        mProgressBar = (ProgressBar) findViewById(R.id.pb_movies_loading);

        // Fetch the movie data
        fetchMovieData();
    }

    /**
     * Keeps track of the current request type
     * @return The currently selected MovieRequestType
     */
    private MovieRequestType getSelectedMovieRequestType() {

        // If the menu hasn't been created yet, reutrn the default request type
        if(mMenu == null) {
            return mDefaultMovieRequestType;
        }

        if(mMenu.findItem(R.id.action_popular).isChecked()) {
            return MovieRequestType.POPULAR;
        }

        if(mMenu.findItem(R.id.action_toprated).isChecked()) {
            return MovieRequestType.TOP_RATED;
        }

        return mDefaultMovieRequestType;
    }


    /**
     * Performs the initial configuration for the RecyclerView
     */
    private void configureRecyclerView() {
        // Get the reference to the RecyclerView from the layout file
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movies);

        // Depending on the screen orientation we can show a different number of columns
        int columns = getResources().getInteger(R.integer.movie_columns);

        // Initialize the layout manager and set the RecyclerView to use it
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, columns);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        // All the movie posters will be the same size
        mRecyclerView.setHasFixedSize(true);

        // Create the adapter and set it
        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);
    }

    /**
     * Kicks off the loading of movie data from The Movie Database
     */
    private void fetchMovieData() {
        FetchMovieDataTask task = new FetchMovieDataTask(this);
        task.execute(getSelectedMovieRequestType());
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

        // Set the default sort method to checked
        if(mDefaultMovieRequestType == MovieRequestType.POPULAR) {
            menu.findItem(R.id.action_popular).setChecked(true);
        }

        if(mDefaultMovieRequestType == MovieRequestType.TOP_RATED) {
            menu.findItem(R.id.action_toprated).setChecked(true);
        }

        // Store a reference to the menu so we can figure out which items are checked later
        mMenu = menu;

        return true;
    }

    /**
     * Handles selection of items in the menu. The "checkable" menu item group actually
     * handles checking if the item is already selected automatically. This prevents firing
     * off a duplicate request to the API when we have already loaded that data.
     *
     * @param item The menu item that was selected
     * @return Return false to allow normal menu processing, true to consume it here
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Popular sort method selected
        if(id == R.id.action_popular) {
            // If the popular sort order wasn't already selected then update the data
            if(getSelectedMovieRequestType() != MovieRequestType.POPULAR) {
                Log.i(TAG, "Popular was not already selected, updating data");
                item.setChecked(true);
                fetchMovieData();
            }

            return true;
        }

        // Top rated sort method selected
        if(id == R.id.action_toprated) {
            // If the top rated sort order wasn't already selected then update the data
            if(getSelectedMovieRequestType() != MovieRequestType.TOP_RATED) {
                Log.i(TAG, "Top rated was not already selected, updating data");
                item.setChecked(true);
                fetchMovieData();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handles selection of movie from the RecyclerView. Creates an intent to load the detail
     * activity and passes along the id of the movie that was selected.
     * @param movie The movie that was selected passed by the ViewHolder
     */
    @Override
    public void onClickMovie(Movie movie) {
        Intent detailIntent = new Intent(this, DetailActivity.class);
        detailIntent.putExtra(Intent.EXTRA_TEXT, movie.moviedb_id);

        startActivity(detailIntent);
    }

    /**
     * AsyncTask that fetches movie data and parses it in the background before returning
     * to the main thread to update the RecyclerView with the data.
     */
    public class FetchMovieDataTask extends AsyncTask<MovieRequestType, Void, ArrayList<Movie>> {

        // The custom URL builder requires a context to be able to pull the api key from the strings
        private Context mContext;

        /**
         * Create a new FetchMovieDataTask
         * @param context Context from the creating activity
         */
        public FetchMovieDataTask(Context context) {
            mContext = context;
        }

        /**
         * Set the visibility of the progress bar prior to executing the background task
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        /**
         * Downloads data from the Movie Database and parses it in the background
         * @param movieRequestTypes Either POPULAR or TOP_RATED movies can be fetched
         * @return Parsed ArrayList containing Movie objects for the downloaded data
         */
        @Override
        protected ArrayList<Movie> doInBackground(MovieRequestType... movieRequestTypes) {

            // Generate the request URL
            URL requestURL = null;
            MovieRequestType requestType = movieRequestTypes[0];

            if(requestType == MovieRequestType.POPULAR) {
                requestURL = NetworkUtils.buildURL(mContext, MovieRequestType.POPULAR);
            }

            if(requestType == MovieRequestType.TOP_RATED) {
                requestURL = NetworkUtils.buildURL(mContext, MovieRequestType.TOP_RATED);
            }

            try {
                // Execute the API call
                String response = NetworkUtils.getResponseFromHttpUrl(requestURL);
                Log.d(TAG, "Response: " + response);

                // Parse the JSON data into an ArrayList of Movie objects
                ArrayList<Movie> movies = MovieParser.parseMovieData(response, requestType);

                return movies;

            } catch (IOException|JSONException e) {
                Log.e(TAG, e.toString());
            }

            return null;
        }

        /**
         * Now that the data has been returned, update the RecyclerView on the main thread
         * with the new movie data
         * @param movies ArrayList of new movie data returned from the background task
         */
        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            super.onPostExecute(movies);

            mMovieAdapter.setMovieData(movies);
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }
}
