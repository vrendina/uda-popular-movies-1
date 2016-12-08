package software.level.udacity.popularmovies1.data;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import software.level.udacity.popularmovies1.utilities.MovieRequestType;


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
    public static ArrayList<Movie> parseMovieData(String jsonData, MovieRequestType requestType) throws JSONException {
        ArrayList<Movie> movieData = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(jsonData);

        if(requestType == MovieRequestType.POPULAR || requestType == MovieRequestType.TOP_RATED) {

            // If the data contains a "results" item then we will parse the array of movie data
            if(jsonObject.has("results")) {
                JSONArray results = jsonObject.getJSONArray("results");

                // Create a Movie object for each result and add it to the ArrayList
                for(int i = 0; i < results.length(); i++) {
                    JSONObject result = results.getJSONObject(i);

                    Movie movie = new Movie();
                    movie.title = result.getString("title");
                    movie.poster_path = result.getString("poster_path");
                    movie.backdrop_path = result.getString("backdrop_path");
                    movie.moviedb_id = result.getInt("id");
                    movie.popularity = result.getDouble("popularity");
                    movie.vote_average = result.getDouble("vote_average");
                    movie.vote_count = result.getInt("vote_count");
                    movie.overview = result.getString("overview");

                    movieData.add(movie);
                }
            }
        }

        return movieData;
    }

}
