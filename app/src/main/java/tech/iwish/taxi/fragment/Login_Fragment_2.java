package tech.iwish.taxi.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.goodiebag.pinview.Pinview;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
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
    private Pinview pinview ;
    private TextView mobNumber;
    private KProgressHUD kProgressHUD;
    String mobile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login__fragment_2 , null);

        otp_button = (Button)view.findViewById(R.id.otp_button);
        pinview = (Pinview)view.findViewById(R.id.pinview);
        mobNumber = (TextView)view.findViewById(R.id.mobNumber);

        kProgressHUD = new KProgressHUD(getActivity());



        pinview.setPinHeight(100);
        pinview.setPinWidth(100);
        pinview.setPinBackgroundRes(R.drawable.pin_design);

         mobile = getArguments().getString("number");
        mobNumber.setText(mobile);

//        pinview.setPinViewEventListener(new Pinview.PinViewEventListener() {
//            @Override
//            public void onDataEntered(Pinview pinview, boolean fromUser) {
//                Toast.makeText(getActivity(), ""+pinview, Toast.LENGTH_SHORT).show();
//                Toast.makeText(getActivity(), ""+fromUser, Toast.LENGTH_SHORT).show();
//            }
//        });



        otp_button.setOnClickListener(view1 -> {


            otpMatch();


//            setProgressDialog("Otp Verify");
//            ConnectionServer connectionServer = new ConnectionServer();
//            connectionServer.set_url(Constants.USER_OTP);
//            connectionServer.requestedMethod("POST");
//            connectionServer.buildParameter("mobile_number" , mobile);
//            connectionServer.buildParameter("otp" , pinview.getValue());
//            connectionServer.execute(output -> {
//                Log.e("output" , output);
//                JsonHelper jsonHelper = new JsonHelper(output);
//                if(jsonHelper.isValidJson()){
//                    String response = jsonHelper.GetResult("response");
//                    if(response.equals("TRUE")){
//
//                        JSONArray jsonArray = jsonHelper.setChildjsonArray(jsonHelper.getCurrentJsonObj(), "data");
//
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            jsonHelper.setChildjsonObj(jsonArray, i);
//
//                            user_detailsLists.add(new User_DetailsList(jsonHelper.GetResult("name"),jsonHelper.GetResult("email"),jsonHelper.GetResult("constact"),jsonHelper.GetResult("refer_code")));
//
//                        }
//                        String name = jsonHelper.GetResult("name");
//                        String email = jsonHelper.GetResult("email");
//                        String contact = jsonHelper.GetResult("contact");
//                        String refer_code = jsonHelper.GetResult("refer_code");
//
//                        SharedpreferencesUser sharedpreferencesUser = new SharedpreferencesUser(getActivity());
//                        sharedpreferencesUser.user_detail(name , email , contact ,refer_code);
//
//                        remove_progress_Dialog();
//
//                        Intent intent = new Intent(getActivity() , MainActivity.class);
//                        startActivity(intent);
//
//                    }
//                    else{
//                        Toast.makeText(getActivity(), "Otp not Match", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
        });



        return view ;
    }

    private void otpMatch() {



        setProgressDialog("Verify Otp");




        OkHttpClient okHttpClient = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile_number", mobile);
            jsonObject.put("otp", pinview.getValue());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request1 = new Request.Builder().url(Constants.USER_OTP).post(body).build();



        okHttpClient.newCall(request1).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Connection Time out", Toast.LENGTH_SHORT).show();
                        remove_progress_Dialog();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    Log.e("result", result);
                    JsonHelper jsonHelper = new JsonHelper(result);
                    if (jsonHelper.isValidJson()) {
                        String responses = jsonHelper.GetResult("response");
                        if (responses.equals("TRUE")) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    JSONArray jsonArray = jsonHelper.setChildjsonArray(jsonHelper.getCurrentJsonObj(), "data");

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        jsonHelper.setChildjsonObj(jsonArray, i);

                                        user_detailsLists.add(new User_DetailsList(jsonHelper.GetResult("name"),jsonHelper.GetResult("email"),jsonHelper.GetResult("constact"),jsonHelper.GetResult("refer_code")));

                                    }
                                    String name = jsonHelper.GetResult("name");
                                    String email = jsonHelper.GetResult("email");
                                    String contact = jsonHelper.GetResult("contact");
                                    String refer_code = jsonHelper.GetResult("refer_code");

                                    SharedpreferencesUser sharedpreferencesUser = new SharedpreferencesUser(getActivity());
                                    sharedpreferencesUser.user_detail(name , email , contact ,refer_code);

                                    Intent intent = new Intent(getActivity() , MainActivity.class);
                                    startActivity(intent);

                                    remove_progress_Dialog();

                                }
                            });
                        }else {

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), "Otp not Match", Toast.LENGTH_SHORT).show();
                                    remove_progress_Dialog();
                                }
                            });
                        }
                    }
                }
            }
        });


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
