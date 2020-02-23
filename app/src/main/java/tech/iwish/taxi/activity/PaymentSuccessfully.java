package tech.iwish.taxi.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

import tech.iwish.taxi.R;
import tech.iwish.taxi.config.Constants;
import tech.iwish.taxi.config.SharedpreferencesUser;
import tech.iwish.taxi.connection.ConnectionServer;
import tech.iwish.taxi.connection.JsonHelper;

import static tech.iwish.taxi.config.SharedpreferencesUser.USER_CONTACT;

public class PaymentSuccessfully extends AppCompatActivity {

    private TextView amount;
    private Map data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_successfully);
        getSupportActionBar().hide();

        amount = (TextView) findViewById(R.id.amount);

        Intent intent = getIntent();
        String message = intent.getStringExtra("message");

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
        amount.setTextColor(R.color.redColor);
        Toast.makeText(this, "fail smacs", Toast.LENGTH_SHORT).show();
    }

    private void successfully() {
        SharedpreferencesUser sharedpreferencesUser = new SharedpreferencesUser(this);
        Object number = data.get(USER_CONTACT);
        if (number != null) {
            Intent intent = getIntent();
            String amount_edit = intent.getStringExtra("edit_wallet_amount");
            ConnectionServer connectionServer = new ConnectionServer();
            connectionServer.set_url(Constants.WALLET_MONEY_UPDATE);
            connectionServer.requestedMethod("POST");
            connectionServer.buildParameter("amount", amount_edit);
            connectionServer.buildParameter("mobile", number.toString());
            connectionServer.execute(output -> {
                Log.e("output", output);
                JsonHelper jsonHelper = new JsonHelper(output);
                if (jsonHelper.isValidJson()) {
                    String response = jsonHelper.GetResult("response");
                    if (response.equals("TRUE")) {

                    }
                }
            });
        }

    }
}
