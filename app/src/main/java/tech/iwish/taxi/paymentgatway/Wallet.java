package tech.iwish.taxi.paymentgatway;

import android.app.Activity;

import com.razorpay.Checkout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import tech.iwish.taxi.config.SharedpreferencesUser;

import static tech.iwish.taxi.config.SharedpreferencesUser.USER_CONTACT;
import static tech.iwish.taxi.config.SharedpreferencesUser.USER_EMAIL;
import static tech.iwish.taxi.config.SharedpreferencesUser.USER_NAME;

public class Wallet{

    private Activity context ;
    private SharedpreferencesUser sharedpreferencesUser;
    private Map data ;

    public Wallet(Activity activity) {

        this.context = activity ;
        sharedpreferencesUser = new SharedpreferencesUser(context);

    }

    public void PaymentDetails(String amount){


//        this.editAmount = money ;

        data = sharedpreferencesUser.getShare();
        String name = data.get(USER_NAME).toString();
        String mob = data.get(USER_CONTACT).toString();
        String email = data.get(USER_EMAIL).toString();
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_zvQhBxwwcpaCDA");
        final  Activity activity = context ;

        JSONObject object= new JSONObject();
        try {
            object.put("name" ,name);
            object.put("description" ,"Iwish");
            object.put("amount" ,Double.valueOf(amount)*100);
            object.put("current" ,"INR");
            object.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");

            JSONObject preFill = new JSONObject();
            preFill.put("email" , email);
            preFill.put("contact" , mob);
            object.put("prfill" ,preFill);

            checkout.open(activity, object);


        } catch (JSONException e) {
//            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }





    /*@Override
    public void onPaymentSuccess(String razorPayId) {
        try {
            Toast.makeText(context, "Payment Successflly"+razorPayId, Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Intent intent = new Intent(context , WallletSuccessfully.class);
            intent.putExtra("message" , "successfully");
            context.startActivity(intent);         
        }
   
    }

    @Override
    public void onPaymentError(int i, String s) {
        Intent intent = new Intent(context , WallletSuccessfully.class);
        intent.putExtra("message" , "fail");
        context.startActivity(intent);
    }*/
}
