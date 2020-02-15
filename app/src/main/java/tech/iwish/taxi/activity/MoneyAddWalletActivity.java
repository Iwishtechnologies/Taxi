package tech.iwish.taxi.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import tech.iwish.taxi.R;
import tech.iwish.taxi.paymentgatway.Wallet;

public class MoneyAddWalletActivity extends AppCompatActivity implements PaymentResultListener {

    private TextView wallet_money_check;
    private Button proccess_btn;
    private EditText amount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_add_wallet);
        Checkout.preload(MoneyAddWalletActivity.this);


        wallet_money_check = (TextView) findViewById(R.id.wallet_money_check);
        proccess_btn = (Button) findViewById(R.id.proccess_btn);
        amount = (EditText) findViewById(R.id.amount);
        wallet_money_check.setText(getResources().getString(R.string.available_bal) + " " + getResources().getString(R.string.rsSymbol) + " " + getIntent().getStringExtra("money"));


        proccess_btn.setOnClickListener((View view) -> {
            String amount_edit = amount.getText().toString().trim();
            if (amountcheck(amount.getText().toString())) {
                Wallet wallet = new Wallet(MoneyAddWalletActivity.this);
                wallet.PaymentDetails(amount_edit);
            }
        });

    }

    private boolean amountcheck(String amt) {
        if (amt.isEmpty()) {
            amount.setError(getResources().getString(R.string.fill_amount));
            return false;
        }
        return true;
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this, "succ", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MoneyAddWalletActivity.this, PaymentSuccessfully.class);
        intent.putExtra("message", "successfully");
        intent.putExtra("edit_wallet_amount", amount.getText().toString().trim());
        startActivity(intent);
    }

    @Override
    public void onPaymentError(int i, String s) {
        paymenterror();
    }

    private void paymenterror() {
        Intent intent = new Intent(this, PaymentSuccessfully.class);
        intent.putExtra("message", "fail");
        startActivity(intent);
    }

}
