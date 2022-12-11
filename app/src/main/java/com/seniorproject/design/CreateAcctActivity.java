package com.seniorproject.design;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateAcctActivity extends AppCompatActivity {

    EditText EmailEditText, PasswordEditText, ConfirmPasswordEditText;
    Button CreateAcctBtn;
    ProgressBar ProgressBar;
    TextView LoginBtnTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acct);

        EmailEditText = findViewById(R.id.emailEditText);
        PasswordEditText = findViewById(R.id.passwordEditText);
        ConfirmPasswordEditText = findViewById(R.id.confPasswordEditText);
        ProgressBar = findViewById(R.id.progressbar);
        CreateAcctBtn = findViewById(R.id.createAcctBtn);
        LoginBtnTextView = findViewById(R.id.logintextbutton);

        CreateAcctBtn.setOnClickListener(v-> createAcct());
        LoginBtnTextView.setOnClickListener((v)->startActivity(new Intent(CreateAcctActivity.this,LoginActivity.class)) );


    }

    void createAcct(){
        String  email = EmailEditText.getText().toString();
        String  password = PasswordEditText.getText().toString();
        String  confirmPassword = ConfirmPasswordEditText.getText().toString();

        boolean isValidated = validateData(email, password, confirmPassword);
        if(!isValidated){
            return;
        }
        createAcctInFirebase(email, password);


    }
void createAcctInFirebase(String email,String password){
        ProgressingChange(true);

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(CreateAcctActivity.this,
            new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    ProgressingChange(false);
                    if(task.isSuccessful()){

                        utility.showToast(CreateAcctActivity.this,"Account Created, Check Your Email for a link to verify your account.");
                        firebaseAuth.getCurrentUser().sendEmailVerification();
                        firebaseAuth.signOut();
                        finish();
                    }else{
                        utility.showToast(CreateAcctActivity.this,task.getException().getLocalizedMessage());

                    }
                }
            }
    );

}
void ProgressingChange(boolean inProgress){
        if(inProgress){
            ProgressBar.setVisibility(View.VISIBLE);
            CreateAcctBtn.setVisibility(View.GONE);
        }else {
            ProgressBar.setVisibility(View.GONE);
            CreateAcctBtn.setVisibility(View.VISIBLE);
        }
}
    boolean validateData(String email,String password,String confirmPassword){
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            EmailEditText.setError("Email is not valid.");
            return false;
        }
        if(password.length()<6){
            PasswordEditText.setError("Password not strong enough.");
            return false;
        }
        if(!password.equals(confirmPassword)){
            ConfirmPasswordEditText.setError("Wrong Password, Try Again.");
            return false;
        }
        return true;
    }
}