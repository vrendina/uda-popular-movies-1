package software.level.udacity.popularmovies1.utilities;

/**
 * Type of requests that can be made to the Movie Database API.
 */
public enum MovieRequestType {
    /**
     * Request popular movies
     */
    POPULAR,

    /**
     * Request top rated movies
     */
    TOP_RATED,

    /**
     * Request details about a specific movie id
     */
    DETAILS

}
