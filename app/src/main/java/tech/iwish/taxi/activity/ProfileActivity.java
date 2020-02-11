package tech.iwish.taxi.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Map;

import tech.iwish.taxi.R;
import tech.iwish.taxi.config.SharedpreferencesUser;

import static tech.iwish.taxi.config.SharedpreferencesUser.USER_CONTACT;
import static tech.iwish.taxi.config.SharedpreferencesUser.USER_EMAIL;
import static tech.iwish.taxi.config.SharedpreferencesUser.USER_NAME;

public class ProfileActivity extends AppCompatActivity {

    private Map data;
    private EditText profile_name ,profile_email , user_number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();

        profile_name = (EditText)findViewById(R.id.profile_name);
        profile_email = (EditText)findViewById(R.id.profile_email);
        user_number = (EditText)findViewById(R.id.user_number);

        SharedpreferencesUser sharedpreferencesUser  = new SharedpreferencesUser(this);
        data = sharedpreferencesUser.getShare();

        Toast.makeText(this, ""+data.get(USER_EMAIL).toString(), Toast.LENGTH_SHORT).show();

        profile_name.setText(data.get(USER_NAME).toString());
        profile_email.setText(data.get(USER_EMAIL).toString());
        user_number.setText(data.get(USER_CONTACT).toString());



    }
}
