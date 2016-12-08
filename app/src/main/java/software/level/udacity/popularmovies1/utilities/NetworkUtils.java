package software.level.udacity.popularmovies1.utilities;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import software.level.udacity.popularmovies1.R;


/**
 * Utilities that will be used to communicate with The Movie Database
 * to retrieve information about popular or top rated movies
 */
public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    // Base scheme for the Uri builder
    private static final String API_SCHEME = "https";

    // Base Uri for the Movie Database API
    private static final String API_AUTHORITY = "api.themoviedb.org";

    // Version string appended to the path of the Movie Database API
    private static final String API_VERSION = "3";

    // Path appended to all movie requests
    private static final String API_PATH = "movie";

    // Language code used for requests
    private static final String API_LANGUAGE = "en-US";

    // Base Uri for the images
    private static final String IMAGE_AUTHORITY = "image.tmdb.org";


    /**
     * Create the base Uri for any API request
     *
     * @return Uri.Builder that can be appended to complete the Uri
     */
    private static Uri.Builder buildBaseUri() {
        Uri.Builder builder = new Uri.Builder();

        builder.scheme(API_SCHEME)
                .authority(API_AUTHORITY)
                .appendPath(API_VERSION)
                .appendPath(API_PATH);

        return builder;
    }

    /**
     * Creates the request URL for the request type. This should only be used
     * to retrieve popular movies and top rated movies not for detail requests.
     *
     * @param context Context for calling activity
     * @param requestType MovieRequestType
     * @return URL to call for API request
     */
    public static URL buildURL(Context context, MovieRequestType requestType) {
        return buildURL(context, requestType, 0);
    }

    /**
     * Creates the request URL for the given request type.
     *
     * @param context Context for calling activity
     * @param requestType MovieRequestType
     * @param id For detail requests, id of movie
     * @return URL to call for API request
     */
    public static URL buildURL(Context context, MovieRequestType requestType, int id) {

        Uri.Builder builder = buildBaseUri();

        switch(requestType) {
            case POPULAR:
                builder.appendPath("popular");
                break;

            case TOP_RATED:
                builder.appendPath("top_rated");
                break;

            case DETAILS:
                builder.appendPath(String.valueOf(id));
                break;
        }

        Uri uri = builder.appendQueryParameter("language", API_LANGUAGE)
                .appendQueryParameter("api_key", context.getString(R.string.API_KEY))
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, "Problem with constructing URL -- " + uri.toString());
        }

        Log.d(TAG, url.toString());

        return url;
    }

    /**
     * Generates a URL for downloading an image from The Movie DB image service
     * @param path API supplied poster_path or backdrop_path
     * @param size Size of image required (w92, w154, w185, w342, w500, w780, original)
     * @return URL for downloading image
     */
    public static URL buildImageURL(String path, String size) {
        // Strip any preceeding slash out of the path
        path = path.replace("/", "");

        Uri.Builder builder = new Uri.Builder();

        builder.scheme(API_SCHEME)
                .authority(IMAGE_AUTHORITY)
                .appendPath("t")
                .appendPath("p")
                .appendPath(size)
                .appendPath(path);

        Uri uri = builder.build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, "Problem with constructing URL -- " + url.toString());
        }

        Log.d(TAG, url.toString());

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}


