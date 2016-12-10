package software.level.udacity.popularmovies1;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import software.level.udacity.popularmovies1.data.Movie;
import software.level.udacity.popularmovies1.data.MovieParser;
import software.level.udacity.popularmovies1.utilities.MovieRequestType;
import software.level.udacity.popularmovies1.utilities.NetworkUtils;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();

    private int mMovieId;
    private TextView mTitleTextView;
    private ImageView mPosterImageView;
    private TextView mYearTextView;
    private TextView mRunTime;
    private TextView mRating;
    private TextView mOverview;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Get references for all the views
        mTitleTextView = (TextView)findViewById(R.id.tv_movie_title_detail);
        mYearTextView = (TextView)findViewById(R.id.tv_movie_year);
        mRunTime = (TextView)findViewById(R.id.tv_movie_runtime);
        mRating = (TextView)findViewById(R.id.tv_movie_rating);
        mOverview = (TextView)findViewById(R.id.tv_movie_overview);

        mPosterImageView = (ImageView)findViewById(R.id.iv_movie_poster_detail);

        // Extract the id of the movie from the passed extra text
        Intent startingIntent = getIntent();

        if(startingIntent != null) {
            if(startingIntent.hasExtra(Intent.EXTRA_TEXT)) {
                mMovieId = startingIntent.getIntExtra(Intent.EXTRA_TEXT, 0);
            }
        }

        FetchMovieDataTask task = new FetchMovieDataTask(this);
        task.execute();
    }

    /**
     * AsyncTask that fetches movie data and parses it in the background before returning
     * to the main thread to update the RecyclerView with the data.
     */
    public class FetchMovieDataTask extends AsyncTask<Void, Void, Movie> {

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
            //mProgressBar.setVisibility(View.VISIBLE);
        }

        /**
         * Downloads movie detail data
         * @param voids No parameters are passed to the task
         * @return The movie object with all details
         */
        @Override
        protected Movie doInBackground(Void... voids) {

            URL requestURL = NetworkUtils.buildURL(mContext, MovieRequestType.DETAILS, mMovieId);

            try {
                // Execute the API call
                String response = NetworkUtils.getResponseFromHttpUrl(requestURL);
                Log.d(TAG, "Response: " + response);

                // Parse the JSON data into an ArrayList of Movie objects
                ArrayList<Movie> movies = MovieParser.parseMovieData(response, MovieRequestType.DETAILS);

                Movie movie = null;
                try {
                    movie = movies.get(0);
                } catch (IndexOutOfBoundsException e) {
                    Log.e(TAG, e.toString());
                }

                return movie;

            } catch (IOException |JSONException e) {
                Log.e(TAG, e.toString());
            }

            return null;
        }

        /**
         * Now that the data has been returned, update the view with all the data
         * @param movie Movie data returned from the background task
         */
        @Override
        protected void onPostExecute(Movie movie) {
            super.onPostExecute(movie);

            //mProgressBar.setVisibility(View.INVISIBLE);

            if(movie != null) {
                /* This is not the most efficient implementation since we already have a lot of the
                   data from the other activity and it should just be passed in a parcelable object.
                 */
                String imageSize = mContext.getResources().getString(R.string.api_image_size);

                URL imageURL = NetworkUtils.buildImageURL(movie.poster_path, imageSize);

                Picasso.with(mContext)
                        .load(imageURL.toString())
                        .placeholder(R.drawable.poster_placeholder)
                        .into(mPosterImageView);

                String rating = String.valueOf(movie.vote_average) + "/10";
                String year = movie.release_date.split("-")[0];
                String runTime = String.valueOf(movie.runtime) + " min";

                mTitleTextView.setText(movie.title);
                mYearTextView.setText(year);
                mRunTime.setText(runTime);
                mRating.setText(rating);
                mOverview.setText(movie.overview);
            }

        }
    }


}
