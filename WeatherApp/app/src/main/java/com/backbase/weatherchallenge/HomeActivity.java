package com.backbase.weatherchallenge;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.backbase.weatherchallenge.dummy.DummyContent;
import com.google.android.gms.maps.model.LatLng;

public class HomeActivity extends AppCompatActivity implements MapWeatherFragment.OnFragmentInteractionListener, BookmarkWeatherFragment.OnListFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    @Override
    public void onMapFragmentMapTouch(LatLng point) {

    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}
