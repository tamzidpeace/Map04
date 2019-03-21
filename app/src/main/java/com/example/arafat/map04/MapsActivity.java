package com.example.arafat.map04;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private static final String TAG = "MapsActivity";
    int checkTime = 0;
    //new
    //private GoogleMap mMap;
    View mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ///code hack for myLocation Button position
        View locationButton = ((View) mapFragment.getView().findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();

        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);

        rlp.addRule(RelativeLayout.ALIGN_PARENT_END, 0);
        rlp.addRule(RelativeLayout.ALIGN_END, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        rlp.setMargins(30, 0, 0, 40);
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
    public void onMapReady(final GoogleMap googleMap) {

        //googleMap = mapView.getMap();
        mMap = googleMap;


        // location manager and permission check

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Log.d(TAG, "onLocationChanged: " + location.getAltitude());
                double lat = location.getLatitude();
                double lng = location.getLongitude();
                final LatLng myLocation = new LatLng(lat, lng);
                mMap.clear();
                //mMap.addMarker(new MarkerOptions().position(myLocation));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
                mMap.setBuildingsEnabled(true);

                //adding circle
                mMap.addCircle(new CircleOptions()
                        .center(new LatLng(location.getLatitude(), location.getLongitude()))
                        .radius(40)
                        .strokeColor(Color.WHITE)
                        .fillColor(Color.GREEN));

                if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                // it will enable my location button

                mMap.setMyLocationEnabled(true);



                //styling map
                MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(MapsActivity.this,
                        R.raw.style_json);
                mMap.setMapStyle(style);

                mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                    @Override
                    public boolean onMyLocationButtonClick() {
                        //Toast.makeText(MapsActivity.this, "Hello, World!", Toast.LENGTH_SHORT).show();

                        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

                        // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(myLocation)      // Sets the center of the map to Mountain View
                                .zoom(17)                   // Sets the zoom
                                .bearing(90)                // Sets the orientation of the camera to east
                                .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                                .build();                   // Creates a CameraPosition from the builder

                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));



                        return true;
                    }
                });


                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                try {
                    List<Address> addressList = geocoder.getFromLocation(lat, lng, 1);
                    String country, dist;
                    Log.d(TAG, "onLocationChanged: " + Arrays.toString(addressList.toArray()));
                    country = addressList.get(0).getCountryName();
                    dist = addressList.get(0).getAddressLine(0);

                    if(!country.equals(null) && !dist.equals(null)) {

                        /*model.setCountry(country);
                        model.setDist(dist);*/


                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("country", country);
                        intent.putExtra("dist", dist);
                        startActivity(intent);
                    }

                    //Toast.makeText(MapsActivity.this, dist, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onLocationChanged: " + dist);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        // permission check
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 500, locationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && PackageManager.PERMISSION_GRANTED == grantResults[0]) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 500, locationListener);
            }
        }
    }
}
