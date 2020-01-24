package tech.iwish.taxi.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mancj.materialsearchbar.MaterialSearchBar;

import org.json.JSONArray;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import tech.iwish.taxi.R;
import tech.iwish.taxi.adapter.PickupLocationAdapter;
import tech.iwish.taxi.config.Constants;
import tech.iwish.taxi.connection.ConnectionServer;
import tech.iwish.taxi.connection.JsonHelper;
import tech.iwish.taxi.other.PickupLocationList;

public class Pickup_Search_fragment extends DialogFragment implements TextWatcher {

    MaterialSearchBar pickuploactionMaterialSearchBar ;
    RecyclerView pickuprecycle;
    List<PickupLocationList> pickupLocationLists= new ArrayList<>();
    private PickupInterFacePlace pickuplocationName;

//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.pickup_dialog_fragemnt ,container , false);
//
//
//        return view;
//    }

        @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pickup_dialog_fragemnt,container , false);

        pickuploactionMaterialSearchBar = (MaterialSearchBar)view.findViewById(R.id.pickuploactionMaterialSearchBar);
        pickuprecycle = (RecyclerView)view. findViewById(R.id.pickuprecycle);
        pickuploactionMaterialSearchBar.setSpeechMode(false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        pickuprecycle.setLayoutManager(linearLayoutManager);

//        PickPlaceAdapter pickPlaceAdapter = new PickPlaceAdapter(PickupSearchActivity.this , );
//        pickuprecycle.setAdapter(pickPlaceAdapter);
//        pickPlaceAdapter.notifyDataSetChanged();


        pickuploactionMaterialSearchBar.addTextChangeListener(this);

        return view ;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        ConnectionServer connectionServer = new ConnectionServer();
        connectionServer.set_url(Constants.SEARCH_PLACE_PICKUP);
        connectionServer.requestedMethod("POST");
        connectionServer.buildParameter("value" , charSequence.toString());
        connectionServer.execute(new ConnectionServer.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                Log.e("output",output);
                JsonHelper jsonHelper = new JsonHelper(output);
                if(jsonHelper.isValidJson()){
                    pickupLocationLists.clear();
                    String response = jsonHelper.GetResult("status");
                    if(response.equals("OK")){
                        JSONArray jsonArray = jsonHelper.setChildjsonArray(jsonHelper.getCurrentJsonObj(), "predictions");


                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonHelper.setChildjsonObj(jsonArray, i);

                            pickupLocationLists.add(new PickupLocationList(jsonHelper.GetResult("description"),jsonHelper.GetResult("id"),jsonHelper.GetResult("matched_substrings"),jsonHelper.GetResult("place_id"),jsonHelper.GetResult("reference"),jsonHelper.GetResult("structured_formatting"),jsonHelper.GetResult("terms")));

                        }
                        PickupLocationAdapter pickupLocationAdapter = new PickupLocationAdapter(getActivity() , pickupLocationLists );
                        pickuprecycle.setAdapter(pickupLocationAdapter);

                        pickupLocationAdapter.setOnPickupListner(new PickupLocationAdapter.onPickupListner() {
                            @Override
                            public void onListen(String location) {


                                pickuplocationName.placeName_Pickup(location);

                            }
                        });



                    }
                }
            }
        });

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }


    public void valuePickup(PickupInterFacePlace pickupInterFacePlaces){
            this.pickuplocationName = pickupInterFacePlaces ;
    }

public    interface PickupInterFacePlace{
         public   void placeName_Pickup(String location);
    }

}
