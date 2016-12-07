package software.level.udacity.popularmovies1;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Adapter used by the RecyclerView to display the grid layout
 * of movie posters.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    /**
     * Create the MovieAdapter
     */
    public MovieAdapter() {

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
        holder.mTextView.setText(String.valueOf(position));
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {
        return 25;
    }

    /**
     * Viewholder class to store references to recycled views.
     */
    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder {

        // Holds a reference to the ImageView that contains the movie poster
        public final ImageView mImageView;

        // Temporary TextView that is used for testing
        public final TextView mTextView;

        /**
         * Constructor for the view holder that creates references to the views within
         * the parent view that will be recycled
         * @param view The parent view that holds all the children that are being recycled
         */
        public MovieAdapterViewHolder(View view) {
            super(view);
            mImageView = (ImageView) view.findViewById(R.id.iv_movie_poster_grid);
            mTextView = (TextView) view.findViewById(R.id.tv_movie_poster_grid);
        }
    }
}
