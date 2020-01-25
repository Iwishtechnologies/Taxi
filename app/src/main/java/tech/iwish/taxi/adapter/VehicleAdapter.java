package tech.iwish.taxi.adapter;

import android.content.Context;
import android.graphics.Color;
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


import com.firebase.client.Firebase;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import tech.iwish.taxi.R;

import tech.iwish.taxi.activity.MainActivity;
import tech.iwish.taxi.config.Constants;
import tech.iwish.taxi.config.SharedpreferencesUser;
import tech.iwish.taxi.connection.ConnectionServer;
import tech.iwish.taxi.connection.JsonHelper;
import tech.iwish.taxi.extended.TextViewFont;
import tech.iwish.taxi.fragment.RideConfiremDriverDetailsFragment;
import tech.iwish.taxi.other.DistanceList;
import tech.iwish.taxi.other.VehicleList;

import static tech.iwish.taxi.config.SharedpreferencesUser.USER_CONTACT;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.Viewholder> {

    List<VehicleList> vehicleList;
    Context context;
    private int currentSelectedPosition = RecyclerView.NO_POSITION;
    private Map data;
    Map<String, LatLng> AllLatLng;
    Map<String, Double> latitude_logitude;
    List<DistanceList> distanceLists = new ArrayList<>();
    Map<String, String> AddressMap;
    VehicleLatLon vehicleLatLongWebSocket ;



    public VehicleAdapter(FragmentActivity activity, List<VehicleList> vehicleList, Map<String, LatLng> allLatLng, Map<String, Double> latitude_logitude, Map<String, String> AddressMap) {

        this.vehicleList = vehicleList;
        this.context = activity;
        this.AllLatLng = allLatLng;
        this.latitude_logitude = latitude_logitude;
        this.AddressMap = AddressMap;


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

        holder.clickConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedpreferencesUser sharedpreferencesUser = new SharedpreferencesUser(context);
                data = sharedpreferencesUser.getShare();
                Object userContact = data.get(USER_CONTACT);

                currentSelectedPosition = position;
                notifyDataSetChanged();

            }
        });

        if (currentSelectedPosition == position) {

//            holder.clickConfirm.setBackgroundColor(context.getResources().getColor(R.color.yellowColor));
            holder.clickConfirm.setBackground(context.getResources().getDrawable(R.drawable.click_design));
            holder.button_Conirm.setVisibility(View.VISIBLE);
            holder.booking_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    RideConfiremDriverDetailsFragment rideConfiremDriverDetailsFragment = new RideConfiremDriverDetailsFragment();
                    FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
                    fm.beginTransaction().replace(R.id.confirRide, rideConfiremDriverDetailsFragment).commit();


                    //=================================web socket============================================
                    SharedpreferencesUser sharedpreferencesUser = new SharedpreferencesUser(context);
                    Map data = sharedpreferencesUser.getShare();
                    Object mobile = data.get(USER_CONTACT);
                    AddressMap.get("PickupCityName");
                    AddressMap.get("PickupstateName");
                    AddressMap.get("PickupcountryName");

                    final String Json = "{\"pickUpLat\" : \"" + latitude_logitude.get("PickLatitude") + "\"  ,\"pickUpLong\" : \"" + latitude_logitude.get("PickLogitude") + "\" ,\"DropLocationLatitude\" : \""+latitude_logitude.get("dropLatitude")+"\",\"DropLocationLogitude\" : \""+latitude_logitude.get("dropLongitude")+"\"  , \"userType\" : \"client\" , \"type\" : \"vehicleCall\" , \"userID\" : \"" + mobile.toString() + "\" , \"PickupCityName\" : \"" + AddressMap.get("PickupCityName") + "\" , \"PickupstateName\" : \"" + AddressMap.get("PickupstateName") + "\" , \"PickupStretName\" : \"" + AddressMap.get("PickupStretName") + "\"}";
                    WebSocketListener webSocketListener = new WebSocketListener() {
                        @Override
                        public void onOpen(WebSocket webSocket, Response response) {
                            webSocket.send(Json);

                        }

                        @Override
                        public void onMessage(WebSocket webSocket, String text) {
//                            onMessage(webSocket, text);
                            Log.e("value",text);
//                            vehicleLatLongWebSocket.vehicaleLatlong(26.1234,72.01234 );

//                            JsonHelper jsonHelper = new JsonHelper(text);
//                            String response = jsonHelper.GetResult("response");
//                            if(response.equals("TRUE")){
//
//                            }
                        }

                        @Override
                        public void onMessage(WebSocket webSocket, ByteString bytes) {
//                            onMessage(webSocket, bytes);
                        }

                        @Override
                        public void onClosing(WebSocket webSocket, int code, String reason) {
//                            onClosing(webSocket, code, reason);
//                            webSocket.close(1000, null);
//                            webSocket.cancel();
//                            Log.e("onCllosing", "CLOSE: " + code + " " + reason);
                        }

                        @Override
                        public void onClosed(WebSocket webSocket, int code, String reason) {
//                            onClosed(webSocket, code, reason);
                        }

                        @Override
                        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
//                            onFailure(webSocket, t, response);

                        }
                    };
                    WebSocket webSocket;
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Request request = new Request.Builder().url("ws://173.212.226.143:8090/").build();
                    webSocket = okHttpClient.newWebSocket(request, webSocketListener);
                    okHttpClient.dispatcher().executorService();

//                    =============================================================================

                }
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

    public void Setvehicle(VehicleLatLon vehicleLatLon){
        this.vehicleLatLongWebSocket = vehicleLatLon ;
    }

    public interface VehicleLatLon{
         void vehicaleLatlong(Double latitude , Double longitude);
    }


}
