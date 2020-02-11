package tech.iwish.taxi.websocket;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import tech.iwish.taxi.activity.MainActivity;
import tech.iwish.taxi.config.SharedpreferencesUser;
import tech.iwish.taxi.fragment.ConfirmRideFragment;

import static tech.iwish.taxi.adapter.VehicleAdapter.JSONDATA;

public class Websocket extends WebSocketListener {

    Context context;
    SharedpreferencesUser sharedpreferencesUser;
    MainActivity fetchVehicle;
    Response response ;
    Object webSockets;
    private static final int NORMAL_CLOSURE_STATUS = 1000;
    public Websocket websocketss;


    public Websocket(Context mainActivity) {
        this.context = mainActivity;
        sharedpreferencesUser = new SharedpreferencesUser(context);
        fetchVehicle = new MainActivity();

    }

     public void Objectwebsocket (Websocket websocketss){

         this.websocketss = websocketss ;

    }
     public void sendData (String Jsondata){

//         websocketss.sendData(Jsondata);


    }


    @Override
    public void onOpen(WebSocket webSocket, Response response)
    {
        Toast.makeText(context, ""+webSocket, Toast.LENGTH_SHORT).show();
//        sendData( null ,  Websocket ,response);
//        String Json = "{ \"type\" : \"initiate\" ,\"userType\" : \"client\"  , \"userID\" : \"8871121959\"}";
//        webSocket.send(Json);
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {


//        sharedpreferencesUser.getDatas(text);

    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {

        onMessage(webSocket, bytes);

    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {

//        onClosing(webSocket, code, reason);
//        webSocket.close(1000, null);
//        webSocket.cancel();
//        Log.e("onCllosing", "CLOSE: " + code + " " + reason);

    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
//        onClosed(webSocket, code, reason);
        webSocket.close(NORMAL_CLOSURE_STATUS, null);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {

    }

    public void UrlWebSocket(String url, Context contexts) {
        Websocket showVeicleSocket = new Websocket(contexts);
        Request request = new Request.Builder().url(url).build();
        OkHttpClient showVeicle = new OkHttpClient();
        showVeicle.newWebSocket(request, showVeicleSocket);
        showVeicle.dispatcher().executorService();


//        SharedpreferencesUser sharedpreferencesUser = new SharedpreferencesUser(context);
//        sharedpreferencesUser.setjson(json);

    }


}
