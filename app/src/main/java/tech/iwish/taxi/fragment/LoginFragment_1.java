package tech.iwish.taxi.fragment;

import android.content.Context;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import tech.iwish.taxi.R;
import tech.iwish.taxi.activity.LoginActivity;
import tech.iwish.taxi.config.Constants;
import tech.iwish.taxi.config.SharedpreferencesUser;
import tech.iwish.taxi.connection.ConnectionServer;
import tech.iwish.taxi.connection.JsonHelper;
import tech.iwish.taxi.extended.EditTextFont;

public class LoginFragment_1 extends Fragment {

    private EditText mobile_number;
    private Button login_button;
    private ProgressBar login_progress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_fragment_1, null);


        mobile_number = (EditText) view.findViewById(R.id.mobile_number);
        login_button = (Button) view.findViewById(R.id.login_button);
        login_progress = (ProgressBar)view.findViewById(R.id.login_progress);



        login_button.setOnClickListener(view1 -> {

            ConnectionServer connectionServer = new ConnectionServer();
            connectionServer.set_url(Constants.USER_LOGIN);
            connectionServer.requestedMethod("POST");
            connectionServer.buildParameter("mobile_number", mobile_number.getText().toString().trim());
            connectionServer.execute(output -> {
                Log.e("output", output);
                JsonHelper jsonHelper = new JsonHelper(output);
                if (jsonHelper.isValidJson()) {
                    String response = jsonHelper.GetResult("response");
                    if (response.equals("TRUE")) {

                        Login_Fragment_2 login_fragment_2 = new Login_Fragment_2();
                        Bundle args = new Bundle();
                        args.putString("number", mobile_number.getText().toString().trim());
                                login_fragment_2.setArguments(args);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.setCustomAnimations(R.anim.enter_fragment1, R.anim.exit_fragment1, R.anim.enter_fragment1, R.anim.exit_fragment1);
                        transaction.addToBackStack(null);
                        transaction.replace(R.id.Login_fragmentLayout, login_fragment_2, "BLANK_FRAGMENT").commit();

                    } else {
                        Toast.makeText(getActivity(), "Mobile Number Not Match", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });


        return view;
    }
}
