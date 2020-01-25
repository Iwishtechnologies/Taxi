package tech.iwish.taxi.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;

import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.internal.http2.Header;
import okio.ByteString;
import tech.iwish.taxi.R;
import tech.iwish.taxi.config.SharedpreferencesUser;
import tech.iwish.taxi.connection.JsonHelper;
import tech.iwish.taxi.fragment.DropSearchFragment;
import tech.iwish.taxi.fragment.ConfirmRideFragment;
import tech.iwish.taxi.fragment.Pickup_Search_fragment;
import tech.iwish.taxi.fragment.RideHistoryFragment;
import tech.iwish.taxi.other.User_DetailsList;
import tech.iwish.taxi.other.VehicleListWb;

import static android.widget.Toast.LENGTH_SHORT;
import static tech.iwish.taxi.config.SharedpreferencesUser.LOCATION_CHANGE;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        NavigationView.OnNavigationItemSelectedListener,
        GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveCanceledListener,
        GoogleMap.OnCameraIdleListener,
        GoogleMap.OnCameraMoveListener {

    int AUTOCOMPLETE_REQUEST_CODE = 1;
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
    FrameLayout confirRide, search_fragment;
    String clikalablu, rentalClickAble, pickupLocationNameIntent, pickupclickvalueget, droplocationchecks;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    LinearLayout dailtLayout, rentalLayout, outStationLayout;
    NavigationView navigationView;
    String pickupPlaceNameInt, dropLocationPlaceInt;
    Map<String, String> AddressMap = new HashMap<>();
    LocationManager locationManager;
    List<VehicleListWb> vehicleLatLongwb = new ArrayList<>();
    Marker marker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        PermissionCheck();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);


        blue_pin_icon = (ImageView) findViewById(R.id.blue_pin_icon);
        red_pin_icon = (ImageView) findViewById(R.id.red_pin_icon);
        pickup = (TextView) findViewById(R.id.pickup);
        droplocation = (TextView) findViewById(R.id.droplocation);
        confirRide = (FrameLayout) findViewById(R.id.confirRide);
        search_fragment = (FrameLayout) findViewById(R.id.search_fragment);
        dailtLayout = (LinearLayout) findViewById(R.id.dailtLayout);
        rentalLayout = (LinearLayout) findViewById(R.id.rentalLayout);
        outStationLayout = (LinearLayout) findViewById(R.id.outStationLayout);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        View headerNavigation = navigationView.inflateHeaderView(R.layout.header_navigation);
        View panel = headerNavigation.findViewById(R.id.header_navigationss);
        panel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });


        Firebase.setAndroidContext(this);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyB0J8NKrImr44cyuB7ZI6S2ZKbA5vhpou4");
        }
        placesClient = Places.createClient(MainActivity.this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();
        permisanLocation();


    }


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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();


        switch (id) {
            case R.id.nav_ride_history:
                setTitle("Ride Hist");
                RideHistoryFragment rideHistoryFragment = new RideHistoryFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.search_fragment, rideHistoryFragment).commit();
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

        googleMap.setOnMapClickListener(this);
        googleMap.setOnCameraMoveStartedListener(this);
        googleMap.setOnCameraMoveCanceledListener(this);
        googleMap.setOnCameraIdleListener(this);
        blue_pin_icon.setImageResource(R.drawable.blue_pin_icon);
        googleMap.setOnCameraMoveListener(this);
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


