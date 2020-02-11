package tech.iwish.taxi.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.firebase.client.Firebase;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.android.libraries.places.api.Places;

import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;

import okhttp3.WebSocketListener;
import tech.iwish.taxi.diractionAPi.Common;
import tech.iwish.taxi.diractionAPi.IGoogleApi;
import tech.iwish.taxi.R;
import tech.iwish.taxi.config.SharedpreferencesUser;
import tech.iwish.taxi.fragment.ConfirmRideFragment;
import tech.iwish.taxi.fragment.OfferFragment;
import tech.iwish.taxi.fragment.Pickup_Search_fragment;
import tech.iwish.taxi.fragment.RateCardFragment;
import tech.iwish.taxi.fragment.RentalFragment;
import tech.iwish.taxi.fragment.RentalPackageFragmnet;
import tech.iwish.taxi.fragment.RideHistoryFragment;
import tech.iwish.taxi.fragment.Search_dropFragment;
import tech.iwish.taxi.fragment.WalletFragment;
import tech.iwish.taxi.other.VehicleListWs;
import tech.iwish.taxi.websocket.SocketService;

import static tech.iwish.taxi.config.SharedpreferencesUser.LOCATION_CHANGE;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        NavigationView.OnNavigationItemSelectedListener,
        GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveCanceledListener,
        GoogleMap.OnCameraIdleListener,
        GoogleMap.OnCameraMoveListener {


    public TextView pickup, droplocation, rentail;
    FusedLocationProviderClient fusedLocationProviderClient;
    Location currentlocation;
    private int STORAGE_PERMISSION_CODE = 2;
    PlacesClient placesClient;
    GoogleMap googleMaps;
    ImageView blue_pin_icon, red_pin_icon;
    public Map<String, LatLng> AllLatLng = new HashMap<String, LatLng>();
    public Map<String, Double> latitude_logitude = new HashMap<String, Double>();
    boolean doubleBackToExitPressedOnce = false;
    Double latD, log;
    FrameLayout confirRide, search_fragment, confirmRideLoad;
    String clikalablu, rentalClickAble, pickupLocationNameIntent, pickupclickvalueget, droplocationchecks;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    LinearLayout dailtLayout, rentalLayout, outStationLayout;
    NavigationView navigationView;
    String pickupPlaceNameInt;
    String dropLocationPlaceInt;
    Map<String, String> AddressMap = new HashMap<>();
    LocationManager locationManager;
    private final int FIVE_SECONDS = 1;
    private final int ONE_SECONDS = 5000;
    List<VehicleListWs> vehicleListWs = new ArrayList<>();
    IGoogleApi mService;
    private List<LatLng> polylineList;
    Handler handler;
    private Polyline blackPolyline, greyPolyline;
    private float v;
    private Double lat, lng, lats;
    private int index, next;
    private LatLng startPosition, endPosition, vehicleLatlng;
    private Marker marker;
    SharedpreferencesUser sharedpreferencesUser;
    private GoogleApiClient mGoogleApiClient;
    private LatLng latlngvehicles;
    private GoogleMap.CancelableCallback callback;
    ConfirmRideFragment confirmRideFragment;
    WebSocketListener webSocketListener;
    private final static String TAG_FRAGMENT = "TAG_FRAGMENT";
    private final static String CONFIR_FRAGMENT_RIDE = "CONFIR_FRAGMENT_RIDE";
    Fragment rateCard;
    boolean checkinit = false;
    private SocketService socketService;
    private String rentalClick = "daily";
    Toolbar toolbar;
    private Button rantalPackage;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//        PermissionCheck();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);


        blue_pin_icon = (ImageView) findViewById(R.id.blue_pin_icon);
        red_pin_icon = (ImageView) findViewById(R.id.red_pin_icon);
        pickup = (TextView) findViewById(R.id.pickup);
        droplocation = (TextView) findViewById(R.id.droplocation);
        confirRide = (FrameLayout) findViewById(R.id.confirRide);
        search_fragment = (FrameLayout) findViewById(R.id.search_fragment);
        confirmRideLoad = (FrameLayout) findViewById(R.id.confirmRideLoad);
        dailtLayout = (LinearLayout) findViewById(R.id.dailtLayout);
        rentalLayout = (LinearLayout) findViewById(R.id.rentalLayout);
        outStationLayout = (LinearLayout) findViewById(R.id.outStationLayout);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        rantalPackage = (Button)findViewById(R.id.rantalPackage);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
//        toolbar  = (Toolbar)findViewById(R.id.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        setSupportActionBar(toolbar);



        View headerNavigation = navigationView.inflateHeaderView(R.layout.header_navigation);
        View panel = headerNavigation.findViewById(R.id.header_navigationss);
        panel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
        setTitle("Home");
        Firebase.setAndroidContext(this);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyApe8T3JMiqj9OgEbqd--zTBfl3fibPeEs");
        }
        placesClient = Places.createClient(MainActivity.this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        fetchLastLocation();
        permisanLocation();
        mService = Common.getGoogleApi();

        isLocationEnabled();

        droplocation.setAlpha((float) 0.6);
        rantalPackage.setVisibility(View.GONE);

        rantalPackage.setOnClickListener((View)->{

        RentalPackageFragmnet rentalPackageFragmnet = new RentalPackageFragmnet();
        getSupportFragmentManager().beginTransaction().replace(R.id.search_fragment ,rentalPackageFragmnet).commit();
        });

    }

    public void isLocationEnabled() {

        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // Check if enabled and if not send user to the GPS settings
        if (!enabled) {

            Intent intent = new Intent(MainActivity.this, GpsActivity.class);
            startActivity(intent);

        }

    }



    /*
    private void PermissionCheck() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
        } else {
            showGPSDisabledAlertToUser();
        }
    }

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }


    */

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.icon_set_upper, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        switch (id) {
            case R.id.nav_ride_history:
                setTitle("Ride History");
                confirRide.setVisibility(View.VISIBLE);
                search_fragment.setVisibility(View.VISIBLE);
                RideHistoryFragment rideHistoryFragment = new RideHistoryFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.search_fragment, rideHistoryFragment).commit();
                break;
            case R.id.nav_rate_card:
                setTitle("Rate Card");
                confirRide.setVisibility(View.VISIBLE);
                search_fragment.setVisibility(View.VISIBLE);

                RateCardFragment rateCardFragment = new RateCardFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.search_fragment, rateCardFragment, TAG_FRAGMENT).addToBackStack("tag").commit();
                break;
            case R.id.wallet:
                setTitle("Wallet");
                WalletFragment walletFragment = new WalletFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.search_fragment, walletFragment, TAG_FRAGMENT).addToBackStack("tag").commit();
                break;
            case R.id.offer:
                setTitle("Offer");
                OfferFragment offerFragment = new OfferFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.search_fragment,offerFragment).addToBackStack("tag").commit();
                break;

        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void fetchLastLocation() {
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentlocation = location;
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    supportMapFragment.getMapAsync(MainActivity.this);
                }
            }
        });
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        googleMaps = googleMap;
//        googleMap.setTrafficEnabled(true);
//        googleMap.setIndoorEnabled(true);
//        googleMap.setBuildingsEnabled(true);


        googleMap.setOnMapClickListener(this);
        googleMap.setOnCameraMoveStartedListener(this);
        googleMap.setOnCameraMoveCanceledListener(this);
        googleMap.setOnCameraIdleListener(this);
        googleMap.setOnCameraMoveListener(this);
