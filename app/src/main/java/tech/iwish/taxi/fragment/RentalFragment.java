package tech.iwish.taxi.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import tech.iwish.taxi.R;
import tech.iwish.taxi.adapter.PickupLocationAdapter;
import tech.iwish.taxi.adapter.RentalAdapter;
import tech.iwish.taxi.config.Constants;
import tech.iwish.taxi.connection.ConnectionServer;
import tech.iwish.taxi.connection.JsonHelper;
import tech.iwish.taxi.other.PickupLocationList;

public class RentalFragment extends DialogFragment implements SearchView.OnQueryTextListener {

    private SearchView rental_searchview ;
    private RecyclerView rental_recycleview ;
    private List<PickupLocationList> pickupLocationLists = new ArrayList<>();
    public Rentalint setrentaldat;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rental , container , false);

        rental_searchview = (SearchView)view.findViewById(R.id.rental_searchview);
        rental_recycleview= (RecyclerView) view.findViewById(R.id.rental_recycleview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rental_recycleview.setLayoutManager(linearLayoutManager);

        rental_searchview.setOnQueryTextListener(this);

        return view;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {


/*
        ConnectionServer connectionServer = new ConnectionServer();
        connectionServer.set_url(Constants.SEARCH_PLACE);
        connectionServer.requestedMethod("POST");
        connectionServer.buildParameter("value", s);
        connectionServer.execute(new ConnectionServer.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                Log.e("output", output);
                JsonHelper jsonHelper = new JsonHelper(output);
                if (jsonHelper.isValidJson()) {
                    pickupLocationLists.clear();
                    String response = jsonHelper.GetResult("status");
                    if (response.equals("OK")) {
                        JSONArray jsonArray = jsonHelper.setChildjsonArray(jsonHelper.getCurrentJsonObj(), "predictions");


                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonHelper.setChildjsonObj(jsonArray, i);

                            pickupLocationLists.add(new PickupLocationList(jsonHelper.GetResult("description"), jsonHelper.GetResult("id"), jsonHelper.GetResult("matched_substrings"), jsonHelper.GetResult("place_id"), jsonHelper.GetResult("reference"), jsonHelper.GetResult("structured_formatting"), jsonHelper.GetResult("terms")));

                        }
                        RentalAdapter rentalAdapter = new RentalAdapter(getActivity() , pickupLocationLists);
                        rental_recycleview.setAdapter(rentalAdapter);
                        rentalAdapter.setrentalPlace(data -> setrentaldat.rentaldatass(data));
                    }
                }
            }
        });
*/




        OkHttpClient okHttpClient = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("value", s);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request1 = new Request.Builder().url(Constants.SEARCH_PLACE).post(body).build();


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
                        pickupLocationLists.clear();
                        String responses = jsonHelper.GetResult("status");
                        if (responses.equals("OK")) {
                            JSONArray jsonArray = jsonHelper.setChildjsonArray(jsonHelper.getCurrentJsonObj(), "predictions");


                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonHelper.setChildjsonObj(jsonArray, i);

                                pickupLocationLists.add(new PickupLocationList(jsonHelper.GetResult("description"), jsonHelper.GetResult("id"), jsonHelper.GetResult("matched_substrings"), jsonHelper.GetResult("place_id"), jsonHelper.GetResult("reference"), jsonHelper.GetResult("structured_formatting"), jsonHelper.GetResult("terms")));

                            }
                            if(getActivity() != null){
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        RentalAdapter rentalAdapter = new RentalAdapter(getActivity() , pickupLocationLists);
                                        rental_recycleview.setAdapter(rentalAdapter);
                                        rentalAdapter.setrentalPlace(data -> setrentaldat.rentaldatass(data));

                                    }
                                });
                            }
                        }
                    }
                }
            }
        });




        return false;
    }

    public void setRentalDatas(Rentalint rentalDatas){
        this.setrentaldat = rentalDatas;
    }

    public interface Rentalint{
        public void rentaldatass(String data);
    }

}






















