package tech.iwish.taxi.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;

import tech.iwish.taxi.R;
import tech.iwish.taxi.adapter.PickPlaceAdapter;
import tech.iwish.taxi.config.Constants;
import tech.iwish.taxi.connection.ConnectionServer;
import tech.iwish.taxi.connection.JsonHelper;

public class PickupSearchActivity extends AppCompatActivity implements  MaterialSearchBar.OnSearchActionListener  ,TextWatcher{

    MaterialSearchBar materialSearchBar ;
    RecyclerView pickuprecycle;
    ArrayList<String> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_search);

        getSupportActionBar().setTitle("Pickup Location");

        materialSearchBar = (MaterialSearchBar)findViewById(R.id.searchBar);
        pickuprecycle = (RecyclerView) findViewById(R.id.pickuprecycle);

        list.add("ram" );
        list.add("ram" );
        list.add("ram" );
        list.add("ram" );
        list.add("ram" );
        list.add("ram" );
        list.add("ram" );
        list.add("ram" );
        list.add("ram" );
        list.add("ram" );

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        pickuprecycle.setLayoutManager(linearLayoutManager);

        PickPlaceAdapter pickPlaceAdapter = new PickPlaceAdapter(PickupSearchActivity.this , list);
        pickuprecycle.setAdapter(pickPlaceAdapter);
        pickPlaceAdapter.notifyDataSetChanged();

        materialSearchBar.setOnSearchActionListener(this);


    }



    @Override
    public void onSearchStateChanged(boolean enabled) {

    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
//        startSearch(text.toString(), true, null, false);



    }

    @Override
    public void onButtonClicked(int buttonCode) {
        Toast.makeText(this, ""+buttonCode, Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

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
        connectionServer.buildParameter("value" , text.toString());
        connectionServer.execute(new ConnectionServer.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                Log.e("output",output);
                JsonHelper jsonHelper = new JsonHelper(output);
                if(jsonHelper.isValidJson()){
                    String response = jsonHelper.GetResult("response");
                    if(response.equals("TRUE")){

                    }
                }

            }
        });
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
