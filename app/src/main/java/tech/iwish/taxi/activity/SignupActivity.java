package tech.iwish.taxi.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import tech.iwish.taxi.R;
import tech.iwish.taxi.config.Constants;
import tech.iwish.taxi.connection.ConnectionServer;
import tech.iwish.taxi.connection.JsonHelper;

public class SignupActivity extends AppCompatActivity {

    private EditText signup_name, signup_email, signup_contact, signup_password;
    private Button signup_button;
    private LinearLayout login_activity_go ;
    boolean doubleBackToExitPressedOnce = false;
    private Spinner spinner;
    private String spinner_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();

        signup_name = (EditText) findViewById(R.id.signup_name);
        signup_email = (EditText) findViewById(R.id.signup_email);
        signup_contact = (EditText) findViewById(R.id.signup_contact);
        signup_password = (EditText) findViewById(R.id.signup_password);
        signup_button = (Button) findViewById(R.id.signup_button);
        login_activity_go = (LinearLayout) findViewById(R.id.login_activity_go);
        spinner = (Spinner)findViewById(R.id.spinner);


        final List<String> categories = new ArrayList<String>();

        categories.add("Select");
        categories.add("Delhi");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        spinner.setAdapter(dataAdapter);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView)parent.getChildAt(0)).setTextColor(Color.rgb(249, 249, 249));
//                ((TextView)view).setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(SignupActivity.this, R.drawable.user_icon_login), null, null, null);
//                ((TextView) view).setCompoundDrawablePadding(20);
                spinner_value = categories.get(position);
//                Toast.makeText(SignupActivity.this, "Selected : "+ categories.get(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        login_activity_go.setOnClickListener(view -> {
            Intent intent = new Intent(SignupActivity.this , LoginActivity.class);
            startActivity(intent);
        });

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Validation()== true){
                    datainsert();
                }
            }

            private Boolean Validation() {
                if(signup_name.getText().toString().trim().isEmpty()){
                    signup_name.setError("Please Fill Your Name");
                    return false ;
                }
                if(signup_email.getText().toString().trim().isEmpty()){
                    signup_email.setError("empty");
                    return false ;
                }
                if(signup_contact.getText().toString().trim().isEmpty()){
                    signup_email.setError("Please Fill Your Contact");
                    return false ;
                }
//                if(Integer.getInteger(signup_contact.getText().toString().trim()) <= 9 ){
//                    signup_email.setError("Please Enter Valid Number ");
//                    return false ;
//                }
                if(spinner_value.equals("Select")){
                    Toast.makeText(SignupActivity.this, "Select City", Toast.LENGTH_SHORT).show();
                    return false;
                }

                return true;
            }

            private void datainsert() {

                ConnectionServer connectionServer = new ConnectionServer();
                connectionServer.set_url(Constants.USER_SIGUP);
                connectionServer.requestedMethod("POST");
                connectionServer.buildParameter("name", signup_name.getText().toString());
                connectionServer.buildParameter("email", signup_email.getText().toString());
                connectionServer.buildParameter("contact", signup_contact.getText().toString());
                connectionServer.buildParameter("password", signup_password.getText().toString());
                connectionServer.buildParameter("city", spinner_value);
                connectionServer.execute(output -> {
                    Log.e("output", output);
                    JsonHelper jsonHelper = new JsonHelper(output);
                    if (jsonHelper.isValidJson()) {
                        String response = jsonHelper.GetResult("response");
                        if (response.equals("TRUE")) {
                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
//                                 login fail
                        }
                    }
                });
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onBackPressed() {
        if(doubleBackToExitPressedOnce){
            super.onBackPressed();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }
        this.doubleBackToExitPressedOnce = true ;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

}
