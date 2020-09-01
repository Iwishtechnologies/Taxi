package tech.iwish.taxi.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import tech.iwish.taxi.R;
import tech.iwish.taxi.config.Constants;
import tech.iwish.taxi.config.SharedpreferencesUser;
import tech.iwish.taxi.connection.ConnectionServer;
import tech.iwish.taxi.connection.JsonHelper;
import tech.iwish.taxi.fragment.RideConfiremDriverDetailsFragment;
import tech.iwish.taxi.other.OutStationVehicleList;
import tech.iwish.taxi.other.VehicleList;

import static tech.iwish.taxi.config.SharedpreferencesUser.USER_CONTACT;

public class OutStationVehicleAdapter extends RecyclerView.Adapter<OutStationVehicleAdapter.Viewholder> {

    private String JSONDATA;
    List<OutStationVehicleList> outStationVehicleLists;
    Context context;
    Map data;

    public Map<String, LatLng> AllLatLng;
    public Map<String, Double> latitude_logitude;
    public Map<String, String> AddressMap;
    private KProgressHUD kProgressHUD;

    public OutStationVehicleAdapter(FragmentActivity activity, List<OutStationVehicleList> outStationVehicleLists, Map<String, LatLng> allLatLng, Map<String, Double> latitude_logitude, Map<String, String> addressMap) {
        this.context = activity;
        this.outStationVehicleLists = outStationVehicleLists;
        this.latitude_logitude = latitude_logitude;
        this.AddressMap = addressMap;
        this.AllLatLng = allLatLng;

    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_outstation_vehicle, parent, false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        kProgressHUD = new KProgressHUD(context);
        SharedpreferencesUser sharedpreferencesUser = new SharedpreferencesUser(context);
        data = sharedpreferencesUser.getShare();
        Object mobile = data.get(USER_CONTACT);
        holder.clickLayout.setOnClickListener(view -> {

            setProgressDialog("Driver_search");

            JSONDATA = "{ \"type\" : \"OutStation\" , \"request\" :\"\" ,\"pickUpLat\" : \"" + latitude_logitude.get("PickLatitude") + "\"  ,\"pickUpLong\" : \"" + latitude_logitude.get("PickLogitude") + "\" ,\"DropLocationLatitude\" : \"" + latitude_logitude.get("dropLatitude") + "\",\"DropLocationLogitude\" : \"" + latitude_logitude.get("dropLongitude") + "\"  , \"userType\" : \"client\"  , \"userID\" : \"" + mobile.toString() + "\" , \"PickupCityName\" : \"" + AddressMap.get("PickupCityName") + "\" , \"PickupstateName\" : \"" + AddressMap.get("PickupstateName") + "\" , \"PickupStretName\" : \"" + AddressMap.get("PickupStretName") + "\"}";
            sharedpreferencesUser.getDatasWebsocket(JSONDATA);

            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {

//                        private String driverShow;

                @Override
                public void run() {
                    String driverData = sharedpreferencesUser.driverReturnData();
                    if (driverData != null) {
                        Log.e("driverData", driverData);
                        driverData(driverData);
                        timer.cancel();
                    }
                }
            }, 0, 1000);




        });
    }

    @Override
    public int getItemCount() {
        return outStationVehicleLists.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private LinearLayout clickLayout;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            clickLayout = (LinearLayout) itemView.findViewById(R.id.clickLayout);
        }
    }

    public void driverData(String data) {

        Bundle bundle = new Bundle();

        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONObject value = jsonObject.getJSONObject("data");
            String driverId = value.getString("driverId");
            String otp = value.getString("otp");
            String trackid = value.getString("trackid");

            bundle.putString("otp", otp);
            bundle.putString("driverId", driverId);
            bundle.putString("trackid", trackid);

   /*         ConnectionServer connectionServer = new ConnectionServer();
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
                        FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
                        rideConfiremDriverDetailsFragment.setArguments(bundle);
                        fm.beginTransaction().replace(R.id.confirmRideLoad, rideConfiremDriverDetailsFragment).commit();

                        remove_progress_Dialog();
                    }
                }
            });*/


//            **************************************************

            OkHttpClient okHttpClient = new OkHttpClient();
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            JSONObject jsonObjects = new JSONObject();
            try {
                jsonObjects.put("driverId", driverId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(JSON, jsonObjects.toString());
            Request request1 = new Request.Builder().url(Constants.DRIVERSHOW).post(body).build();


            okHttpClient.newCall(request1).enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    e.printStackTrace();
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
                                    bundle.putString("driver_name", jsonHelper.GetResult("DriverName"));
                                    bundle.putString("driver_mobile", jsonHelper.GetResult("Mobile"));
                                }
                                ((Activity) context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        RideConfiremDriverDetailsFragment rideConfiremDriverDetailsFragment = new RideConfiremDriverDetailsFragment();
                                        FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
                                        rideConfiremDriverDetailsFragment.setArguments(bundle);
                                        fm.beginTransaction().replace(R.id.confirmRideLoad, rideConfiremDriverDetailsFragment).commit();
                                        remove_progress_Dialog();

                                    }
                                });

                            }
                        }
                    }
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
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
