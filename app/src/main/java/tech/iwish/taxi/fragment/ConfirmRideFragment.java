package tech.iwish.taxi.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import tech.iwish.taxi.R;
import tech.iwish.taxi.activity.MainActivity;
import tech.iwish.taxi.adapter.PackageAdapter;
import tech.iwish.taxi.adapter.VehicleAdapter;
import tech.iwish.taxi.config.Constants;
import tech.iwish.taxi.connection.ConnectionServer;
import tech.iwish.taxi.connection.JsonHelper;
import tech.iwish.taxi.other.PackageVehicle;
import tech.iwish.taxi.other.VehicleList;

public class ConfirmRideFragment extends Fragment {

    RecyclerView vehicle_recycle;
    List<VehicleList> vehicleList = new ArrayList<>();
    Map<String , LatLng>  allLatLng ;
    Map<String , Double>  latitude_logitude ;
    Map<String , String> AddressMap ;
    MainActivity WebsocketHandel;
    public vehicleInter jsodata;
    private KProgressHUD kProgressHUD;
    Double PickLatitude , PickLogitude ,destinationsLatitude, destinationsLogitude;


    public ConfirmRideFragment( Map<String, LatLng> allLatLng   , Map<String , Double> latitude_logitude ,Map<String , String> AddressMap) {

        this.allLatLng = allLatLng ;
        this.latitude_logitude = latitude_logitude ;
        this.AddressMap = AddressMap ;
        WebsocketHandel = new MainActivity();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_confirm_ride , null);


        vehicle_recycle = (RecyclerView)view.findViewById(R.id.vehicle_recycle);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        vehicle_recycle.setLayoutManager(linearLayoutManager);

        kProgressHUD = new KProgressHUD(getContext());

         PickLatitude = latitude_logitude.get("PickLatitude");
         PickLogitude = latitude_logitude.get("PickLogitude");
         destinationsLatitude = latitude_logitude.get("dropLatitude");
         destinationsLogitude = latitude_logitude.get("dropLongitude");

        setProgressDialog(getString(R.string.vehicle_search));

/*
        ConnectionServer connectionServer = new ConnectionServer();
        connectionServer.requestedMethod("POST");
        connectionServer.set_url(Constants.Vehicle);
        connectionServer.buildParameter("PickLatitude" ,PickLatitude.toString());
        connectionServer.buildParameter("PickLogitude" ,PickLogitude.toString());
        connectionServer.buildParameter("destinationsLatitude" ,destinationsLatitude.toString());
        connectionServer.buildParameter("destinationsLogitude" ,destinationsLogitude.toString());
        connectionServer.execute(output -> {
            Log.e("output" , output);
            JsonHelper jsonHelper = new JsonHelper(output);
            if(jsonHelper.isValidJson()){
                String response = jsonHelper.GetResult("response");
                if(response.equals("TRUE")){
                    remove_progress_Dialog();
                    JSONArray jsonArray = jsonHelper.setChildjsonArray(jsonHelper.getCurrentJsonObj(), "result1");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonHelper.setChildjsonObj(jsonArray, i);

                        vehicleList.add(new VehicleList(jsonHelper.GetResult("catagory_id"),jsonHelper.GetResult("catagory_name"),jsonHelper.GetResult("vehicle_type"),jsonHelper.GetResult("waitingRate_m"),jsonHelper.GetResult("totrate"),jsonHelper.GetResult("tottime"),jsonHelper.GetResult("distance")));

                    }
                    VehicleAdapter vehicleAdapter = new VehicleAdapter(getActivity() , vehicleList , allLatLng , latitude_logitude , AddressMap);
                    vehicle_recycle.setAdapter(vehicleAdapter);
                    vehicle_recycle.setNestedScrollingEnabled(false);
                    vehicleAdapter.Setvehicle(jsoneData -> jsodata.websok(jsoneData));
                    remove_progress_Dialog();
                }
            }
        });
*/

        datainsert();

//        ************************************************************************************


        return view;
    }


    private void datainsert() {


        OkHttpClient okHttpClient = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("PickLatitude", PickLatitude.toString());
            jsonObject.put("PickLogitude", PickLogitude.toString());
            jsonObject.put("destinationsLatitude", destinationsLatitude.toString());
            jsonObject.put("destinationsLogitude", destinationsLogitude.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request1 = new Request.Builder().url(Constants.Vehicle).post(body).build();


        okHttpClient.newCall(request1).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
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

                            JSONArray jsonArray = jsonHelper.setChildjsonArray(jsonHelper.getCurrentJsonObj(), "result1");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonHelper.setChildjsonObj(jsonArray, i);
                                vehicleList.add(new VehicleList(jsonHelper.GetResult("catagory_id"),jsonHelper.GetResult("catagory_name"),jsonHelper.GetResult("vehicle_type"),jsonHelper.GetResult("waitingRate_m"),jsonHelper.GetResult("totrate"),jsonHelper.GetResult("tottime"),jsonHelper.GetResult("distance")));
                            }

                        if(getActivity() != null){


                            Handler handler = new Handler(getActivity().getMainLooper());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    VehicleAdapter vehicleAdapter = new VehicleAdapter(getActivity() , vehicleList , allLatLng , latitude_logitude , AddressMap);
                                    vehicle_recycle.setAdapter(vehicleAdapter);
                                    vehicle_recycle.setNestedScrollingEnabled(false);
                                    vehicleAdapter.Setvehicle(jsoneData -> jsodata.websok(jsoneData));
                                    remove_progress_Dialog();
                                }
                            });

                        }
                        } else {

                            Handler handler = new Handler(getActivity().getMainLooper());
                            handler.post(new Runnable() {
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





    public void setdata(vehicleInter vehicleInter){
        this.jsodata = vehicleInter;
    }



    public interface vehicleInter{
        void websok(String value);
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
