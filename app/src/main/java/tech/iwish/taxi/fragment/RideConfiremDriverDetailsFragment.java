package tech.iwish.taxi.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.goodiebag.pinview.Pinview;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.Random;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import tech.iwish.taxi.R;
import tech.iwish.taxi.activity.MainActivity;
import tech.iwish.taxi.config.Constants;
import tech.iwish.taxi.config.SharedpreferencesUser;
import tech.iwish.taxi.connection.ConnectionServer;
import tech.iwish.taxi.connection.JsonHelper;
import tech.iwish.taxi.extended.ButtonFont;

import static tech.iwish.taxi.config.SharedpreferencesUser.DRIVER_OTP;


public class RideConfiremDriverDetailsFragment extends Fragment {

    private LinearLayout confirmOtp, trip_dis;
    private Random generate_otp;
    private int otp;
    private Button info_driver;
    private String rate;
    private String time;
    private String distance;
    private String driver_name;
    private String driver_mobile;
    private String driverId, rental, trackid;
    private String otps, outstation, otpsdriver;
    private TextView trip_distance, trip_duration, trip_rate, call_driver, amts;
    private SharedpreferencesUser sharedpreferencesUser;
    private AlertDialog.Builder builder;
    private View dialogView;
    View view;
    private Map data;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ride_confirem_driver_details, null);

//        confirmOtp = (LinearLayout)view.findViewById(R.id.confirmOtp);
        info_driver = (Button) view.findViewById(R.id.info_driver);
        trip_distance = (TextView) view.findViewById(R.id.trip_distance);
        trip_duration = (TextView) view.findViewById(R.id.trip_duration);
        trip_rate = (TextView) view.findViewById(R.id.trip_rate);
        amts = (TextView) view.findViewById(R.id.amts);
        trip_dis = (LinearLayout) view.findViewById(R.id.trip_dis);


        generate_otp = new Random();
        sharedpreferencesUser = new SharedpreferencesUser(getActivity());

        data = sharedpreferencesUser.getShare();

        Bundle arguments = getArguments();
        rate = arguments.getString("rate");
        time = arguments.getString("time");
        distance = arguments.getString("distance");
        driver_name = arguments.getString("driver_name");
        driver_mobile = arguments.getString("driver_mobile");
//        driverId = arguments.getString("driverId");
        otps = arguments.getString("otp");
        rental = arguments.getString("rental");
        trackid = arguments.getString("trackid");

        this.otpsdriver = otps;

        amts.setText(rate);


        OkHttpClient okHttpClient = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("driverId", driverId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request1 = new Request.Builder().url(Constants.OTP_CHECK).post(body).build();


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

                            JSONArray jsonArray = jsonHelper.setChildjsonArray(jsonHelper.getCurrentJsonObj(), "data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonHelper.setChildjsonObj(jsonArray, i);
                                driverId = jsonHelper.GetResult("data");
                            }
                        }
                    }
                }
            }
        });


        if (rental != null) {
            trip_dis.setVisibility(View.GONE);
            driver();
        } else if (outstation != null) {
            driver();
        } else {

            otp = Integer.parseInt(otps);
            trip_distance.setText(distance);
            trip_duration.setText(time);
            trip_rate.setText(rate);
            driver();
        }
        return view;
    }

    private void driver() {
        sharedpreferencesUser.driverInfo(driver_name, driver_mobile, rate, time, distance, otpsdriver, trackid);

        info_driver.setOnClickListener((View) -> {
            char[] breack_otp = otpsdriver.toCharArray();
            builder = new AlertDialog.Builder(getActivity());
//            ViewGroup viewGroup = view.findViewById(android.R.id.content);
            dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.row_driver_info_layout, null);
            builder.setView(dialogView);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            call_driver = dialogView.findViewById(R.id.call_driver);
            TextView name_drivers = dialogView.findViewById(R.id.name_drivers);
            TextView otp_1 = dialogView.findViewById(R.id.otp_1);
            TextView otp_2 = dialogView.findViewById(R.id.otp_2);
            TextView otp_3 = dialogView.findViewById(R.id.otp_3);
            TextView otp_4 = dialogView.findViewById(R.id.otp_4);
            name_drivers.setText(driver_name);
//            Toast.makeText(getActivity(), "" + breack_otp, Toast.LENGTH_SHORT).show();
            otp_1.setText(String.valueOf(breack_otp[0]));
            otp_2.setText(String.valueOf(breack_otp[1]));
            otp_3.setText(String.valueOf(breack_otp[2]));
            otp_4.setText(String.valueOf(breack_otp[3]));
            call_driver.setOnClickListener((view1) -> {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + driver_mobile));
                startActivity(intent);
            });

        });

    }

}






















