package com.backbase.weather_challenge.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;

import com.backbase.weather_challenge.R;
import com.backbase.weather_challenge.model.Bookmark;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Created by JGomez on 5/13/17.
 * Controller to save the current user selected bookmarks
 */
public class BookmarkController {
    private static final String TAG = BookmarkController.class.getSimpleName();
    private static final String PREFERENCES_NAME = "bookmarks_preference";
    private static final String PREF_BOOKMARK_HISTORY = "pref_bookmarks_history";

    private final Context myContext;
    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;
    private Gson gson;

    public BookmarkController(Context context) {
        super();
        myContext = context;
        gson = new Gson();

        prefs = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public Bookmark createBookmarkFromPosition(double latitude, double longitude) {
        String country = myContext.getString(R.string.unknown_mark);
        String city = myContext.getString(R.string.unknown_mark);
        Geocoder gcd = new Geocoder(myContext, Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                country = addresses.get(0).getCountryName();
                if (city != null && !city.equals("null")) {
                    city = addresses.get(0).getLocality();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bookmark bookmark = new Bookmark();
        bookmark.country = country;
        bookmark.city = city;
        bookmark.latitude = latitude;
        bookmark.longitude = longitude;
        return bookmark;
    }

    public void saveBookmark(Bookmark bookmark) {
        ArrayList<Bookmark> bookmarksList = getBookmarks();
        if (!bookmarksList.contains(bookmark)) {
            bookmarksList.add(bookmark);
            String bookmarkListJson = gson.toJson(bookmarksList);
            editor.putString(PREF_BOOKMARK_HISTORY, bookmarkListJson);
            editor.commit();
        }
    }

    public void removeBookmark(int index) {
        ArrayList<Bookmark> bookmarksList = getBookmarks();
        bookmarksList.remove(index);
        String bookmarkListJson = gson.toJson(bookmarksList);
        editor.putString(PREF_BOOKMARK_HISTORY, bookmarkListJson);
        editor.commit();
    }


    public ArrayList<Bookmark> getBookmarks() {
        String bookmarkJson = prefs.getString(PREF_BOOKMARK_HISTORY, null);
        Type type = new TypeToken<ArrayList<Bookmark>>() {
        }.getType();
        ArrayList<Bookmark> bookmarks = new ArrayList<>();
        if (bookmarkJson != null) {
            bookmarks = gson.fromJson(bookmarkJson, type);
        }
        return bookmarks;
    }

    public Bookmark getBookmarkByIndex(int index) {
        ArrayList<Bookmark> bookmarks = getBookmarks();
        Bookmark bookmark = bookmarks.get(index);
        return bookmark;
    }

}
