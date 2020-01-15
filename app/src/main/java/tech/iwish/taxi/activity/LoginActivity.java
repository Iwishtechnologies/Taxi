package tech.iwish.taxi.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import tech.iwish.taxi.R;
import tech.iwish.taxi.config.Constants;
import tech.iwish.taxi.config.SharedpreferencesUser;
import tech.iwish.taxi.connection.ConnectionServer;
import tech.iwish.taxi.connection.JsonHelper;
import tech.iwish.taxi.fragment.LoginFragment_1;
import tech.iwish.taxi.other.DropLocationList;
import tech.iwish.taxi.other.User_DetailsList;

public class LoginActivity extends AppCompatActivity {


    private List<User_DetailsList> user_detailsLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        getSupportActionBar().hide();

        LoginFragment_1 loginFragment_1 = new LoginFragment_1();
        getSupportFragmentManager().beginTransaction().replace(R.id.Login_fragmentLayout , loginFragment_1).commit() ;

    }
}
