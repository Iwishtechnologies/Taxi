package tech.iwish.taxi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tech.iwish.taxi.R;
import tech.iwish.taxi.other.RateCardLists;


public class RateCardAdapter extends RecyclerView.Adapter<RateCardAdapter.Viewholder> {

    private Context context;
    private List<RateCardLists> rateCardLists ;

    public RateCardAdapter(FragmentActivity activity, List<RateCardLists> rateCardLists) {
        this.context = activity ;
        this.rateCardLists = rateCardLists;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_ratecard,parent,false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return rateCardLists.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        public Viewholder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
