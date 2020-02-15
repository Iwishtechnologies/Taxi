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

import java.util.Random;

import tech.iwish.taxi.R;
import tech.iwish.taxi.activity.MainActivity;
import tech.iwish.taxi.config.Constants;
import tech.iwish.taxi.config.SharedpreferencesUser;
import tech.iwish.taxi.connection.ConnectionServer;
import tech.iwish.taxi.connection.JsonHelper;
import tech.iwish.taxi.extended.ButtonFont;


public class RideConfiremDriverDetailsFragment extends Fragment {

    private LinearLayout confirmOtp;
    private Random generate_otp;
    private int otp;
    private Button info_driver;
    private String rate;
    private String time;
    private String distance;
    private String driver_name;
    private String driver_mobile;
    private String driverId;
    private String otps;
    private TextView trip_distance, trip_duration, trip_rate, call_driver;
    private SharedpreferencesUser sharedpreferencesUser;
    private AlertDialog.Builder builder;
    private View dialogView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ride_confirem_driver_details, null);

//        confirmOtp = (LinearLayout)view.findViewById(R.id.confirmOtp);
        info_driver = (Button) view.findViewById(R.id.info_driver);
        trip_distance = (TextView) view.findViewById(R.id.trip_distance);
        trip_duration = (TextView) view.findViewById(R.id.trip_duration);
        trip_rate = (TextView) view.findViewById(R.id.trip_rate);


        generate_otp = new Random();
//        otp = generate_otp.nextInt(10000);
//        otp = generate_otp.nextInt((10000 - 9999));
//         otp = Integer.parseInt(String.format("%04d", generate_otp.nextInt(10000)));
        sharedpreferencesUser = new SharedpreferencesUser(getActivity());


        Bundle arguments = getArguments();
        rate = arguments.getString("rate");
        time = arguments.getString("time");
        distance = arguments.getString("distance");
        driver_name = arguments.getString("driver_name");
        driver_mobile = arguments.getString("driver_mobile");
        driverId = arguments.getString("driverId");
        otps = arguments.getString("otp");
        otp = Integer.parseInt(otps);

        trip_distance.setText(distance);
        trip_duration.setText(time);
        trip_rate.setText(rate);


        sharedpreferencesUser.driverInfo(driver_name, driver_mobile, rate, time, distance, otp);

        info_driver.setOnClickListener((View) -> {
            char[] breack_otp = String.valueOf(otp).toCharArray();
            builder = new AlertDialog.Builder(getActivity());
            ViewGroup viewGroup = view.findViewById(android.R.id.content);
            dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.row_driver_info_layout, viewGroup, false);
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
            Toast.makeText(getActivity(), "" + otp, Toast.LENGTH_SHORT).show();
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


        return view;
    }


}






















