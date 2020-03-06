package tech.iwish.taxi.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.navigation.NavigationView;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.WebSocketListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.iwish.taxi.config.Constants;
import tech.iwish.taxi.connection.ConnectionServer;
import tech.iwish.taxi.connection.JsonHelper;
import tech.iwish.taxi.diractionAPi.Common;
import tech.iwish.taxi.diractionAPi.IGoogleApi;
import tech.iwish.taxi.R;
import tech.iwish.taxi.config.SharedpreferencesUser;
import tech.iwish.taxi.fragment.AboutFragmnet;
import tech.iwish.taxi.fragment.ConfirmRideFragment;
import tech.iwish.taxi.fragment.OfferFragment;
import tech.iwish.taxi.fragment.OutstationVehicleshowFragment;
import tech.iwish.taxi.fragment.Pickup_Search_fragment;
import tech.iwish.taxi.fragment.RateCardFragment;
import tech.iwish.taxi.fragment.ReferEarnFragment;
import tech.iwish.taxi.fragment.RentalFragment;
import tech.iwish.taxi.fragment.RentalMainFragment;
import tech.iwish.taxi.fragment.RideHistoryFragment;
import tech.iwish.taxi.fragment.Search_dropFragment;
import tech.iwish.taxi.fragment.SupportFragmnet;
import tech.iwish.taxi.fragment.WalletFragment;
import tech.iwish.taxi.other.VehicleListWs;
import tech.iwish.taxi.websocket.SocketService;

