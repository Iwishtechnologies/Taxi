package tech.iwish.taxi.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.razorpay.Checkout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import tech.iwish.taxi.R;
import tech.iwish.taxi.activity.MoneyAddWalletActivity;
import tech.iwish.taxi.config.Constants;
import tech.iwish.taxi.config.SharedpreferencesUser;
import tech.iwish.taxi.connection.ConnectionServer;
import tech.iwish.taxi.connection.JsonHelper;
import tech.iwish.taxi.other.User_DetailsList;

import static tech.iwish.taxi.config.SharedpreferencesUser.USER_CONTACT;

public class WalletFragment extends Fragment {

    private LinearLayout wallet_click;
    private Map data;
    private TextView wallet_money;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet,null);


        wallet_click = (LinearLayout)view.findViewById(R.id.wallet_click);
        wallet_money = (TextView)view.findViewById(R.id.wallet_money);

        SharedpreferencesUser sharedpreferencesUser = new SharedpreferencesUser(getActivity());
        data = sharedpreferencesUser.getShare();
        String mob = data.get(USER_CONTACT).toString();
        ConnectionServer connectionServer = new ConnectionServer();
        connectionServer.set_url(Constants.WALLET);
        connectionServer.requestedMethod("POST");
        connectionServer.buildParameter("mobile" , mob);
        connectionServer.execute(output -> {
            Log.e("output" , output);
            JsonHelper jsonHelper = new JsonHelper(output);
            if(jsonHelper.isValidJson()){
                String response = jsonHelper.GetResult("response");
                if(response.equals("TRUE")){
                    JSONArray jsonArray = jsonHelper.setChildjsonArray(jsonHelper.getCurrentJsonObj(), "data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonHelper.setChildjsonObj(jsonArray, i);
                        wallet_money.setText(jsonHelper.GetResult("wallet"));
                        sharedpreferencesUser.walletAdd(jsonHelper.GetResult("wallet"));
                    }
                }
            }
        });
        wallet_click.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity() , MoneyAddWalletActivity.class);
            intent.putExtra("money",wallet_money.getText().toString().trim());
            startActivity(intent);
        });


        return view ;
    }
}
