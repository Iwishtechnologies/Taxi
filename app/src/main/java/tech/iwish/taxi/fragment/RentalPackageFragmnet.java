package tech.iwish.taxi.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import tech.iwish.taxi.Interface.RentalVehicleInterface;
import tech.iwish.taxi.R;
import tech.iwish.taxi.RecyclerTouchListener;
import tech.iwish.taxi.activity.LoginActivity;
import tech.iwish.taxi.activity.SignupActivity;
import tech.iwish.taxi.adapter.PackageAdapter;
import tech.iwish.taxi.adapter.VehicleAdapter;
import tech.iwish.taxi.config.Constants;
import tech.iwish.taxi.config.SharedpreferencesUser;
import tech.iwish.taxi.connection.ConnectionServer;
import tech.iwish.taxi.connection.JsonHelper;
import tech.iwish.taxi.other.PackageVehicle;
import tech.iwish.taxi.other.VehicleList;

import static tech.iwish.taxi.config.SharedpreferencesUser.USER_CONTACT;


public class RentalPackageFragmnet extends Fragment {

    private RecyclerView pakagerecycleview;
    private List<PackageVehicle> packageVehicles = new ArrayList<>();
    public Map<String, Double> latitude_logitude;
    Map<String, String> AddressMap;
    private KProgressHUD kProgressHUD;
    PackageAdapter packageAdapter;
    RentalVehicleInterface rentalVehicleInterface;
    Map data;
    String JSONDATA;
    SharedpreferencesUser sharedpreferencesUser;
    Bundle bundle;

    public RentalPackageFragmnet(Map<String, Double> latitude_logitude, Map<String, String> addressMap, RentalVehicleInterface rentalVehicleInterface) {
        this.latitude_logitude = latitude_logitude;
        this.AddressMap = addressMap;
        this.rentalVehicleInterface = rentalVehicleInterface;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rental_package_fragmnet, null);

        pakagerecycleview = (RecyclerView) view.findViewById(R.id.pakagerecycleview);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        pakagerecycleview.setLayoutManager(linearLayoutManager);
        kProgressHUD = new KProgressHUD(getContext());


        pakagerecycleview.addOnItemTouchListener(new RecyclerTouchListener(getContext(), pakagerecycleview, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

//                rentalVehicleSelect();
//                rentalVehicleInterface.rentalvehicalInterface(packageVehicles.get(position).getPackid(),packageVehicles);
                sharedpreferencesUser = new SharedpreferencesUser(Objects.requireNonNull(getActivity()));
                data = sharedpreferencesUser.getShare();
                Object mobile = data.get(USER_CONTACT);
                setProgressDialog("");
                JSONDATA = "{ \"type\" : \"Rental\" , \"request\" :\"" + packageVehicles.get(position).getVahicle() + "\", \"TimeDuration\" :\"" + packageVehicles.get(position).getPackage_type() + "\" ,\"pickUpLat\" : \"" + latitude_logitude.get("PickLatitude") + "\"  ,\"pickUpLong\" : \"" + latitude_logitude.get("PickLogitude") + "\"  , \"userType\" : \"client\"  , \"userID\" : \"" + mobile.toString() + "\" , \"PickupCityName\" : \"" + AddressMap.get("PickupCityName") + " \"}";
                sharedpreferencesUser.getDatasWebsocket(JSONDATA);

                bundle = new Bundle();
                Timer timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {

                    @Override
                    public void run() {
                        String driverData = sharedpreferencesUser.driverReturnData();
                        if (driverData != null) {

                            Log.e("driverData", driverData);

//                            JSONObject jsonObject = new JSONObject(data);
//                            try {
//                                String type = jsonObject.getString("type");
//                                Log.e("rentaldriverData: ",type);
//                                if(type.equals("vehicleAccept")){
                            rentaldriverData(driverData);
//                                }else {
//                                    Toast.makeText(getActivity(), "Driver n0t online", Toast.LENGTH_SHORT).show();
//                                }
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                                Toast.makeText(getActivity(), "Driver n0t online", Toast.LENGTH_SHORT).show();
//                                timer.cancel();
//                            }
                            timer.cancel();

                        }
                    }
                }, 0, 1000);


            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        datainsert();
/*
        setProgressDialog("Package Search");
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

                        packageVehicles.add(new PackageVehicle(jsonHelper.GetResult("packid"), jsonHelper.GetResult("package_type"), jsonHelper.GetResult("vahicle_cat_id"), jsonHelper.GetResult("amount")));

                    }
                    PackageAdapter packageAdapter = new PackageAdapter(getActivity(), packageVehicles, latitude_logitude, AddressMap);
                    pakagerecycleview.setAdapter(packageAdapter);
                    remove_progress_Dialog();
                }
            }
        });
*/

        return view;
    }


