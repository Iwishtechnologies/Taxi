package tech.iwish.taxi.activity;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;

import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import tech.iwish.taxi.config.SharedpreferencesUser;
import tech.iwish.taxi.connection.JsonHelper;

public class ShowVeicleSocket extends WebSocketListener {


    public String responseWebsocket;
    String senddata;
    Context context;
    SharedpreferencesUser sharedpreferencesUser;


    public ShowVeicleSocket(Context mainActivity) {
        this.context = mainActivity;
         sharedpreferencesUser = new SharedpreferencesUser(context);
    }


    public void JsonDataSend(String json) {



    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {

            webSocket.send(sharedpreferencesUser.getjson());


    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
//                            onMessage(webSocket, text);

        sharedpreferencesUser.getDatas(text);

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
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
//        onFailure(webSocket, t, response);

    }

    public void UrlWebSocket(String url , String json , Context contexts) {
        ShowVeicleSocket showVeicleSocket = new ShowVeicleSocket(contexts);
        Request request = new Request.Builder().url(url).build();
        OkHttpClient showVeicle = new OkHttpClient();
        showVeicle.newWebSocket(request, showVeicleSocket);
        showVeicle.dispatcher().executorService();

        SharedpreferencesUser sharedpreferencesUser = new SharedpreferencesUser(context);
        sharedpreferencesUser.setjson(json);

    }


}
