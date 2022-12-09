package com.example.mealer.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealer.EventBus.MyUpdateCartEvent;
import com.example.mealer.PageMain;
import com.example.mealer.R;
import com.example.mealer.model.CartModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.MyCartViewHolder> {

    FirebaseUser fUser;
    FirebaseAuth fAuth;
    private Context context;
    private List<CartModel> cartModelList;

    public MyCartAdapter(Context context, List<CartModel> cartModelList) {
        this.context = context;
        this.cartModelList = cartModelList;
    }

    @NonNull
    @Override
    public MyCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyCartViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.layout_cart_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyCartViewHolder holder, int position) {
        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();

        DatabaseReference userCart = FirebaseDatabase.getInstance().getReference("cart").child(fUser.getUid());
        userCart.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                CartModel cartModel = snapshot.getValue(CartModel.class);
                //String price = snapshot.child(cartModel.getKey()).child("price").getValue(String.class);
                //cartModel.setTotalPrice(cartModel.getQuantity()*Float.parseFloat(price));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.txtPrice.setText(new StringBuilder("CAD ").append(cartModelList.get(position).getPrice()));
       // holder.txtName.setText(new StringBuilder().append(cartModelList.get(position).getName()));
        holder.txtQuantity.setText(new StringBuilder().append(cartModelList.get(position).getQuantity()));
        if(!cartModelList.get(position).isStatus())
            holder.txtName.setText(new StringBuilder().append(cartModelList.get(position).getName()+"(not approved yet)"));
        else
            holder.txtName.setText(new StringBuilder().append(cartModelList.get(position).getName()+"(approved)"));
        //Event
        holder.btnMinus.setOnClickListener(v -> {
            minusCartItem(holder, cartModelList.get(position));
        });
        holder.btnPlus.setOnClickListener(v -> {
            plusCartItem(holder, cartModelList.get(position), cartModelList.get(position).getPrice());
        });

        holder.btnDelete.setOnClickListener(V ->{
                    AlertDialog dialog = new AlertDialog.Builder(context)
                            .setTitle("Delete item")
                            .setMessage("Do you really want to delete the item")
                            .setNegativeButton("CANCEL", (dialog1, which) -> dialog1.dismiss())
                            .setPositiveButton("OK", (dialog12, which) -> {

                                //Temp remove
                                notifyItemRemoved(position);

                                deleteFromFirebase(cartModelList.get(position));
                                dialog12.dismiss();
                            }).create();
                    dialog.show();
                });
    }

    private void deleteFromFirebase(CartModel cartModel) {
        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();

        FirebaseDatabase.getInstance()
                .getReference("cart")
                .child(fUser.getUid())
                .child(cartModel.getKey())
                .removeValue()
                .addOnSuccessListener(aVoid -> EventBus.getDefault().postSticky(new MyUpdateCartEvent()));
    }

    private void plusCartItem(MyCartViewHolder holder, CartModel cartModel, String price) {

            cartModel.setQuantity(cartModel.getQuantity() + 1);
            cartModel.setTotalPrice(cartModel.getQuantity()*Float.parseFloat(price));
            //Update quantity
            holder.txtQuantity.setText(new StringBuilder().append(cartModel.getQuantity()));
            updateFirebase(cartModel);

    }

    private void minusCartItem(MyCartViewHolder holder, CartModel cartModel) {
        if (cartModel.getQuantity() > 1){
            cartModel.setQuantity(cartModel.getQuantity() - 1);
            cartModel.setTotalPrice(cartModel.getQuantity()*Float.parseFloat(cartModel.getPrice()));

            //Update quantity
            holder.txtQuantity.setText(new StringBuilder().append(cartModel.getQuantity()));
            updateFirebase(cartModel);
        }
    }

    private void updateFirebase(CartModel cartModel) {
        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();

        FirebaseDatabase.getInstance()
                .getReference("cart")
                .child(fUser.getUid())
                .child(cartModel.getKey())
                .setValue(cartModel)
                .addOnSuccessListener(aVoid -> EventBus.getDefault().postSticky(new MyUpdateCartEvent()));
    }

    @Override
    public int getItemCount() {
        return cartModelList.size();
    }

    public class MyCartViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.btnMinus)
        ImageView btnMinus;
        @BindView(R.id.btnPlus)
        ImageView btnPlus;
        @BindView(R.id.btnDelete)
        ImageView btnDelete;
        @BindView(R.id.txtName)
        TextView txtName;
        @BindView(R.id.txtPrice)
        TextView txtPrice;
        @BindView(R.id.txtQuantity)
        TextView txtQuantity;

        Unbinder unbinder;
        public MyCartViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);

        }
    }
}
