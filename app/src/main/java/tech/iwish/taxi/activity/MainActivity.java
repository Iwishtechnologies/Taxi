package tech.iwish.taxi.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceGroup;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;

import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import tech.iwish.taxi.R;
import tech.iwish.taxi.fragment.ConfirmRideFragment;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    int AUTOCOMPLETE_REQUEST_CODE = 1;
    List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);
    TextView pickup, droplocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    Location currentlocation;
    private int STORAGE_PERMISSION_CODE = 2;
    PlacesClient placesClient;
    GoogleMap googleMaps;
    ImageView blue_pin_icon, red_pin_icon;
    public Map<String, LatLng> AllLatLng = new HashMap<String, LatLng>();
    public Map<String, Double> latitude_logitude = new HashMap<String, Double>();
    FrameLayout confirRide;
    String clikalablu;
    String dropLocationNameIntent;
    Firebase url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        blue_pin_icon = (ImageView) findViewById(R.id.blue_pin_icon);
        red_pin_icon = (ImageView) findViewById(R.id.red_pin_icon);
        pickup = (TextView) findViewById(R.id.pickup);
        droplocation = (TextView) findViewById(R.id.droplocation);
        confirRide = (FrameLayout) findViewById(R.id.confirRide);


        Firebase.setAndroidContext(this);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyB0J8NKrImr44cyuB7ZI6S2ZKbA5vhpou4");
        }
        placesClient = Places.createClient(MainActivity.this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();
        permisanLocation();

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

        blue_pin_icon.setImageResource(R.drawable.blue_pin_icon);
        if (googleMap != null) {
            googleMap.setMyLocationEnabled(true);
            final View locationButton = ((View) MainActivity.this.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            // position on right bottom
            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            rlp.setMargins(0, 0, 20, 50);
            googleMap.getMaxZoomLevel();
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            LatLng locationCurrent = new LatLng(currentlocation.getLatitude(), currentlocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions().position(locationCurrent).title("I m here").visible(false);
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(locationCurrent));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationCurrent, 5), 100, null);
            googleMap.addMarker(markerOptions);
            googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                @Override
                public void onCameraIdle() {

                    if (clikalablu != null) {

                        if (clikalablu.equals("pickLocations")) {
                            LatLng locationCurrents = new LatLng(googleMap.getCameraPosition().target.latitude, googleMap.getCameraPosition().target.longitude);
                            MarkerOptions markerOptions1 = new MarkerOptions().position(locationCurrents).visible(false);
                            googleMap.animateCamera(CameraUpdateFactory.newLatLng(locationCurrents));
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationCurrents, 15), 10, null);
                            googleMap.addMarker(markerOptions1);
                            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                            try {
                                List<Address> addresses = geocoder.getFromLocation(googleMap.getCameraPosition().target.latitude, googleMap.getCameraPosition().target.longitude, 1);
                                Address obj = addresses.get(0);
                                String add = obj.getAddressLine(0);
                                if (pickup.getText().toString().equals(add)) {
                                } else {

                                    pickup.setText(add);
                                    AllLatLng.put("PickupCurrentLocation", googleMap.getCameraPosition().target);
                                    latitude_logitude.put("PickLatitude", googleMap.getCameraPosition().target.latitude);
                                    latitude_logitude.put("PickLogitude", googleMap.getCameraPosition().target.longitude);


                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else if (clikalablu.equals("dropLocations")) {
                            Intent intent = getIntent();
                            dropLocationNameIntent = intent.getStringExtra("dropLocation");
                            if (dropLocationNameIntent != null) {

                                if (dropLocationNameIntent.equals(droplocation.getText().toString())) {
                                    Geocoder geocoder = new Geocoder(MainActivity.this);
                                    try {
                                        List<Address> addresses = geocoder.getFromLocationName(dropLocationNameIntent, 1);
                                        Address address = addresses.get(0);
                                        LatLng droplocation = new LatLng(address.getLatitude(), address.getLongitude());
                                        AllLatLng.put("DroplocationLatLng", droplocation);
                                        latitude_logitude.put("dropLatitude", address.getLatitude());
                                        latitude_logitude.put("dropLongitude", address.getLongitude());
//  *******************************************************************************************************************************
                                        AllLatLng.get("PickupCurrentLocation");
                                        ConfirmRideFragment confirmRideFragment = new ConfirmRideFragment(AllLatLng, latitude_logitude);
//  *******************************************************************************************************************************

                                        MarkerOptions markerOptionsss = new MarkerOptions().position(droplocation).title("dropLocation").visible(false);
                                        if (googleMaps != null) {
                                            googleMaps.animateCamera(CameraUpdateFactory.newLatLng(droplocation));
                                            googleMaps.animateCamera(CameraUpdateFactory.newLatLngZoom(droplocation, 15), 200, null);
                                            googleMaps.addMarker(markerOptionsss);
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                } else {
                                    Geocoder geocoder = new Geocoder(MainActivity.this);
                                    try {
                                        List<Address> addresses = geocoder.getFromLocationName(dropLocationNameIntent, 1);
                                        Address address = addresses.get(0);
                                        LatLng droplocation = new LatLng(address.getLatitude(), address.getLongitude());
                                        AllLatLng.put("DroplocationLatLng", droplocation);
                                        MarkerOptions markerOptionsss = new MarkerOptions().position(droplocation).title("dropLocation").visible(false);
                                        if (googleMaps != null) {
                                            googleMaps.animateCamera(CameraUpdateFactory.newLatLng(droplocation));
                                            googleMaps.animateCamera(CameraUpdateFactory.newLatLngZoom(droplocation, 15), 200, null);
                                            googleMaps.addMarker(markerOptionsss);
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                }

                            }

                        }
                    } else {
                        LatLng locationCurrents = new LatLng(googleMap.getCameraPosition().target.latitude, googleMap.getCameraPosition().target.longitude);
                        MarkerOptions markerOptions1 = new MarkerOptions().position(locationCurrents).visible(false);
                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(locationCurrents));
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationCurrents, 15), 10, null);
                        googleMap.addMarker(markerOptions1);
                        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(googleMap.getCameraPosition().target.latitude, googleMap.getCameraPosition().target.longitude, 1);
                            Address obj = addresses.get(0);
                            String add = obj.getAddressLine(0);

                            if (pickup.getText().toString().equals(add)) {
                            } else {
                                pickup.setText(add);
                                AllLatLng.put("PickupCurrentLocation", googleMap.getCameraPosition().target);
                                latitude_logitude.put("PickLatitude", googleMap.getCameraPosition().target.latitude);
                                latitude_logitude.put("PickLogitude", googleMap.getCameraPosition().target.longitude);

                            }


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    Intent intent = getIntent();
                    dropLocationNameIntent = intent.getStringExtra("dropLocation");
                    if (dropLocationNameIntent != null) {
                        if (droplocation.getText().toString() == dropLocationNameIntent) {
                        } else {

                            blue_pin_icon.setVisibility(View.GONE);
                            red_pin_icon.setVisibility(View.VISIBLE);
                            red_pin_icon.setBackgroundResource(R.drawable.red_pin_icon);
                            droplocation.setText(dropLocationNameIntent);
                            Geocoder geocoder = new Geocoder(MainActivity.this);
                            dropLocationNameIntent = intent.getStringExtra("dropLocation");
                            List<Address> addresses = null;
                            try {
                                addresses = geocoder.getFromLocationName(dropLocationNameIntent, 1);
                                Address address = addresses.get(0);

                                latitude_logitude.put("dropLatitude", address.getLatitude());
                                latitude_logitude.put("dropLongitude", address.getLongitude());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            clikalablu = "dropLocations";

                            ConfirmRideFragment confirmRideFragment = new ConfirmRideFragment(AllLatLng, latitude_logitude);
                            getSupportFragmentManager().beginTransaction().replace(R.id.confirRide, confirmRideFragment).commit();

                        }
                    }

                }
            });


        }

//        Geocoder geocoder = new Geocoder(MainActivity.this);
//        try {
//            List<Address> addresses = geocoder.getFromLocationName("Hannover, Germany",1);
//
//            Address address = addresses.get(0);
//            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
//            Toast.makeText(this, ""+latLng, LENGTH_SHORT).show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Toast.makeText(this, "" + place, LENGTH_SHORT).show();
                Log.i("getname", "Place: " + place.getName() + ", " + place.getId());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("getsta", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }


    public void locationpic(View view) {

        String tagIdLocation = view.getTag().toString();


        if (tagIdLocation.equals("pickLocations")) {
            if (clikalablu == view.getTag().toString()) {
                Intent intent = new Intent(MainActivity.this, PickupSearchActivity.class);
                startActivity(intent);
            } else {
                if (clikalablu == null) {

                } else if (clikalablu.equals("dropLocations")) {

                    red_pin_icon.setVisibility(View.GONE);
                    blue_pin_icon.setVisibility(View.VISIBLE);
                    LatLng currentlocationValue = AllLatLng.get("PickupCurrentLocation");
                    MarkerOptions markerOptions = new MarkerOptions().position(currentlocationValue).visible(false);
                    googleMaps.animateCamera(CameraUpdateFactory.newLatLngZoom(currentlocationValue, 15), 200, null);
                    googleMaps.addMarker(markerOptions);

//                    url = new Firebase("https://taxi-df33e.firebaseio.com/");
//                    Firebase firebase = url.child("Latlng");
//                    firebase.setValue("vikas");
//                    firebase.setValue(AllLatLng.get("PickupCurrentLocation"));

                } else {
                    Intent intent = new Intent(MainActivity.this, PickupSearchActivity.class);
                    startActivity(intent);
                }
            }
        }


        if (tagIdLocation.equals("dropLocations")) {

            if (clikalablu == view.getTag().toString()) {
                Intent intent = new Intent(MainActivity.this, DropPlaceLocation.class);
                startActivity(intent);


            } else {
                if (clikalablu == null) {
                    this.clikalablu = view.getTag().toString();
                    Intent intent = new Intent(MainActivity.this, DropPlaceLocation.class);
                    startActivity(intent);

                } else if (clikalablu.equals("pickLocations")) {

                    blue_pin_icon.setVisibility(View.GONE);
                    red_pin_icon.setVisibility(View.VISIBLE);
                    red_pin_icon.setBackgroundResource(R.drawable.red_pin_icon);
                    AllLatLng.get("DroplocationLatLng");

                }

            }
        }


        this.clikalablu = view.getTag().toString();

    }


}
















