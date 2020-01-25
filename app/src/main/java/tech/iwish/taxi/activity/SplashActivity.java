package tech.iwish.taxi.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.widget.Toast;

import java.util.Map;

import tech.iwish.taxi.R;
import tech.iwish.taxi.config.SharedpreferencesUser;

import static tech.iwish.taxi.config.SharedpreferencesUser.TEST_CHECH;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN_TIME_OUT=2000;
    Map data ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        SharedpreferencesUser sharedpreferencesUser = new SharedpreferencesUser(this);
        data = sharedpreferencesUser.getShare();
        sharedpreferencesUser.clickLocationDestroy();
        sharedpreferencesUser.dropLocationIntenrt_Destrou();
        sharedpreferencesUser.pickup_location_Intenrt_Destrou();
        final Object check = data.get(TEST_CHECH);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(SplashActivity.this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
        }else{
            showGPSDisabledAlertToUser();
        }

        new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(check == null){
                        Intent i=new Intent(SplashActivity.this, SignupActivity.class);
                        startActivity(i);
                        finish();
                    }else{


                        Intent i=new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                }
            }, SPLASH_SCREEN_TIME_OUT);
    }

    private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
//                .setCancelable(false)
//                .
//        AlertDialog alert = alertDialogBuilder.create();
//        alert.show();
    }
}
