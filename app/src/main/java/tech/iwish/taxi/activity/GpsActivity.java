package tech.iwish.taxi.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import tech.iwish.taxi.R;

public class GpsActivity extends AppCompatActivity {

    private Button gpsenable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);
        getSupportActionBar().hide();

        gpsenable = (Button) findViewById(R.id.gpsenable);

        // Check if enabled and if not send user to the GPS settings

        gpsenable.setOnClickListener(view -> {

//            Toast.makeText(this, ""+checkGps (), Toast.LENGTH_SHORT).show();

            if (!checkGps()) {
                Toast.makeText(this, "Check Gps", Toast.LENGTH_SHORT).show();
                checkGps ();
            } else {
//                Intent intent = new Intent(GpsActivity.this, GpsActivity.class);
//                startActivity(intent);
                Intent intent = new Intent(GpsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }

    public boolean checkGps (){
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return enabled ;
    }

}
