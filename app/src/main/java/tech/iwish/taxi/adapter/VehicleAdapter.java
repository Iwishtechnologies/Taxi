package tech.iwish.taxi.adapter;

import android.app.DialogFragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.maps.model.LatLng;
import com.kaopiz.kprogresshud.KProgressHUD;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import tech.iwish.taxi.R;

import tech.iwish.taxi.activity.MainActivity;
import tech.iwish.taxi.config.Constants;
import tech.iwish.taxi.config.SharedpreferencesUser;
import tech.iwish.taxi.connection.ConnectionServer;
import tech.iwish.taxi.connection.JsonHelper;
import tech.iwish.taxi.fragment.RideConfiremDriverDetailsFragment;
import tech.iwish.taxi.other.DistanceList;
import tech.iwish.taxi.other.User_DetailsList;
import tech.iwish.taxi.other.VehicleList;
import tech.iwish.taxi.websocket.SocketService;
import tech.iwish.taxi.websocket.Websocket;

import static tech.iwish.taxi.config.SharedpreferencesUser.JSON;
import static tech.iwish.taxi.config.SharedpreferencesUser.USER_CONTACT;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.Viewholder> {

    List<VehicleList> vehicleList;
    Context context;
    private int currentSelectedPosition = RecyclerView.NO_POSITION;
    private Map data;
    Map<String, LatLng> AllLatLng;
    Map<String, Double> latitude_logitude;
    Map<String, String> AddressMap;
    VehicleLatLon vehicleLatLongWebSocket;
    //    public KProgressHUD kProgressHUD;
    MainActivity mainActivity;
    private final static String CONFIRM_FRAGMENT_RID = "CONFIR_FRAGMENT_RIDESSS";
    public static String JSONDATA = "jsno";
    Bundle bundle;
    private KProgressHUD kProgressHUD;

    public VehicleAdapter(FragmentActivity activity, List<VehicleList> vehicleList, Map<String, LatLng> allLatLng, Map<String, Double> latitude_logitude, Map<String, String> AddressMap) {

        this.vehicleList = vehicleList;
        this.context = activity;
        this.AllLatLng = allLatLng;
        this.latitude_logitude = latitude_logitude;
        this.AddressMap = AddressMap;
        this.mainActivity = new MainActivity();

    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_vehicle_design, parent, false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull final Viewholder holder, final int position) {

        holder.total_rate.setText(vehicleList.get(position).getTotrate());
        holder.total_titme.setText(vehicleList.get(position).getTottime());

        kProgressHUD = new KProgressHUD(context);
        bundle = new Bundle();
//        **********************************************************
/*
        ConnectionServer connectionServer = new ConnectionServer();
        connectionServer.set_url(Constants.DRIVERSHOW);
        connectionServer.requestedMethod("POST");
        connectionServer.buildParameter("driverId", "6");
        connectionServer.execute(new ConnectionServer.AsyncResponse() {
            @Override
            public void processFinish(String output) {
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

//                        remove_progress_Dialog();

                    }
                }
            }
        });
*/

//        **********************************************************

        holder.clickConfirm.setOnClickListener(view -> {

            SharedpreferencesUser sharedpreferencesUser = new SharedpreferencesUser(context);
            data = sharedpreferencesUser.getShare();
            Object userContact = data.get(USER_CONTACT);

            currentSelectedPosition = position;
            notifyDataSetChanged();
        });

        if (currentSelectedPosition == position) {

//            holder.clickConfirm.setBackgroundColor(context.getResources().getColor(R.color.yellowColor));
            holder.clickConfirm.setBackground(context.getResources().getDrawable(R.drawable.click_design));
            holder.button_Conirm.setVisibility(View.VISIBLE);
            holder.booking_confirm.setOnClickListener(view -> {


                //=================================web socket============================================


//                    progress bar start
                setProgressDialog("Vehicle Serach..");
                autoDismissProgressbar();
//                    progress bar end

                SharedpreferencesUser sharedpreferencesUser = new SharedpreferencesUser(context);
                Map data = sharedpreferencesUser.getShare();
                Object mobile = data.get(USER_CONTACT);
                AddressMap.get("PickupCityName");
                AddressMap.get("PickupstateName");
                AddressMap.get("PickupcountryName");


                JSONDATA = "{ \"type\" : \"vehicleCall\",\"totalRate\" : \"" + vehicleList.get(currentSelectedPosition).getTotrate() + "\",\"totalTime\" : \"" + vehicleList.get(currentSelectedPosition).getTottime() + "\" , \"request\" :\""+vehicleList.get(currentSelectedPosition).getCatagory_name()+"\" ,\"pickUpLat\" : \"" + latitude_logitude.get("PickLatitude") + "\"  ,\"pickUpLong\" : \"" + latitude_logitude.get("PickLogitude") + "\" ,\"DropLocationLatitude\" : \"" + latitude_logitude.get("dropLatitude") + "\",\"DropLocationLogitude\" : \"" + latitude_logitude.get("dropLongitude") + "\"  , \"userType\" : \"client\"  , \"userID\" : \"" + mobile.toString() + "\" , \"PickupCityName\" : \"" + AddressMap.get("PickupCityName") + "\" , \"PickupstateName\" : \"" + AddressMap.get("PickupstateName") + "\" , \"PickupStretName\" : \"" + AddressMap.get("PickupStretName") + "\"}";
                sharedpreferencesUser.getDatasWebsocket(JSONDATA);


                Timer timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {

//                        private String driverShow;

                    @Override
                    public void run() {
                        String driverData = sharedpreferencesUser.driverReturnData();
                        if (driverData != null) {
                            Log.e("driverData", driverData);
                            latitude_logitude.put("confir_drop_location_latitude",latitude_logitude.get("dropLatitude"));
                            latitude_logitude.put("confir_drop_location_logitude",latitude_logitude.get("dropLongitude"));
                            driverData(driverData);
                            timer.cancel();
                        }
                    }
                }, 0, 1000);

                bundle.putString("rate", vehicleList.get(position).getTotrate());
                bundle.putString("time", vehicleList.get(position).getTottime());
                bundle.putString("distance", vehicleList.get(position).getDistance());

            });
        } else {
            holder.button_Conirm.setVisibility(View.GONE);
            holder.clickConfirm.setBackground(context.getResources().getDrawable(R.drawable.fragment_design_vehicle));
        }

    }

    @Override
    public int getItemCount() {

        return vehicleList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        LinearLayout clickConfirm;
        LinearLayout button_Conirm;
        Button booking_confirm;
        TextView total_rate, total_titme;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            clickConfirm = (LinearLayout) itemView.findViewById(R.id.clickConfirm);
            button_Conirm = (LinearLayout) itemView.findViewById(R.id.button_Conirm);
            booking_confirm = (Button) itemView.findViewById(R.id.booking_confirm);
            total_rate = (TextView) itemView.findViewById(R.id.total_rate);
            total_titme = (TextView) itemView.findViewById(R.id.total_titme);
        }
    }

    private ServiceConnection Mconnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };


    public void driverData(String data){

        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONObject value = jsonObject.getJSONObject("data");
            String driverId = value.getString("driverId");
            String trackid = value.getString("trackid");
            String otp = value.getString("otp");
            bundle.putString("otp",otp);
            bundle.putString("driverId",driverId);
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
                        FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
                        rideConfiremDriverDetailsFragment.setArguments(bundle);
                        fm.beginTransaction().replace(R.id.confirmRideLoad, rideConfiremDriverDetailsFragment, CONFIRM_FRAGMENT_RID).commit();

                        remove_progress_Dialog();
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void autoDismissProgressbar(){
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                remove_progress_Dialog();
                Log.e("vehicle not " , "vehcl not");
                timer.cancel();
            }
        },30000,30000);
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

    public void Setvehicle(VehicleLatLon vehicleLatLon) {
        this.vehicleLatLongWebSocket = vehicleLatLon;
    }

    public interface VehicleLatLon {
        public void vehicaleLatlong(String jsoneData);
    }

}
