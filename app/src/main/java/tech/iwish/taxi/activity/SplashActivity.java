package tech.iwish.taxi.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
    SharedpreferencesUser sharedpreferencesUser;
    Object check ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

         sharedpreferencesUser = new SharedpreferencesUser(this);
        data = sharedpreferencesUser.getShare();
        sharedpreferencesUser.clickLocationDestroy();
        sharedpreferencesUser.dropLocationIntenrt_Destrou();
        sharedpreferencesUser.removeDataWebsocket();
        sharedpreferencesUser.driverShowRemove();
         check = data.get(TEST_CHECH);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        getConnectivityStatusString();
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(SplashActivity.this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
        }else{

        }

    }



    public String getConnectivityStatusString() {
        String status = null;
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                splashTime();
                return status;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                splashTime();
                return status;
            }
        } else {
            status = "No internet is available";
            Intent intent = new Intent(SplashActivity.this , InternetActivity.class);
            startActivity(intent);
            return status;
        }
        return status;
    }

    public void splashTime(){

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
}
