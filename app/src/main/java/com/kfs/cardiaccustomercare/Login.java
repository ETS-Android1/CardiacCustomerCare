package com.kfs.cardiaccustomercare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class Login extends AppCompatActivity {

    //for inflating the email and password edit text
    EditText emailET,passwordET;

    //login button for send the data to server for checking
    Button login;
    //login with facbock or google
    ImageView facbock_Login ,google_login ;

    //two strings which will carry the values  which the user typed
    String email,password;

    //for inflating the createOne text view
    TextView creteOne , forgetPassword;


    Context context=this;

    //facebook
    LoginButton facebookLogin;
    CallbackManager callbackManager;


    //google
    GoogleSignInClient mGoogleSignInClient;
    SignInButton signInButton;
    private int RC_SIGN_IN =0;

    //Toast that we will use to prevent repeating of instantiating
    Toast mToast;

    private SharedPreferencesConfig sharedPreferencesConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        instantiatingReferenceForSharedPreferences();
        checkForUserStatus();
        permissionsChecking();
        inflatingViews();

        //what will happen when the user click login
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //first get the values typed
                getValues();

                //special case to login without database for my team
                if(email.equals("team") && password.equals("12345"))
                {
                    startActivity(new Intent(Login.this , Home.class));
                }
                //check if the values are empties
                if(checkValues())
                {
                    SendData(email , password);
                }
            }});


        //handle create one to make it lead to register activity
        creteOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegister();
            }
        });



        //handle  forget password to make it lead to forget password activity
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Verification();
            }
        });

        facbock_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebookLogin.performClick();


            }
        });

        google_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInButton.performClick();

            }
        });


        //Login with Facebook

        callbackManager = CallbackManager.Factory.create() ;
      facebookLogin.setReadPermissions(Arrays.asList("email"));
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
        });





        //google login



      GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;

                }
            }
        });



    }

    // This method for retrieving password if user forget password , and email verification when user regestring with email
    private void Verification() {
        if(!isNetworkAvailable())
        {
            if(mToast != null) {
                mToast.cancel();

            }
            mToast = Toast.makeText(context, "No internet connection !!", Toast.LENGTH_LONG);
            mToast.show();
            return;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "http://192.168.1.9/verification.php"
                ,
                //in case of successful response get the message from server if failed show that email or password is wrong
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("login response",response);


                            startActivity(new Intent(Login.this , Confirmation.class));

                    }
                },
                //in case of failed response the main reason is because no connection
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
                params.put("verification", "the user forget password");
                return params;
            }
        };

        // add the string request to our single queue
        Singleton.getInstance(Login.this).addToRequest(stringRequest);

    }

    private boolean checkValues() {

        if (email.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"please enter E-mail",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (password.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"please enter password",Toast.LENGTH_SHORT).show();
            return false;
        }else if(email.endsWith(".com")!= true || email.contains("@")!= true)
        {
            Toast.makeText(getApplicationContext(),"please valid E-mail",Toast.LENGTH_SHORT).show();
            return false;
        }else if(password.length() < 5)
        {
            Toast.makeText(getApplicationContext(),"Password must exceed 5 characters",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void getValues() {
        email = emailET.getText().toString();
        password = passwordET.getText().toString();
    }


    private void instantiatingReferenceForSharedPreferences() {
        sharedPreferencesConfig = new SharedPreferencesConfig(getApplicationContext());
    }

    private void checkForUserStatus() {
        if(sharedPreferencesConfig.readLoginStatus())
        {
            startActivity(new Intent(this, Home.class));
            finish();
        }
    }

    //this method if to check for permission if them are granted or denied  ,  and we check for results in onRequestPermissionsResult method
    private void permissionsChecking() {
        if (ActivityCompat.checkSelfPermission(Login.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(Login.this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(Login.this, new String[]
                            {Manifest.permission.WRITE_EXTERNAL_STORAGE ,Manifest.permission.RECORD_AUDIO},
                    2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if( requestCode == 2)
        {
            if (grantResults[0]== PackageManager.PERMISSION_GRANTED && grantResults[1]== PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(getApplicationContext(),"You give the right to store files and record audio", Toast.LENGTH_LONG).show();
            }else if (grantResults[0]== PackageManager.PERMISSION_GRANTED && grantResults[1]== PackageManager.PERMISSION_DENIED){
                Toast.makeText(getApplicationContext(),"You will not be able to record an audio because permission is denied", Toast.LENGTH_LONG).show();
            }else if (grantResults[0]== PackageManager.PERMISSION_DENIED && grantResults[1]== PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getApplicationContext(),"You will not be able to store an audio files because permission is denied , you can`t benefit of our service", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(),"you can`t benefit of our service because of permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }



    //This method for inflating view
    private void inflatingViews() {
        facbock_Login=findViewById(R.id.login_facbock);
        google_login=findViewById(R.id.login_google);
        emailET = (EditText) findViewById(R.id.edit_text_email);
        passwordET =(EditText) findViewById(R.id.edit_text_password);
        login =(Button) findViewById(R.id.button_login);
        creteOne = (TextView)findViewById(R.id.register);
        forgetPassword = (TextView)findViewById(R.id.forget_password);
        facebookLogin = (LoginButton)findViewById(R.id.login_button);

       signInButton = (SignInButton)findViewById(R.id.sign_in_button);
//        TextView textView = (TextView) signInButton.getChildAt(0);..............................................................................
       //textView.setText("Continue with Google");.......................................................................................................
    }

    //Go to Register Activity
    private void goToRegister() {
        startActivity(new Intent(Login.this, Register.class));
    }


    // This method to check if user has no internet connetion
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void SendData(String email , String password)
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

        final String EMAIL = email;
        final String PASSWORD = password;
        //send the data to server and wait for response
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "http://192.168.1.9/login.php"
                ,
                //in case of successful response get the message from server if failed show that email or password is wrong
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("login response",response);
                        String result = handlingJsonArray(response);
                        if(result.equals("login failed"))
                        {
                            Toast.makeText(context, "Wrong email or password", Toast.LENGTH_LONG).show();
                        }else{
                            sharedPreferencesConfig.writeLoginStatus(true);
                            Toast.makeText(context, "Login successfully", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(Login.this , Home.class));
                        }
                    }
                },
                //in case of failed response the main reason is because no connection
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
                params.put("email", EMAIL);
                params.put("password", PASSWORD);
                return params;
            }
        };

        // add the string request to our single queue
        Singleton.getInstance(Login.this).addToRequest(stringRequest);
    }

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


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);


    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode , resultCode , data);
        if(requestCode == 64206)
        {
            Log.e("faceboooooooooook", requestCode+" , "+resultCode+" , "+ data);
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }else if (requestCode == 0)
        {
            Log.e("gooooooooogle",requestCode+" , "+resultCode+" , "+ data);
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

    }



    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {

                String personEmail = acct.getEmail();
                SendData(personEmail,"-1Google");
                sharedPreferencesConfig.writeLoginStatus(true);
            }} catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("ggg", "signInResult:failed code=" + e.getStatusCode());

        }
    }








    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            if (currentAccessToken==null)
            {
                Toast.makeText(Login.this,"nulllllllll",Toast.LENGTH_LONG).show();
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

                    Toast.makeText(Login.this,email+"  ",Toast.LENGTH_LONG);
                    Log.e("emaillllllll",email);
                    SendData(email,"-1Fcebook");
                    sharedPreferencesConfig.writeLoginStatus(true);
                    startActivity(new Intent(Login.this,Home.class));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "email");
        request.setParameters(parameters);
        request.executeAsync();
    }


}
