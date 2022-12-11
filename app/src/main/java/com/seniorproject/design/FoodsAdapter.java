package com.seniorproject.design;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class FoodsAdapter extends FirestoreRecyclerAdapter<Foods, FoodsAdapter.FoodsViewHolder> {
    Context context;

    public FoodsAdapter(@NonNull FirestoreRecyclerOptions<Foods> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull FoodsViewHolder holder, int position, @NonNull Foods foods) {
        holder.ExpDateView.setText(foods.ExpDate);
        holder.itemNameView.setText(foods.foodName);
        holder.itemTypeView.setText(foods.foodType);

        holder.itemView.setOnClickListener((v)->{
            Intent intent = new Intent(context,FoodDetailsActivity.class);
            intent.putExtra("FoodName", foods.foodName);
            intent.putExtra("Exp Date", foods.ExpDate);
            String docID = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("Food Type", foods.foodType);
            intent.putExtra("docID",docID);

            context.startActivity(intent);

        });
    }

    @NonNull
    @Override
    public FoodsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_food_item,parent,false);
        return new FoodsViewHolder(view );
    }

    class FoodsViewHolder  extends RecyclerView.ViewHolder{

        TextView itemNameView, ExpDateView, itemTypeView;

        public FoodsViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNameView = itemView.findViewById(R.id.ItemNameView);
            ExpDateView = itemView.findViewById(R.id.ExpDateView);
            itemTypeView = itemView.findViewById(R.id.itemTypeView);

        }
    }
}
