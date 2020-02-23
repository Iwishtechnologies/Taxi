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

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.mancj.materialsearchbar.MaterialSearchBar;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tech.iwish.taxi.R;
import tech.iwish.taxi.adapter.SearchDropAdapter;
import tech.iwish.taxi.config.Constants;
import tech.iwish.taxi.connection.ConnectionServer;
import tech.iwish.taxi.connection.JsonHelper;
import tech.iwish.taxi.other.DropLocationList;
import tech.iwish.taxi.other.PickupLocationList;


public class Search_dropFragment extends DialogFragment  {

    private RecyclerView search_drop_recycle ;
    private MaterialSearchBar search_drops;
    private List<DropLocationList> dropLocationListMap  = new ArrayList<>();
    private DropValueInterFace valuedrop;
    private SearchView searchview;
    private KProgressHUD kProgressHUD;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_drop , container , false);

        search_drop_recycle = (RecyclerView)view.findViewById(R.id.search_drop_recycle);
        searchview = (SearchView)view.findViewById(R.id.searchview);

        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                ConnectionServer connectionServer = new ConnectionServer();
                connectionServer.set_url(Constants.SEARCH_PLACE_DROP);
                connectionServer.requestedMethod("POST");
                connectionServer.buildParameter("value" , s);
                connectionServer.readTimeout(400);
                connectionServer.execute(output -> {
                    Log.e("output" , output);
                    JsonHelper jsonHelper = new JsonHelper(output);
                    if(jsonHelper.isValidJson()){
                        String response = jsonHelper.GetResult("status");
                        if(response.equals("OK")){
                            dropLocationListMap.clear();
                            JSONArray jsonArray = jsonHelper.setChildjsonArray(jsonHelper.getCurrentJsonObj(), "predictions");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonHelper.setChildjsonObj(jsonArray, i);

                                dropLocationListMap.add(new DropLocationList(jsonHelper.GetResult("description"), jsonHelper.GetResult("id"), jsonHelper.GetResult("matched_substrings"), jsonHelper.GetResult("place_id"), jsonHelper.GetResult("reference"), jsonHelper.GetResult("structured_formatting"), jsonHelper.GetResult("terms")));

                            }

                            SearchDropAdapter searchDropAdapter = new SearchDropAdapter(getActivity() , dropLocationListMap);
                            search_drop_recycle.setAdapter(searchDropAdapter);
                            searchDropAdapter.setdropLOcationValue(data -> valuedrop.DroplocationValue(data));

                        }
                    }
                });
                return false;
            }
        });


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        search_drop_recycle.setLayoutManager(linearLayoutManager);

        return view;
    }



    public void setValueDrop(DropValueInterFace drop){
        this.valuedrop = drop ;
    }
    
    public interface DropValueInterFace{
        void DroplocationValue(String data);
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