import static java.lang.Math.toDegrees;
import static tech.iwish.taxi.config.SharedpreferencesUser.LOCATION_CHANGE;
import static tech.iwish.taxi.config.SharedpreferencesUser.OTP_CONFIRM_DRIVER;
import static tech.iwish.taxi.config.SharedpreferencesUser.TRACK_ID;
import static tech.iwish.taxi.config.SharedpreferencesUser.USER_CONTACT;
import static tech.iwish.taxi.config.SharedpreferencesUser.VEHICLE_DATA;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        NavigationView.OnNavigationItemSelectedListener,
        GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveCanceledListener,
        GoogleMap.OnCameraIdleListener,
        GoogleMap.OnCameraMoveListener {


    public TextView pickup , pic;
    public TextView droplocation;
    public TextView rentail , drop;
    public String driverOfflineCheck;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location currentlocation;
    private int STORAGE_PERMISSION_CODE = 2;
    private PlacesClient placesClient;
    private GoogleMap googleMaps;
    private Map data = null;
    private ImageView blue_pin_icon, red_pin_icon;
    public Map<String, LatLng> AllLatLng = new HashMap<String, LatLng>();
    public Map<String, Double> latitude_logitude = new HashMap<String, Double>();
    public Map<String, String> AddressMap = new HashMap<>();
    boolean doubleBackToExitPressedOnce = false;
    private Double latD, log;
    private FrameLayout confirRide, search_fragment, confirmRideLoad;
    private String clikalablu, rentalClickAble, pickupLocationNameIntent, pickupclickvalueget, droplocationchecks;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private LinearLayout dailtLayout, rentalLayout, outStationLayout, aa, bottom;
    private NavigationView navigationView;
    private String pickupPlaceNameInt;
    private DirectionsRoute currentRoute;
    private String dropLocationPlaceInt;
    private LocationManager locationManager;
    private final int FIVE_SECONDS = 1;
    private final int ONE_SECONDS = 5000;
    private List<VehicleListWs> vehicleListWs = new ArrayList<>();
    private IGoogleApi mService;
    private List<LatLng> polylineList;
    private Handler handler;
    private Polyline blackPolyline, greyPolyline;
    private float v;
    private Double lat, lng, lats;
    private int index, next;
    private LatLng startPosition, endPosition, vehicleLatlng;
    private Marker marker;
    private SharedpreferencesUser sharedpreferencesUser;
    private GoogleApiClient mGoogleApiClient;
    private LatLng latlngvehicles;
    private GoogleMap.CancelableCallback callback;
    private ConfirmRideFragment confirmRideFragment;
    private WebSocketListener webSocketListener;
    private final static String TAG_FRAGMENT = "TAG_FRAGMENT";
    private final static String CONFIR_FRAGMENT_RIDE = "CONFIR_FRAGMENT_RIDE";
    private Fragment rateCard;
    boolean checkinit = false;
    private SocketService socketService;
    private String rentalClick = "daily";
    private Toolbar toolbar;
    private Button rantalPackage, outstationButton, dailyButton, navigation_Button;
    private AlertDialog.Builder builder;
    private View dialogView;
    private KProgressHUD kProgressHUD;
    private String rentalclick;
    private Search_dropFragment search_dropFragment;
    private static final String TAG = "MainActivity";
    private boolean socketcheck = true;
    private boolean backValid = false;


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
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
        aa = (LinearLayout) findViewById(R.id.aa);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        rantalPackage = (Button) findViewById(R.id.rantalPackage);
        outstationButton = (Button) findViewById(R.id.outstationButton);
        dailyButton = (Button) findViewById(R.id.dailyButton);
        navigation_Button = (Button) findViewById(R.id.navigation_Button);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        bottom = (LinearLayout) findViewById(R.id.bottom);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drop = (TextView)findViewById(R.id.drop);
        pic = (TextView)findViewById(R.id.pic);

//        View headerLayout = navigationView.getHeaderView(0);
//        TextView headermobile = View.findViewById(R.id.headermobile);


        sharedpreferencesUser = new SharedpreferencesUser(this);
        data = sharedpreferencesUser.getShare();
//        headermobile.setText(data.get(USER_CONTACT).toString());
        navigation_Button.setVisibility(View.GONE);

        setTitle("Home");

        Object otp_confirm = data.get(OTP_CONFIRM_DRIVER);
        String a = getIntent().getStringExtra("checkDriverData");
        if (otp_confirm == null) {
            if (a != null) {

                String driver_name = getIntent().getStringExtra("driver_name");
                String driver_number = getIntent().getStringExtra("driver_number");
                String otp = getIntent().getStringExtra("otp");
                builder = new android.app.AlertDialog.Builder(MainActivity.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.row_driver_info_layout, viewGroup, false);
                builder.setView(dialogView);
                TextView call_driver = dialogView.findViewById(R.id.call_driver);
                TextView name_drivers = dialogView.findViewById(R.id.name_drivers);
                TextView otp_1 = dialogView.findViewById(R.id.otp_1);
                TextView otp_2 = dialogView.findViewById(R.id.otp_2);
                TextView otp_3 = dialogView.findViewById(R.id.otp_3);
                TextView otp_4 = dialogView.findViewById(R.id.otp_4);
                name_drivers.setText(driver_name);
                char[] breack_otp = (otp).toCharArray();
                otp_1.setText(String.valueOf(breack_otp[0]));
                otp_2.setText(String.valueOf(breack_otp[1]));
                otp_3.setText(String.valueOf(breack_otp[2]));
                otp_4.setText(String.valueOf(breack_otp[3]));
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        } else {
            pickup.setVisibility(View.GONE);
            droplocation.setVisibility(View.GONE);
            aa.setVisibility(View.VISIBLE);
            rantalPackage.setVisibility(View.GONE);
            outstationButton.setVisibility(View.GONE);
            dailyButton.setVisibility(View.GONE);
            bottom.setVisibility(View.GONE);
            navigation_Button.setVisibility(View.VISIBLE);
            drop.setVisibility(View.GONE);
            pic.setVisibility(View.GONE);
        }

        View headerNavigation = navigationView.inflateHeaderView(R.layout.header_navigation);
        View panel = headerNavigation.findViewById(R.id.header_navigationss);
        panel.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
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
        outstationButton.setVisibility(View.GONE);

//      Button click all set
        dailyButton.setOnClickListener((View) -> {
            if (droplocation.getText().toString().equals("Enter drop location")) {
//                   data.get(LOCATION_CHANGE)
                sharedpreferencesUser.location("dropLocations");
                dropLocationSearch();
            } else {

                if (AddressMap.get("DropLocationCityName").equals(AddressMap.get("PickupCityName"))) {
                    confirmRideFragment = new ConfirmRideFragment(AllLatLng, latitude_logitude, AddressMap);
                    getSupportFragmentManager().beginTransaction().replace(R.id.confirRide, confirmRideFragment, CONFIR_FRAGMENT_RIDE).commit();
                } else {
                    View dialogView = LayoutInflater.from(this).inflate(R.layout.alert_outstation, null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setView(dialogView);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    Button changePlace = (Button) dialogView.findViewById(R.id.changePlace);
                    Button continueOutstaion = (Button) dialogView.findViewById(R.id.continueOutstaion);
                    changePlace.setOnClickListener(view -> {
                        alertDialog.dismiss();
                        dropLocationSearch();
                    });
                    continueOutstaion.setOnClickListener(view -> {

                    });
                }
            }
        });
        rantalPackage.setOnClickListener((View) -> {
            setProgressDialog("Packshow");
            RentalMainFragment rentalMainFragment = new RentalMainFragment(latitude_logitude, AddressMap);
            getSupportFragmentManager().beginTransaction().replace(R.id.search_fragment, rentalMainFragment).commit();
            rantalPackage.setVisibility(View.GONE);
            bottom.setVisibility(View.GONE);
            googleMaps.getUiSettings().setScrollGesturesEnabled(false);
            remove_progress_Dialog();
        });
        outstationButton.setOnClickListener(view -> {
            if (droplocation.getText().toString().equals("Enter drop location")) {
                sharedpreferencesUser.location("dropLocations");
                dropLocationSearch();
            } else {
                OutstationVehicleshowFragment outstationVehicleshowFragment = new OutstationVehicleshowFragment(AllLatLng, latitude_logitude, AddressMap);
                getSupportFragmentManager().beginTransaction().replace(R.id.search_fragment, outstationVehicleshowFragment).commit();
                outstationButton.setVisibility(View.GONE);
                bottom.setVisibility(View.GONE);
                googleMaps.getUiSettings().setScrollGesturesEnabled(false);
            }
        });


        kProgressHUD = new KProgressHUD(this);
        String otpconfirmRide = getIntent().getStringExtra("otpconfirmRide");
        if (otpconfirmRide != null) {

            pickup.setVisibility(View.GONE);
            droplocation.setVisibility(View.GONE);
            aa.setVisibility(View.VISIBLE);
            rantalPackage.setVisibility(View.GONE);
            outstationButton.setVisibility(View.GONE);
            dailyButton.setVisibility(View.GONE);
            bottom.setVisibility(View.GONE);

        }


        navigation_Button.setOnClickListener(view -> {


            String trackingid = data.get(TRACK_ID).toString();
            ConnectionServer connectionServer = new ConnectionServer();
            connectionServer.set_url(Constants.TRACKING_CHECK);
            connectionServer.requestedMethod("POST");
            connectionServer.buildParameter("trackingId",trackingid);
            Log.e("trackingId",trackingid);
            connectionServer.execute(output -> {
                Log.e("output",output);
                JsonHelper jsonHelper = new JsonHelper(output);
                if(jsonHelper.isValidJson()){
                    String response = jsonHelper.GetResult("response");
                    if(response.equals("TRUE")){
                        JSONArray jsonArray = jsonHelper.setChildjsonArray(jsonHelper.getCurrentJsonObj(), "data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonHelper.setChildjsonObj(jsonArray, i);
                            Intent intent = new Intent(MainActivity.this , BillActivity.class);
                            intent.putExtra("booking_confirm","booking_confirm");

                            String picadd = jsonHelper.GetResult("pickup_city_name");
                            String dropadd = jsonHelper.GetResult("drop_city_name");
                            String amt = jsonHelper.GetResult("amount");

                            Log.e("pic",picadd);
                            Log.e("pic",dropadd);

                            intent.putExtra("pickaddress",picadd);
                            intent.putExtra("dropaddress",dropadd);
                            intent.putExtra("amount",amt);

                            startActivity(intent);
                        }
                    }else{
                        Toast.makeText(this, "Ride not Complete", Toast.LENGTH_SHORT).show();
                    }
                }
            });


//            Intent intent = new Intent(MainActivity.this , BillActivity.class);
//            intent.putExtra("booking_confirm","booking_confirm");
//            startActivity(intent);

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
//            case R.id.offer:
//                setTitle("Offer");
//                OfferFragment offerFragment = new OfferFragment();
//                getSupportFragmentManager().beginTransaction().replace(R.id.search_fragment, offerFragment).addToBackStack("tag").commit();
//                break;
//            case R.id.refer_earn:
//                setTitle("Trfer earn");
//                ReferEarnFragment referEarnFragment = new ReferEarnFragment();
//                getSupportFragmentManager().beginTransaction().replace(R.id.search_fragment, referEarnFragment).addToBackStack("tag").commit();
//                break;
            case R.id.support:
                setTitle("Support");
                SupportFragmnet supportFragmnet = new SupportFragmnet();
                getSupportFragmentManager().beginTransaction().replace(R.id.search_fragment, supportFragmnet).addToBackStack("tag").commit();
                break;
//            case R.id.about:
//                setTitle("About");
//                AboutFragmnet aboutFragmnet = new AboutFragmnet();
//                getSupportFragmentManager().beginTransaction().replace(R.id.search_fragment, aboutFragmnet).addToBackStack("tag").commit();
//                break;
            case R.id.logout:
                sharedpreferencesUser.logout();
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);

        }

        pickup.setVisibility(View.GONE);
        droplocation.setVisibility(View.GONE);
        dailyButton.setVisibility(View.GONE);
        outstationButton.setVisibility(View.GONE);
        rantalPackage.setVisibility(View.GONE);
        navigation_Button.setVisibility(View.GONE);

        this.backValid = false;


        bottom.setVisibility(View.GONE);
        googleMaps.getUiSettings().setScrollGesturesEnabled(false);

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void fetchLastLocation() {
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(location -> {
            if (location != null) {
                currentlocation = location;
                SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                supportMapFragment.getMapAsync(MainActivity.this);
            }
        });
    }


    private void GpsRetry(Location location){
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (location != null) {
                    currentlocation = location;
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    supportMapFragment.getMapAsync(MainActivity.this);
                    timer.cancel();
                }  else{
                    fetchLastLocation();
                }
            }
        },0,1000);
    }





    @Override
    public void onMapReady(final GoogleMap googleMap) {
        googleMaps = googleMap;
        googleMap.setOnMapClickListener(this);
        googleMap.setOnCameraMoveStartedListener(this);
        googleMap.setOnCameraMoveCanceledListener(this);
        googleMap.setOnCameraIdleListener(this);
        googleMap.setOnCameraMoveListener(this);
//        googleMap.setOnMyLocationClickListener(this);
//        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
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
                String add = addresses.get(0).getAddressLine(0);
                String cityName = addresses.get(0).getLocality();
                String stateName = addresses.get(0).getAdminArea();
                String streetName = addresses.get(0).getFeatureName();

                AddressMap.put("PickupCityName", cityName);
                AddressMap.put("PickupstateName", stateName);
                AddressMap.put("PickupcountryName", streetName);
                AddressMap.put("Pickupfulladress", add);

                if (!checkinit) {
                    this.checkinit = true;
                    boolean boll = sharedpreferencesUser.getSocketConnection();
                    if (!boll) {
                        this.socketcheck = false;
                        socketService = new SocketService();
                        socketService.  SocketService(this, AllLatLng, latitude_logitude, AddressMap);
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        socketService.onStartCommand(intent, 1, 1);
                        startService(intent);
                    }
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


/*
           Intent intent = new Intent( Intent.ACTION_VIEW,
                    Uri.parse("http://ditu.google.cn/maps?f=d&source=s_d"+"&saddr="+currentlocation.getLatitude()+","+currentlocation.getLongitude()+"&daddr=26.238787,78.178590&hl=zh&t=m&dirflg=d"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK&Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            startActivity(intent);
*/


            VehicleCheckOnline();
        }
    }


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

