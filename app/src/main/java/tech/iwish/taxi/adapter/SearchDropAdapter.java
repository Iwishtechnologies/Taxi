package tech.iwish.taxi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tech.iwish.taxi.R;
import tech.iwish.taxi.fragment.ConfirmRideFragment;
import tech.iwish.taxi.other.DropLocationList;

public class SearchDropAdapter extends RecyclerView.Adapter<SearchDropAdapter.Viewholder> {

    private List<DropLocationList>dropLocationLists ;
    private Context context;
    private DropInterFace dropInterFace;


    public SearchDropAdapter(FragmentActivity activity, List<DropLocationList> dropLocationListMap) {
        this.context = activity;
        this.dropLocationLists = dropLocationListMap;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_picklist , parent ,false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        holder.place.setText(dropLocationLists.get(position).getDescription());
        holder.placeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dropInterFace.droplocationInterFace(dropLocationLists.get(position).getDescription());

/*
                ConfirmRideFragment confirmRideFragment = new ConfirmRideFragment(null , null , null);
                FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.confirmRideLoad, confirmRideFragment).commit();
*/



            }
        });
    }

    @Override
    public int getItemCount() {
        return dropLocationLists.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        TextView place;
        LinearLayout placeLayout, click_linerLayout_vehicle;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            place = (TextView) itemView.findViewById(R.id.place);
            placeLayout = (LinearLayout) itemView.findViewById(R.id.placeLayout);

        }
    }

    public void setdropLOcationValue(DropInterFace dropInterFace){
        this.dropInterFace = dropInterFace;
    }
    public interface DropInterFace{
        void droplocationInterFace(String data);
    }
}
