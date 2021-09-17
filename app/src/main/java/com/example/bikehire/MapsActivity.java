package com.example.bikehire;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationProvider;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.internal.OnConnectionFailedListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener, ActivityCompat.OnRequestPermissionsResultCallback{

    // public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    SearchView searchview;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean permissionDenied = false;

    Location myLocation = null;
    Location destinationLocation = null;
    protected LatLng start = null;
    protected LatLng end = null;

    //to get location permissions.
    private final static int LOCATION_REQUEST_CODE = 23;
    boolean locationPermission = false;

    //polyline object
    private List<Polyline> polylines = null;

    //private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // fusedLocationProviderClient = LocationProvider.getFusedLocationProviderClient()
        searchview = findViewById(R.id.sv_location);
        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchview.getQuery().toString();
                List<Address> addressList = null;
                if (location != null || !location.equals("")) {
                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    mGoogleMap.addMarker(new MarkerOptions().position(latLng).title(location));
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        //  checkMyPermissions();
        initMap();


    }

    private void initMap() {


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFrag = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
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
        mGoogleMap = googleMap;
        mGoogleMap.setOnMyLocationButtonClickListener(this);
        mGoogleMap.setOnMyLocationClickListener(this);
        enableLocation();
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        LatLng kingongo = new LatLng(-0.412284, 36.950513);
        googleMap.addMarker(new MarkerOptions()
                .position(kingongo)
                .title("Kingongo")
                .snippet("23 max biking stations")
                .icon(bitmapDescriptorfromVector(getApplicationContext(), R.drawable.ic_baseline_electric_bike_24)));

        LatLng kingongo2 = new LatLng(-0.413490, 36.955249);
        googleMap.addMarker(new MarkerOptions()
                .position(kingongo2)
                .title("Kingongo2")
                .snippet("23 max biking stations")
                .icon(bitmapDescriptorfromVector(getApplicationContext(), R.drawable.ic_baseline_electric_bike_24)));

        LatLng kingongo3 = new LatLng(-0.409058, 36.945765);
        googleMap.addMarker(new MarkerOptions()
                .position(kingongo3)
                .title("Kingongo3")
                .snippet("23 max biking stations")
                .icon(bitmapDescriptorfromVector(getApplicationContext(), R.drawable.ic_baseline_electric_bike_24)));

        LatLng kingongo4 = new LatLng(-0.412284, 36.950513);
        googleMap.addMarker(new MarkerOptions()
                .position(kingongo4)
                .title("Kingongo4")
                .snippet("23 max biking stations")
                .icon(bitmapDescriptorfromVector(getApplicationContext(), R.drawable.ic_baseline_electric_bike_24)));

        LatLng kingongo5 = new LatLng(-0.419200, 36.948663);
        googleMap.addMarker(new MarkerOptions()
                .position(kingongo5)
                .title("Kingongo5")
                .snippet("23 max biking stations")
                .icon(bitmapDescriptorfromVector(getApplicationContext(), R.drawable.ic_baseline_electric_bike_24)));

        LatLng kingongo6 = new LatLng(-0.421277, 36.950132);
        googleMap.addMarker(new MarkerOptions()
                .position(kingongo6)
                .title("Kingongo6")
                .snippet("23 max biking stations")
                .icon(bitmapDescriptorfromVector(getApplicationContext(), R.drawable.ic_baseline_electric_bike_24)));

        LatLng kingongo7 = new LatLng(-0.422756, 36.949778);
        googleMap.addMarker(new MarkerOptions()
                .position(kingongo7)
                .title("Kingongo7")
                .snippet("23 max biking stations")
                .icon(bitmapDescriptorfromVector(getApplicationContext(), R.drawable.ic_baseline_electric_bike_24)));

        LatLngBounds australiaBounds = new LatLngBounds(
                new LatLng(-1.29207, 36), // SW bounds
                new LatLng(-1, 38)  // NE bounds
        );
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(australiaBounds.getCenter(), 10));


        googleMap.moveCamera(CameraUpdateFactory.newLatLng(kingongo));
        //get destination location when user click on map
        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                end = latLng;

                mGoogleMap.clear();

                start = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());

            }
        });
        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {

                startActivity( new Intent(MapsActivity.this, HireNow.class));
                return false;
            }
        });
     


    }

    private BitmapDescriptor bitmapDescriptorfromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);


    }


    private void enableLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (mGoogleMap != null) {
                mGoogleMap.setMyLocationEnabled(true);
            }


        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "location button clicked", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "location is" + location, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;

        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableLocation();
        } else {
            // Permission was denied. Display an error message
            // [START_EXCLUDE]
            // Display the missing permission error dialog when the fragments resume.
            permissionDenied = true;
            // [END_EXCLUDE]
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (permissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            permissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }


}

