package tech.iwish.taxi.config;

import android.content.Context;
import android.content.SharedPreferences;

import java.security.PublicKey;
import java.util.Map;

public class SharedpreferencesUser {

    SharedPreferences Preferences ;
    public static final String MyPREFERENCES = "TaxtSharepreferen" ;

    public static final String USER_EMAIL = "user_email";
    public static final String USER_NAME = "user_email";
    public static final String USER_CONTACT = "branch_code";

    public static final String USER_LOGIN = "login";
    public static final String TEST_CHECH = "0" ;
    public static final String LOCATION_CHANGE = "mo" ;
    public static final String DROP_INTENT_CITY = "" ;
    public static final String PICKUP_INTENT_CITY = "" ;


    SharedPreferences.Editor editor;

    int PRIVATE_MODE = 0;

    public Context context ;

    public SharedpreferencesUser(Context context) {
        this.context = context;
        Preferences = context.getSharedPreferences(MyPREFERENCES ,PRIVATE_MODE) ;
        editor = Preferences.edit();
    }

    public void user_detail(String user_name , String user_email ,String contact ){

        editor.putString(USER_EMAIL , user_email);
        editor.putString(USER_NAME , user_name);
        editor.putString(USER_CONTACT , contact);
        editor.putString(TEST_CHECH, "1");
        editor.commit() ;

    }

    public void location(String value){
        editor.putString(LOCATION_CHANGE, value);
        editor.commit();
    }
    public Map getShare(){
        Preferences.getAll();
        return Preferences.getAll();
    }

    public void dropIntent_City(String value){
        editor.putString(DROP_INTENT_CITY, value);
        editor.commit();
    }
    public void pickup_Intent_City(String value){
        editor.putString(PICKUP_INTENT_CITY, value);
        editor.commit();
    }
    public void clickLocationDestroy(){
        editor.remove(LOCATION_CHANGE).commit();
    }
    public void dropLocationIntenrt_Destrou(){
        editor.remove(DROP_INTENT_CITY).commit();
    }
    public void pickup_location_Intenrt_Destrou(){
        editor.remove(PICKUP_INTENT_CITY).commit();
    }



}
