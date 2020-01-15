package tech.iwish.taxi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tech.iwish.taxi.R;
import tech.iwish.taxi.activity.PickupSearchActivity;

public class PickPlaceAdapter extends RecyclerView.Adapter<PickPlaceAdapter.Viewholder> {

    ArrayList<String> list ;
    Context context ;

    public PickPlaceAdapter(PickupSearchActivity pickupSearchActivity, ArrayList<String> list) {
        this.context = pickupSearchActivity ;
        this.list = list ;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_picklist,parent,false);
        Viewholder holder = new Viewholder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        holder.place.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView  place ;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            place = (TextView)itemView.findViewById(R.id.place);
        }
    }
}
