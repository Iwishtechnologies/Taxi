package tech.iwish.taxi.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

import tech.iwish.taxi.R;

import tech.iwish.taxi.config.SharedpreferencesUser;
import tech.iwish.taxi.fragment.ConfirmRideFragment;
import tech.iwish.taxi.other.DropLocationList;

public class DropLocationAdapter extends RecyclerView.Adapter<DropLocationAdapter.Viewholder> {
    List<DropLocationList> dropLocationLists ;
    Context context ;
    public  DropListener droplocationnameinterface;

    public DropLocationAdapter(FragmentActivity activity, List<DropLocationList> dropLocationListList ) {
        this.dropLocationLists = dropLocationListList;
        this.context = activity;


    }



    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_picklist,parent,false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, final int position) {


        holder.place.setText(dropLocationLists.get(position).getDescription());

        holder.placeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                droplocationnameinterface.setdropPlace(dropLocationLists.get(position).getDescription());
//                ConfirmRideFragment confirmRideFragment = new ConfirmRideFragment(AllLatLng, latitude_logitude, AddressMap);
//                 getSupportFragmentManager().beginTransaction().replace(R.id.confirRide, confirmRideFragment).commit();


            }
        });

    }



    @Override
    public int getItemCount() {
        return dropLocationLists.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        TextView place;
        LinearLayout placeLayout , click_linerLayout_vehicle;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            place = (TextView)itemView.findViewById(R.id.place);
            placeLayout = (LinearLayout)itemView.findViewById(R.id.placeLayout);

        }
    }

    public void dropLoactionval(DropListener dropListener){
        this.droplocationnameinterface = dropListener ;
    }

    public interface DropListener{
       public void setdropPlace(String value);
    }
}
