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
import android.widget.SearchView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import tech.iwish.taxi.R;
import tech.iwish.taxi.adapter.PackageAdapter;
import tech.iwish.taxi.config.Constants;
import tech.iwish.taxi.connection.ConnectionServer;
import tech.iwish.taxi.connection.JsonHelper;
import tech.iwish.taxi.other.PackageVehicle;
import tech.iwish.taxi.other.User_DetailsList;


public class RentalPackageFragmnet extends Fragment {

    private RecyclerView pakagerecycleview ;
    private List<PackageVehicle> packageVehicles = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rental_package_fragmnet , null);

        pakagerecycleview = (RecyclerView)view.findViewById(R.id.pakagerecycleview);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        pakagerecycleview.setLayoutManager(linearLayoutManager);


        ConnectionServer connectionServer = new ConnectionServer();
        connectionServer.set_url(Constants.PACKAGEVEHICLE);
        connectionServer.requestedMethod("POST");
        connectionServer.execute(new ConnectionServer.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                Log.e("output" , output);
                JsonHelper jsonHelper = new JsonHelper(output);
                if(jsonHelper.isValidJson()){
                    String response = jsonHelper.GetResult("response");
                    if(response.equals("TRUE")){

                        JSONArray jsonArray = jsonHelper.setChildjsonArray(jsonHelper.getCurrentJsonObj(), "data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonHelper.setChildjsonObj(jsonArray, i);

                            packageVehicles.add(new PackageVehicle(jsonHelper.GetResult("packid"),jsonHelper.GetResult("package_type"),jsonHelper.GetResult("vahicle_cat_id"),jsonHelper.GetResult("amount")));

                        }
                        PackageAdapter packageAdapter = new PackageAdapter(getActivity(),packageVehicles);

                    }
                }
            }
        });

        return view;
    }
}
