package software.level.udacity.popularmovies1;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

import software.level.udacity.popularmovies1.data.Movie;
import software.level.udacity.popularmovies1.utilities.NetworkUtils;

/**
 * Adapter used by the RecyclerView to display the grid layout
 * of movie posters.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();

    // ArrayList that holds all of the movie data in Movie objects
    private ArrayList<Movie> mMovieData;

    // Reference to the onClickHandler class that handles selection of movies
    private MovieOnClickHandler mMovieOnClickHandler;

    /**
     * Create the MovieAdapter
     * @param movieOnClickHandler Class that implements the click handling interface
     */
    public MovieAdapter(MovieOnClickHandler movieOnClickHandler) {
        mMovieOnClickHandler = movieOnClickHandler;
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param parent The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new ForecastAdapterViewHolder that holds the View for each list item
     */
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // Inflate the layout for the parent view that contains the child views that are recycled
        View view =  inflater.inflate(R.layout.movie_grid_item, parent, false);

        // Return the view holder with the inflated view
        return new MovieAdapterViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position.
     *
     * @param holder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        Movie movie = mMovieData.get(position);
        ImageView imageView = holder.mImageView;

        // String resource holds the api image size
        String imageSize = imageView.getResources().getString(R.string.api_image_size);

        // Load the poster image when we bind the view
        URL imageURL = NetworkUtils.buildImageURL(movie.poster_path, imageSize);

        Picasso.with(holder.mImageView.getContext())
                .load(imageURL.toString())
                .placeholder(R.drawable.poster_placeholder)
                .into(holder.mImageView);
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {
        if(mMovieData == null) {
            return 0;
        }
        return mMovieData.size();
    }

    /**
     * Set the movie data and refresh the ViewHolder to display the new data
     * @param movieData An ArrayList of Movie objects
     */
    public void setMovieData(ArrayList<Movie> movieData) {
        mMovieData = movieData;
        notifyDataSetChanged();
    }

    /**
     * Viewholder class to store references to recycled views. Class also passes along the onClick
     * event.
     */
    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Holds a reference to the ImageView that contains the movie poster
        public final ImageView mImageView;

        /**
         * Constructor for the view holder that creates references to the views within
         * the parent view that will be recycled
         * @param view The parent view that holds all the children that are being recycled
         */
        public MovieAdapterViewHolder(View view) {
            super(view);
            mImageView = (ImageView) view.findViewById(R.id.iv_movie_poster_grid);

            // Set the onClickListener to the ViewHolder
            view.setOnClickListener(this);
        }

        /**
         * Handles onClick events for items in the RecyclerView
         * @param view The view that was clicked on
         */
        @Override
        public void onClick(View view) {
            // Retrieve the Movie object that was selected
            int postion = getAdapterPosition();
            Movie selectedMovie = mMovieData.get(postion);

            // Call the click handler with the selected movie object
            mMovieOnClickHandler.onClickMovie(selectedMovie);
        }
    }

    /**
     * Interface that defines what a movie click handler object should implement
     */
    public interface MovieOnClickHandler {
        void onClickMovie(Movie movie);
    }
}
