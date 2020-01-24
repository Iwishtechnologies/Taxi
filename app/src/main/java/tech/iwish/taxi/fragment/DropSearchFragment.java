package tech.iwish.taxi.fragment;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.mancj.materialsearchbar.MaterialSearchBar;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tech.iwish.taxi.R;
import tech.iwish.taxi.adapter.DropLocationAdapter;
import tech.iwish.taxi.config.Constants;
import tech.iwish.taxi.connection.ConnectionServer;
import tech.iwish.taxi.connection.JsonHelper;
import tech.iwish.taxi.other.DropLocationList;

public class DropSearchFragment extends DialogFragment implements TextWatcher {

    MaterialSearchBar droplocationMaterial;
    List<DropLocationList> dropLocationListList = new ArrayList<>();
    RecyclerView droplocationrecycleview;
    Map<String, LatLng> allLatLng;
    public dropValuePass droplocationplacenae;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.drop_dialog_fragment, container, false);
        droplocationMaterial = (MaterialSearchBar) view.findViewById(R.id.droplocationMaterial);
        droplocationrecycleview = (RecyclerView) view.findViewById(R.id.droplocationrecycleview);
        droplocationMaterial.setSpeechMode(false);
        droplocationMaterial.addTextChangeListener(this);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        droplocationrecycleview.setLayoutManager(linearLayoutManager);



        return view;


    }




    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


        ConnectionServer connectionServer = new ConnectionServer();
        connectionServer.set_url(Constants.SEARCH_PLACE);
        connectionServer.requestedMethod("POST");
        connectionServer.buildParameter("value", charSequence.toString());
        connectionServer.execute(new ConnectionServer.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                Log.e("output", output);
                JsonHelper jsonHelper = new JsonHelper(output);
                if (jsonHelper.isValidJson()) {
                    dropLocationListList.clear();
                    String response = jsonHelper.GetResult("status");
                    if (response.equals("OK")) {
                        JSONArray jsonArray = jsonHelper.setChildjsonArray(jsonHelper.getCurrentJsonObj(), "predictions");

//                        JSONObject aa = jsonHelper.setChildjsonObj(jsonHelper.getCurrentJsonObj(), "structured_formatting");
//                        for(int j = 0 ; j< aa.length() ; j++){
//
//                            Toast.makeText(DropPlaceLocation.this, ""+j, Toast.LENGTH_SHORT).show();
//                        }

                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonHelper.setChildjsonObj(jsonArray, i);

                            dropLocationListList.add(new DropLocationList(jsonHelper.GetResult("description"), jsonHelper.GetResult("id"), jsonHelper.GetResult("matched_substrings"), jsonHelper.GetResult("place_id"), jsonHelper.GetResult("reference"), jsonHelper.GetResult("structured_formatting"), jsonHelper.GetResult("terms")));

                        }
                        DropLocationAdapter dropLocationAdapter = new DropLocationAdapter(getActivity(), dropLocationListList);
                        droplocationrecycleview.setAdapter(dropLocationAdapter);

                        dropLocationAdapter.dropLoactionval(new DropLocationAdapter.DropListener() {
                            @Override
                            public void setdropPlace(String value) {

                                droplocationplacenae.dropLOcationPlaceName(value);

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

    public void Dropvalue(dropValuePass dropValuePasss){
        this.droplocationplacenae = dropValuePasss;
    }

    public interface dropValuePass {
        void dropLOcationPlaceName(String value);
    }




}
