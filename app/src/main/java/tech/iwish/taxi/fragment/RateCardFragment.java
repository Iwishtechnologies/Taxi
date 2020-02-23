package tech.iwish.taxi.fragment;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import tech.iwish.taxi.R;
import tech.iwish.taxi.activity.MainActivity;
import tech.iwish.taxi.adapter.RateCardAdapter;
import tech.iwish.taxi.config.Constants;
import tech.iwish.taxi.connection.ConnectionServer;
import tech.iwish.taxi.connection.JsonHelper;
import tech.iwish.taxi.other.RateCardLists;


public class RateCardFragment extends Fragment {

    public List<RateCardLists> rateCardLists = new ArrayList<>();
    public RecyclerView ratecardRecycleview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rate_card, null);

        ratecardRecycleview = (RecyclerView)view.findViewById(R.id.ratecardRecycleview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        ratecardRecycleview.setLayoutManager(linearLayoutManager);

        ConnectionServer connectionServer = new ConnectionServer();
        connectionServer.set_url(Constants.RATE_CARD);
        connectionServer.requestedMethod("POST");
        connectionServer.execute(output -> {
            Log.e("output", output);
            JsonHelper jsonHelper = new JsonHelper(output);
            if (jsonHelper.isValidJson()) {
                String response = jsonHelper.GetResult("response");
                if (response.equals("TRUE")) {
                    JSONArray jsonArray = jsonHelper.setChildjsonArray(jsonHelper.getCurrentJsonObj(), "data");

//                    if(jsonArray.length() > 0){
//                        Toast.makeText(getActivity(), "data", Toast.LENGTH_SHORT).show();
//                    }else{
//                        Toast.makeText(getActivity(), "data not", Toast.LENGTH_SHORT).show();
//                    }

                    for (int i = 0; i < jsonArray.length(); i++) {

                        jsonHelper.setChildjsonObj(jsonArray, i);
                        rateCardLists.add(new RateCardLists(jsonHelper.GetResult("catagory_id"), jsonHelper.GetResult("catagory_name"), jsonHelper.GetResult("MinRate"), jsonHelper.GetResult("Rate_Km"), jsonHelper.GetResult("waitingRate_m"), jsonHelper.GetResult("rtc_m"), jsonHelper.GetResult("img")));

                    }
                    RateCardAdapter rateCardAdapter = new RateCardAdapter(getActivity(),rateCardLists);
                    ratecardRecycleview.setAdapter(rateCardAdapter);


                }
            }
        });

        return view;
    }

    public boolean allowBackPressed() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        return true;
    }
}
