package com.seniorproject.design;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText EmailEditText, PasswordEditText;
    Button Loginbtn;
    android.widget.ProgressBar ProgressBar;
    TextView CreateAcctBtnTextView;


    //GoogleSignInOptions gso;
    //GoogleSignInClient gsc;
    ImageView googleBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EmailEditText = findViewById(R.id.emailEdittext);
        PasswordEditText = findViewById(R.id.passwordEdittext);
        ProgressBar = findViewById(R.id.progressbar);
        Loginbtn = findViewById(R.id.logintextbutton);
        CreateAcctBtnTextView = findViewById(R.id.createAcctBtn);

        Loginbtn.setOnClickListener((v)-> loginUser() );
        CreateAcctBtnTextView.setOnClickListener((v)->startActivity(new Intent(LoginActivity.this,CreateAcctActivity.class)) );




        //googleBtn = findViewById(R.id.googlebtn);


       /* gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct!=null){
            navigateToSecondActivity();
        }

        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });*/

    }

    void loginUser(){
        String  email = EmailEditText.getText().toString();
        String  password = PasswordEditText.getText().toString();


        boolean isValidated = validateData(email, password);
        if(!isValidated){
            return;
        }
        LoginAcctInFirebase(email, password);
    }
    void LoginAcctInFirebase(String email,String password){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        ProgressingChange(true);
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                ProgressingChange(false);
                if(task.isSuccessful()){
                   if(firebaseAuth.getCurrentUser().isEmailVerified()){
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }else {
                        utility.showToast(LoginActivity.this,"Email Not Verified.");
                    }
                }else {
                    utility.showToast(LoginActivity.this,task.getException().getLocalizedMessage());
                }
            }
        });

    }

    void ProgressingChange(boolean inProgress){
        if(inProgress){
            ProgressBar.setVisibility(View.VISIBLE);
            Loginbtn.setVisibility(View.GONE);
        }else {
            ProgressBar.setVisibility(View.GONE);
            Loginbtn.setVisibility(View.VISIBLE);
        }
    }
    boolean validateData(String email,String password){
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            EmailEditText.setError("Email is not valid.");
            return false;
        }
        if(password.length()<6){
            PasswordEditText.setError("Password not strong enough.");
            return false;
        }
        return true;
    }


    /*void signIn(){
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, 1000);
    } */

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                task.getResult(ApiException.class);
                navigateToSecondActivity();
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "There was an issue", Toast.LENGTH_SHORT).show();
            }
        }
    }
   void navigateToSecondActivity(){
        finish();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }
}