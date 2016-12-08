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
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;

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

    // Holds a reference to the RecyclerView that holds all the movie posters
    private RecyclerView mRecyclerView;

    // Holds a reference to the RecyclerView adapter
    private MovieAdapter mMovieAdapter;

    // Holds a reference to the loading indicator
    private ProgressBar mProgressBar;

    // Hold the current movie request type, by default we will get popular movies
    private MovieRequestType mCurrentMovieRequestType = MovieRequestType.POPULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        // Do the initial setup on the RecyclerView
        configureRecyclerView();

        // Picasso.with(context).load("http://i.imgur.com/DvpvklR.png").into(imageView);

        // Fetch the movie data
        fetchMovieData();
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
     * Kicks off the loading of movie data from The Movie Database
     */
    private void fetchMovieData() {
        FetchMovieDataTask task = new FetchMovieDataTask(this);

        task.execute(mCurrentMovieRequestType);
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
            // If the popular sort order wasn't already selected then update the data
            if(mCurrentMovieRequestType != MovieRequestType.POPULAR) {
                Log.i(TAG, "Popular was not already selected, updating data");
                mCurrentMovieRequestType = MovieRequestType.POPULAR;
                fetchMovieData();
            }

            return true;
        }

        // Top rated sort method selected
        if(id == R.id.action_toprated) {
            // If the top rated sort order wasn't already selected then update the data
            if(mCurrentMovieRequestType != MovieRequestType.TOP_RATED) {
                Log.i(TAG, "Top rated was not already selected, updating data");
                mCurrentMovieRequestType = MovieRequestType.TOP_RATED;
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
        detailIntent.putExtra(Intent.EXTRA_TEXT, "id");

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

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
        }
    }
}
