package tech.iwish.taxi.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import tech.iwish.taxi.R;
import tech.iwish.taxi.config.Constants;
import tech.iwish.taxi.connection.ConnectionServer;
import tech.iwish.taxi.connection.JsonHelper;

public class SignupActivity extends AppCompatActivity {

    private EditText signup_name , signup_email , signup_contact , signup_password ;
    private Button signup_button ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();

        signup_name = (EditText)findViewById(R.id.signup_name);
        signup_email = (EditText)findViewById(R.id.signup_email);
        signup_contact = (EditText)findViewById(R.id.signup_contact);
        signup_password = (EditText)findViewById(R.id.signup_password);
        signup_button = (Button)findViewById(R.id.signup_button);

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ConnectionServer connectionServer = new ConnectionServer();
                connectionServer.set_url(Constants.USER_SIGUP);
                connectionServer.requestedMethod("POST");
                connectionServer.buildParameter("name" , signup_name.getText().toString());
                connectionServer.buildParameter("email" , signup_email.getText().toString());
                connectionServer.buildParameter("contact" , signup_contact.getText().toString());
                connectionServer.buildParameter("password" , signup_password.getText().toString());
                connectionServer.execute(new ConnectionServer.AsyncResponse() {
                    @Override
                    public void processFinish(String output) {
                        Log.e("output" , output);
                        JsonHelper jsonHelper = new JsonHelper(output);
                        if(jsonHelper.isValidJson()){
                            String response = jsonHelper.GetResult("response");
                            if(response.equals("TRUE")){
                                Intent intent = new Intent(SignupActivity.this , LoginActivity.class);
                                startActivity(intent);
                            }else{
//                                 login fail
                            }
                        }
                    }
                });
            }
        });
    }

}
