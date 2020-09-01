package tech.iwish.taxi.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import tech.iwish.taxi.R;
import tech.iwish.taxi.config.Constants;
import tech.iwish.taxi.config.SharedpreferencesUser;
import tech.iwish.taxi.connection.ConnectionServer;
import tech.iwish.taxi.connection.JsonHelper;

import static tech.iwish.taxi.config.SharedpreferencesUser.USER_CONTACT;
import static tech.iwish.taxi.config.SharedpreferencesUser.USER_EMAIL;
import static tech.iwish.taxi.config.SharedpreferencesUser.USER_NAME;

public class ProfileActivity extends AppCompatActivity {

    private Map datas;
    private EditText profile_name, profile_email, user_number;
    private Button edit_button;
    private CircleImageView profile_image;
    File imageValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();

        profile_name = (EditText) findViewById(R.id.profile_name);
        profile_email = (EditText) findViewById(R.id.profile_email);
        user_number = (EditText) findViewById(R.id.user_number);
        edit_button = (Button) findViewById(R.id.edit_button);
        profile_image = findViewById(R.id.profile_image);

        SharedpreferencesUser sharedpreferencesUser = new SharedpreferencesUser(this);
        datas = sharedpreferencesUser.getShare();

        edit_button.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
        });


        profile_name.setText(datas.get(USER_NAME).toString());
        profile_email.setText(datas.get(USER_EMAIL).toString());
        user_number.setText(datas.get(USER_CONTACT).toString());

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {

                        Uri uri = Uri.parse(data.getData().toString());
                        imageValue = new File(uri.getPath());
                        Log.e("aa", String.valueOf(imageValue));
                        Log.e("aa", String.valueOf(imageValue));


                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                        profile_image.setImageBitmap(bitmap);

                        sendData();



/*
                        ConnectionServer connectionServer = new ConnectionServer();
                        connectionServer.set_url(Constants.PROFILE_IMAGE);
                        connectionServer.requestedMethod("POST");
                        connectionServer.buildParameter("mob" , datas.get(USER_CONTACT).toString());
                        connectionServer.setFilepath("profile_image" ,data.getData().toString());
                        connectionServer.execute(output -> {
                            Log.e("output" , output);
                            JsonHelper jsonHelper = new JsonHelper(output);
                            if(jsonHelper.isValidJson()){
                                String response = jsonHelper.GetResult("response");
                                if(response.equals("TRUE")){

                                }

                            }
                        });

*/


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void sendData() {
        OkHttpClient okHttpClient1 = new OkHttpClient();
        final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("images", imageValue.getName(), RequestBody.create(MediaType.parse("image/*"), imageValue))
                .addFormDataPart("mob", datas.get(USER_CONTACT).toString())
                .build();


        Request request1 = new Request.Builder().url(Constants.PROFILE_IMAGE).post(requestBody).build();
        okHttpClient1.newCall(request1).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                ProfileActivity.this.runOnUiThread(() -> {
                    Toast.makeText(ProfileActivity.this, "Connection Timeout", Toast.LENGTH_SHORT).show();
                });
                Log.e("error", String.valueOf(e));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {

                    String result = response.body().string();
                    Log.e("response", result);
                    final JsonHelper jsonHelper = new JsonHelper(result);
                    if (jsonHelper.isValidJson()) {
                        String responses = jsonHelper.GetResult("response");
                        if (responses.equals("TRUE")) {


                        } else {

                        }
                    }

                }
            }
        });
    }



}











