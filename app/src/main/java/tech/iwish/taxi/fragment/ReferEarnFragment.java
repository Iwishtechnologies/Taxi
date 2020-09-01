package tech.iwish.taxi.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import java.security.cert.LDAPCertStoreParameters;
import java.util.Map;

import tech.iwish.taxi.BuildConfig;
import tech.iwish.taxi.R;
import tech.iwish.taxi.config.SharedpreferencesUser;

import static android.content.Context.CLIPBOARD_SERVICE;
import static com.android.volley.VolleyLog.TAG;
import static tech.iwish.taxi.config.SharedpreferencesUser.REFER_CODE;


public class ReferEarnFragment extends Fragment {

    private Button invite_money;
    private Map data;
    private TextView code, copy_code;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_refer_earn, null);

        invite_money = (Button) view.findViewById(R.id.invite_money);
        code = (TextView) view.findViewById(R.id.code);
        copy_code = (TextView) view.findViewById(R.id.copy_code);

        SharedpreferencesUser sharedpreferencesUser = new SharedpreferencesUser(getActivity());
        data = sharedpreferencesUser.getShare();
        Object refer_code = data.get(REFER_CODE);
        if (refer_code != null) {
            code.setText(refer_code.toString());
        }

        invite_money.setOnClickListener(view1 -> {

            String a = "{\"data\" : \"TRUE\" , \"refer_code\" : \"" + refer_code.toString() + "\"}";
            DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                    .setLink(Uri.parse("https://rayru.com/?refer_code=" + refer_code.toString()))
//                    .setDomainUriPrefix("https://rayru.page.link")
                    .setDomainUriPrefix("https://rayru.page.link")
                    // Open links with this app on Android
                    .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                    // Open links with com.example.ios on iOS
                    .setIosParameters(new DynamicLink.IosParameters.Builder("refer code" + refer_code.toString()).build())
                    .buildDynamicLink();

            Uri dynamicLinkUri = dynamicLink.getUri();
            Log.e(TAG, dynamicLinkUri + "?");
            Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                    .setLongLink(dynamicLinkUri)
//                    .setLongLink(Uri.parse(sharelinktext))
                    .buildShortDynamicLink()
                    .addOnCompleteListener(getActivity(), task -> {

                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();

//                            Intent intent = new Intent();
//                            intent.setAction(Intent.ACTION_SEND);
//                            intent.putExtra(Intent.EXTRA_STREAM, "");
//                            intent.putExtra(intent.EXTRA_TEXT, shortLink.toString());
//                            intent.setType("text/plain");
//                            startActivity(intent);

                            Log.e(TAG, shortLink.toString());

                            Intent intent = new Intent();
                            intent.setType("text/plain");
                            intent.setAction(Intent.ACTION_SEND);
                            intent.putExtra(intent.EXTRA_TEXT ,  "Refer code\n"+ shortLink);
                            startActivity(intent);


                        } else {
                            Log.e(TAG, "error", task.getException());
                        }
                    });

/*
            try {
//              https://play.google.com/store/apps/details?id=tech.iwish.taxi
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Rayru");
                String shareMessage= "\nJoin me on Rayru! You get "+getResources().getString(R.string.rs_symbol)+"50 off when you sign up using my code:"+refer_code.toString()+". Download Rayru now\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch(Exception e) {
                //e.toString();
            }
*/


        });


        copy_code.setOnClickListener(view12 -> {

            final ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("Source Text", data.get(REFER_CODE).toString());
            clipboardManager.setPrimaryClip(clipData);

            Toast.makeText(getActivity(), "Code copy", Toast.LENGTH_SHORT).show();
        });


        return view;
    }


}
