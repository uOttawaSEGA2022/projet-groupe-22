package com.example.mealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mealer.EventBus.MyUpdateCartEvent;
import com.example.mealer.adapter.MyCartAdapter;
import com.example.mealer.listener.ICartLoadListener;
import com.example.mealer.model.CartModel;
import com.example.mealer.utils.SpaceItemDecoration;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindAnim;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CartActivity extends AppCompatActivity implements ICartLoadListener {

    @BindView(R.id.recycler_drink)
    RecyclerView recyclerDrink;
    @BindView(R.id.cartActivity)
    RelativeLayout cartLayout;
    @BindView(R.id.btnBack)
    ImageView btnBack;
    @BindView(R.id.txtTotal)
    TextView txtTotal;

    ICartLoadListener cartLoadListener;

    FirebaseUser fUser;
    FirebaseAuth fAuth;

    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop(){
        if(EventBus.getDefault().hasSubscriberForEvent(MyUpdateCartEvent.class))
            EventBus.getDefault().removeStickyEvent(MyUpdateCartEvent.class);
        EventBus.getDefault().unregister(this);

        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onUpdateCart(MyUpdateCartEvent event){
        loadCartFromFirebase();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();

        init();
        loadCartFromFirebase();
    }

    private void loadCartFromFirebase() {

        List<CartModel> cartModels = new ArrayList<>();
        FirebaseDatabase.getInstance()
                .getReference("cart")
                .child(fUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("SuspiciousIndentation")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for(DataSnapshot cartSnapshot: snapshot.getChildren()){
                                CartModel cartModel = cartSnapshot.getValue(CartModel.class);
                                cartModel.setKey(cartSnapshot.getKey());
                                cartModels.add(cartModel);

                            }
                            onCartLoadSuccess(cartModels);
                        }
                        else
                        onCartLoadFailed("Cart empty");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    onCartLoadFailed(error.getMessage());
                    }
                });
    }

    private void init(){
        ButterKnife.bind(this);
        cartLoadListener = this;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerDrink.setLayoutManager(layoutManager);
        recyclerDrink.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));

        btnBack.setOnClickListener(v -> finish());
    }

    @Override
    public void onCartLoadSuccess(List<CartModel> cartModelList) {
        double sum = 0;
        for(CartModel cartModel : cartModelList){
            sum += cartModel.getTotalPrice();

        }
        txtTotal.setText(new StringBuilder("CAD").append(sum));
        MyCartAdapter adapter  = new MyCartAdapter(this, cartModelList);
        recyclerDrink.setAdapter(adapter);
    }

    @Override
    public void onCartLoadFailed(String message) {
        Snackbar.make(cartLayout, message, Snackbar.LENGTH_LONG).show();
    }
}