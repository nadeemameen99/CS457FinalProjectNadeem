package com.seniorproject.design;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {
    //GoogleSignInOptions gso;
    //GoogleSignInClient gsc;



    RecyclerView recyclerView;


    ImageButton themenuBtn;

    FloatingActionButton addFoodBtn;

    FoodsAdapter foodsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = findViewById(R.id.recyclerView);
        themenuBtn = findViewById(R.id.menuBtn);
        addFoodBtn = findViewById(R.id.addFoodBtn);

        


        addFoodBtn.setOnClickListener((v)-> startActivity(new Intent(MainActivity.this,FoodDetailsActivity.class)));
        setupRecyclerView();

/*
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct!= null){
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            name.setText(personName);
            email.setText(personEmail);

        } */

        themenuBtn.setOnClickListener((v)->showMenu() );

    }

    void showMenu(){
        //This will display a menu with logout options for now

        PopupMenu popupMenu = new PopupMenu(MainActivity.this,themenuBtn);
        popupMenu.getMenu().add("Logout");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getTitle()=="Logout"){
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    finish();
                    return true;
                }
                return false;
            }
        });

    }

    void setupRecyclerView(){
        Query query = utility.getCollectionReferenceForFoodItems();
        FirestoreRecyclerOptions<Foods> options = new FirestoreRecyclerOptions.Builder<Foods>()
                .setQuery(query, Foods.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        foodsAdapter = new FoodsAdapter(options, this);
        recyclerView.setAdapter(foodsAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        foodsAdapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        foodsAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        foodsAdapter.notifyDataSetChanged();
    }
    /* void signOut(){
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                finish();
                startActivity(new Intent(MainActivity.this,MainActivity.class));
            }
        });
    } */
}