package tech.iwish.taxi.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.model.LatLng;

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
    public static final String JSON = "json" ;
    public static final String DATA_WB = "datawb" ;
    public static final String SOCKETCONNECTION = "socketconnection";
    private static final String VEHICALE_UPDATE_LATLNG = "vehicled";
    private static final String WALLET_AMOUNT = "walletamount";
    private static final String DRIVERSHOW = "drivershow";

    SharedPreferences.Editor editor;

    int PRIVATE_MODE = 0;

    public Context context ;

    public SharedpreferencesUser(Context context) {
        this.context = context;
        Preferences = context.getSharedPreferences(MyPREFERENCES ,PRIVATE_MODE) ;
        editor = Preferences.edit();
    }

    public void user_detail(String user_name , String user_email ,String contact ){

        editor.putString(USER_NAME , user_name);
        editor.putString(USER_EMAIL , user_email);
        editor.putString(USER_CONTACT , contact);
        editor.putString(TEST_CHECH, "1");
        editor.commit() ;

    }

    public void walletAdd(String data){
        editor.putString(WALLET_AMOUNT , data);
    }

    public void location(String value){
        editor.putString(LOCATION_CHANGE, value);
        editor.commit();
    }
    public Map getShare(){
        Preferences.getAll();
        return Preferences.getAll();
    }


    public void clickLocationDestroy(){
        editor.remove(LOCATION_CHANGE).commit();
    }
    public void dropLocationIntenrt_Destrou(){
        editor.remove(DROP_INTENT_CITY).commit();
    }
    public void removedata(){
        editor.remove(DATA_WB).commit();

    }


     public void setjson(String json ){
        editor.putString(JSON,json);
        editor.commit();
     }

     public void driverShow(String json){
         editor.putString(DRIVERSHOW,json);
         editor.commit();
     }

     public void driverShowRemove(){
         editor.remove(DRIVERSHOW).commit();
     }


     public String driverReturnData(){
        return Preferences.getString(DRIVERSHOW,null);
     }

     public String getjson()
     {
       return   Preferences.getString(JSON,null);
     }

     public void getDatasWebsocket(String string){
        editor.putString(DATA_WB , string).commit();
     }
     public void removeDataWebsocket(){
          editor.remove(DATA_WB).commit();
     }

    /**
     * set Socketconnection
     * **/
    public void setSocketConnection(Boolean aBoolean)
    {
        editor.putString(SOCKETCONNECTION,String.valueOf(aBoolean)).commit();
    }
    /**
     * set Socketconnection
     *
     * @return**/
    public boolean getSocketConnection()
    {
        return Boolean.valueOf(Preferences.getString(SOCKETCONNECTION,null));
    }
}
