package com.backbase.weather_challenge;

import android.app.DialogFragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.backbase.weather_challenge.model.Forecast;
import com.backbase.weather_challenge.util.Bookmark;
import com.backbase.weather_challenge.util.BookmarkController;
import com.backbase.weather_challenge.util.ConnectionHelper;
import com.google.gson.Gson;

import java.util.ArrayList;


/**
 * Created by Easy Solutions on 5/13/17.
 */

public class CityForecastFragmentDialog extends DialogFragment {
    private static final String BUNDLE_BOOKMARK_INDEX = "current_bookmark_index";
    int bookmarkIndex;
    private Context myContext;
    private BookmarkController bookmarkController;
    private Gson gson = new Gson();

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    static CityForecastFragmentDialog newInstance(int bookmarkIndex) {
        CityForecastFragmentDialog dialogFragment = new CityForecastFragmentDialog();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt(BUNDLE_BOOKMARK_INDEX, bookmarkIndex);
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookmarkIndex = getArguments().getInt(BUNDLE_BOOKMARK_INDEX);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_city_forecast_dialog, container, false);
        myContext = getActivity();
        getDialog().setTitle(myContext.getString(R.string.city_forecast_dialog_title));
        bookmarkController = new BookmarkController(myContext);
        Bookmark bookmark = bookmarkController.getBookmarkByIndex(bookmarkIndex);
        loadBookmarkWeatherTask(bookmark);
        return v;
    }

    public void loadBookmarkWeatherTask(final Bookmark bookmark) {
        new AsyncTask<Bookmark, Void, Forecast>() {

            Bookmark _bookmark;

            @Override
            protected Forecast doInBackground(Bookmark... params) {
                _bookmark = params[0];
                ConnectionHelper connectionHelper = new ConnectionHelper();
                String serverResponse = connectionHelper.getWeatherServiceInfo(bookmark.latitude, bookmark.longitude, "");
                Forecast forecast = gson.fromJson(serverResponse, Forecast.class);
                return forecast;
            }

            @Override
            protected void onPostExecute(Forecast forecast) {
                super.onPostExecute(forecast);

            }
        }.execute(bookmark);
    }
}

