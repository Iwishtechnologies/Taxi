package tech.iwish.taxi.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import tech.iwish.taxi.R;
import tech.iwish.taxi.activity.LoginActivity;
import tech.iwish.taxi.activity.MainActivity;
import tech.iwish.taxi.config.Constants;
import tech.iwish.taxi.config.SharedpreferencesUser;
import tech.iwish.taxi.connection.ConnectionServer;
import tech.iwish.taxi.connection.JsonHelper;
import tech.iwish.taxi.other.User_DetailsList;


public class Login_Fragment_2 extends Fragment {

    private Button otp_button ;
    private EditText otp_check ;
    private List<User_DetailsList> user_detailsLists = new ArrayList<>() ;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login__fragment_2 , null);

        otp_button = (Button)view.findViewById(R.id.otp_button);
        otp_check = (EditText)view.findViewById(R.id.otp_check);



        otp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String mobile = getArguments().getString("number");
//                Toast.makeText(getActivity(), ""+mobile, Toast.LENGTH_SHORT).show();

                ConnectionServer connectionServer = new ConnectionServer();
                connectionServer.set_url(Constants.USER_OTP);
                connectionServer.requestedMethod("POST");
                connectionServer.buildParameter("mobile_number" , mobile);
                connectionServer.buildParameter("otp" , otp_check.getText().toString().trim());
                connectionServer.execute(new ConnectionServer.AsyncResponse() {
                    @Override
                    public void processFinish(String output) {
                        Log.e("output" , output);
                        JsonHelper jsonHelper = new JsonHelper(output);
                        if(jsonHelper.isValidJson()){
                            String response = jsonHelper.GetResult("response");
                            if(response.equals("TRUE")){

                                JSONArray jsonArray = jsonHelper.setChildjsonArray(jsonHelper.getCurrentJsonObj(), "data");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    jsonHelper.setChildjsonObj(jsonArray, i);

                                    user_detailsLists.add(new User_DetailsList(jsonHelper.GetResult("name"),jsonHelper.GetResult("email"),jsonHelper.GetResult("constact")));

                                }
                                String name = jsonHelper.GetResult("name");
                                String email = jsonHelper.GetResult("email");
                                String contact = jsonHelper.GetResult("contact");

                                SharedpreferencesUser sharedpreferencesUser = new SharedpreferencesUser(getActivity());
                                sharedpreferencesUser.user_detail(name , email , contact );

                                Intent intent = new Intent(getActivity() , MainActivity.class);
                                startActivity(intent);

                            }
                            else{
                                Toast.makeText(getActivity(), "Otp not Match", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });



        return view ;
    }
}
