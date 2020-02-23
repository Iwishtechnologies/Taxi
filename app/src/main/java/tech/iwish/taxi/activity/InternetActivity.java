package tech.iwish.taxi.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.security.Security;
import java.util.Map;

import tech.iwish.taxi.R;
import tech.iwish.taxi.config.SharedpreferencesUser;

import static tech.iwish.taxi.config.SharedpreferencesUser.DRIVER_NAME;
import static tech.iwish.taxi.config.SharedpreferencesUser.DRIVER_NUMBER;
import static tech.iwish.taxi.config.SharedpreferencesUser.OTP;
import static tech.iwish.taxi.config.SharedpreferencesUser.TEST_CHECH;

public class InternetActivity extends AppCompatActivity implements View.OnClickListener {

    private Button retrybutton;
    private SharedpreferencesUser sharedpreferencesUser ;
    Map data;
    Object driver_name , driver_number , otp , check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet);
        getSupportActionBar().hide();

        retrybutton = (Button) findViewById(R.id.retrybutton);
        retrybutton.setOnClickListener(this);
        sharedpreferencesUser = new SharedpreferencesUser(this);
        data = sharedpreferencesUser.getShare();
        check = data.get(TEST_CHECH);
        driver_name = data.get(DRIVER_NAME);
        driver_number = data.get(DRIVER_NUMBER);
        otp = data.get(OTP);

    }

    @Override
    public void onClick(View view) {
            ch();
    }
    public void ch() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
//                Toast.makeText(this, "Wifi enabled", Toast.LENGTH_SHORT).show();
                if (check == null) {
                    Intent i = new Intent(InternetActivity.this, SignupActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    if (driver_name != null) {
                        Intent i = new Intent(InternetActivity.this, MainActivity.class);
                        i.putExtra("checkDriverData", "checkDriverData");
                        i.putExtra("driver_name", driver_name.toString());
                        i.putExtra("driver_number", driver_number.toString());
                        i.putExtra("otp", "5822");
                        startActivity(i);

                    } else {
                        Intent i = new Intent(InternetActivity.this, MainActivity.class);
                        startActivity(i);
                    }
                }
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
//                Toast.makeText(this, "Mobile data enabled", Toast.LENGTH_SHORT).show();
                if (check == null) {
                    Intent i = new Intent(InternetActivity.this, SignupActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    if (driver_name != null) {
                        Intent i = new Intent(InternetActivity.this, MainActivity.class);
                        i.putExtra("checkDriverData", "checkDriverData");
                        i.putExtra("driver_name", driver_name.toString());
                        i.putExtra("driver_number", driver_number.toString());
                        i.putExtra("otp", "5822");
                        startActivity(i);

                    } else {
                        Intent i = new Intent(InternetActivity.this, MainActivity.class);
                        startActivity(i);
                    }
                }
            }
        } else {
            Toast.makeText(this, "No internet is available", Toast.LENGTH_SHORT).show();
        }
    }
}
