package com.kfs.cardiaccustomercare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Confirmation extends AppCompatActivity {

    Context context = this;

    LinearLayout verificationContainer;
    EditText verifyET ;
    TextView errorTV ;
    Button verify;

    int code;
    String typedCode;

    //Toast that we will use to prevent repeating of instantiating
    Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        InflatingViews();

        Intent intent = getIntent();
        int code = intent.getIntExtra("code",0);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Verifiy();
            }
        });





    }

    private void Verifiy() {
        typedCode = verifyET.getText().toString();
        if(typedCode.isEmpty())
        {
            if(mToast != null) {
                mToast.cancel();

            }
            mToast = Toast.makeText(context, "Please enter code which is sent to your email ", Toast.LENGTH_LONG);
            mToast.show();
            return;

        }else if (Integer.parseInt(typedCode ) == code){
            ChangePassword();
            startActivity(new Intent(Confirmation.this,Home.class));
        }else {
            errorTV.setText("invalid code");
        }
    }

    private void ChangePassword() {

    }

    private void InflatingViews() {
        verificationContainer = (LinearLayout) findViewById(R.id.verificationContainer);
        verifyET = (EditText) findViewById(R.id.verifyET);
        errorTV = (TextView)findViewById(R.id.errorTV);
        verify = (Button)findViewById(R.id.verifyBTN);
    }
}
