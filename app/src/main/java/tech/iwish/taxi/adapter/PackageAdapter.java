package tech.iwish.taxi.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

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
import tech.iwish.taxi.fragment.Package_vehicle;
import tech.iwish.taxi.fragment.RideConfiremDriverDetailsFragment;
import tech.iwish.taxi.other.PackageVehicle;

import static tech.iwish.taxi.adapter.VehicleAdapter.JSONDATA;
import static tech.iwish.taxi.config.SharedpreferencesUser.USER_CONTACT;

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.Viewholder> {

    Context context;
    private List<PackageVehicle> packageVehicles;
    public Map<String, Double> latitude_logitude;
    Map<String, String> AddressMap;
    Map data;
    private Bundle bundle;
    private KProgressHUD kProgressHUD;

    public PackageAdapter(FragmentActivity activity, List<PackageVehicle> packageVehicles, Map<String, Double> latitude_logitude, Map<String, String> addressMap) {
        this.context = activity;
        this.packageVehicles = packageVehicles;
        this.latitude_logitude = latitude_logitude;
        this.AddressMap = addressMap;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_package_show, parent, false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        holder.packageshow.setText(packageVehicles.get(position).getPackage_type());
        holder.vehicle_type.setText(packageVehicles.get(position).getVahicle());
        kProgressHUD = new KProgressHUD(context);

//        holder.clickPackage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

    /*
        holder.clickPackage.setOnClickListener(view -> {
            SharedpreferencesUser sharedpreferencesUser = new SharedpreferencesUser(context);
            data = sharedpreferencesUser.getShare();
            Object mobile = data.get(USER_CONTACT);
            setProgressDialog("");
            JSONDATA = "{ \"type\" : \"Rental\" , \"request\" :\"rental\", \"TimeDuration\" :\""+packageVehicles.get(position).getPackage_type()+"\" ,\"pickUpLat\" : \"" + latitude_logitude.get("PickLatitude") + "\"  ,\"pickUpLong\" : \"" + latitude_logitude.get("PickLogitude") + "\"  , \"userType\" : \"client\"  , \"userID\" : \"" + mobile.toString() + "\" , \"PickupCityName\" : \"" + AddressMap.get("PickupCityName") + " \"}";
            sharedpreferencesUser.getDatasWebsocket(JSONDATA);

            bundle = new Bundle();
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {

                @Override
                public void run() {
                    String driverData = sharedpreferencesUser.driverReturnData();
                    if (driverData != null) {

                        if(driverData.equals("NO_VEICLE")){
                            remove_progress_Dialog();
                        }else {
                            Log.e("driverData", driverData);
                            rentaldriverData(driverData);
                        }
                        timer.cancel();

                    }
                }
            }, 0, 1000);

*//*
            Bundle bundle = new Bundle();
            bundle.putString("time",packageVehicles.get(position).getPackage_type());
            Package_vehicle package_vehicle = new Package_vehicle(latitude_logitude , AddressMap);
            FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
            package_vehicle.setArguments(bundle);
            fm.beginTransaction().replace(R.id.rental_frame, package_vehicle).commit();
*//*


        });
    */
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
                        FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
                        rideConfiremDriverDetailsFragment.setArguments(bundle);
                        fm.beginTransaction().replace(R.id.confirmRideLoad, rideConfiremDriverDetailsFragment).commit();


                        remove_progress_Dialog();
                    }
                }
            });


//************************************************
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
                                RideConfiremDriverDetailsFragment rideConfiremDriverDetailsFragment = new RideConfiremDriverDetailsFragment();
                                FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
                                rideConfiremDriverDetailsFragment.setArguments(bundle);
                                fm.beginTransaction().replace(R.id.confirmRideLoad, rideConfiremDriverDetailsFragment).commit();


                                remove_progress_Dialog();
                            }
                        }
                    }
                }
            });
//************************************************


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return packageVehicles.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView packageshow, vehicle_type;
        private LinearLayout clickPackage;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            packageshow = (TextView) itemView.findViewById(R.id.packageshow);
            vehicle_type = (TextView) itemView.findViewById(R.id.vehicle_type);
            clickPackage = (LinearLayout) itemView.findViewById(R.id.clickPackage);
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
