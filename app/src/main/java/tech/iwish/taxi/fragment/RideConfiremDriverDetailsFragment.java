package tech.iwish.taxi.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

import java.util.Random;

import tech.iwish.taxi.R;
import tech.iwish.taxi.activity.MainActivity;
import tech.iwish.taxi.extended.ButtonFont;


public class RideConfiremDriverDetailsFragment extends Fragment  {

    private LinearLayout confirmOtp ;
    private Random generate_otp ;
    private int otp ;
    private Button info_driver;
    private String rate , time , distance , driver_name , driver_mobile;
    private TextView trip_distance , trip_duration ,trip_rate ;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ride_confirem_driver_details,null);

        confirmOtp = (LinearLayout)view.findViewById(R.id.confirmOtp);
        info_driver = (Button)view.findViewById(R.id.info_driver);
        trip_distance = (TextView)view.findViewById(R.id.trip_distance);
        trip_duration = (TextView)view.findViewById(R.id.trip_duration);
        trip_rate = (TextView)view.findViewById(R.id.trip_rate);


        generate_otp = new Random();
        otp = generate_otp.nextInt(10000);
        Toast.makeText(getActivity(), ""+otp, Toast.LENGTH_SHORT).show();
        Bundle arguments = getArguments();
        rate = arguments.getString("rate");
        time = arguments.getString("time");
        distance= arguments.getString("distance");
        driver_name = arguments.getString("driver_name");
        driver_mobile = arguments.getString("driver_mobile");

        Toast.makeText(getActivity(), ""+driver_name +" "+driver_mobile, Toast.LENGTH_SHORT).show();


        trip_distance.setText(distance);
        trip_duration.setText(time);
        trip_rate.setText(rate);

//        Toast.makeText(getActivity(), ""+rate + " "+time +" "+distance, Toast.LENGTH_SHORT).show();


        info_driver.setOnClickListener((View)->{

            Toast.makeText(getActivity(), ""+otp, Toast.LENGTH_SHORT).show();
            Animation animation =  AnimationUtils.loadAnimation(getActivity(),R.anim.otp_anim);

        });




        return view;
    }


}
