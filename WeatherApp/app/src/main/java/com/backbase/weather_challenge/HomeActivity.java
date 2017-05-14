package com.backbase.weather_challenge;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.backbase.weather_challenge.util.Bookmark;

public class HomeActivity extends AppCompatActivity implements BookmarkMapFragment.OnFragmentInteractionListener, BookmarkListFragment.OnListFragmentInteractionListener {

    private BookmarkMapFragment bookmarkMapFragment;
    private BookmarkListFragment bookmarkListFragment;
    private int mStackLevel = 0;

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
        showDialog();

    }


    @Override
    public void onRemoveBookmarkFromList(Bookmark bookmark, int index) {
        bookmarkListFragment.removeBookmarkFromList(index);
        bookmarkMapFragment.loadBookmarkMapMarks();
        bookmarkMapFragment.focusMapPosition(bookmark.latitude, bookmark.longitude);

    }

    public void showDialog() {
        mStackLevel++;

        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        // Create and show the dialog.
        CityForecastFragmentDialog newFragment = CityForecastFragmentDialog.newInstance(mStackLevel);
        newFragment.show(ft, "dialog");
    }
}
