package com.backbase.weather_challenge.view;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.backbase.weather_challenge.R;
import com.backbase.weather_challenge.model.Bookmark;
import com.backbase.weather_challenge.controller.BookmarkController;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BookmarkMapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BookmarkMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookmarkMapFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final String TAG = BookmarkMapFragment.class.getSimpleName();
    private static final float DEFAULT_ZOOM = 5;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private Context myContext;
    private View content;
    private MapView mapView;
    private GoogleMap mMap;
    private boolean mLocationPermissionGranted = false;
    private Location mLastKnownLocation;
    private GoogleApiClient mGoogleApiClient;
    private CameraPosition mCameraPosition;
    private BookmarkController bookmarkController;

    public BookmarkMapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BookmarkMapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookmarkMapFragment newInstance(String param1, String param2) {
        BookmarkMapFragment fragment = new BookmarkMapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myContext = this.getContext();
        bookmarkController = new BookmarkController(myContext);
        // Inflate the layout for this fragment
        content = inflater.inflate(R.layout.fragment_map_weather, container, false);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapView = (MapView) content.findViewById(R.id.mapView);

        // Build the Play services client for use by the Fused Location Provider and the Places API.
        // Use the addApi() method to request the Google Places API and the Fused Location Provider.
        mGoogleApiClient = new GoogleApiClient.Builder(myContext)
                .enableAutoManage(getActivity(), this/* OnConnectionFailedListener */)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mGoogleApiClient.connect();
        return content;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        // Turn on the My Location layer and the related control on the map.
        //updateLocationUI();
        // Get the current location of the device and set the position of the map.
        getDeviceMarkingLocation();
        mapTouchConfiguration();

    }

    private void mapTouchConfiguration() {
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                Bookmark bookmark = bookmarkController.createBookmarkFromPosition(point.latitude, point.longitude);
                String markTitle = String.format("%s-%s", bookmark.country, bookmark.city);
                MarkerOptions marker = new MarkerOptions().position(point).title(markTitle);
                mMap.addMarker(marker).showInfoWindow();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, DEFAULT_ZOOM));
                bookmarkController.saveBookmark(bookmark);
                if (mListener != null) {
                    mListener.onMapFragmentMapTouch(bookmark);
                }
            }
        });
    }


    private void getDeviceMarkingLocation() {
        // User real time localization permission
        if (ContextCompat.checkSelfPermission(myContext, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        // Get current location
        if (mLocationPermissionGranted) {
            mLastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }

        // Set the map's camera position to the current location of the device.
        if (mCameraPosition != null) {
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        } else if (mLastKnownLocation != null) {
            Bookmark bookmark = bookmarkController.createBookmarkFromPosition(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
            String markTitle = String.format("%s-%s", bookmark.country, bookmark.city);
            LatLng mLocation = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLocation, DEFAULT_ZOOM));
            // mMap.addMarker(new MarkerOptions().position(mLocation).title(markTitle)).showInfoWindow();
        } else {
            Log.d(TAG, "Current location is null. Using defaults.");
            LatLng mDefaultLocation = new LatLng(-34, 151);
            MarkerOptions defaultMarker = new MarkerOptions().position(mDefaultLocation).title("Marker in Sydney");
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
            //mMap.addMarker(defaultMarker).showInfoWindow();
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
        loadBookmarkMapMarks();

    }

    public void loadBookmarkMapMarks() {
        ArrayList<Bookmark> bookmarkList = bookmarkController.getBookmarks();
        mMap.clear();
        for (Bookmark bookmark : bookmarkList) {
            String markTitle = String.format("%s-%s", bookmark.country, bookmark.city);
            LatLng mLocation = new LatLng(bookmark.latitude, bookmark.longitude);
            MarkerOptions marker = new MarkerOptions().position(mLocation).title(markTitle);
            mMap.addMarker(marker).showInfoWindow();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        // updateLocationUI();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mapView.onCreate(bundle);
        mapView.onResume();
        mapView.getMapAsync(this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void focusBookmarkMapMark(Bookmark bookmark) {
        String markTitle = String.format("%s-%s", bookmark.country, bookmark.city);
        LatLng mLocation = new LatLng(bookmark.latitude, bookmark.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLocation, DEFAULT_ZOOM));
        MarkerOptions marker = new MarkerOptions().position(mLocation).title(markTitle);
        mMap.addMarker(marker).showInfoWindow();
    }

    public void focusMapPosition(double latitude, double longitude) {
        LatLng mLocation = new LatLng(latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLocation, DEFAULT_ZOOM));
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onMapFragmentMapTouch(Bookmark bookmark);
    }

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }
}
