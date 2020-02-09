package com.sometning.zeesh.naidukapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class SignUpActivity extends AppCompatActivity {

    Button callLogin, signUpGo;
    EditText signUpUsername, signUpEmail, signUpPassword, signUpConfirmPassword;
    ProgressBar loadingBar;

    DatabaseReference rootReference;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        rootReference = FirebaseDatabase.getInstance().getReference();
        user=mAuth.getCurrentUser();

        callLogin = findViewById(R.id.callLogin);
        signUpGo = findViewById(R.id.signUpGo);
        signUpUsername = findViewById(R.id.signUpUsername);
        signUpEmail = findViewById(R.id.signUpEmail);
        signUpPassword = findViewById(R.id.signUpPassword);
        signUpConfirmPassword = findViewById(R.id.signUpConfirmPassword);
        loadingBar = findViewById(R.id.loadingbar);
        Wave wave = new Wave();
        loadingBar.setIndeterminateDrawable(wave);

        callLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        signUpGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RequestAccount();
            }
        });
    }

    private void RequestAccount() {
        final String email = signUpEmail.getText().toString().trim();
        final String username = signUpUsername.getText().toString().trim();
        final String password = signUpPassword.getText().toString().trim();
        String confirm_password = signUpConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            String error = "Enter your Username";
            signUpUsername.setError(error);
        }
        if (TextUtils.isEmpty(email)) {
            String error = "Enter your Email";
            signUpEmail.setError(error);
        }
        if (TextUtils.isEmpty(password)) {
            String error = "Enter your Password";
            signUpPassword.setError(error);
        }
        if (TextUtils.isEmpty(confirm_password)) {
            String error = "Confirm your Password";
            signUpConfirmPassword.setError(error);
        }
        if (!(password.equals(confirm_password))) {
            Toasty.error(SignUpActivity.this, "Password and Confirm Password Must Be Same", Toast.LENGTH_SHORT, true).show();

        } else {

            createAccount(username, email, password);
        }
    }

    private void createAccount(final String username, final String email, final String password)
    {
        loadingBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())
                {
                    final String currentUserID = mAuth.getCurrentUser().getUid();
                    HashMap<String  , String> profileMap =  new HashMap<>();
                    profileMap.put("ID",currentUserID);
                    profileMap.put("Email", email);
                    profileMap.put("Username",username);
                    profileMap.put("Password",password);

                    rootReference.child("Users").child(currentUserID).setValue(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful())
                            {
                                loadingBar.setVisibility(View.INVISIBLE);
                                Toasty.info(SignUpActivity.this , username + " ! Your Account has been Created.",Toasty.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUpActivity.this, AppDevelopmentFormActivity.class);
                                startActivity(intent);
                                finish();

                            }else
                            {
                                loadingBar.setVisibility(View.INVISIBLE);
                                Toasty.error(SignUpActivity.this , task.getException().toString()+ "" , Toasty.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
                else
                {
                    loadingBar.setVisibility(View.INVISIBLE);
                    Toasty.error(SignUpActivity.this , task.getException().toString()+ "" , Toasty.LENGTH_SHORT).show();
                }
            }
        });
    }
}
