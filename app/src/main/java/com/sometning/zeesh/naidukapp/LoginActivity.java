package com.sometning.zeesh.naidukapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {

    Button callSignup , go;
    EditText Email, Password;
    ProgressBar loadingBar;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //This Line will hide the status bar from the screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        callSignup = findViewById(R.id.callSignup);
        go = findViewById(R.id.loginGO);
        Email = findViewById(R.id.email);
        Password = findViewById(R.id.password);
        loadingBar  = findViewById(R.id.loginLoadingBar);
        Wave wave = new Wave();
        loadingBar.setIndeterminateDrawable(wave);

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {


                    final String email = Email.getText().toString().trim();
                    final String password = Password.getText().toString().trim();

                    if(TextUtils.isEmpty(email))
                    {
                        Email.setError("Email Required");

                    }
                    if(TextUtils.isEmpty(password))
                    {
                        Password.setError("Password Required");

                    }
                    if(!(TextUtils.isEmpty(email)))
                    {
                        if(!(TextUtils.isEmpty(password)))
                        {
                            ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                            ConnectivityManager cm =
                                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                            boolean isConnected = activeNetwork != null &&
                                    activeNetwork.isConnectedOrConnecting();


                            if (mWifi.isConnected() || isConnected) {

                                AllowUserToLogin(email, password);
                            }
                            else
                            {
                                Toasty.warning(LoginActivity.this, "Check Your Internet ! Make Sure Your are Connected to Internet ", Toasty.LENGTH_SHORT).show();
                            }



                        }

                    }
                }catch (Exception e)
                {
                    Toast.makeText(LoginActivity.this, ""+ e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        callSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this , SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void AllowUserToLogin(String email, String password)
    {
        loadingBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    loadingBar.setVisibility(View.INVISIBLE);
                    Toasty.info(LoginActivity.this , "Login Successfully.",Toasty.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, AppDevelopmentFormActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    loadingBar.setVisibility(View.INVISIBLE);
                    Toasty.error(LoginActivity.this , task.getException().toString()+ "" , Toasty.LENGTH_SHORT).show();
                }
            }
        });
    }
}
