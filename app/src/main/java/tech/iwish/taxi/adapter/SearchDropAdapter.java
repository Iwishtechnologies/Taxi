package tech.iwish.taxi.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import tech.iwish.taxi.Interface.DropLocationInterface;
import tech.iwish.taxi.R;
import tech.iwish.taxi.fragment.ConfirmRideFragment;
import tech.iwish.taxi.other.DropLocationList;

public class SearchDropAdapter extends RecyclerView.Adapter<SearchDropAdapter.Viewholder> implements Filterable {

    private List<DropLocationList>dropLocationLists ;
    private Context context;
    private DropInterFace dropInterFace;
    private KProgressHUD kProgressHUD;
    DropLocationInterface dropLocationInterface;

    public SearchDropAdapter(FragmentActivity activity, List<DropLocationList> dropLocationListMap ,DropLocationInterface dropLocationInterface) {
        this.context = activity;
        this.dropLocationLists = dropLocationListMap;
        this.dropLocationInterface = dropLocationInterface;
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



        String setVal = "";

//        try {
//            JSONArray jsonArray = new JSONArray(dropLocationLists.get(position).getTerms());
//            for (int i = 0; i < jsonArray.length() ; i++) {
//                JSONObject jsonObject =jsonArray.getJSONObject(i);
//
//                setVal += jsonObject.getString("value") + " ";
//
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        holder.status_check.setText(setVal);

        holder.place.setText(dropLocationLists.get(position).getDescription());
         kProgressHUD = new KProgressHUD(context);

        holder.placeLayout.setOnClickListener(view -> {

            setProgressDialog("");
//
////            keyboad open code
//            InputMethodManager input = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
//            input.hideSoftInputFromWindow(view.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
//
////            dropInterFace.droplocationInterFace(dropLocationLists.get(position).getDescription());
//            dropLocationInterface.drop_loaction_Interface(dropLocationLists.get(position).getDescription());
//            remove_progress_Dialog();
/*
            ConfirmRideFragment confirmRideFragment = new ConfirmRideFragment(null , null , null);
            FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.confirmRideLoad, confirmRideFragment).commit();
*/



        });
    }

    @Override
    public int getItemCount() {
        return dropLocationLists.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                Toast.makeText(context, ""+charSequence.toString(), Toast.LENGTH_SHORT).show();
                return null;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                Toast.makeText(context, ""+charSequence.toString(), Toast.LENGTH_SHORT).show();
            }
        };
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        TextView place ,status_check;
        LinearLayout placeLayout, click_linerLayout_vehicle;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            place = (TextView) itemView.findViewById(R.id.place);
            status_check = (TextView) itemView.findViewById(R.id.status_check);
            placeLayout = (LinearLayout) itemView.findViewById(R.id.placeLayout);

        }
    }

    public void setdropLOcationValue(DropInterFace dropInterFace){
        this.dropInterFace = dropInterFace;
    }
    public interface DropInterFace{
        void droplocationInterFace(String data);

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
