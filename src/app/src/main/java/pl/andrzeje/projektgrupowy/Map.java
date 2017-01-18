package pl.andrzeje.projektgrupowy;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class Map extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener
        {

    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;
    private UiSettings mMapUISet;
    private CameraPosition mLastCameraPosition;
//    TextView tvField1;
    private static final LatLng LUBLIN = new LatLng(51.2473568,22.5638809);
    private static final String TAG = Map.class.getSimpleName();
    private static final float MaxDistFromCityCentre = 15000; // maksymalna odleglosc obecnej lokalizacji urzadzenia od zmiennej LUBLIN

    void SendToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate");
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mLastCameraPosition = null;
        //tvField1 = (TextView) findViewById(R.id.textView);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        Log.d(TAG,"onMapReady");

        if (!checkLocationPermission()) {
            SendToast("checkSelfPermission error");
            return;
        }
        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);

        mMapUISet = mMap.getUiSettings();
        mMapUISet.setZoomControlsEnabled(true);

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                SendToast(latLng.toString() + " " + mMap.getCameraPosition().zoom);

            }
        });

    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart");
        if(mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop");
        if(mGoogleApiClient != null) {
            mLastCameraPosition = mMap.getCameraPosition();
            mGoogleApiClient.disconnect();
        }
    }
    @Override
    public void onBackPressed() {
        Log.d(TAG,"onBackPressed");
        super.onBackPressed();
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG,"onConnected");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(500);
        mLocationRequest.setFastestInterval(200);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if(!checkLocationPermission()){
            SendToast("checkSelfPermission onConnected error");
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }
    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        Log.d(TAG,"onLocationChanged");
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        /*
                CZASEM PRZY OTWIERANIU MAPY KAMERA WEDRUJE W PIZ*U DALEKO
                MYSLE ZE TO PROBLEM ZE ZLYMI WARTOSCIAMI LATITUDE I LONGITUDE
                DO TESTOWANIA!!!!
                DEBUG WARTOSCI:
                SendToast(latLng.latitude + " " + latLng.longitude);
        */

        float []result = new float[1];
        Location.distanceBetween(LUBLIN.latitude,LUBLIN.longitude,latLng.latitude,latLng.longitude,result);
        if(result[0] > MaxDistFromCityCentre) SendToast(String.valueOf(result[0]));


        if(mLastCameraPosition != null ) {
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(mLastCameraPosition));
        } else {
            moveCamera(latLng, 16);
        }

        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }
    private void moveCamera(LatLng where,int zoom)
    {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(where));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));
    }
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                    moveCamera(LUBLIN,14);
                }
                return;
            }
        }
    }

    public void bBackListener(View view) {
        onBackPressed();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
