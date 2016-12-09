# Popular Movies (Part 1)

The purpose of this application is to demonstrate some of the fundamental skills learned in the first part of the Udacity Android Developer Nanodegree program. The application pulls data about popular movies from The Movie Database API (https://www.themoviedb.org/documentation/api) and parses the returned JSON into an Android Recyclerview. The Picasso library is used to retrieve movie poster images and load them into a GridLayout. Selecting a movie poster creates an intent that passes data to a detail activity where addition data about the movie is retrieved. 

### Usage
In order for this application to work properly, a new string resource file `key.xml` should be created in the `res/values` folder. The value of `API_KEY` should be set to your API key assigned by The Movie Database.

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="API_KEY">[insert MovieDB API key here]</string>
</resources>
```
