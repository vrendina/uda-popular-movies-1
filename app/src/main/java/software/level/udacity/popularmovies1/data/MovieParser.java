package software.level.udacity.popularmovies1.data;

import android.util.Log;

import java.util.ArrayList;


/**
 * Utilities for parsing the JSON data returned from The Movie Database. Right now this class
 * does not do any error checking on the JSON to make sure we got a valid response.
 */
public class MovieParser {

    public static final String TAG = MovieParser.class.getSimpleName();

    /**
     * Parses JSON data from any Movie Database request any creates an ArrayList that contains
     * all of the movies. If a details for a single movie are request then the ArrayList will
     * only contain a single Movie object.
     * @param jsonData Data returned from the API call
     * @return ArrayList containing parsed Movie objects
     */
    public static ArrayList<Movie> parseMovieData(String jsonData) {
        ArrayList<Movie> movieData = new ArrayList<>();

        Log.d(TAG, "Parsing JSON data ---------------------");
        Log.d(TAG, jsonData);
        Log.d(TAG, "---------------------------------------");

        return movieData;
    }
}