//  ================================================================================================

                String Json = "{\"userType\" : \"client\" , \"type\" : \"vehicleInfo\" , \"userID\" : \"8871121959\" , \"PickupCityName\" : \"" + AddressMap.get("PickupCityName") + "\" , \"PickupstateName\" : \"" + AddressMap.get("PickupstateName") + "\" , \"PickupStretName\" : \"" + AddressMap.get("PickupStretName") + "\"}";
                ShowVeicleSocket showVeicleSocket = new ShowVeicleSocket(this);
                showVeicleSocket.UrlWebSocket("ws://173.212.226.143:8090/", Json, this);
                SharedpreferencesUser sharedpreferencesUser = new SharedpreferencesUser(this);
                String data = null;
                data = sharedpreferencesUser.getdata();
                JsonHelper jsonHelper = new JsonHelper(data);
                if (data != null) {
                    if (jsonHelper.isValidJson()) {
                        String response = jsonHelper.GetResult("type");
                        if (response.equals("vehicleInfo")) {
                            JSONArray jsonArray = jsonHelper.setChildjsonArray(jsonHelper.getCurrentJsonObj(), "data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonHelper.setChildjsonObj(jsonArray, i);
                                vehicleLatLongwb.add(new VehicleListWb(jsonHelper.GetResult("lat"), jsonHelper.GetResult("long")));
                            }

                            for (int i = 0; i < vehicleLatLongwb.size(); i++) {

                                String lat = vehicleLatLongwb.get(i).getLatitude();
                                String longitude = vehicleLatLongwb.get(i).getLogitude();
                                if (lat != null || longitude != null) {
                                    latD = Double.valueOf(lat);
                                    log = Double.valueOf(longitude);
                                }
                            }
                            if (latD != null || log != null) {
                                LatLng latLng = new LatLng(latD, log);
                                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("I m here");
                                googleMaps.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15), 200, null);
                                googleMaps.addMarker(markerOptions);
                            }





            /*
                        Timer timer = new Timer();
                        TimerTask timerTask = new TimerTask() {
                            @Override
                            public void run() {
                                //your code
//                vehicleLatLongwb.get(0).getLatitude();

                                Log.e("loat" , vehicleLatLongwb.get(0).getLatitude());

                                for (int i = 0; i < vehicleLatLongwb.size(); i++) {

                                    String lat = vehicleLatLongwb.get(i).getLatitude();
                                    String longitude = vehicleLatLongwb.get(i).getLogitude();
                                     latD = Double.valueOf(lat);
                                     log = Double.valueOf(longitude);

                                }

                                LatLng latLng = new LatLng(latD, log);
                                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("I m here");
                                googleMaps.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15), 200, null);
                                googleMaps.addMarker(markerOptions);

                            }
                        };
                        timer.schedule(timerTask, 0, 1000);
*/
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            AllLatLng.put("pickMapValue", locationCurrent);
            latitude_logitude.put("PickLatitude", googleMaps.getCameraPosition().target.latitude);
            latitude_logitude.put("PickLogitude", googleMaps.getCameraPosition().target.longitude);
            MarkerOptions markerOptions = new MarkerOptions().position(locationCurrent).title("I m here").visible(false);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationCurrent, 15), 100, null);
            googleMap.addMarker(markerOptions);


        }
    }

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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
                dailtLayout.setBackground(getDrawable(R.drawable.bottom_design));
                DailyLayoutMethod();
                break;
            case R.id.rentalLayout:
                rentalLayout.setBackground(getDrawable(R.drawable.bottom_design));
                RentalMethod();
                this.rentalClickAble = view.getTag().toString();
                break;
            case R.id.outStationLayout:
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
        blue_pin_icon.setVisibility(View.GONE);
        red_pin_icon.setVisibility(View.GONE);
        confirRide.setVisibility(View.GONE);
        googleMaps.setMyLocationEnabled(false);
        MarkerOptions markerOptions = new MarkerOptions().position(AllLatLng.get("pickMapValue")).title("I m here").visible(false);
        googleMaps.animateCamera(CameraUpdateFactory.newLatLngZoom(AllLatLng.get("pickMapValue"), 15), 200, null);
        googleMaps.addMarker(markerOptions);
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
                    confirRide.setVisibility(View.VISIBLE);
                    search_fragment.setVisibility(View.VISIBLE);
                    ConfirmRideFragment confirmRideFragment = new ConfirmRideFragment(AllLatLng, latitude_logitude, AddressMap);
                    getSupportFragmentManager().beginTransaction().replace(R.id.confirRide, confirmRideFragment).commit();
                    blue_pin_icon.setVisibility(View.GONE);

                    red_pin_icon.setVisibility(View.VISIBLE);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (pickupLocationNameIntent.equals("dropLocations")) {
                Geocoder geocoder = new Geocoder(MainActivity.this);
                try {
                    List<Address> addresses = geocoder.getFromLocation(googleMaps.getCameraPosition().target.latitude, googleMaps.getCameraPosition().target.longitude, 1);
                    Address address = addresses.get(0);
                    String add = address.getAddressLine(0);
                    LatLng pickUpLatLog = new LatLng(address.getLatitude(), address.getLongitude());
                    AllLatLng.put("DropLocationMapValue", pickUpLatLog);
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
                    ConfirmRideFragment confirmRideFragment = new ConfirmRideFragment(AllLatLng, latitude_logitude, AddressMap);
                    getSupportFragmentManager().beginTransaction().replace(R.id.confirRide, confirmRideFragment).commit();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void locationpic(View view) {
        Map data;
        SharedpreferencesUser sharedpreferencesUser = new SharedpreferencesUser(MainActivity.this);
        data = sharedpreferencesUser.getShare();
        String location = null;
        Object Objlocation = data.get(LOCATION_CHANGE);
        if (Objlocation != null) {
            location = Objlocation.toString();
        }
        String tagIdLocation = view.getTag().toString();

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
                DropSearchFragment dropSearchFragment = new DropSearchFragment();
//                dropSearchFragment.show(getSupportFragmentManager(), "droplocationfra");
                getSupportFragmentManager().beginTransaction().replace(R.id.search_fragment, dropSearchFragment).commit();
                dropSearchFragment.Dropvalue(new DropSearchFragment.dropValuePass() {
                    @Override
                    public void dropLOcationPlaceName(String value) {

                        dropSearchFragment.dismiss();
                        dropLocationPlaceInt = value;
                        droplocationmethod(dropLocationPlaceInt);
                        blue_pin_icon.setVisibility(View.GONE);
                        red_pin_icon.setImageResource(R.drawable.red_pin_icon);
                        red_pin_icon.setVisibility(View.VISIBLE);
                    }
                });

            } else if (location == tagIdLocation) {
                DropSearchFragment dropSearchFragment = new DropSearchFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.search_fragment, dropSearchFragment).commit();
//                dropSearchFragment.show(getSupportFragmentManager(),"droplocationfra");
                dropSearchFragment.Dropvalue(new DropSearchFragment.dropValuePass() {
                    @Override
                    public void dropLOcationPlaceName(String value) {
                        Toast.makeText(MainActivity.this, "" + value, LENGTH_SHORT).show();
                        dropSearchFragment.dismiss();
                        dropLocationPlaceInt = value;
                        droplocationmethod(dropLocationPlaceInt);
                        blue_pin_icon.setVisibility(View.GONE);
                        red_pin_icon.setImageResource(R.drawable.red_pin_icon);
                        red_pin_icon.setVisibility(View.VISIBLE);

                    }
                });

            } else {
                if (location.equals("pickLocations")) {
                    if (droplocation.getText().toString().equals("Enter drop location")) {
                        DropSearchFragment dropSearchFragment = new DropSearchFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.search_fragment, dropSearchFragment).commit();
//                        dropSearchFragment.show(getSupportFragmentManager(),"droplocationfra");
                        dropSearchFragment.Dropvalue(new DropSearchFragment.dropValuePass() {
                            @Override
                            public void dropLOcationPlaceName(String value) {
                                dropSearchFragment.dismiss();
                                dropLocationPlaceInt = value;
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
                    }
                }
            }
        }
        sharedpreferencesUser.location(tagIdLocation);
        this.clikalablu = view.getTag().toString();
    }

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        confirRide.setVisibility(View.GONE);
        search_fragment.setVisibility(View.GONE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);


    }


}

