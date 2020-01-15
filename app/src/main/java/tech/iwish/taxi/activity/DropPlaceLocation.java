package tech.iwish.taxi.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.mancj.materialsearchbar.MaterialSearchBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tech.iwish.taxi.R;
import tech.iwish.taxi.adapter.DropLocationAdapter;
import tech.iwish.taxi.config.Constants;
import tech.iwish.taxi.connection.ConnectionServer;
import tech.iwish.taxi.connection.JsonHelper;
import tech.iwish.taxi.other.DropLocationList;

public class DropPlaceLocation extends AppCompatActivity implements  MaterialSearchBar.OnSearchActionListener  , TextWatcher {

    MaterialSearchBar droplocationMaterial;
    List<DropLocationList> dropLocationListList = new ArrayList<>();
    RecyclerView droplocationrecycleview;
    Map<String, LatLng> allLatLng ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drop_place_location);
        droplocationMaterial = (MaterialSearchBar) findViewById(R.id.droplocationMaterial);
        droplocationrecycleview = (RecyclerView)findViewById(R.id.droplocationrecycleview);
        droplocationMaterial.setSpeechMode(false);
        droplocationMaterial.setOnSearchActionListener(this);
        droplocationMaterial.addTextChangeListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DropPlaceLocation.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        droplocationrecycleview.setLayoutManager(linearLayoutManager);

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String text = charSequence.toString() ;
        ConnectionServer connectionServer = new ConnectionServer();
        connectionServer.set_url(Constants.SEARCH_PLACE);
        connectionServer.requestedMethod("POST");
        connectionServer.buildParameter("value" , text);
        connectionServer.execute(new ConnectionServer.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                Log.e("output",output);
                JsonHelper jsonHelper = new JsonHelper(output);
                if(jsonHelper.isValidJson()){
                    dropLocationListList.clear();
                    String response = jsonHelper.GetResult("status");
                    if(response.equals("OK")){
                        JSONArray jsonArray = jsonHelper.setChildjsonArray(jsonHelper.getCurrentJsonObj(), "predictions");

//                        JSONObject aa = jsonHelper.setChildjsonObj(jsonHelper.getCurrentJsonObj(), "structured_formatting");
//                        for(int j = 0 ; j< aa.length() ; j++){
//
//                            Toast.makeText(DropPlaceLocation.this, ""+j, Toast.LENGTH_SHORT).show();
//                        }

                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonHelper.setChildjsonObj(jsonArray, i);

                            dropLocationListList.add(new DropLocationList(jsonHelper.GetResult("description"),jsonHelper.GetResult("id"),jsonHelper.GetResult("matched_substrings"),jsonHelper.GetResult("place_id"),jsonHelper.GetResult("reference"),jsonHelper.GetResult("structured_formatting"),jsonHelper.GetResult("terms")));

                        }
                        DropLocationAdapter dropLocationAdapter = new DropLocationAdapter(DropPlaceLocation.this , dropLocationListList );
                        droplocationrecycleview.setAdapter(dropLocationAdapter);
                    }
                }
            }
        });
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {
    }

    @Override
    public void onSearchConfirmed(CharSequence text) {

    }

    @Override
    public void onButtonClicked(int buttonCode) {
    }
}
