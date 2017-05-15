package com.backbase.weather_challenge.view;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.backbase.weather_challenge.R;
import com.backbase.weather_challenge.model.Bookmark;

public class HomeActivity extends AppCompatActivity implements BookmarkMapFragment.OnFragmentInteractionListener, BookmarkListFragment.OnListFragmentInteractionListener {

    private BookmarkMapFragment bookmarkMapFragment;
    private BookmarkListFragment bookmarkListFragment;
    private Context myContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        myContext = this;
        bookmarkMapFragment = (BookmarkMapFragment) getSupportFragmentManager().findFragmentById(R.id.bookmarkMapFragment);
        bookmarkListFragment = (BookmarkListFragment) getSupportFragmentManager().findFragmentById(R.id.bookmarkListFragment);


    }

    @Override
    public void onMapFragmentMapTouch(Bookmark bookmark) {
        bookmarkListFragment.loadBookmarkListTask();
    }

    @Override
    public void onSelectBookmarkFromList(Bookmark bookmark, int index) {
        bookmarkMapFragment.focusBookmarkMapMark(bookmark);
        showCityForecastDialog(index);

    }


    @Override
    public void onRemoveBookmarkFromList(Bookmark bookmark, int index) {
        bookmarkListFragment.removeBookmarkFromList(index);
        bookmarkMapFragment.loadBookmarkMapMarks();
        bookmarkMapFragment.focusMapPosition(bookmark.latitude, bookmark.longitude);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_help) {

            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void showCityForecastDialog(int index) {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        String dialogTitle = myContext.getString(R.string.city_forecast_dialog_title);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag(dialogTitle);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        // Create and show the dialog.
        CityForecastFragmentDialog newFragment = CityForecastFragmentDialog.newInstance(index);
        newFragment.show(ft, dialogTitle);
    }
}
