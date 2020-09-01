package tech.iwish.taxi.fragment;

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
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
import tech.iwish.taxi.R;
import tech.iwish.taxi.RecyclerTouchListener;
import tech.iwish.taxi.adapter.RentalVehicleAdapter;
import tech.iwish.taxi.config.Constants;
import tech.iwish.taxi.config.SharedpreferencesUser;
import tech.iwish.taxi.connection.ConnectionServer;
import tech.iwish.taxi.connection.JsonHelper;

import static tech.iwish.taxi.config.SharedpreferencesUser.USER_CONTACT;

public class RentalVehicleFragment extends Fragment {

    String package_select;
    Map<String, Double> latitude_logitude;
    Map<String, String> addressMap;
    private KProgressHUD kProgressHUD;
    Map data;
    String JSONDATA;
    Bundle bundle;
    RecyclerView rentalVehicleRecyclerView;

    public RentalVehicleFragment(Map<String, Double> latitude_logitude, Map<String, String> addressMap, String package_select) {
        this.latitude_logitude = latitude_logitude;
        this.addressMap = addressMap;
        this.package_select = package_select;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rental_vehicle, null);

        rentalVehicleRecyclerView = view.findViewById(R.id.rentalVehicleRecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rentalVehicleRecyclerView.setLayoutManager(linearLayoutManager);
        rentalVehicleRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), rentalVehicleRecyclerView, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

/*
                SharedpreferencesUser sharedpreferencesUser = new SharedpreferencesUser(getContext());
                data = sharedpreferencesUser.getShare();
                Object mobile = data.get(USER_CONTACT);
                setProgressDialog("");
//                JSONDATA = "{ \"type\" : \"Rental\" , \"request\" :\"rental\", \"TimeDuration\" :\"" + packageVehicles.get(position).getPackage_type() + "\" ,\"pickUpLat\" : \"" + latitude_logitude.get("PickLatitude") + "\"  ,\"pickUpLong\" : \"" + latitude_logitude.get("PickLogitude") + "\"  , \"userType\" : \"client\"  , \"userID\" : \"" + mobile.toString() + "\" , \"PickupCityName\" : \"" + AddressMap.get("PickupCityName") + " \"}";
                sharedpreferencesUser.getDatasWebsocket(JSONDATA);

                bundle = new Bundle();
                Timer timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {

                    @Override
                    public void run() {
                        String driverData = sharedpreferencesUser.driverReturnData();
                        if (driverData != null) {

                            if (driverData.equals("NO_VEICLE")) {
                                remove_progress_Dialog();
                            } else {
                                Log.e("driverData", driverData);
                                rentaldriverData(driverData);
                            }
                            timer.cancel();
                        }
                    }
                }, 0, 1000);
*/

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        kProgressHUD = new KProgressHUD(Objects.requireNonNull(getContext()));
        rentalVehicleSelect();
        return view;

    }

    private void rentalVehicleSelect() {


        OkHttpClient okHttpClient = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("package", package_select);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request1 = new Request.Builder().url(Constants.RENTAL_VEHICLE).post(body).build();



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

                            }


                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RentalVehicleAdapter rentalVehicleAdapter = new RentalVehicleAdapter();
                                    rentalVehicleRecyclerView.setAdapter(rentalVehicleAdapter);

                                    remove_progress_Dialog();

                                }
                            });
                        } else {

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    remove_progress_Dialog();

                                }
                            });
                        }
                    }
                }
            }
        });

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

    private void rentaldriverData(String data) {

        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONObject value = jsonObject.getJSONObject("data");
            String driverId = value.getString("driverId");
            String otp = value.getString("otp");
            String trackid = value.getString("trackid");

            bundle.putString("otp",otp);
            bundle.putString("driverId",driverId);
            bundle.putString("rental","rental");
            bundle.putString("trackid",trackid);

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
                        FragmentManager fm = ((FragmentActivity) getActivity()).getSupportFragmentManager();
                        rideConfiremDriverDetailsFragment.setArguments(bundle);
                        fm.beginTransaction().replace(R.id.confirmRideLoad, rideConfiremDriverDetailsFragment).commit();

                        remove_progress_Dialog();
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}