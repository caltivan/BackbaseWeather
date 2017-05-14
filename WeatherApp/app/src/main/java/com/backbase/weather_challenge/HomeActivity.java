package com.backbase.weather_challenge;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.backbase.weather_challenge.util.Bookmark;

public class HomeActivity extends AppCompatActivity implements BookmarkMapFragment.OnFragmentInteractionListener, BookmarkListFragment.OnListFragmentInteractionListener {

    private BookmarkMapFragment bookmarkMapFragment;
    private BookmarkListFragment bookmarkListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bookmarkMapFragment = (BookmarkMapFragment) getSupportFragmentManager().findFragmentById(R.id.bookmarkMapFragment);
        bookmarkListFragment = (BookmarkListFragment) getSupportFragmentManager().findFragmentById(R.id.bookmarkListFragment);


    }

    @Override
    public void onMapFragmentMapTouch(Bookmark bookmark) {
        bookmarkListFragment.loadBookmarkListTask();
    }

    @Override
    public void onSelectBookmarkFromList(Bookmark bookmark) {
        bookmarkMapFragment.focusBookmarkMapMark(bookmark);

    }

    @Override
    public void onRemoveBookmarkFromList(Bookmark bookmark, int index) {
        bookmarkListFragment.removeBookmarkFromList(index);
        bookmarkMapFragment.loadBookmarkMapMarks();
        bookmarkMapFragment.focusMapPosition(bookmark.latitude,bookmark.longitude);

    }
}
