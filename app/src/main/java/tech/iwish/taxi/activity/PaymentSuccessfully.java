package tech.iwish.taxi.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import tech.iwish.taxi.R;
import tech.iwish.taxi.config.Constants;
import tech.iwish.taxi.connection.ConnectionServer;

public class PaymentSuccessfully extends AppCompatActivity {

    private TextView amount ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_successfully);
        getSupportActionBar().hide();

        amount = (TextView)findViewById(R.id.amount);

        Intent intent = getIntent();
        String message = intent.getStringExtra("message");
        Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();
        switch (message){
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
        Intent intent = getIntent();
        String amount_edit = intent.getStringExtra("edit_wallet_amount");
        ConnectionServer connectionServer = new ConnectionServer();
//        connectionServer.set_url(Constants);
        connectionServer.requestedMethod("POST");
        connectionServer.buildParameter("amount" , amount_edit );
//        connectionServer.buildParameter("mobile" ,);
        connectionServer.execute(new ConnectionServer.AsyncResponse() {
            @Override
            public void processFinish(String output) {

            }
        });

        Toast.makeText(this, ""+amount_edit, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
    }
}
