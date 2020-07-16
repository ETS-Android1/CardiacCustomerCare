package com.kfs.cardiaccustomercare;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Register extends AppCompatActivity {

    //Strings will store values
    String   Email, Password ;
    //EditTexts for inflatin
    EditText email, pass, repass ;
    //For inflating Register button
    Button btn;
    //The context
    Context context = this;


    //facebook
  //  LoginButton facebookLogin;
   // CallbackManager callbackManager;


    //google
    /*GoogleSignInClient mGoogleSignInClient;
    SignInButton signInButton;
    GoogleSignInOptions gso;
    private int RC_SIGN_IN =0; */


    //Toast that we will use to prevent repeating of instantiating
    Toast mToast;
    String number = generateRandomNumber();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inflatingViews();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //first get the values typed
                getValues();

                //check if the values are empties
                if(checkValues())
                {
                    SendData(Email , Password);
                }
            }

        });


        //facebook
       /* instantiatingFacebookObjects();
        facebookLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        }); */






        //google login
      /*   instantiatingGoogleObjects(); ........................................................................................................................
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                    // ...
                }
            }
        });*/ // .........................................................................................................................

    }


    //Infalting all Views in the screen
    private void inflatingViews() {
        email = (EditText) findViewById(R.id.edit_text_email_register);
        pass = (EditText) findViewById(R.id.edit_text_password);
        btn = (Button) findViewById(R.id.Register);

       // facebookLogin = (LoginButton)findViewById(R.id.login_button);...........................................................................

       // signInButton = (SignInButton)findViewById(R.id.sign_in_button);........................................................................
       // TextView textView = (TextView) signInButton.getChildAt(0);
      //  textView.setText("Continue with Google");
    }



    //Get the values which the user typed in EditTexts
    private void getValues() {
        Email = email.getText().toString();
        Password = pass.getText().toString();
    }


    //Check for values typed in EditTexts and called when Register button if clicked ///
    private boolean checkValues() {

        if (Email.isEmpty()) {
            if(mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(this, "Please enter your email ", Toast.LENGTH_LONG);
            mToast.show();
            return false;
        }
        else if (Password.isEmpty()) {
            if(mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(this, "Please enter your password ", Toast.LENGTH_LONG);
            mToast.show();
            return false;
        }else if(Email.trim().endsWith(".com")!= true || Email.contains("@")!= true )
        {
            if(mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(this, "Please enter valid email ", Toast.LENGTH_LONG);
            mToast.show();
            return false;
        }else if(Password.length() < 6)
        {
            if(mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(this, "Your password must be 6 characters at least ", Toast.LENGTH_LONG);
            mToast.show();
            return false;
        }
        return true;
    }

// This method to check if user has no internet connetion
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    //Send data method which send email and password to server
    public void SendData(final String email , final String password)
    {
        if(!isNetworkAvailable())
        {
            if(mToast != null) {
                mToast.cancel();

            }
            mToast = Toast.makeText(context, "No internet connection !!", Toast.LENGTH_LONG);
            mToast.show();
            return;
        }
        final String emailToSend = email;
        final String passwordToSend = password;
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "http://192.168.1.9/register.php"
                ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String result = handlingJsonArray(response);
                        if(result.equals("reg failed"))
                        {
                            if(mToast != null) {
                                mToast.cancel();
                            }
                            mToast = Toast.makeText(context, "Sorry email already exist !! ", Toast.LENGTH_LONG);
                            mToast.show();
                        }else{
                            if(mToast != null) {
                                mToast.cancel();
                            }
                            mToast = Toast.makeText(context, "Registeration completed successfully ", Toast.LENGTH_LONG);
                            mToast.show();
                            Intent intent = new Intent(Register.this,AccountInfo.class);
                            intent.putExtra("code",number);

                            startActivity(intent);
                        }
                    }
                }

                ,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                            if(mToast != null) {
                                mToast.cancel();
                            }
                            mToast = Toast.makeText(context, "Sorry we have some problems in server \n Please try after a few minutes", Toast.LENGTH_LONG);
                            mToast.show();
                            return;
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", emailToSend);
                params.put("password", passwordToSend);
                Log.e("eap",""+emailToSend+"  /  "+passwordToSend);
                params.put("code", number);
                Log.e("codeeeeeeeeeeeeee","hhhhhhhhhhhh"+number);
                return params;
            }
        };


        Singleton.getInstance(Register.this).addToRequest(stringRequest);
    }



    //Handling json array coming from the server to get results of sending email and password
    private String handlingJsonArray(String response) {
        JSONArray jsonArray = null;
        String result = null;
        try {
            jsonArray = new JSONArray(response);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            result = jsonObject.getString("code");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("handling Json Array",e.getMessage());
        }
        return result;
    }





    //Get results from  facebook and google login
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode , resultCode , data);
        if(requestCode == 64206)
        {
            Log.e("faceboooooooooook", requestCode+" , "+resultCode+" , "+ data);
           // callbackManager.onActivityResult(requestCode, resultCode, data);
        }else if (requestCode == 0)
        {
            Log.e("gooooooooogle",requestCode+" , "+resultCode+" , "+ data);
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
         //   handleSignInResult(task);
        }
    }





    // Google Login related methods
  /* private void instantiatingGoogleObjects()
    {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                String personEmail = acct.getEmail();
                SendData(personEmail,"-1Google");
            }} catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("ggg", "signInResult:failed code=" + e.getStatusCode());

        }
    } */






    //This method to generate a random number and send this number for the email typed
    private String generateRandomNumber()
    {
        int min = 100000;
        int max = 999999;
        int num = (int)(Math.random()*((max-min)+1))+min;
        String number = ""+num;
        return number;
    }






    //Facebook Login related methods
  /*  private void instantiatingFacebookObjects()
    {
        callbackManager = CallbackManager.Factory.create();
        facebookLogin.setReadPermissions(Arrays.asList("email"));
    }
    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            if (currentAccessToken==null)
            {
                Toast.makeText(Register.this,"nulllllllll",Toast.LENGTH_LONG).show();
            }else {
                loadData(currentAccessToken);
            }
        }
    };

    private void loadData (AccessToken newAccessToken)
    {
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String email = object.getString("email");
                    SendData(email,"-1Fcebook");

                    Toast.makeText(Register.this,email+"  ",Toast.LENGTH_LONG);
                    Log.e("emaillllllll",email);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "email");
        request.setParameters(parameters);
        request.executeAsync();
    } */
}


// Facebook request code = 64206 , result code = -1 , data = Intent in onActivityResult method
// Google request code = 0 , result code = -1 , data = Intent in onActivityResult method

/*
<com.google.android.material.textfield.TextInputLayout
        style="@style/EditText"
        android:layout_height="wrap_content"
        android:layout_width="match_parent"
        android:layout_below="@id/emailRegister"
        android:id="@+id/layoutTextInput"
        app:endIconMode="custom"
        app:endIconDrawable="@drawable/eyeoffimg"
        app:passwordToggleEnabled="true"
        app:passwordToggleDrawable="@drawable/show_password_selector">
        <com.google.android.material.textfield.TextInputEditText
            style="@style/EditText"
            android:id="@+id/passwordRegister"
            android:hint=" password"
            android:layout_below="@id/emailRegister"
            android:inputType="textPassword"
            android:drawableStart="@drawable/passwordimg"
            android:drawableEnd="@drawable/eyeoffimg"/>
    </com.google.android.material.textfield.TextInputLayout>
 */