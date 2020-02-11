package tech.iwish.taxi.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import tech.iwish.taxi.R;
import tech.iwish.taxi.activity.MainActivity;


public class RateCardFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rate_card , null);

        return view ;
    }

    public boolean allowBackPressed() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        return true ;
    }
}
