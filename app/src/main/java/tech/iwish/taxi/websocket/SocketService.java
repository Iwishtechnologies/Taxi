package tech.iwish.taxi.websocket;

import android.animation.ValueAnimator;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import tech.iwish.taxi.activity.BillActivity;
import tech.iwish.taxi.activity.MainActivity;
import tech.iwish.taxi.activity.PaymentOptionActivity;
import tech.iwish.taxi.config.SharedpreferencesUser;
import tech.iwish.taxi.fragment.ConfirmRideFragment;

import static tech.iwish.taxi.config.SharedpreferencesUser.DATA_WB;
import static tech.iwish.taxi.config.SharedpreferencesUser.USER_CONTACT;


public class SocketService extends Service {


    Boolean socketconnection = false;
    private WebSocket ws;
    SharedpreferencesUser sharedpreferencesUser;
    MainActivity mainActivity;
    Context context;
    OkHttpClient client;
    String VehicleData;
    private Handler handler;
    String type = "blank";
    Map<String, LatLng> allLatLng;
    Map<String, Double> latitude_logitude;
    Map<String, String> AddressMap;
    //    private VehicleShow_Data dataVehi;
    private Map data;


    public SocketService() { }
    public void SocketService(Context context, Map<String, LatLng> allLatLng, Map<String, Double> latitude_logitude, Map<String, String> addressMap) {
        this.context = context;
        sharedpreferencesUser = new SharedpreferencesUser(context);
        this.allLatLng = allLatLng;
        this.latitude_logitude = latitude_logitude;
        this.AddressMap = addressMap;
        mainActivity = new MainActivity();
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

    public int onStartCommand(final Intent intent, int flags, int startId) {

//        Toast.makeText(mainActivity, ""+intent, Toast.LENGTH_SHORT).show();

        WebSocketListener webSocketListener = new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                String Json = "{ \"type\" : \"initiate\" ,\"userType\" : \"client\"  , \"userID\" : \""+data.get(USER_CONTACT).toString()+"\" , \"PickupCityName\" : \"" + AddressMap.get("PickupCityName") + "\" , \"PickupstateName\" : \"" + AddressMap.get("PickupstateName") + "\" , \"PickupStretName\" : \"" + AddressMap.get("PickupStretName") + "\"}";
                webSocket.send(Json);
                socketconnection = true;
                sharedpreferencesUser.setSocketConnection(true);

            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
//                super.onMessage(webSocket, text);
                Log.e("data websocket", text);
                try {
                    JSONObject jsonObject = new JSONObject(text);
                    String type = jsonObject.getString("type");
                    switch (type) {
                        case "vehicleInfo":
                            Log.e("text", text);
//                            checkehi(text);
                            sharedpreferencesUser.vehicledata(text);
                            break;
                        case "vehicleAccept":
                            Log.e("text", text);
                            sharedpreferencesUser.driverShowRemove();
                            sharedpreferencesUser.driverShow(text);
                            break;
                        case "otpconfirm":
                            Log.e("text", text);
                            sharedpreferencesUser.remove_otpConfirmDriver();
                            sharedpreferencesUser.otpConfirmDriver(text);
                            Intent intent1 = new Intent(context, MainActivity.class);
                            intent1.putExtra("otpconfirmRide","otpconfirmRide");
                            context.startActivity(intent1);
                            break;
                        case "RideConfirm":
                            Log.e("text", text);
                            if (text != null) {
                                sharedpreferencesUser.remove_otpConfirmDriver();
                                Intent intent = new Intent(context, BillActivity.class);
                                intent.putExtra("confirmRide", text);
                                context.startActivity(intent);
                            }
                            break;
                        default:
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                super.onMessage(webSocket, bytes);
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {

            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                onOpen(webSocket, response);
                socketconnection = false;
            }
        };

        client = new OkHttpClient();
        Request request = new Request.Builder().url("ws://173.212.226.143:8090/").build();
        ws = client.newWebSocket(request, webSocketListener);
        client.dispatcher().executorService();



/*

//        Intent data =Intent.getIntent();
        if (intent.getExtras() != null) {
            String data = intent.getExtras().getString("JSON");
            Log.e("data", data);
            VehicleData = data;

            try {
                JSONObject mainObject = new JSONObject(data);
//                JSONObject clientdata = mainObject.getJSONObject("data");
                type = "vehicleCall";
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        switch (type) {
            case "vehicleCall":
                ws.send(VehicleData);
                break;
            default:
                break;
        }


*/

        handler = new Handler();
        handler.post(() -> new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                data = sharedpreferencesUser.getShare();
                Object jsondatass = data.get(DATA_WB);
                if (jsondatass != null) {
                    String jsondata = jsondatass.toString();
                    ws.send(jsondata);
                    Log.e("data", jsondata);
                    sharedpreferencesUser.removeDataWebsocket();
                }
            }
        }, 30, 1000));


        return START_NOT_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
