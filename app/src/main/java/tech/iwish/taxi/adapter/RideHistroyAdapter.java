package tech.iwish.taxi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tech.iwish.taxi.R;
import tech.iwish.taxi.other.RideHistroy;

public class RideHistroyAdapter extends RecyclerView.Adapter<RideHistroyAdapter.Viewholder> {

    private List<RideHistroy> rideHistroys;
    private Context context;

    public RideHistroyAdapter(FragmentActivity activity, List<RideHistroy> rideHistroys) {
        this.rideHistroys = rideHistroys;
        this.context = activity;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_ride_histroy,parent , false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;

    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        holder.pickupaddress.setText(rideHistroys.get(position).getPickup_city_name());
        holder.dropAddresss.setText(rideHistroys.get(position).getDrop_city_name());
    }

    @Override
    public int getItemCount() {
        return rideHistroys.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        private TextView pickupaddress ,dropAddresss ;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            pickupaddress = (TextView)itemView.findViewById(R.id.pickupaddress);
            dropAddresss = (TextView)itemView.findViewById(R.id.dropAddresss);

        }
    }
}


















