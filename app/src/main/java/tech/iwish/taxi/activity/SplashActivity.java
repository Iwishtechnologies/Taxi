package tech.iwish.taxi.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import java.util.Map;

import tech.iwish.taxi.R;
import tech.iwish.taxi.config.Constants;
import tech.iwish.taxi.config.SharedpreferencesUser;
import tech.iwish.taxi.connection.ConnectionServer;
import tech.iwish.taxi.connection.JsonHelper;
import tech.iwish.taxi.fragment.RideConfiremDriverDetailsFragment;

import static tech.iwish.taxi.config.SharedpreferencesUser.DRIVER_NAME;
import static tech.iwish.taxi.config.SharedpreferencesUser.DRIVER_NUMBER;
import static tech.iwish.taxi.config.SharedpreferencesUser.OTP;
import static tech.iwish.taxi.config.SharedpreferencesUser.REFER_CODE_LINK_CHECK;
import static tech.iwish.taxi.config.SharedpreferencesUser.TEST_CHECH;
import static tech.iwish.taxi.config.SharedpreferencesUser.USER_CONTACT;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN_TIME_OUT = 2000;
    Map data;
    SharedpreferencesUser sharedpreferencesUser;
    Object check;
    private AlertDialog.Builder builder;
    private View dialogView;
    Object driver_name , driver_number , otp;
    private String TAG;

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

        sharedpreferencesUser.setSocketConnection(false);
        check = data.get(TEST_CHECH);
//        sharedpreferencesUser.remove_otpConfirmDriver();

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        driver_name = data.get(DRIVER_NAME);
        driver_number = data.get(DRIVER_NUMBER);
        otp = data.get(OTP);
        getConnectivityStatusString();

    }


    public String getConnectivityStatusString() {
        String status = null;
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);
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
            Intent intent = new Intent(SplashActivity.this, InternetActivity.class);
            startActivity(intent);
            return status;
        }
        return status;
    }

    public void splashTime() {

        new Handler().postDelayed(() -> {
            if (check == null) {

//                ============================================================================
                FirebaseDynamicLinks.getInstance()
                        .getDynamicLink(getIntent())
                        .addOnSuccessListener(this, pendingDynamicLinkData -> {
                            // Get deep link from result (may be null if no link is found)
                            Uri deepLink = null;
                            if (pendingDynamicLinkData != null) {
                                deepLink = pendingDynamicLinkData.getLink();
                                String[] arrOfStr = deepLink.toString().split("=", 2);

                                sharedpreferencesUser.refer_code_chech(arrOfStr[1]);

                            }
                        })
                        .addOnFailureListener(this, e -> Log.w(TAG, "getDynamicLink:onFailure", e));
//                ============================================================================

//                if(data.get(REFER_CODE_LINK_CHECK).toString() != null){
//                    Toast.makeText(this, "refer code", Toast.LENGTH_SHORT).show();
//                }
                Intent i = new Intent(SplashActivity.this, SignupActivity.class);
                startActivity(i);
                finish();
            } else {

                if (driver_name != null) {
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    i.putExtra("checkDriverData", "checkDriverData");
                    i.putExtra("driver_name", driver_name.toString());
                    i.putExtra("driver_number", driver_number.toString());
                    i.putExtra("otp", otp.toString());
                    startActivity(i);

                } else {
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }

        }, SPLASH_SCREEN_TIME_OUT);
    }
}
