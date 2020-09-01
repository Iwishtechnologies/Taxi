package tech.iwish.taxi.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import tech.iwish.taxi.R;
import tech.iwish.taxi.adapter.OutStationVehicleAdapter;
import tech.iwish.taxi.config.Constants;
import tech.iwish.taxi.connection.ConnectionServer;
import tech.iwish.taxi.connection.JsonHelper;
import tech.iwish.taxi.other.OutStationVehicleList;
import tech.iwish.taxi.other.User_DetailsList;

import static com.mapbox.services.android.navigation.ui.v5.feedback.FeedbackBottomSheet.TAG;


public class OutstationVehicleshowFragment extends Fragment {

    private TextView leaveDateTime, returnTimeDate, pick, drop;

    public Map<String, LatLng> AllLatLng;
    public Map<String, Double> latitude_logitude;
    public Map<String, String> AddressMap;
    RecyclerView vehicleOutstationshe;
    private DatePickerDialog.OnDateSetListener mleave, dropcal;
    private KProgressHUD kProgressHUD;
    String dropCalt;
    List<OutStationVehicleList> outStationVehicleLists = new ArrayList<>();


    public OutstationVehicleshowFragment(Map<String, LatLng> allLatLng, Map<String, Double> latitude_logitude, Map<String, String> addressMap) {

        this.AllLatLng = allLatLng;
        this.latitude_logitude = latitude_logitude;
        this.AddressMap = addressMap;

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_outstation_vehicleshow, null);

        getActivity().setTitle("Book Your Outstation ride");

        leaveDateTime = (TextView) view.findViewById(R.id.leaveDateTime);
        returnTimeDate = (TextView) view.findViewById(R.id.returnTimeDate);
        pick = (TextView) view.findViewById(R.id.pick);
        drop = (TextView) view.findViewById(R.id.drop);
        vehicleOutstationshe = (RecyclerView) view.findViewById(R.id.vehicleOutstationshe);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        vehicleOutstationshe.setLayoutManager(linearLayoutManager);

        kProgressHUD = new KProgressHUD(getActivity());

        Date currentTime = Calendar.getInstance().getTime();
        Log.e("onCreateView: ", currentTime.toString());

        Calendar c = Calendar.getInstance();
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM" + " ," + "HH:mm a");
        String formattedDate = df.format(c.getTime());

        leaveDateTime.setText(formattedDate);

        pick.setText(AddressMap.get("Pickupfulladress").toString());
        drop.setText(AddressMap.get("Address_DropLocation").toString());




        returnTimeDate.setOnClickListener(view1 -> {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Dialog_MinWidth, mleave, year, month, day);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

        });

        mleave = (datePicker, ye, i1, dy) -> {
            Calendar calss = Calendar.getInstance();
            calss.set(Calendar.YEAR, ye);
            calss.set(Calendar.MONTH, i1);
            calss.set(Calendar.DAY_OF_MONTH, dy);
            dropCalt = DateFormat.getDateInstance(DateFormat.FULL).format(calss.getTime());
            returnTimeDate.setText(dropCalt);
        };


        leaveDateTime.setOnClickListener(View -> {

            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Dialog_MinWidth, dropcal, year, month, day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });

        dropcal = (datePicker, ye, i1, dy) -> {
            Calendar calss = Calendar.getInstance();
            calss.set(Calendar.YEAR, ye);
            calss.set(Calendar.MONTH, i1);
            calss.set(Calendar.DAY_OF_MONTH, dy);
            String da = DateFormat.getDateInstance(DateFormat.FULL).format(calss.getTime());
            leaveDateTime.setText(da);
        };


        setProgressDialog("Vehicle Search");

        Double PickLatitude = latitude_logitude.get("PickLatitude");
        Double PickLogitude = latitude_logitude.get("PickLogitude");
        Double destinationsLatitude = latitude_logitude.get("dropLatitude");
        Double destinationsLogitude = latitude_logitude.get("dropLongitude");

        ConnectionServer connectionServer = new ConnectionServer();
        connectionServer.set_url(Constants.OUTSTATIONVEHICLE);
//        connectionServer.buildParameter("PickLatitude" ,PickLatitude.toString());
//        connectionServer.buildParameter("PickLogitude" ,PickLogitude.toString());
//        connectionServer.buildParameter("destinationsLatitude" ,destinationsLatitude.toString());
//        connectionServer.buildParameter("destinationsLogitude" ,destinationsLogitude.toString());
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
//                        user_detailsLists.add(new User_DetailsList(jsonHelper.GetResult("name"),jsonHelper.GetResult("email"),jsonHelper.GetResult("constact")));
                        outStationVehicleLists.add(new OutStationVehicleList(jsonHelper.GetResult("catagory_id"), jsonHelper.GetResult("catagory_name"), jsonHelper.GetResult("MinRate"), jsonHelper.GetResult("Rate_Km"), jsonHelper.GetResult("waitingRate_m"), jsonHelper.GetResult("rtc_m"), jsonHelper.GetResult("img"), jsonHelper.GetResult("vahicle_cat")));
                    }
                    OutStationVehicleAdapter outStationVehicleAdapter = new OutStationVehicleAdapter(getActivity(), outStationVehicleLists, AllLatLng, latitude_logitude, AddressMap);
                    vehicleOutstationshe.setAdapter(outStationVehicleAdapter);

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



















