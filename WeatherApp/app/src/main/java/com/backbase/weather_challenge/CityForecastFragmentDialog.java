package com.backbase.weather_challenge;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by Easy Solutions on 5/13/17.
 */

public class CityForecastFragmentDialog extends DialogFragment {
    int mNum;
    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    static CityForecastFragmentDialog newInstance(int num) {
        CityForecastFragmentDialog f = new CityForecastFragmentDialog();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNum = getArguments().getInt("num");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_city_forecast_dialog, container, false);
        return v;
    }
}

