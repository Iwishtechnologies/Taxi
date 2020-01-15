package tech.iwish.taxi.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

        SharedpreferencesUser sharedpreferencesUser = new SharedpreferencesUser(this);
        data = sharedpreferencesUser.getShare();

        Object check = data.get(TEST_CHECH);

        Toast.makeText(this, ""+check, Toast.LENGTH_SHORT).show();


//        if(check == true){
//
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    Intent i=new Intent(SplashActivity.this,
//                            MainActivity.class);
//                    startActivity(i);
//                    finish();
//                }
//            }, SPLASH_SCREEN_TIME_OUT);
//
//        }
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent i=new Intent(SplashActivity.this,
//                        SignupActivity.class);
//                //Intent is used to switch from one activity to another.
//
//                startActivity(i);
//                //invoke the SecondActivity.
//
//                finish();
//                //the current activity will get finished.
//            }
//        }, SPLASH_SCREEN_TIME_OUT);

    }
}