    private void datainsert() {

        setProgressDialog("Package Search");


        OkHttpClient okHttpClient = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request1 = new Request.Builder().url(Constants.PACKAGEVEHICLE).post(body).build();



//        for (Call call : okHttpClient.dispatcher().queuedCalls()) {
//            if (call.request().tag().equals("requestKey"))
//                call.cancel();
//        }
//
//        //B) go through the running calls and cancel if the tag matches:
//        for (Call call : okHttpClient.dispatcher().runningCalls()) {
//            if (call.request().tag().equals("requestKey"))
//                call.cancel();
//        }


        okHttpClient.newCall(request1).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(getActivity(), "Connection Time out", Toast.LENGTH_SHORT).show();
                        remove_progress_Dialog();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    Log.e("result", result);
                    JsonHelper jsonHelper = new JsonHelper(result);
                    if (jsonHelper.isValidJson()) {
                        String responses = jsonHelper.GetResult("response");
                        if (responses.equals("TRUE")) {

                            JSONArray jsonArray = jsonHelper.setChildjsonArray(jsonHelper.getCurrentJsonObj(), "data");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonHelper.setChildjsonObj(jsonArray, i);
                                packageVehicles.add(new PackageVehicle(jsonHelper.GetResult("packid"), jsonHelper.GetResult("package_type"), jsonHelper.GetResult("vahicle_cat_id"), jsonHelper.GetResult("amount"), jsonHelper.GetResult("vahicle"), jsonHelper.GetResult("packageAmt")));
                            }

                            getActivity().runOnUiThread(() -> {

                                packageAdapter = new PackageAdapter(getActivity(), packageVehicles, latitude_logitude, AddressMap);
                                pakagerecycleview.setAdapter(packageAdapter);
                                remove_progress_Dialog();

                            });
                        } else {


                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), "No Packet", Toast.LENGTH_SHORT).show();
                                    remove_progress_Dialog();
                                }
                            });
                        }
                    }
                }
            }
        });

    }

    private void rentaldriverData(String data) {

        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONObject value = jsonObject.getJSONObject("data");
            String driverId = value.getString("driverId");
            String otp = value.getString("otp");
            String trackid = value.getString("trackid");

            bundle.putString("otp", otp);
            bundle.putString("driverId", driverId);
            bundle.putString("rental", "rental");
            bundle.putString("trackid", trackid);

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
                            bundle.putString("driver_name", jsonHelper.GetResult("DriverName"));
                            bundle.putString("driver_mobile", jsonHelper.GetResult("Mobile"));
                        }
                        RideConfiremDriverDetailsFragment rideConfiremDriverDetailsFragment = new RideConfiremDriverDetailsFragment();
                        FragmentManager fm = ((FragmentActivity) Objects.requireNonNull(getContext())).getSupportFragmentManager();
                        rideConfiremDriverDetailsFragment.setArguments(bundle);
                        fm.beginTransaction().replace(R.id.confirmRideLoad, rideConfiremDriverDetailsFragment).commit();


                        remove_progress_Dialog();
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Driver Offline", Toast.LENGTH_SHORT).show();
                        remove_progress_Dialog();
                    }
                });
            }
        }
    }


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
}
