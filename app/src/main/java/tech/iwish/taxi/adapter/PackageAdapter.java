package tech.iwish.taxi.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

import tech.iwish.taxi.R;
import tech.iwish.taxi.config.SharedpreferencesUser;
import tech.iwish.taxi.fragment.Package_vehicle;
import tech.iwish.taxi.fragment.RideConfiremDriverDetailsFragment;
import tech.iwish.taxi.other.PackageVehicle;

import static tech.iwish.taxi.adapter.VehicleAdapter.JSONDATA;
import static tech.iwish.taxi.config.SharedpreferencesUser.USER_CONTACT;

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.Viewholder> {

    Context context;
    private List<PackageVehicle>packageVehicles;
    public Map<String, Double> latitude_logitude;
    Map<String, String> AddressMap;
    Map data ;

    public PackageAdapter(FragmentActivity activity, List<PackageVehicle> packageVehicles, Map<String, Double> latitude_logitude, Map<String, String> addressMap) {
        this.context = activity ;
        this.packageVehicles = packageVehicles ;
        this.latitude_logitude = latitude_logitude ;
        this.AddressMap = addressMap ;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_package_show,parent , false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        holder.packageshow.setText(packageVehicles.get(position).getPackage_type());
        holder.clickPackage.setOnClickListener(view -> {
            SharedpreferencesUser sharedpreferencesUser = new SharedpreferencesUser(context);
            data = sharedpreferencesUser.getShare();
            Object mobile = data.get(USER_CONTACT);

            JSONDATA = "{ \"type\" : \"Rental\" , \"request\" :\"rental\" ,\"pickUpLat\" : \"" + latitude_logitude.get("PickLatitude") + "\"  ,\"pickUpLong\" : \"" + latitude_logitude.get("PickLogitude") + "\"  , \"userType\" : \"client\"  , \"userID\" : \"" + mobile.toString() + "\" , \"PickupCityName\" : \"" + AddressMap.get("PickupCityName") + " \"}";
            sharedpreferencesUser.getDatasWebsocket(JSONDATA);
/*
            Bundle bundle = new Bundle();
            bundle.putString("time",packageVehicles.get(position).getPackage_type());
            Package_vehicle package_vehicle = new Package_vehicle(latitude_logitude , AddressMap);
            FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
            package_vehicle.setArguments(bundle);
            fm.beginTransaction().replace(R.id.rental_frame, package_vehicle).commit();
*/


        });
    }

    @Override
    public int getItemCount() {
        return packageVehicles.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView packageshow ;
        private LinearLayout clickPackage ;
        public Viewholder(@NonNull View itemView) {
            super(itemView);

            packageshow = (TextView)itemView.findViewById(R.id.packageshow);
            clickPackage = (LinearLayout)itemView.findViewById(R.id.clickPackage);
        }
    }
}
