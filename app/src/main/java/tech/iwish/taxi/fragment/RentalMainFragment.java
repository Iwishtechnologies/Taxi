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
import android.widget.FrameLayout;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.HashMap;
import java.util.Map;

import tech.iwish.taxi.R;

public class RentalMainFragment extends Fragment {

    public FrameLayout rental_frame;
    public Map<String, Double> latitude_logitude;
    Map<String, String> AddressMap;
    private KProgressHUD kProgressHUD;
    private TextView pickupsetlocation;


    public RentalMainFragment(Map<String, Double> latitude_logitude, Map<String, String> addressMap) {
        this.latitude_logitude = latitude_logitude ;
        this.AddressMap = addressMap ;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rental_main, null);

        rental_frame = (FrameLayout) view.findViewById(R.id.rental_frame);
        pickupsetlocation = (TextView)view.findViewById(R.id.pickupsetlocation);

        pickupsetlocation.setText(AddressMap.get("Pickupfulladress"));

        kProgressHUD = new KProgressHUD(getContext());


        RentalPackageFragmnet rentalPackageFragmnet = new RentalPackageFragmnet(latitude_logitude , AddressMap);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.rental_frame, rentalPackageFragmnet).commit();
        return view;

    }


    public void setProgressDialog(String msg) {
        kProgressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(msg)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

    }

    public void remove_progress_Dialog() {

        kProgressHUD.dismiss();
    }

}
