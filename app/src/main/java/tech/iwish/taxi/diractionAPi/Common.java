package tech.iwish.taxi.diractionAPi;

import tech.iwish.taxi.diractionAPi.IGoogleApi;
import tech.iwish.taxi.diractionAPi.RetrofitClient;

public class Common {
    public static final String baseURL = "http://googleapis.com";
    public static IGoogleApi getGoogleApi(){
         return RetrofitClient.getClient(baseURL).create(IGoogleApi.class);
    }
}
