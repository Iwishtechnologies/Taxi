package tech.iwish.taxi.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import tech.iwish.taxi.R;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();
    }
}
