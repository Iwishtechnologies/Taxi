package tech.iwish.taxi.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import tech.iwish.taxi.R;
import tech.iwish.taxi.adapter.PackageAdapter;
import tech.iwish.taxi.config.Constants;
import tech.iwish.taxi.config.SharedpreferencesUser;
import tech.iwish.taxi.connection.ConnectionServer;
import tech.iwish.taxi.connection.JsonHelper;
import tech.iwish.taxi.other.PackageVehicle;

import static tech.iwish.taxi.adapter.VehicleAdapter.JSONDATA;
import static tech.iwish.taxi.config.SharedpreferencesUser.USER_CONTACT;

public class Package_vehicle extends Fragment {

    private RecyclerView vehicle_package_recycle ;
    public Map<String, Double> latitude_logitude;
    Map data ;
    Map<String, String> AddressMap ;

    public Package_vehicle(Map<String, Double> latitude_logitude, Map<String, String> addressMap) {
        this.latitude_logitude = latitude_logitude ;
        this.AddressMap = addressMap ;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_package_vehicle,null);

        vehicle_package_recycle = (RecyclerView) view.findViewById(R.id.vehicle_package_recycle);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        vehicle_package_recycle.setLayoutManager(linearLayoutManager);

        SharedpreferencesUser sharedpreferencesUser = new SharedpreferencesUser(getActivity());
        data = sharedpreferencesUser.getShare();
        Object mobile = data.get(USER_CONTACT);

        JSONDATA = "{ \"type\" : \"Rental\" , \"request\" :\"rental\" ,\"pickUpLat\" : \"" + latitude_logitude.get("PickLatitude") + "\"  ,\"pickUpLong\" : \"" + latitude_logitude.get("PickLogitude") + "\"  , \"userType\" : \"client\"  , \"userID\" : \"" + mobile.toString() + "\" , \"PickupCityName\" : \"" + AddressMap.get("PickupCityName") + " \"}";
        sharedpreferencesUser.getDatasWebsocket(JSONDATA);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

//                        private String driverShow;

            @Override
            public void run() {
                String driverData = sharedpreferencesUser.driverReturnData();
                if (driverData != null) {
                    Log.e("driverData", driverData);
                    rentalVehicle(driverData);
                    timer.cancel();
                }
            }
        }, 0, 1000);



/*
        ConnectionServer connectionServer = new ConnectionServer();
        connectionServer.set_url(Constants.PACKAGEVEHICLE);
        connectionServer.requestedMethod("POST");
        connectionServer.execute(output -> {
            Log.e("output", output);
            JsonHelper jsonHelper = new JsonHelper(output);
            if (jsonHelper.isValidJson()) {
                String response = jsonHelper.GetResult("response");
                if (response.equals("TRUE")) {

                    JSONArray jsonArray = jsonHelper.setChildjsonArray(jsonHelper.getCurrentJsonObj(), "data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonHelper.setChildjsonObj(jsonArray, i);

//                        packageVehicles.add(new PackageVehicle(jsonHelper.GetResult("packid"), jsonHelper.GetResult("package_type"), jsonHelper.GetResult("vahicle_cat_id"), jsonHelper.GetResult("amount")));

                    }
//                    PackageAdapter packageAdapter = new PackageAdapter(getActivity(), packageVehicles);
//                    pakagerecycleview.setAdapter(packageAdapter);


                }
            }
        });
*/


        return view;
    }

    public void rentalVehicle(String data){

        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONObject value = jsonObject.getJSONObject("data");
            String driverId = value.getString("driverId");
            String otp = value.getString("otp");

/*
            ConnectionServer connectionServer = new ConnectionServer();
            connectionServer.set_url(Constants.DRIVERSHOW);
            connectionServer.requestedMethod("POST");
            connectionServer.buildParameter("driverId", driverId);
            connectionServer.execute(output -> {
                Log.e("output", output);
                JsonHelper jsonHelper = new JsonHelper(output);
                if (jsonHelper.isValidJson()) {
                    String response = jsonHelper.GetResult("response");
                    if (response.equals("TRUE")) {

                        JSONArray jsonArray = jsonHelper.setChildjsonArray(jsonHelper.getCurrentJsonObj(), "data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonHelper.setChildjsonObj(jsonArray, i);
                            bundle.putString("driver_name" , jsonHelper.GetResult("DriverName"));
                            bundle.putString("driver_mobile" , jsonHelper.GetResult("Mobile"));
                        }
                        RideConfiremDriverDetailsFragment rideConfiremDriverDetailsFragment = new RideConfiremDriverDetailsFragment();
                        FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
                        rideConfiremDriverDetailsFragment.setArguments(bundle);
                        fm.beginTransaction().replace(R.id.confirmRideLoad, rideConfiremDriverDetailsFragment, CONFIRM_FRAGMENT_RID).commit();
                        remove_progress_Dialog();
                    }
                }
            });
*/

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
