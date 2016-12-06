package software.level.udacity.popularmovies1;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import static android.content.ContentValues.TAG;

/**
 * Utilities that will be used to communicate with The Movie Database
 * to retrieve information about popular or top rated movies
 */
public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static String API_SCHEME = "https";
    private static String API_AUTHORITY = "api.themoviedb.org";
    private static String API_VERSION = "3";

    private static Uri getBaseUri() {
        Uri.Builder builder = new Uri.Builder();

        builder.scheme(API_SCHEME)
                .authority(API_AUTHORITY)
                .appendPath(API_VERSION);

        return builder.build();
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


