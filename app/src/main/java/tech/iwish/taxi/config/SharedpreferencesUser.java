package tech.iwish.taxi.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.model.LatLng;

import java.security.PublicKey;
import java.util.Map;

import tech.iwish.taxi.activity.MainActivity;

public class SharedpreferencesUser {

    SharedPreferences Preferences;
    public static final String MyPREFERENCES = "TaxtSharepreferen";

    public static final String USER_EMAIL = "user_email";
    public static final String WEBSOCKET = "websocket";
    public static final String USER_NAME = "user_name";
    public static final String USER_CONTACT = "user_contact";
    public static final String REFER_CODE = "refer_code";

    public static final String USER_LOGIN = "login";
    public static final String TEST_CHECH = "0";
    public static final String LOCATION_CHANGE = "mo";
    public static final String DROP_INTENT_CITY = "";
    public static final String PICKUP_INTENT_CITY = "";
    public static final String JSON = "json";
    public static final String DATA_WB = "datawb";
    public static final String SOCKETCONNECTION = "socketconnection";
    private static final String VEHICALE_UPDATE_LATLNG = "vehicled";
    public static final String WALLET_AMOUNT = "walletamount";
    public static final String DRIVERSHOW = "drivershow";
    public static final String VEHICLE_DATA = "scdscsdcd";
    public static final String OTP_CONFIRM_DRIVER = "otpconfirmdriver";
    public static final String RIDE_CONFIRM_DATA = "rideconfirmdata";
    public static final String IMAGE_URL = "173.212.226.143:8083/";
//    info driver
    public static final String DRIVER_NAME = "driver_name";
    public static final String DRIVER_NUMBER = "driver_mob";
    public static final String RATE = "rate";
    public static final String TIME = "time";
    public static final String DISTANCE = "distance";
    public static final String OTP = "5484";
    public static final String DRIVER_OTP = "otp_share_driver";
    public static final String TRACK_ID = "track_id";
    public static final String REFER_CODE_LINK_CHECK = "refer_link_check";

    SharedPreferences.Editor editor;

    int PRIVATE_MODE = 0;

    public Context context;

    public SharedpreferencesUser(Context context) {
        this.context = context;
        Preferences = context.getSharedPreferences(MyPREFERENCES, PRIVATE_MODE);
        editor = Preferences.edit();
    }

    public void user_detail(String user_name, String user_email, String contact , String refer_code) {

        editor.putString(USER_NAME, user_name);
        editor.putString(USER_EMAIL, user_email);
        editor.putString(USER_CONTACT, contact);
        editor.putString(REFER_CODE, refer_code);
        editor.putString(TEST_CHECH, "1");
        editor.commit();

    }
    public void logout(){
        editor.remove(TEST_CHECH).commit();
    }

    public void walletAdd(String data) {
        editor.putString(WALLET_AMOUNT, data).commit();
    }

    public void location(String value) {
        editor.putString(LOCATION_CHANGE, value);
        editor.commit();
    }

    public Map getShare() {
        Preferences.getAll();
        return Preferences.getAll();
    }


    public void clickLocationDestroy() {
        editor.remove(LOCATION_CHANGE).commit();
    }

    public void dropLocationIntenrt_Destrou() {
        editor.remove(DROP_INTENT_CITY).commit();
    }

    public void removedata() {
        editor.remove(DATA_WB).commit();

    }


    public void setjson(String json) {
        editor.putString(JSON, json);
        editor.commit();
    }

    public void driverShow(String json) {
        editor.putString(DRIVERSHOW, json);
        editor.commit();
    }

    public void driverShowRemove() {
        editor.remove(DRIVERSHOW).commit();
    }


    public String driverReturnData() {
        return Preferences.getString(DRIVERSHOW, null);
    }

    public String getjson() {
        return Preferences.getString(JSON, null);
    }

    public void getDatasWebsocket(String string) {
        editor.putString(DATA_WB, string).commit();
    }

    public void removeDataWebsocket() {
        editor.remove(DATA_WB).commit();
    }


    public void setSocketConnection(Boolean aBoolean) {
        editor.putString(SOCKETCONNECTION, String.valueOf(aBoolean)).commit();
    }

    public boolean getSocketConnection() {
        return Boolean.valueOf(Preferences.getString(SOCKETCONNECTION, null));
    }

    public void driverInfo(String driver_name, String driver_mobile, String rate, String time, String distance, String otp , String trackid) {

        editor.putString(DRIVER_NAME, driver_name).commit();
        editor.putString(DRIVER_NUMBER, driver_mobile).commit();
        editor.putString(RATE, rate).commit();
        editor.putString(TIME, time).commit();
        editor.putString(DISTANCE, distance).commit();
        editor.putString(OTP, otp).commit();
        editor.putString(TRACK_ID, trackid).commit();

    }

    public void driverInfo_remove() {

        editor.remove(DRIVER_NAME).commit();
        editor.remove(DRIVER_NUMBER).commit();
        editor.remove(RATE).commit();
        editor.remove(TIME).commit();
        editor.remove(DISTANCE).commit();
        editor.remove(String.valueOf(OTP)).commit();

    }

    public void vehicledata(String text) {
        editor.putString(VEHICLE_DATA, text).commit();
    }

    public void remove_vehicledata() {
        editor.remove(VEHICLE_DATA).commit();
    }

    public void otpConfirmDriver(String text) {
        editor.putString(OTP_CONFIRM_DRIVER, text).commit();
    }

    public void remove_otpConfirmDriver() {
        editor.remove(OTP_CONFIRM_DRIVER).commit();
    }

    public void refer_code_chech(String refercode){
        editor.putString(REFER_CODE_LINK_CHECK,refercode).commit();

    }
    public void remove_refer_code_chech(){
        editor.remove(REFER_CODE_LINK_CHECK).commit();
    }


}