//        googleMap.setOnMyLocationClickListener(this);

        blue_pin_icon.setImageResource(R.drawable.blue_pin_icon);

        if (googleMap != null) {
            googleMap.setMyLocationEnabled(true);
            final View locationButton = ((View) MainActivity.this.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            // position on right bottom
            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            rlp.setMargins(0, 0, 20, 120);
            googleMap.getMaxZoomLevel();
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);

            LatLng locationCurrent = new LatLng(currentlocation.getLatitude(), currentlocation.getLongitude());


            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(currentlocation.getLatitude(), currentlocation.getLongitude(), 1);
                String cityName = addresses.get(0).getLocality();
                String stateName = addresses.get(0).getAdminArea();
                String streetName = addresses.get(0).getFeatureName();

                AddressMap.put("PickupCityName", cityName);
                AddressMap.put("PickupstateName", stateName);
                AddressMap.put("PickupcountryName", streetName);
                if (!checkinit) {
                    this.checkinit = true;
                    socketService = new SocketService(this, AllLatLng, latitude_logitude, AddressMap);
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    socketService.onStartCommand(intent, 1, 1);
                    startService(intent);
//                    socketService.setdataVehicle(new SocketService.VehicleShow_Data() {
//                        @Override
//                        public void vehicleshow(String data) {
//                            vehicleShow(data);
//                        }
//                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            AllLatLng.put("pickMapValue", locationCurrent);
            latitude_logitude.put("PickLatitude", googleMaps.getCameraPosition().target.latitude);
            latitude_logitude.put("PickLogitude", googleMaps.getCameraPosition().target.longitude);
            MarkerOptions markerOptions = new MarkerOptions().position(locationCurrent).title("I m here").visible(false);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationCurrent, 16), 100, null);
            googleMap.addMarker(markerOptions);


        }
    }

    public void vehicleShow(String value) {


        JSONObject jsondobj = null;
        try {
            jsondobj = new JSONObject(value);
            JSONObject datas = jsondobj.getJSONObject("data");
            String lat = datas.getString("lat");
            String logit = datas.getString("long");
            if (lat != null || logit != null) {
                latD = Double.valueOf(lat);
                log = Double.valueOf(logit);
                if (latD != null || log != null) {
//                    LatLng latLng = new LatLng(latD, log);
                    LatLng latLng = new LatLng(26.204543, 78.190961);

                    this.vehicleLatlng = latLng;
                    showveh();

//                    MarkerOptions markerOptions = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.car_icon));
//                    googleMaps.addMarker(markerOptions);
//                    Log.e("lat", String.valueOf(latLng));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void showveh() {

        if (vehicleLatlng != null) {
            MarkerOptions markerOptionss = new MarkerOptions().position(vehicleLatlng).title("vehicle").visible(true);
            googleMaps.addMarker(markerOptionss);
        }


    }
/*

    public void fetchVehicle(String value) {

        try {
            JSONObject jsondobj = new JSONObject(value);
            JSONObject datas = null;
            String type = jsondobj.getString("type");
            switch (type) {
                case "vehicleAccept":
                    datas = jsondobj.getJSONObject("data");
                    Log.e("vehicleAccept", "vehicleAccept");
                    Log.e("vehicleAccept", datas.toString());
                    break;
                case "vehicleInfo":
                    vehicleShow(value);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        


     */
/*   try {
            JSONObject jsondobj = new JSONObject(value);
            JSONObject datas = jsondobj.getJSONObject("data");
            String type = jsondobj.getString("type");

            String lat = datas.getString("lat");
            String logit = datas.getString("long");
            if (lat != null || logit != null) {
                latD = Double.valueOf(lat);
                log = Double.valueOf(logit);
                if (latD != null || log != null) {
                    LatLng latLng = new LatLng(latD, log);

                    MarkerOptions markerOptions = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.car_icon));
                    googleMaps.addMarker(markerOptions);

//                    MarkerOptions markerOptions = new MarkerOptions().position(bb).title("I m here");
//                    googleMaps.animateCamera(CameraUpdateFactory.newLatLngZoom(bb, 15), 200, null);
//                    googleMaps.addMarker(markerOptions);
//
                }
            }
*//*

     */
/*

                            Location temp = new Location(LocationManager.GPS_PROVIDER);
                            temp.setLatitude(latD);
                            temp.setLongitude(log);
                            LatLng bb = new LatLng(26.203417, 78.189470);
                            if (marker == null) {
                                marker = googleMaps.addMarker(new MarkerOptions().position(latLng)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.car_icon)));
//                                      MarkerAnimation.animateMarkerToGB(marker, aa, new LatLngInterpolator.Spherical());
                                animateMarker(temp, marker);
                                marker.setRotation(getBearing(latLng, bb));

                            } else {
                                MarkerAnimation.animateMarkerToICS(marker, latLng, new LatLngInterpolator.Spherical());
                                marker.setRotation(getBearing(latLng, bb));
                            }

*//*
     */
/*

        } catch (JSONException e) {
            e.printStackTrace();
        }*//*


     */
/*
        marker = googleMaps.addMarker(new MarkerOptions().position(aa)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.car_icon)));
        animateMarker(currentlocation ,marker );
*//*


//                googleMaps.clear();
//                Marker marker = new Marker().remove();


    }


    public void WebsocketHandel(String data) {


        webSocketListener = new WebSocketListener() {


            @Override
            public void onOpen(WebSocket webSocket, Response response) {

                String Json;
                if (data != null) {
                    Json = data;
                } else {
                    Json = "{ \"type\" : \"initiate\" ,\"userType\" : \"client\"  , \"userID\" : \"8871121959\" , \"PickupCityName\" : \"" + AddressMap.get("PickupCityName") + "\" , \"PickupstateName\" : \"" + AddressMap.get("PickupstateName") + "\" , \"PickupStretName\" : \"" + AddressMap.get("PickupStretName") + "\"}";
                }
                webSocket.send(Json);
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {

                fetchVehicle(text);


            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {

            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {

            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {

            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {

            }
        };
        Request request = new Request.Builder().url("ws://173.212.226.143:8090/").build();
        OkHttpClient showVeicle = new OkHttpClient();
        showVeicle.newWebSocket(request, webSocketListener);
        showVeicle.dispatcher().executorService();


    }

*/

    public void permisanLocation() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            fetchLastLocation();
        } else {
            requestStoragePermission();
        }
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
                && (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION))) {
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLastLocation();
            } else {
            }
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        confirRide.setVisibility(View.GONE);
    }

    @Override
    public void onCameraMoveStarted(int i) {

    }

    @Override
    public void onCameraMove() {

    }

    @Override
    public void onCameraMoveCanceled() {

    }

    public void RentalCheck(View view) {
        dailtLayout.setBackground(getDrawable(R.drawable.bottom_design_1));
        rentalLayout.setBackground(getDrawable(R.drawable.bottom_design_1));
        outStationLayout.setBackground(getDrawable(R.drawable.bottom_design_1));
        int id = view.getId();
        switch (id) {
            case R.id.dailtLayout:
                confirRide.setVisibility(View.VISIBLE);
                this.rentalClick = "daily";
                dailtLayout.setBackground(getDrawable(R.drawable.bottom_design));
                rantalPackage.setVisibility(View.GONE);
                DailyLayoutMethod();
                break;
            case R.id.rentalLayout:
                this.rentalClick = "rental";
                rantalPackage.setVisibility(View.VISIBLE);
                rentalLayout.setBackground(getDrawable(R.drawable.bottom_design));
                RentalMethod();
                this.rentalClickAble = view.getTag().toString();
                break;
            case R.id.outStationLayout:
                this.rentalClick = "outstation";
                outStationLayout.setBackground(getDrawable(R.drawable.bottom_design));
                break;
        }
    }

    private void DailyLayoutMethod() {
        pickup.setVisibility(View.VISIBLE);
        droplocation.setVisibility(View.VISIBLE);
        blue_pin_icon.setVisibility(View.VISIBLE);
        red_pin_icon.setVisibility(View.VISIBLE);
        googleMaps.setMyLocationEnabled(true);

    }

    private void RentalMethod() {
        droplocation.setVisibility(View.GONE);
        blue_pin_icon.setVisibility(View.VISIBLE);
        red_pin_icon.setVisibility(View.GONE);
        confirRide.setVisibility(View.GONE);
        googleMaps.setMyLocationEnabled(false);
//        MarkerOptions markerOptions = new MarkerOptions().position(AllLatLng.get("pickMapValue")).title("I m here").visible(false);
        googleMaps.animateCamera(CameraUpdateFactory.newLatLngZoom(AllLatLng.get("pickMapValue"), 15), 200, null);
//        googleMaps.addMarker(markerOptions);
        SharedpreferencesUser sharedpreferencesUser = new SharedpreferencesUser(MainActivity.this);
        sharedpreferencesUser.location("pickLocations");




    }

    @Override
    public void onCameraIdle() {

        SharedpreferencesUser sharedpreferencesUser = new SharedpreferencesUser(MainActivity.this);
        Map data = sharedpreferencesUser.getShare();
        Object clickValue = data.get(LOCATION_CHANGE);
        if (clickValue != null)
            pickupLocationNameIntent = clickValue.toString();

        if (pickupLocationNameIntent != null) {
            Pickupmethod(pickupPlaceNameInt);
            droplocationmethod(dropLocationPlaceInt);

        } else {

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(googleMaps.getCameraPosition().target.latitude, googleMaps.getCameraPosition().target.longitude, 1);

                String add = addresses.get(0).getAddressLine(0);
                String cityName = addresses.get(0).getLocality();
                String stateName = addresses.get(0).getAdminArea();
                String streetName = addresses.get(0).getFeatureName();


                pickup.setText(add);
                latitude_logitude.put("PickLatitude", googleMaps.getCameraPosition().target.latitude);
                latitude_logitude.put("PickLogitude", googleMaps.getCameraPosition().target.longitude);
                AddressMap.put("PickupCityName", cityName);
                AddressMap.put("PickupstateName", stateName);
                AddressMap.put("PickupStretName", streetName);

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    private void Pickupmethod(String value) {
        SharedpreferencesUser sharedpreferencesUser = new SharedpreferencesUser(MainActivity.this);
        Map data = sharedpreferencesUser.getShare();
        Object clickValue = data.get(LOCATION_CHANGE);
        if (clickValue != null)
            pickupLocationNameIntent = clickValue.toString();

        if (pickupPlaceNameInt != null) {
//            if(value != null){}else{}
            if (pickupclickvalueget == null) {
                this.pickupclickvalueget = "some value";
                Geocoder geocoder = new Geocoder(MainActivity.this);
                try {
                    List<Address> addresses = geocoder.getFromLocationName(value, 1);
                    Address address = addresses.get(0);
                    String cityName = addresses.get(0).getLocality();
                    String stateName = addresses.get(0).getAdminArea();
                    String streetName = addresses.get(0).getFeatureName();
                    LatLng pickUpLatLog = new LatLng(address.getLatitude(), address.getLongitude());
                    AllLatLng.put("pickMapValue", pickUpLatLog);
                    latitude_logitude.put("PickLatitude", googleMaps.getCameraPosition().target.latitude);
                    latitude_logitude.put("PickLogitude", googleMaps.getCameraPosition().target.longitude);
//                    ***************************************

                    AddressMap.put("PickupCityName", cityName);
                    AddressMap.put("PickupstateName", stateName);
                    AddressMap.put("PickupStretName", streetName);

//                    ****************************************
                    MarkerOptions markerOptions = new MarkerOptions().position(pickUpLatLog).title("I m here").visible(false);
                    googleMaps.animateCamera(CameraUpdateFactory.newLatLngZoom(pickUpLatLog, 15), 200, null);
                    googleMaps.addMarker(markerOptions);
                    pickup.setText(cityName);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (pickupLocationNameIntent.equals("pickLocations")) {
                Geocoder geocoder = new Geocoder(MainActivity.this);
                try {
                    List<Address> addresses = geocoder.getFromLocation(googleMaps.getCameraPosition().target.latitude, googleMaps.getCameraPosition().target.longitude, 1);
                    Address address = addresses.get(0);
                    String add = address.getAddressLine(0);
                    String cityName = addresses.get(0).getLocality();
                    String stateName = addresses.get(0).getAdminArea();
                    String streetName = addresses.get(0).getFeatureName();
                    LatLng pickUpLatLog = new LatLng(address.getLatitude(), address.getLongitude());
                    AllLatLng.put("pickMapValue", pickUpLatLog);
                    pickup.setText(add);
                    latitude_logitude.put("PickLatitude", googleMaps.getCameraPosition().target.latitude);
                    latitude_logitude.put("PickLogitude", googleMaps.getCameraPosition().target.longitude);
                    AddressMap.put("PickupCityName", cityName);
                    AddressMap.put("PickupstateName", stateName);
                    AddressMap.put("PickupStretName", streetName);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void droplocationmethod(String droplocationvalue) {

        SharedpreferencesUser sharedpreferencesUser = new SharedpreferencesUser(MainActivity.this);
        Map data = sharedpreferencesUser.getShare();
        Object clickValue = data.get(LOCATION_CHANGE);
        if (clickValue != null)
            pickupLocationNameIntent = clickValue.toString();
        if (droplocationvalue != null) {
            if (droplocationchecks == null) {
                this.droplocationchecks = "some value";
                Geocoder geocoder = new Geocoder(MainActivity.this);
                try {
                    List<Address> addresses = geocoder.getFromLocationName(droplocationvalue, 1);

                    Address address = addresses.get(0);
                    String add = address.getAddressLine(0);
                    String cityName = addresses.get(0).getLocality();
                    String stateName = addresses.get(0).getAdminArea();
                    String streetName = addresses.get(0).getFeatureName();

                    LatLng dropLocatinsUpLatLog = new LatLng(address.getLatitude(), address.getLongitude());
                    AllLatLng.put("DropLocationMapValue", dropLocatinsUpLatLog);
                    MarkerOptions markerOptions = new MarkerOptions().position(dropLocatinsUpLatLog).title("I m here").visible(false);
                    googleMaps.animateCamera(CameraUpdateFactory.newLatLngZoom(dropLocatinsUpLatLog, 15), 200, null);
                    googleMaps.addMarker(markerOptions);

                    droplocation.setText(add);

                    latitude_logitude.put("dropLatitude", dropLocatinsUpLatLog.latitude);
                    latitude_logitude.put("dropLongitude", dropLocatinsUpLatLog.longitude);
                    AddressMap.put("DropLocationCityName", cityName);
                    AddressMap.put("DropLocationstateName", stateName);
                    AddressMap.put("DropLocationcountryName", streetName);
                    confirmRideFragment = new ConfirmRideFragment(AllLatLng, latitude_logitude, AddressMap);
                    getSupportFragmentManager().beginTransaction().replace(R.id.confirRide, confirmRideFragment, CONFIR_FRAGMENT_RIDE).commit();
                    blue_pin_icon.setVisibility(View.GONE);
                    red_pin_icon.setVisibility(View.VISIBLE);
                    confirmRideFragment.setdata(new ConfirmRideFragment.vehicleInter() {
                        @Override
                        public void websok(String value) {

                        }
                    });


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
           /* else if (pickupLocationNameIntent.equals("dropLocations")) {
                Geocoder geocoder = new Geocoder(MainActivity.this);
                try {
                    List<Address> addresses = geocoder.getFromLocation(googleMaps.getCameraPosition().target.latitude, googleMaps.getCameraPosition().target.longitude, 1);
                    Address address = addresses.get(0);
                    String add = address.getAddressLine(0);
                    LatLng dropLocatinsUpLatLog = new LatLng(address.getLatitude(), address.getLongitude());
                    AllLatLng.put("DropLocationMapValue", dropLocatinsUpLatLog);

//                    MarkerOptions markerOptions = new MarkerOptions().position(dropLocatinsUpLatLog).title("I m here").visible(false);
                    googleMaps.animateCamera(CameraUpdateFactory.newLatLngZoom(dropLocatinsUpLatLog, 15), 200, null);
//                    googleMaps.addMarker(markerOptions);


                    droplocation.setText(add);
                    String cityName = addresses.get(0).getLocality();
                    String stateName = addresses.get(0).getAdminArea();
                    String streetName = addresses.get(0).getFeatureName();
                    AddressMap.put("DropLocationCityName", cityName);
                    AddressMap.put("DropLocationstateName", stateName);
                    AddressMap.put("DropLocationcountryName", streetName);
                    droplocation.setText(add);
                    latitude_logitude.put("dropLatitude", googleMaps.getCameraPosition().target.latitude);
                    latitude_logitude.put("dropLongitude", googleMaps.getCameraPosition().target.longitude);
                    confirRide.setVisibility(View.VISIBLE);
                    search_fragment.setVisibility(View.VISIBLE);


*//*


                    ConfirmRideFragment confirmRideFragment = new ConfirmRideFragment(AllLatLng, latitude_logitude, AddressMap);
                    getSupportFragmentManager().beginTransaction().replace(R.id.confirRide, confirmRideFragment).commit();
                    confirmRideFragment.setdata(new ConfirmRideFragment.vehicleInter() {
                        @Override
                        public void websok(String value) {

                        }
                    });

*//*


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
       */
        }


    }

    public void locationpic(View view) {
        Map data;
        SharedpreferencesUser sharedpreferencesUser = new SharedpreferencesUser(MainActivity.this);
        data = sharedpreferencesUser.getShare();
        droplocationchecks = null;
        pickupclickvalueget = null;
        String location = null;
        Object Objlocation = data.get(LOCATION_CHANGE);
        if (Objlocation != null) {
            location = Objlocation.toString();
        }

        String tagIdLocation = view.getTag().toString();

        switch (rentalClick) {
            case "daily":
                if (tagIdLocation.equals("pickLocations")) {
                    if (location == null) {

                        Pickup_Search_fragment pickup_search_fragment = new Pickup_Search_fragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.search_fragment, pickup_search_fragment).commit();
                        pickup_search_fragment.valuePickup(new Pickup_Search_fragment.PickupInterFacePlace() {
                            @Override
                            public void placeName_Pickup(String location) {

                                pickup_search_fragment.dismiss();
                                pickupPlaceNameInt = location;
                                Pickupmethod(pickupPlaceNameInt);
                                blue_pin_icon.setVisibility(View.VISIBLE);
                                red_pin_icon.setVisibility(View.GONE);
                                pickupclickvalueget = null;

                            }
                        });
                    } else if (location == tagIdLocation) {
                        Pickup_Search_fragment pickup_search_fragment = new Pickup_Search_fragment();
//                pickup_search_fragment.show(getSupportFragmentManager(), "myFragment");
                        getSupportFragmentManager().beginTransaction().replace(R.id.search_fragment, pickup_search_fragment).commit();
                        pickup_search_fragment.valuePickup(new Pickup_Search_fragment.PickupInterFacePlace() {
                            @Override
                            public void placeName_Pickup(String location) {
                                pickup_search_fragment.dismiss();
                                pickupPlaceNameInt = location;
                                Pickupmethod(pickupPlaceNameInt);
                                blue_pin_icon.setVisibility(View.VISIBLE);
                                red_pin_icon.setVisibility(View.GONE);
                            }
                        });
                    } else {
                        if (location.equals("dropLocations")) {
                            if (pickupPlaceNameInt != null) {
                                MarkerOptions markerOptions = new MarkerOptions().position(AllLatLng.get("pickMapValue")).title("I m here").visible(false);
                                googleMaps.animateCamera(CameraUpdateFactory.newLatLngZoom(AllLatLng.get("pickMapValue"), 15), 200, null);
                                googleMaps.addMarker(markerOptions);
                                blue_pin_icon.setVisibility(View.VISIBLE);
                                red_pin_icon.setVisibility(View.GONE);
                                pickup.setAlpha(1);
                                droplocation.setAlpha((float) 0.6);
                            } else {
                                Pickup_Search_fragment pickup_search_fragment = new Pickup_Search_fragment();
//                        pickup_search_fragment.show(getSupportFragmentManager(), "myFragment");
                                getSupportFragmentManager().beginTransaction().replace(R.id.search_fragment, pickup_search_fragment).commit();
                                pickup_search_fragment.valuePickup(new Pickup_Search_fragment.PickupInterFacePlace() {
                                    @Override
                                    public void placeName_Pickup(String location) {
                                        pickup_search_fragment.dismiss();
                                        pickupPlaceNameInt = location;
                                        Pickupmethod(pickupPlaceNameInt);
                                        blue_pin_icon.setVisibility(View.VISIBLE);
                                        red_pin_icon.setVisibility(View.GONE);
                                    }
                                });

                            }
                        }
                    }
                }
                if (tagIdLocation.equals("dropLocations")) {
                    if (location == null) {

                        Search_dropFragment search_dropFragment = new Search_dropFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.search_fragment, search_dropFragment).commit();
                        search_dropFragment.setValueDrop(new Search_dropFragment.DropValueInterFace() {
                            @Override
                            public void DroplocationValue(String data) {
                                search_dropFragment.dismiss();
                                dropLocationPlaceInt = data;
                                droplocationmethod(dropLocationPlaceInt);
                                blue_pin_icon.setVisibility(View.GONE);
                                red_pin_icon.setImageResource(R.drawable.red_pin_icon);
                                red_pin_icon.setVisibility(View.VISIBLE);
                                droplocationchecks = null;
                            }
                        });


                    } else if (location == tagIdLocation) {

                        Search_dropFragment search_dropFragment = new Search_dropFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.search_fragment, search_dropFragment).commit();
                        search_dropFragment.setValueDrop(new Search_dropFragment.DropValueInterFace() {
                            @Override
                            public void DroplocationValue(String data) {
                                Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT).show();
                                search_dropFragment.dismiss();
                                dropLocationPlaceInt = data;
                                droplocationmethod(dropLocationPlaceInt);
                                blue_pin_icon.setVisibility(View.GONE);
                                red_pin_icon.setImageResource(R.drawable.red_pin_icon);
                                red_pin_icon.setVisibility(View.VISIBLE);
                            }
                        });


                    } else {
                        if (location.equals("pickLocations")) {
                            if (droplocation.getText().toString().equals("Enter drop location")) {

                                Search_dropFragment search_dropFragment = new Search_dropFragment();
                                getSupportFragmentManager().beginTransaction().replace(R.id.search_fragment, search_dropFragment).commit();

                                search_dropFragment.setValueDrop(new Search_dropFragment.DropValueInterFace() {
                                    @Override
                                    public void DroplocationValue(String data) {
                                        Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT).show();
                                        search_dropFragment.dismiss();
                                        dropLocationPlaceInt = data;
                                        droplocationmethod(dropLocationPlaceInt);
                                        blue_pin_icon.setVisibility(View.GONE);
                                        red_pin_icon.setImageResource(R.drawable.red_pin_icon);
                                        red_pin_icon.setVisibility(View.VISIBLE);
                                    }
                                });


                            } else {

                                MarkerOptions markerOptions = new MarkerOptions().position(AllLatLng.get("DropLocationMapValue")).title("I m here").visible(false);
                                googleMaps.animateCamera(CameraUpdateFactory.newLatLngZoom(AllLatLng.get("DropLocationMapValue"), 15), 200, null);
                                googleMaps.addMarker(markerOptions);
                                blue_pin_icon.setVisibility(View.GONE);
                                red_pin_icon.setImageResource(R.drawable.red_pin_icon);
                                red_pin_icon.setVisibility(View.VISIBLE);
                                pickup.setAlpha((float) 0.6);
                                droplocation.setAlpha(1);

                            }
                        }
                    }
                }
                break;
            case "rental":
                RentalFragment rentalFragment = new RentalFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.search_fragment, rentalFragment).commit();
                break;
            case "outstation":
                break;
            default:
                break;
        }


        sharedpreferencesUser.location(tagIdLocation);
        this.clikalablu = view.getTag().toString();
    }


    @Override
    public void onBackPressed() {
        RateCardFragment rateCardFragment;
        ConfirmRideFragment confirmRideFragment;
        ConfirmRideFragment confirmRideFragment1 = new ConfirmRideFragment(AllLatLng, latitude_logitude, AddressMap);
        confirmRideFragment = (ConfirmRideFragment) getSupportFragmentManager().findFragmentByTag(CONFIR_FRAGMENT_RIDE);
        rateCardFragment = (RateCardFragment) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.confirmRideLoad);
        Fragment confirRideFrameLay = getSupportFragmentManager().findFragmentById(R.id.confirRide);
        Fragment search_fragmentss = getSupportFragmentManager().findFragmentById(R.id.search_fragment);

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            getSupportFragmentManager().beginTransaction().remove(confirRideFrameLay).commit();

        } else if (confirRideFrameLay != null) {
            getSupportFragmentManager().beginTransaction().remove(confirRideFrameLay).commit();
            setTitle("Home");
        } else if (search_fragmentss != null) {
            getSupportFragmentManager().beginTransaction().remove(search_fragmentss).commit();
            setTitle("Home");
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    /*
    public void directionUrl() {

        String requestUrl = "https://maps.googleapis.com/maps/api/directions/json?mode=driving&transit_routing_preference=less_driving&origin=26.238996,78.181535&destination=26.243067,78.185623&key=AIzaSyApe8T3JMiqj9OgEbqd--zTBfl3fibPeEs";

//        String requestUrl = "https://maps.googleapis.com/maps/api/directions/json?mode=driving&transit_routing_preference=less_driving&" +
//                "origin=" + latitude_logitude.get("PickLatitude") + "," + latitude_logitude.get("PickLogitude") + "&destination=" +
//                latitude_logitude.get("dropLatitude") + "," + latitude_logitude.get("dropLongitude") + "&key=AIzaSyApe8T3JMiqj9OgEbqd--zTBfl3fibPeEs";

        mService.getDataFromGoogleApi(requestUrl).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("routes");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject route = jsonArray.getJSONObject(i);
                        JSONObject poly = route.getJSONObject("overview_polyline");
                        String polyline = poly.getString("points");
                        polylineList = decodePoly(polyline);
                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        for (LatLng latLng : polylineList)
                            builder.include(latLng);
                        LatLngBounds bounds = builder.build();
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 2);
                        googleMaps.animateCamera(cameraUpdate);


                        PolylineOptions polylineOptions = new PolylineOptions();
                        polylineOptions.color(Color.BLUE);
                        polylineOptions.width(5);
                        polylineOptions.startCap(new SquareCap());
                        polylineOptions.endCap(new SquareCap());
                        polylineOptions.jointType(JointType.ROUND);
                        polylineOptions.addAll(polylineList);
                        greyPolyline = googleMaps.addPolyline(polylineOptions);


                        PolylineOptions blackpolylineOptions = new PolylineOptions();
                        blackpolylineOptions.color(Color.BLUE);
                        blackpolylineOptions.width(10);
                        blackpolylineOptions.startCap(new SquareCap());
                        blackpolylineOptions.endCap(new SquareCap());
                        blackpolylineOptions.jointType(JointType.ROUND);
                        blackpolylineOptions.addAll(polylineList);
                        blackPolyline = googleMaps.addPolyline(blackpolylineOptions);

                        googleMaps.addMarker(new MarkerOptions().position(polylineList.get(polylineList.size() - 1)));

                        ValueAnimator polylineAnimator = ValueAnimator.ofInt(0, 100);
                        polylineAnimator.setDuration(2000);
                        polylineAnimator.setInterpolator(new LinearInterpolator());
                        polylineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                List<LatLng> points = greyPolyline.getPoints();
                                int percentValue = (int) valueAnimator.getAnimatedValue();
                                int size = points.size();
                                int newPoints = (int) (size * (percentValue / 100.0f));
                                List<LatLng> p = points.subList(0, newPoints);
                                blackPolyline.setPoints(p);
                            }
                        });
                        polylineAnimator.start();
                        LatLng cu = new LatLng(26.241464, 78.181773);

                        marker = googleMaps.addMarker(new MarkerOptions().position(cu)
                                .flat(true)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.car_icon)));


                     handler = new Handler();
                        index = -1;
                        next = 1;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (index < polylineList.size() - 1) {
                                    index++;
                                    next = index + 1;
                                }
                                if (index < polylineList.size() - 1) {
                                    startPosition = polylineList.get(index);
                                    endPosition = polylineList.get(next);
                                }

                                ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
                                valueAnimator.setDuration(3000);
                                valueAnimator.setInterpolator(new LinearInterpolator());
                                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                        v = valueAnimator.getAnimatedFraction();
                                        lng = v * endPosition.longitude + (1 - v) * startPosition.longitude;
                                        lat = v * endPosition.latitude + (1 - v) * startPosition.latitude;
                                        LatLng newPos = new LatLng(lat, lng);
                                        marker.setPosition(newPos);
                                        marker.setAnchor(0.5f, 0.5f);
                                        marker.setRotation(getBearing(startPosition, newPos));
                                        googleMaps.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                                                .target(newPos)
                                                .zoom(15.5f)
                                                .build()));
                                    }
                                });
                                polylineAnimator.start();
                                handler.postDelayed(this, 3000);

                            }
                        }, 3000);



                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }


    private float getBearing(LatLng startPosition, LatLng newPos) {
        double lat = Math.abs(startPosition.latitude - newPos.latitude);
        double lng = Math.abs(startPosition.longitude - newPos.longitude);
        if (startPosition.latitude < newPos.latitude && startPosition.longitude < newPos.longitude)
            return (float) (toDegrees(Math.atan(lng / lat)));
        else if (startPosition.latitude >= newPos.latitude && startPosition.longitude < newPos.longitude)
            return (float) ((90 - toDegrees(Math.atan(lng / lat))) + 90);
        else if (startPosition.latitude >= newPos.latitude && startPosition.longitude >= newPos.longitude)
            return (float) (toDegrees(Math.atan(lng / lat)) + 180);
        else if (startPosition.latitude < newPos.latitude && startPosition.longitude >= newPos.longitude)
            return (float) ((90 - toDegrees(Math.atan(lng / lat))) + 270);
        return -1;
    }
 */


    private float getBearing(LatLng begin, LatLng end) {
        double lat = Math.abs(begin.latitude - end.latitude);
        double lng = Math.abs(begin.longitude - end.longitude);

        if (begin.latitude < end.latitude && begin.longitude < end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)));
        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90);
        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);
        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270);
        return -1;
    }


    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;

    }

    public void animateMarker(final Marker marker, final LatLng toPosition,
                              final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = googleMaps.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 3000;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }


