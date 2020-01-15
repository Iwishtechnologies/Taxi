package tech.iwish.taxi.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Map;

import tech.iwish.taxi.R;
import tech.iwish.taxi.activity.DropPlaceLocation;
import tech.iwish.taxi.activity.MainActivity;
import tech.iwish.taxi.other.DropLocationList;

public class DropLocationAdapter extends RecyclerView.Adapter<DropLocationAdapter.Viewholder> {
    List<DropLocationList> dropLocationLists ;
    Context context ;



    public DropLocationAdapter(DropPlaceLocation dropPlaceLocation, List<DropLocationList> dropLocationListList ) {
        this.dropLocationLists = dropLocationListList;
        this.context = dropPlaceLocation;

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

                Intent intent = new Intent(context, MainActivity.class);
//                intent.putExtra("dropLocation" , "Hazira Chowk, Tansen Nagar, Gwalior, Madhya Pradesh, India");
                intent.putExtra("dropLocation" , dropLocationLists.get(position).getDescription());
//                intent.putExtra("clickDrop" , "dropLocations");
                context.startActivity(intent);
                Toast.makeText(context, ""+dropLocationLists.get(position).getDescription(), Toast.LENGTH_SHORT).show();
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
}
