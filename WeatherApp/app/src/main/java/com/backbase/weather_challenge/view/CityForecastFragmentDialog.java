package com.backbase.weather_challenge.view;

import android.app.DialogFragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.backbase.weather_challenge.R;
import com.backbase.weather_challenge.model.Forecast;
import com.backbase.weather_challenge.model.Bookmark;
import com.backbase.weather_challenge.controller.BookmarkController;
import com.backbase.weather_challenge.util.ConnectionHelper;
import com.google.gson.Gson;


/**
 * Created by JGomez on 5/13/17.
 */

public class CityForecastFragmentDialog extends DialogFragment {
    private static final String BUNDLE_BOOKMARK_INDEX = "current_bookmark_index";
    int bookmarkIndex;
    private Context myContext;
    private BookmarkController bookmarkController;
    private Gson gson = new Gson();
    private TextView ciyTextView;
    private TextView countryTextView;
    private TextView temperatureTextView;
    private TextView weatherDescriptionTextView;

    private TextView humidityTextView;
    private TextView pressureTextView;
    private TextView windSpeedTextView;
    private TextView windDegTextView;
    private ProgressBar weatherProgressBar;


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
        View content = inflater.inflate(R.layout.fragment_dialog_city_forecast, container, false);
        myContext = getActivity();
        getDialog().setTitle(myContext.getString(R.string.city_forecast_dialog_title));
        ciyTextView = (TextView) content.findViewById(R.id.cityTextView);
        countryTextView = (TextView) content.findViewById(R.id.countryTextView);
        temperatureTextView = (TextView) content.findViewById(R.id.temperatureTextView);
        weatherDescriptionTextView = (TextView) content.findViewById(R.id.weatherDescriptionTextView);

        // Weather values
        humidityTextView = (TextView) content.findViewById(R.id.humidityTextView);
        pressureTextView = (TextView) content.findViewById(R.id.pressureTextView);

        // Wind values
        windSpeedTextView = (TextView) content.findViewById(R.id.windSpeedTextView);
        windDegTextView = (TextView) content.findViewById(R.id.windDegTextView);
        //
        weatherProgressBar = (ProgressBar) content.findViewById(R.id.weatherProgressBar);

        bookmarkController = new BookmarkController(myContext);
        Bookmark bookmark = bookmarkController.getBookmarkByIndex(bookmarkIndex);
        String city = bookmark.city;
        String country = bookmark.country;

        ciyTextView.setText(city);
        countryTextView.setText(country);

        loadBookmarkWeatherTask(bookmark);
        return content;
    }

    public void loadBookmarkWeatherTask(final Bookmark bookmark) {
        new AsyncTask<Bookmark, Void, Forecast>() {

            Bookmark _bookmark;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                weatherProgressBar.setVisibility(View.VISIBLE);
            }

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
                // Main Weather values setup
                String temperature = String.format("%sº", forecast.main.temp);
                String weatherDescription = forecast.weather.get(0).description;

                temperatureTextView.setText(temperature);
                weatherDescriptionTextView.setText(weatherDescription);

                // Main Weather values setup
                String humidity = String.format("%s", forecast.main.humidity);
                String pressure = String.format("%s", forecast.main.pressure);

                humidityTextView.setText(humidity);
                pressureTextView.setText(pressure);

                // Wind values
                String windSpeed = String.format("%s", forecast.wind.speed);
                String windDeg = String.format("%s", forecast.wind.deg);
                windSpeedTextView.setText(windSpeed);
                windDegTextView.setText(windDeg);

                //

                weatherProgressBar.setVisibility(View.GONE);


            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, bookmark);
    }
}