*/

/*
        marker = googleMaps.addMarker(new MarkerOptions().position(aa)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.car_icon)));
        animateMarker(currentlocation ,marker );

//                googleMaps.clear();
//                Marker marker = new Marker().remove();


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

                dailyButton.setVisibility(View.VISIBLE);
                confirRide.setVisibility(View.VISIBLE);
                outstationButton.setVisibility(View.GONE);

                this.rentalClick = "daily";
                dailtLayout.setBackground(getDrawable(R.drawable.bottom_design));
                rantalPackage.setVisibility(View.GONE);
                DailyLayoutMethod();
                break;
            case R.id.rentalLayout:
                this.rentalClick = "rental";
                dailyButton.setVisibility(View.GONE);
                rantalPackage.setVisibility(View.VISIBLE);
                outstationButton.setVisibility(View.GONE);
                drop.setVisibility(View.GONE);
                rentalLayout.setBackground(getDrawable(R.drawable.bottom_design));
                RentalMethod();
                this.rentalClickAble = view.getTag().toString();
                break;
            case R.id.outStationLayout:
                this.rentalClick = "outstation";

                dailyButton.setVisibility(View.GONE);
                outstationButton.setVisibility(View.VISIBLE);
                rantalPackage.setVisibility(View.GONE);

                droplocation.setVisibility(View.VISIBLE);
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
        googleMaps.animateCamera(CameraUpdateFactory.newLatLngZoom(AllLatLng.get("pickMapValue"), 14), 1000, null);
        sharedpreferencesUser.location("pickLocations");


    }

    @Override
    public void onCameraIdle() {

//        SharedpreferencesUser sharedpreferencesUser = new SharedpreferencesUser(MainActivity.this);
        Map data = sharedpreferencesUser.getShare();
        Object clickValue = data.get(LOCATION_CHANGE);


        switch (rentalClick) {
            case "daily":
            case "outstation":
                if (clickValue != null)
                    pickupLocationNameIntent = clickValue.toString();

                if (pickupLocationNameIntent != null) {
                    if (pickupLocationNameIntent.equals("pickLocations")) {
                        Pickupmethod(pickupPlaceNameInt);
                    } else {
                        droplocationmethod(dropLocationPlaceInt);
                    }
                } else {
                    currentKocationGet();
                }
                break;
            case "rental":
                break;
        }


    }


    private void Pickupmethod(String value) {
//        SharedpreferencesUser sharedpreferencesUser = new SharedpreferencesUser(MainActivity.this);
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
                    String add = addresses.get(0).getAddressLine(0);
                    String cityName = addresses.get(0).getLocality();
                    String stateName = addresses.get(0).getAdminArea();
                    String streetName = addresses.get(0).getFeatureName();
                    LatLng pickUpLatLog = new LatLng(address.getLatitude(), address.getLongitude());
                    googleMaps.animateCamera(CameraUpdateFactory.newLatLngZoom(pickUpLatLog, 15), 200, null);
                    AllLatLng.put("pickMapValue", pickUpLatLog);
                    latitude_logitude.put("PickLatitude", googleMaps.getCameraPosition().target.latitude);
                    latitude_logitude.put("PickLogitude", googleMaps.getCameraPosition().target.longitude);
                    AddressMap.put("PickupCityName", cityName);
                    AddressMap.put("PickupstateName", stateName);
                    AddressMap.put("PickupStretName", streetName);
                    AddressMap.put("Pickupfulladress", add);

                    pickup.setText(add);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (pickupLocationNameIntent.equals("pickLocations")) {
                currentKocationGet();
            }
        }

    }

    private void droplocationmethod(String droplocationvalue) {

//        SharedpreferencesUser sharedpreferencesUser = new SharedpreferencesUser(MainActivity.this);
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
                    googleMaps.animateCamera(CameraUpdateFactory.newLatLngZoom(dropLocatinsUpLatLog, 15), 200, null);
                    droplocation.setText(add);
                    latitude_logitude.put("dropLatitude", dropLocatinsUpLatLog.latitude);
                    latitude_logitude.put("dropLongitude", dropLocatinsUpLatLog.longitude);
                    AddressMap.put("DropLocationCityName", cityName);
                    AddressMap.put("DropLocationstateName", stateName);
                    AddressMap.put("DropLocationcountryName", streetName);
                    AddressMap.put("Address_DropLocation", add);


                    blue_pin_icon.setVisibility(View.GONE);
                    red_pin_icon.setVisibility(View.VISIBLE);
                    remove_progress_Dialog();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (pickupLocationNameIntent.equals("dropLocations")) {
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

//                    getSupportFragmentManager().beginTransaction().remove(confirmRideFragment).commit();
                    droplocation.setText(add);
                    String cityName = addresses.get(0).getLocality();
                    String stateName = addresses.get(0).getAdminArea();
                    String streetName = addresses.get(0).getFeatureName();
                    AddressMap.put("DropLocationCityName", cityName);
                    AddressMap.put("DropLocationstateName", stateName);
                    AddressMap.put("DropLocationcountryName", streetName);
                    AddressMap.put("Address_DropLocation", add);
                    droplocation.setText(add);
                    latitude_logitude.put("dropLatitude", googleMaps.getCameraPosition().target.latitude);
                    latitude_logitude.put("dropLongitude", googleMaps.getCameraPosition().target.longitude);
                    confirRide.setVisibility(View.VISIBLE);
                    search_fragment.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    public void rental(String data) {

        this.rentalclick = null;

        if (rentalclick == null) {
            this.rentalclick = "rentalclicks";
            Geocoder geocoder = new Geocoder(MainActivity.this);
            try {
                List<Address> addresses = geocoder.getFromLocationName(data, 1);
                Address address = addresses.get(0);

                String add = addresses.get(0).getAddressLine(0);
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
                pickup.setText(add);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
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

    private void outstatiopndroplocation(String droplocationvalue) {
//        SharedpreferencesUser sharedpreferencesUser = new SharedpreferencesUser(MainActivity.this);
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
                    blue_pin_icon.setVisibility(View.GONE);
                    red_pin_icon.setVisibility(View.VISIBLE);
                    directionUrl();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (pickupLocationNameIntent.equals("dropLocations")) {
                Geocoder geocoder = new Geocoder(MainActivity.this);
                try {
                    List<Address> addresses = geocoder.getFromLocation(googleMaps.getCameraPosition().target.latitude, googleMaps.getCameraPosition().target.longitude, 1);
                    Address address = addresses.get(0);
                    String add = address.getAddressLine(0);
                    LatLng dropLocatinsUpLatLog = new LatLng(address.getLatitude(), address.getLongitude());
                    AllLatLng.put("DropLocationMapValue", dropLocatinsUpLatLog);
                    googleMaps.animateCamera(CameraUpdateFactory.newLatLngZoom(dropLocatinsUpLatLog, 15), 200, null);
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
                    directionUrl();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    public void locationpic(View view) {

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
            case "outstation":
                if (tagIdLocation.equals("pickLocations")) {
                    if (location == null) {

                        PickUpLocationSearch();
                    } else if (location == tagIdLocation) {
                        PickUpLocationSearch();
                    } else {
                        if (location.equals("dropLocations")) {
                            if (pickupPlaceNameInt != null) {
//                                MarkerOptions markerOptions = new MarkerOptions().position(AllLatLng.get("pickMapValue")).title("I m here").visible(false);
                                googleMaps.animateCamera(CameraUpdateFactory.newLatLngZoom(AllLatLng.get("pickMapValue"), 15), 200, null);
//                                googleMaps.addMarker(markerOptions);
                                blue_pin_icon.setVisibility(View.VISIBLE);
                                red_pin_icon.setVisibility(View.GONE);
                                pickup.setAlpha(1);
                                droplocation.setAlpha((float) 0.6);
                            } else {
                                PickUpLocationSearch();
                            }
                        }
                    }
                }
                if (tagIdLocation.equals("dropLocations")) {
                    if (location == null) {
                        dropLocationSearch();

                    } else if (location == tagIdLocation) {
                        search_fragment.setVisibility(View.VISIBLE);
                        confirRide.setVisibility(View.VISIBLE);
                        dropLocationSearch();
                    } else {
                        if (location.equals("pickLocations")) {
                            if (droplocation.getText().toString().equals("Enter drop location")) {
                                confirRide.setVisibility(View.VISIBLE);
                                dropLocationSearch();
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
                rentalFragment.setRentalDatas(data1 -> {
                    rental(data1);
                    getSupportFragmentManager().beginTransaction().remove(rentalFragment).commit();
                });
                break;
            default:
                break;
        }


        sharedpreferencesUser.location(tagIdLocation);
        this.clikalablu = view.getTag().toString();
    }

    private void currentKocationGet() {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(googleMaps.getCameraPosition().target.latitude, googleMaps.getCameraPosition().target.longitude, 1);
            Address address = addresses.get(0);
            String add = addresses.get(0).getAddressLine(0);
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
            AddressMap.put("Pickupfulladress", add);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.confirmRideLoad);
        Fragment confirRideFrameLay = getSupportFragmentManager().findFragmentById(R.id.confirRide);
        Fragment search_fragmentss = getSupportFragmentManager().findFragmentById(R.id.search_fragment);

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            getSupportFragmentManager().beginTransaction().remove(confirRideFrameLay).commit();
            googleMaps.getUiSettings().setScrollGesturesEnabled(true);
        } else if (confirRideFrameLay != null) {
            getSupportFragmentManager().beginTransaction().remove(confirRideFrameLay).commit();
            setTitle("Home");
            googleMaps.getUiSettings().setScrollGesturesEnabled(true);
            bottom.setVisibility(View.VISIBLE);
        } else if (search_fragmentss != null) {
            if(backValid){
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            this.backValid = true ;
            getSupportFragmentManager().beginTransaction().remove(search_fragmentss).commit();
            setTitle("Home");
            googleMaps.getUiSettings().setScrollGesturesEnabled(true);
            bottom.setVisibility(View.VISIBLE);

            pickup.setVisibility(View.VISIBLE);
            droplocation.setVisibility(View.VISIBLE);
            dailyButton.setVisibility(View.VISIBLE);
            outstationButton.setVisibility(View.VISIBLE);
            rantalPackage.setVisibility(View.VISIBLE);
//            navigation_Button.setVisibility(View.VISIBLE);


        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return;
            }
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
            this.doubleBackToExitPressedOnce = true;
            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
        }
        switch (rentalClick) {
            case "daily":
                dailyButton.setVisibility(View.VISIBLE);
                rantalPackage.setVisibility(View.GONE);
                outstationButton.setVisibility(View.GONE);
                break;
            case "rental":
                dailyButton.setVisibility(View.GONE);
                rantalPackage.setVisibility(View.VISIBLE);
                outstationButton.setVisibility(View.GONE);
                break;
            case "outstation":
                dailyButton.setVisibility(View.GONE);
                rantalPackage.setVisibility(View.GONE);
                outstationButton.setVisibility(View.VISIBLE);
                break;
        }
    }


    public void directionUrl() {

//        String requestUrl = "https://maps.googleapis.com/maps/api/directions/json?mode=driving&transit_routing_preference=less_driving&origin=26.238996,78.181535&destination=26.243067,78.185623&key=AIzaSyApe8T3JMiqj9OgEbqd--zTBfl3fibPeEs";

//        String requestUrl = "https://maps.googleapis.com/maps/api/directions/json?mode=driving&transit_routing_preference=less_driving&" +
//                "origin=" + latitude_logitude.get("PickLatitude") + "," + latitude_logitude.get("PickLogitude") + "&destination=" +
//                latitude_logitude.get("dropLatitude") + "," + latitude_logitude.get("dropLongitude") + "&key=AIzaSyApe8T3JMiqj9OgEbqd--zTBfl3fibPeEs";

        String requestUrl = "https://maps.googleapis.com/maps/api/directions/json?mode=driving&transit_routing_preference=less_driving&" +
                "origin=" + currentlocation.getLatitude() + "," + currentlocation.getLongitude() + "&destination=" +
                latitude_logitude.get("dropLatitude") + "," + latitude_logitude.get("dropLongitude") + "&key=AIzaSyApe8T3JMiqj9OgEbqd--zTBfl3fibPeEs";

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

                        LatLng nsv = new LatLng(currentlocation.getLatitude(), currentlocation.getLongitude());
//                        googleMaps.animateCamera(CameraUpdateFactory.newLatLngZoom(nsv,17),100,null);
//                        googleMaps.getUiSettings().setTiltGesturesEnabled(true);
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(nsv)
                                .zoom(17.0f)
                                .bearing(10)
                                .tilt(90)
                                .build();
                        googleMaps.animateCamera( CameraUpdateFactory.zoomTo( 17.0f ) );
                        googleMaps.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 17,null);


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
/*
    public void directionUrl() {

//        String requestUrl = "https://maps.googleapis.com/maps/api/directions/json?mode=driving&transit_routing_preference=less_driving&origin=26.238996,78.181535&destination=26.243067,78.185623&key=AIzaSyApe8T3JMiqj9OgEbqd--zTBfl3fibPeEs";

//        String requestUrl = "https://maps.googleapis.com/maps/api/directions/json?mode=driving&transit_routing_preference=less_driving&" +
//                "origin=" + latitude_logitude.get("PickLatitude") + "," + latitude_logitude.get("PickLogitude") + "&destination=" +
//                latitude_logitude.get("dropLatitude") + "," + latitude_logitude.get("dropLongitude") + "&key=AIzaSyApe8T3JMiqj9OgEbqd--zTBfl3fibPeEs";

        String requestUrl = "https://maps.googleapis.com/maps/api/directions/json?mode=driving&transit_routing_preference=less_driving&" +
                "origin=" + currentlocation.getLatitude() + "," + currentlocation.getLongitude() + "&destination=" +
                latitude_logitude.get("dropLatitude") + "," + latitude_logitude.get("dropLongitude") + "&key=AIzaSyApe8T3JMiqj9OgEbqd--zTBfl3fibPeEs";

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
*/

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