//    work animation

    public static void animateMarker(Location destination, Marker marker) {
        if (marker != null) {
            LatLng startPosition = marker.getPosition();
            LatLng endPosition = new LatLng(destination.getLatitude(), destination.getLongitude());

            float startRotation = marker.getRotation();

            LatLngInterpolatorAni latLngInterpolator = new LatLngInterpolatorAni.LinearFixed();
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(1000); // duration 1 second
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    try {
                        float v = animation.getAnimatedFraction();
                        LatLng newPosition = latLngInterpolator.interpolate(v, startPosition, endPosition);
                        marker.setPosition(newPosition);
                        marker.setRotation(computeRotation(v, startRotation, destination.getBearing()));
                    } catch (Exception ex) {
                        // I don't care atm..
                    }
                }
            });

            valueAnimator.start();
        }
    }

    private static float computeRotation(float fraction, float start, float end) {
        float normalizeEnd = end - start; // rotate start to 0
        float normalizedEndAbs = (normalizeEnd + 360) % 360;

        float direction = (normalizedEndAbs > 180) ? -1 : 1; // -1 = anticlockwise, 1 = clockwise
        float rotation;
        if (direction > 0) {
            rotation = normalizedEndAbs;
        } else {
            rotation = normalizedEndAbs - 360;
        }

        float result = fraction * rotation + start;
        return (result + 360) % 360;
    }



    private interface LatLngInterpolatorAni {
        LatLng interpolate(float fraction, LatLng a, LatLng b);

        class LinearFixed implements LatLngInterpolatorAni {
            @Override
            public LatLng interpolate(float fraction, LatLng a, LatLng b) {
                double lat = (b.latitude - a.latitude) * fraction + a.latitude;
                double lngDelta = b.longitude - a.longitude;
                // Take the shortest path across the 180th meridian.
                if (Math.abs(lngDelta) > 180) {
                    lngDelta -= Math.signum(lngDelta) * 360;
                }
                double lng = lngDelta * fraction + a.longitude;
                return new LatLng(lat, lng);
            }
        }
    }
//    work animation


}