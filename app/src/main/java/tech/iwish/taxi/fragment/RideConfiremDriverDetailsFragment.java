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
import android.widget.ImageView;
import android.widget.TextView;

import tech.iwish.taxi.R;


public class RideConfiremDriverDetailsFragment extends Fragment {
    ImageView blue_pin_icon , red_pin_icon ;
    TextView pickup , droplocation ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ride_confirem_driver_details,null);

        blue_pin_icon = (ImageView)view.findViewById(R.id.red_pin_icon);
        red_pin_icon = (ImageView)view.findViewById(R.id.red_pin_icon);
        pickup = (TextView)view.findViewById(R.id.pickup);
        droplocation = (TextView)view.findViewById(R.id.droplocation);

//        blue_pin_icon.setVisibility(View.GONE);
//        red_pin_icon.setVisibility(View.GONE);
//        pickup.setVisibility(View.GONE);
//        droplocation.setVisibility(View.GONE);


        return view;
    }
}