/*

    private float getBearing(LatLng begin, LatLng end) {
        double lat = Math.abs(begin.latitude - end.latitude);
        double lng = Math.abs(begin.longitude - end.longitude);

        if (begin.latitude < end.latitude && begin.longitude < end.longitude)
            return (float) (toDegrees(Math.atan(lng / lat)));
        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
            return (float) ((90 - toDegrees(Math.atan(lng / lat))) + 90);
        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
            return (float) (toDegrees(Math.atan(lng / lat)) + 180);
        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
            return (float) ((90 - toDegrees(Math.atan(lng / lat))) + 270);
        return -1;
    }

*/

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


    public void setProgressDialog(String msg) {
        kProgressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(msg)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

    }

    public void remove_progress_Dialog() {
        kProgressHUD.dismiss();
    }

    public void VehicleCheckOnline() {

        handler = new Handler();
        handler.post(() -> new Timer().scheduleAtFixedRate(new TimerTask() {


            @Override
            public void run() {
//                sharedpreferencesUser = new SharedpreferencesUser(MainActivity.this);
                data = sharedpreferencesUser.getShare();
                Object jsondatass = data.get(VEHICLE_DATA);
                JSONObject jsonObject = null;
                if (jsondatass != null) {
                    try {
                        jsonObject = new JSONObject((String) jsondatass);
                        JSONObject data = jsonObject.getJSONObject("data");

                        String lat = data.getString("lat");
                        String logit = data.getString("long");
                        if (lat != null || logit != null) {
                            latD = Double.valueOf(lat);
                            log = Double.valueOf(logit);
                            LatLng latLng = new LatLng(latD, log);
                            new Thread() {
                                public void run() {
                                    runOnUiThread(() -> vehicleShow(latLng));
                                }
                            }.start();
                        }
                        sharedpreferencesUser.remove_vehicledata();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.e("run: ", "ofline");
                    offlineDriver();
//                    new Thread() {
//                        public void run() {
//                            runOnUiThread(() -> rantalPackage.setEnabled(false));
//                        }
//                    }.start();
                }
            }
        }, 30, 5000));


    }

    private void offlineDriver() {
        this.driverOfflineCheck = "offline";
    }

    private void vehicleShow(LatLng latLng) {
        this.driverOfflineCheck = "online";
        googleMaps.addMarker(new MarkerOptions().position(latLng).title("car").icon(BitmapDescriptorFactory.fromResource(R.drawable.car_icon)));
    }

    private void dropLocationSearch() {
        search_dropFragment = new Search_dropFragment(latitude_logitude);
        getSupportFragmentManager().beginTransaction().replace(R.id.search_fragment, search_dropFragment).commit();
        confirRide.setVisibility(View.VISIBLE);
        search_dropFragment.setValueDrop(data13 -> {
            remove_progress_Dialog();
            search_dropFragment.dismiss();
            dropLocationPlaceInt = data13;
            droplocationmethod(dropLocationPlaceInt);
            blue_pin_icon.setVisibility(View.GONE);
            red_pin_icon.setImageResource(R.drawable.red_pin_icon);
            red_pin_icon.setVisibility(View.VISIBLE);
            droplocationchecks = null;
        });

    }

    private void PickUpLocationSearch() {
        Pickup_Search_fragment pickup_search_fragment = new Pickup_Search_fragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.search_fragment, pickup_search_fragment).commit();
        pickup_search_fragment.valuePickup(location14 -> {
            pickup_search_fragment.dismiss();
            pickupPlaceNameInt = location14;
            Pickupmethod(pickupPlaceNameInt);
            blue_pin_icon.setVisibility(View.VISIBLE);
            red_pin_icon.setVisibility(View.GONE);
        });
    }

//    private void confirnRideNavigationon() {
//        directionUrl();
//    }

}