package tech.iwish.taxi.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.Toast;

import com.mancj.materialsearchbar.MaterialSearchBar;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import java.io.IOException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.http.Url;
import tech.iwish.taxi.Interface.PickupLocationInterface;
import tech.iwish.taxi.R;
import tech.iwish.taxi.RecyclerTouchListener;
import tech.iwish.taxi.activity.LoginActivity;
import tech.iwish.taxi.activity.SignupActivity;
import tech.iwish.taxi.adapter.PickupLocationAdapter;
import tech.iwish.taxi.config.Constants;
import tech.iwish.taxi.connection.ConnectionServer;
import tech.iwish.taxi.connection.JsonHelper;
import tech.iwish.taxi.other.PickupLocationList;

public class Pickup_Search_fragment extends DialogFragment {

    MaterialSearchBar pickuploactionMaterialSearchBar;
    RecyclerView pickuprecycle;
    List<PickupLocationList> pickupLocationLists = new ArrayList<>();
    private PickupInterFacePlace pickuplocationName;
    private SearchView placeSearchview;
    private Map<String, Double> latitude_logitude;
    PickupLocationAdapter pickupLocationAdapter;
    PickupLocationInterface pickupLocationInterface;

    public Pickup_Search_fragment(Map<String, Double> latitude_logitude , PickupLocationInterface pickupLocationInterface) {
        this.latitude_logitude = latitude_logitude;
        this.pickupLocationInterface = pickupLocationInterface;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pickup_dialog_fragemnt, container, false);

        pickuploactionMaterialSearchBar = (MaterialSearchBar) view.findViewById(R.id.pickuploactionMaterialSearchBar);
        pickuprecycle = (RecyclerView) view.findViewById(R.id.pickuprecycle);
        placeSearchview = (SearchView) view.findViewById(R.id.placeSearchview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        pickuprecycle.setLayoutManager(linearLayoutManager);

        pickuprecycle.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), pickuprecycle, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                InputMethodManager input = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                input.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                pickupLocationAdapter.setOnPickupListner(location -> pickuplocationName.placeName_Pickup(location));
                pickupLocationInterface.pickuplocationInterface(pickupLocationLists.get(position).getDescription());

            }

            @Override
            public void onLongItemClick(View view, int position) {


            }
        }));


        placeSearchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                setPickupLocation(s);

/*
                ConnectionServer connectionServer = new ConnectionServer();
                connectionServer.set_url(Constants.SEARCH_PLACE);
                connectionServer.requestedMethod("POST");
                connectionServer.buildParameter("PickLatitude", String.valueOf(latitude_logitude.get("PickLatitude")));
                connectionServer.buildParameter("PickLogitude", String.valueOf(latitude_logitude.get("PickLogitude")));
                connectionServer.buildParameter("value", s);
                connectionServer.execute(output -> {
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
                            PickupLocationAdapter pickupLocationAdapter = new PickupLocationAdapter(getActivity(), pickupLocationLists);
                            pickuprecycle.setAdapter(pickupLocationAdapter);

                            pickupLocationAdapter.setOnPickupListner(location -> pickuplocationName.placeName_Pickup(location));

                        }
                    }
                });
*/
                return false;
            }
        });

        return view;
    }

    private void setPickupLocation(String s) {
        pickupLocationLists.clear();
        String URL = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input='india'"+s+"&location="+String.valueOf(latitude_logitude.get("PickLatitude"))+","+ String.valueOf(latitude_logitude.get("PickLogitude"))+" &radius=5000&key=AIzaSyApe8T3JMiqj9OgEbqd--zTBfl3fibPeEs";


        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(URL)
                .build();



//        //A) go through the queued calls and cancel if the tag matches:
//        for (Call call : client.dispatcher().queuedCalls()) {
//
//            if (call.request().tag().equals("requestKey"))
//                call.cancel();
//        }
//
//        //B) go through the running calls and cancel if the tag matches:
//        for (Call call : client.dispatcher().runningCalls()) {
//            if (call.request().tag().equals("requestKey"))
//                call.cancel();
//        }

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }



            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();

                    Log.e("result",result);

                    JsonHelper jsonHelper = new JsonHelper(result);
                    if (jsonHelper.isValidJson()) {
                        String responses = jsonHelper.GetResult("status");
                        if (responses.equals("OK")) {
//                            pickupLocationLists.clear();
                            JSONArray jsonArray = jsonHelper.setChildjsonArray(jsonHelper.getCurrentJsonObj(), "predictions");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonHelper.setChildjsonObj(jsonArray, i);
                                pickupLocationLists.add(new PickupLocationList(jsonHelper.GetResult("description"), jsonHelper.GetResult("id"), jsonHelper.GetResult("matched_substrings"), jsonHelper.GetResult("place_id"), jsonHelper.GetResult("reference"), jsonHelper.GetResult("structured_formatting"), jsonHelper.GetResult("terms")));

                            }

                            if (getActivity() != null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                         pickupLocationAdapter = new PickupLocationAdapter(getActivity(), pickupLocationLists);
                                        pickuprecycle.setAdapter(pickupLocationAdapter);


                                    }
                                });
                            }

                        }
                    }

                }
            }
        });

    }

    /*private void setPickupLocation(String s) {

        String URL = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input='india'"+s+"&location="+String.valueOf(latitude_logitude.get("PickLatitude"))+","+ String.valueOf(latitude_logitude.get("PickLogitude"))+" &radius=5000&key=AIzaSyApe8T3JMiqj9OgEbqd--zTBfl3fibPeEs";


        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("PickLatitude", String.valueOf(latitude_logitude.get("PickLatitude")))
                .addFormDataPart("PickLogitude", String.valueOf(latitude_logitude.get("PickLogitude")))
                .addFormDataPart("value", s)
                .build();
//        Request request1 = new Request.Builder().url(Constants.SEARCH_PLACE).post(requestBody).build();
        Request request1 = new Request.Builder().url(URL).post(requestBody).build();

        //A) go through the queued calls and cancel if the tag matches:
        for (Call call : okHttpClient.dispatcher().queuedCalls()) {

            if (call.request().tag().equals("requestKey"))
                call.cancel();
        }

        //B) go through the running calls and cancel if the tag matches:
        for (Call call : okHttpClient.dispatcher().runningCalls()) {
            if (call.request().tag().equals("requestKey"))
                call.cancel();
        }


        okHttpClient.newCall(request1).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Connection Time out", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    Log.e("result", String.valueOf(result));
                    Log.e("result", String.valueOf(result));


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

                            if (getActivity() != null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        PickupLocationAdapter pickupLocationAdapter = new PickupLocationAdapter(getActivity(), pickupLocationLists);
                                        pickuprecycle.setAdapter(pickupLocationAdapter);
                                        pickupLocationAdapter.setOnPickupListner(location -> pickuplocationName.placeName_Pickup(location));


                                    }
                                });
                            }


                        }
                    }

                }
            }
        });


    }
*/

    public void valuePickup(PickupInterFacePlace pickupInterFacePlaces) {
        this.pickuplocationName = pickupInterFacePlaces;
    }

    public interface PickupInterFacePlace {
        void placeName_Pickup(String location);
    }

}
