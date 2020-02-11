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
import tech.iwish.taxi.other.PackageVehicle;

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.Viewholder> {

    Context context;
    private List<PackageVehicle>packageVehicles;

    public PackageAdapter(FragmentActivity activity, List<PackageVehicle> packageVehicles) {
        this.context = activity ;
        this.packageVehicles = packageVehicles ;
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
    }

    @Override
    public int getItemCount() {
        return packageVehicles.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView packageshow ;
        public Viewholder(@NonNull View itemView) {
            super(itemView);

            packageshow = (TextView)itemView.findViewById(R.id.packageshow);

        }
    }
}
