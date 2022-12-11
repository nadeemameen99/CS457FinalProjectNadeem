package com.seniorproject.design;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class utility {
    static void showToast(Context context, String message){

        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();

    }
    static CollectionReference getCollectionReferenceForFoodItems(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
         return FirebaseFirestore.getInstance().collection("foods").document(currentUser.getUid()).collection("FoodItems");
    }

}
