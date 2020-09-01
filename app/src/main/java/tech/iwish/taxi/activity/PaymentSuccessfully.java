package tech.iwish.taxi.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
import tech.iwish.taxi.config.Constants;
import tech.iwish.taxi.config.SharedpreferencesUser;
import tech.iwish.taxi.connection.ConnectionServer;
import tech.iwish.taxi.connection.JsonHelper;
import tech.iwish.taxi.fragment.WalletFragment;

import static tech.iwish.taxi.config.SharedpreferencesUser.USER_CONTACT;

public class PaymentSuccessfully extends AppCompatActivity {

    private TextView amount , fail_payment;
    private Map data;
    private String amount_edit ,before_wallet_amount;
    private SharedpreferencesUser sharedpreferencesUser;
    private WalletFragment walletFragment ;

    public PaymentSuccessfully() {
        this.walletFragment = new WalletFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_successfully);
        getSupportActionBar().hide();

        amount = (TextView) findViewById(R.id.amount);
        fail_payment = (TextView) findViewById(R.id.fail_payment);

        sharedpreferencesUser = new SharedpreferencesUser(this);
        data = sharedpreferencesUser.getShare();

        fail_payment.setVisibility(View.GONE);
        Intent intent = getIntent();
        String message = intent.getStringExtra("message");
        amount_edit = intent.getStringExtra("edit_wallet_amount");
        before_wallet_amount = intent.getStringExtra("before_wallet_amount");
        amount.setText(amount_edit);

        switch (message) {
            case "successfully":
                successfully();
                break;
            case "fail":
                fail();
                break;
            default:
                break;
        }
    }

    private void fail() {
        fail_payment.setVisibility(View.VISIBLE);
        amount.setTextColor(getResources().getColor(R.color.redColor));

    }

    private void successfully() {

        Object number = data.get(USER_CONTACT);
        if (number != null) {


            OkHttpClient okHttpClient = new OkHttpClient();
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("amount", amount_edit);
                jsonObject.put("mobile", number.toString());
                jsonObject.put("before_wallet_amount", before_wallet_amount);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(JSON, jsonObject.toString());
            Request request1 = new Request.Builder().url(Constants.WALLET_MONEY_UPDATE).post(body).build();


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
                                walletFragment.WalletRef(PaymentSuccessfully.this);
                            }
                        }
                    }
                }
            });



//            *******************************************
            /*ConnectionServer connectionServer = new ConnectionServer();
            connectionServer.set_url(Constants.WALLET_MONEY_UPDATE);
            connectionServer.requestedMethod("POST");
            connectionServer.buildParameter("amount", amount_edit);
            connectionServer.buildParameter("mobile", number.toString());
            connectionServer.buildParameter("before_wallet_amount", before_wallet_amount);
            connectionServer.execute(output -> {
                Log.e("output", output);
                JsonHelper jsonHelper = new JsonHelper(output);
                if (jsonHelper.isValidJson()) {
                    String response = jsonHelper.GetResult("response");
                    if (response.equals("TRUE")) {
                        walletFragment.WalletRef(PaymentSuccessfully.this);
                    }
                }
            });

            */
        }

    }
}
