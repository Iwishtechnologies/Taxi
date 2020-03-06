package tech.iwish.taxi.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tech.iwish.taxi.R;
import tech.iwish.taxi.adapter.RideHistroyAdapter;
import tech.iwish.taxi.config.Constants;
import tech.iwish.taxi.config.SharedpreferencesUser;
import tech.iwish.taxi.connection.ConnectionServer;
import tech.iwish.taxi.connection.JsonHelper;
import tech.iwish.taxi.other.RideHistroy;
import tech.iwish.taxi.other.User_DetailsList;

import static tech.iwish.taxi.config.SharedpreferencesUser.USER_CONTACT;

public class RideHistoryFragment extends Fragment {

    private Map data;
    private List<RideHistroy> rideHistroys = new ArrayList<>();
    private RecyclerView rideHistroy_recycle;
    private KProgressHUD kProgressHUD;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ride_history, null);

        rideHistroy_recycle = (RecyclerView)view.findViewById(R.id.rideHistroy_recycle);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rideHistroy_recycle.setLayoutManager(linearLayoutManager);

        SharedpreferencesUser sharedpreferencesUser = new SharedpreferencesUser(getContext());
        data = sharedpreferencesUser.getShare();
        String user_contact = data.get(USER_CONTACT).toString();

        kProgressHUD = new KProgressHUD(getActivity());
        setProgressDialog("Ride Histroy");

        ConnectionServer connectionServer = new ConnectionServer();
        connectionServer.set_url(Constants.RIDE_HISTROY);
        connectionServer.requestedMethod("POST");
        connectionServer.buildParameter("trackingid", user_contact);
        connectionServer.execute(output -> {
            Log.e("output", output);
            JsonHelper jsonHelper = new JsonHelper(output);
            if (jsonHelper.isValidJson()) {
                String response = jsonHelper.GetResult("response");
                if (response.equals("TRUE")) {
                    JSONArray jsonArray = jsonHelper.setChildjsonArray(jsonHelper.getCurrentJsonObj(), "data");



                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonHelper.setChildjsonObj(jsonArray, i);
                        rideHistroys.add(new RideHistroy(jsonHelper.GetResult("id"),jsonHelper.GetResult("driver_id"),jsonHelper.GetResult("pickup_city_name"),jsonHelper.GetResult("drop_city_name"),jsonHelper.GetResult("amount"),jsonHelper.GetResult("vehicle_cat"),jsonHelper.GetResult("date"),jsonHelper.GetResult("time"),jsonHelper.GetResult("status"),jsonHelper.GetResult("trackingid"),jsonHelper.GetResult("image")));

                    }

                    RideHistroyAdapter rideHistroyAdapter= new RideHistroyAdapter(getActivity() , rideHistroys);
                    rideHistroy_recycle.setAdapter(rideHistroyAdapter);
                    remove_progress_Dialog();
                }
            }
        });

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























