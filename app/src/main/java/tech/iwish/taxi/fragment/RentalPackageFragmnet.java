package tech.iwish.taxi.fragment;

import android.content.Context;
import android.content.pm.PackageManager;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tech.iwish.taxi.R;
import tech.iwish.taxi.adapter.PackageAdapter;
import tech.iwish.taxi.config.Constants;
import tech.iwish.taxi.connection.ConnectionServer;
import tech.iwish.taxi.connection.JsonHelper;
import tech.iwish.taxi.other.PackageVehicle;


public class RentalPackageFragmnet extends Fragment {

    private RecyclerView pakagerecycleview;
    private List<PackageVehicle> packageVehicles = new ArrayList<>();
    public Map<String, Double> latitude_logitude;
    Map<String, String> AddressMap;
    private KProgressHUD kProgressHUD;


    public RentalPackageFragmnet(Map<String, Double> latitude_logitude, Map<String, String> addressMap) {
        this.latitude_logitude = latitude_logitude;
        this.AddressMap = addressMap;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rental_package_fragmnet, null);

        pakagerecycleview = (RecyclerView) view.findViewById(R.id.pakagerecycleview);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        pakagerecycleview.setLayoutManager(linearLayoutManager);
        kProgressHUD = new KProgressHUD(getContext());

        setProgressDialog("Package Search");

        ConnectionServer connectionServer = new ConnectionServer();
        connectionServer.set_url(Constants.PACKAGEVEHICLE);
        connectionServer.requestedMethod("POST");
        connectionServer.execute(output -> {
            Log.e("output", output);
            JsonHelper jsonHelper = new JsonHelper(output);
            if (jsonHelper.isValidJson()) {
                String response = jsonHelper.GetResult("response");
                if (response.equals("TRUE")) {

                    JSONArray jsonArray = jsonHelper.setChildjsonArray(jsonHelper.getCurrentJsonObj(), "data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonHelper.setChildjsonObj(jsonArray, i);

                        packageVehicles.add(new PackageVehicle(jsonHelper.GetResult("packid"), jsonHelper.GetResult("package_type"), jsonHelper.GetResult("vahicle_cat_id"), jsonHelper.GetResult("amount")));

                    }
                    PackageAdapter packageAdapter = new PackageAdapter(getActivity(), packageVehicles, latitude_logitude, AddressMap);
                    pakagerecycleview.setAdapter(packageAdapter);
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
