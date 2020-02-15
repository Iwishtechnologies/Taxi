package tech.iwish.taxi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tech.iwish.taxi.R;
import tech.iwish.taxi.other.PickupLocationList;

public class RentalAdapter extends RecyclerView.Adapter<RentalAdapter.Viewholder> {

    private List<PickupLocationList> pickupLocationLists;
    private Context context;
    public RentalPlace rentalPlaceDatas;

    public RentalAdapter(FragmentActivity activity, List<PickupLocationList> pickupLocationLists) {
        this.context = activity ;
        this.pickupLocationLists = pickupLocationLists ;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.row_picklist, parent, false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        holder.place.setText(pickupLocationLists.get(position).getDescription());

        holder.placeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager input = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                input.hideSoftInputFromWindow(view.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                rentalPlaceDatas.rentalVehiclePass(pickupLocationLists.get(position).getDescription());
            }
        });
    }

    @Override
    public int getItemCount() {
        return pickupLocationLists.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        private TextView place;
        private LinearLayout placeLayout, click_linerLayout_vehicle;

        public Viewholder(@NonNull View itemView)
        {
            super(itemView);
            place = (TextView) itemView.findViewById(R.id.place);
            placeLayout = (LinearLayout) itemView.findViewById(R.id.placeLayout);
        }
    }

    public void setrentalPlace(RentalPlace rentalPlace){
        this.rentalPlaceDatas = rentalPlace;
    }

    public interface RentalPlace{
        void rentalVehiclePass(String data);
    }


}
