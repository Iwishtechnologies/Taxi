package tech.iwish.taxi.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.razorpay.Checkout;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import tech.iwish.taxi.R;
import tech.iwish.taxi.activity.MoneyAddWalletActivity;
import tech.iwish.taxi.config.Constants;
import tech.iwish.taxi.config.SharedpreferencesUser;
import tech.iwish.taxi.connection.ConnectionServer;
import tech.iwish.taxi.connection.JsonHelper;
import tech.iwish.taxi.other.User_DetailsList;

import static tech.iwish.taxi.config.SharedpreferencesUser.USER_CONTACT;

public class WalletFragment extends Fragment {

    private LinearLayout wallet_click;
    private Map data;
    private TextView wallet_money;
    private JsonHelper jsonHelper ;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet,null);


        wallet_click = (LinearLayout)view.findViewById(R.id.wallet_click);
        wallet_money = (TextView)view.findViewById(R.id.wallet_money);

        WalletRef(getActivity());


        wallet_click.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity() , MoneyAddWalletActivity.class);
            intent.putExtra("money",wallet_money.getText().toString().trim());
            startActivity(intent);
        });


        return view ;
    }

    public void WalletRef(Context context){

        SharedpreferencesUser sharedpreferencesUser = new SharedpreferencesUser(context);
        data = sharedpreferencesUser.getShare();
        String mob = data.get(USER_CONTACT).toString();

 /*       ConnectionServer connectionServer = new ConnectionServer();
        connectionServer.set_url(Constants.WALLET);
        connectionServer.requestedMethod("POST");
        connectionServer.buildParameter("mobile" , mob);
        connectionServer.execute(output -> {
            Log.e("output" , output);
             jsonHelper = new JsonHelper(output);
            if(jsonHelper.isValidJson()){
                String response = jsonHelper.GetResult("response");
                if(response.equals("TRUE")){
                    JSONArray jsonArray = jsonHelper.setChildjsonArray(jsonHelper.getCurrentJsonObj(), "data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonHelper.setChildjsonObj(jsonArray, i);
                        sharedpreferencesUser.walletAdd(jsonHelper.GetResult("wallet"));
                        if(context == getActivity()){
                            wallet_money.setText(jsonHelper.GetResult("wallet"));
                        }
                    }
                }
            }
        });*/


        OkHttpClient okHttpClient = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", mob);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request1 = new Request.Builder().url(Constants.WALLET).post(body).build();


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
                    if(jsonHelper.isValidJson()){
                        String responses = jsonHelper.GetResult("response");
                        if(responses.equals("TRUE")){
                            JSONArray jsonArray = jsonHelper.setChildjsonArray(jsonHelper.getCurrentJsonObj(), "data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonHelper.setChildjsonObj(jsonArray, i);
                                sharedpreferencesUser.walletAdd(jsonHelper.GetResult("wallet"));
                                if(context == getActivity()){
                                    if(getActivity() != null){
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                wallet_money.setText(jsonHelper.GetResult("wallet"));
                                            }
                                        });
                                    }

                                }
                            }
                        }
                    }
                }
            }
        });


    }

}






















