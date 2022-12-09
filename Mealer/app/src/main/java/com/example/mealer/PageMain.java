package com.example.mealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mealer.EventBus.MyUpdateCartEvent;
import com.example.mealer.adapter.MyMealAdapter;
import com.example.mealer.listener.ICartLoadListener;
import com.example.mealer.listener.IMealLoadListener;
import com.example.mealer.model.CartModel;
import com.example.mealer.model.MealModel;
import com.example.mealer.utils.SpaceItemDecoration;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PageMain extends AppCompatActivity implements IMealLoadListener, ICartLoadListener {

    //setting the cart stuff
    //@BindView(R.id.recycler_drink)
    //RecyclerView recyclerDrink;
    @BindView(R.id.pageMainLayout)
    RelativeLayout pagemainlayout;
    @BindView(R.id.badge)
    NotificationBadge badge;
    @BindView(R.id.btnCart)
    FrameLayout btnCart;
    @BindView(R.id.btnBack)
    ImageView btnBack;

    IMealLoadListener mealLoadListener;
    ICartLoadListener cartLoadListener;
    //end of the set

    FirebaseUser fUser;
    FirebaseAuth fAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();
    //
    DatabaseReference cartMealReference = database.getReference();
    //
    Button addComplaintBtn;
    Button signOutBtn;

    //meals attributes
    DatabaseReference mealsReference;
    List<Meal> meals;
    ListView mealsListView;

    SearchView mealsListViewSearch;
    ArrayAdapter<String> arrayAdapter;

    DatabaseReference mealsSearchReference;
    List<String> mealsSearch;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_main);
        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();

        init();

        //loadDrinkFromFirebase();
        countCartItem();

        initSearchWidgets();
        viewMealsList();
        //setting the meals attributes
        mealsListView = (ListView) findViewById(R.id.mealsListView);
        mealsSearch = new ArrayList<>();
        meals = new ArrayList<>();

        addComplaintBtn = findViewById(R.id.addComplaintBtn);
        addComplaintBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                addComplaint();
            }
        });

        signOutBtn = (Button) findViewById(R.id.signOutBtn);
        signOutBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                fAuth.signOut();
                finish();
                Intent intent = new Intent(PageMain.this,
                        MainActivity.class);
                startActivity(intent);
            }
        });


      mealsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Meal meal = meals.get(position);
                viewMealDetails(meal.getChefUid(), meal.getPrice(), position, meal.getID());
                return true;
            }
        });
    }

    private void init(){
        ButterKnife.bind(this);

        mealLoadListener = this;
        cartLoadListener = this;

        btnBack.setOnClickListener(v -> finish());

        btnCart.setOnClickListener(v -> startActivity(new Intent (this, CartActivity.class)));
    }

    private void viewMealDetails(String chefUid, String mealPrice, int position, String mealID){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_display_meal_details, null);
        dialogBuilder.setView(dialogView);

        final Button addToCartBtn = (Button) dialogView.findViewById(R.id.addToCartBtn);
        final TextView textPrice = (TextView) dialogView.findViewById(R.id.textPrice);
        final TextView textViewChefRating = (TextView) dialogView.findViewById(R.id.textViewChefRating);
        final TextView textViewChefName = (TextView) dialogView.findViewById(R.id.textViewChefName);

        DatabaseReference chefReference = FirebaseDatabase.getInstance().getReference().child("users").child(chefUid).child("rating");
        chefReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String chefRating = snapshot.getValue(Double.class).toString();
                    textViewChefRating.setText(chefRating);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DatabaseReference chefReference1 = FirebaseDatabase.getInstance().getReference().child("users").child(chefUid).child("name");
        chefReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String chefName = snapshot.getValue(String.class).toString();
                textViewChefName.setText(chefName);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        textPrice.setText(mealPrice+" CAD");

        dialogBuilder.setTitle("Chef's Details");

        final AlertDialog b = dialogBuilder.create();
        b.show();
                addToCartBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO add the meal to the cart (create the cart)!!!!!!
                        cartMealReference = FirebaseDatabase.getInstance().getReference().child("meals")
                                .child(chefUid).child(mealID);
                        cartMealReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Meal meal = snapshot.getValue(Meal.class);
                                addToCart(meal);
                                b.dismiss();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                        b.dismiss();
                    }
                });


    }//end of decisionMake method

    public void addToCart(Meal meal){

        DatabaseReference userCart = FirebaseDatabase.getInstance().getReference("cart").child(fUser.getUid());
        userCart.child(meal.getID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())//if the user has already item in the cart
                {
                    //just update quantity and total Price
                    CartModel cartModel = snapshot.getValue(CartModel.class);
                     Integer quantity = snapshot.child("quantity").getValue(Integer.class);
                    cartModel.setQuantity(quantity + 1);
                    Map<String, Object> updateData = new HashMap<>();
                    updateData.put("quantity", cartModel.getQuantity());
                    updateData.put("totalPrice", cartModel.getQuantity()*Float.parseFloat(meal.getPrice()));

                    userCart.child(cartMealReference.getKey())
                            .updateChildren(updateData)
                            .addOnSuccessListener(aVoid -> {
                                onCartLoadFailed("Meal added to the cart");
                                //successToaster("Meal added to the cart");
                                countCartItem();
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    failingToaster(e.getMessage());
                                }
                            });
                } else //if the item is not in the cart, add new
                {
                    CartModel cartModel = new CartModel();
                    cartModel.setChefName(meal.getChefName());
                    cartModel.setName(meal.getMealName());
                    cartModel.setPrice(meal.getPrice());
                    cartModel.setKey(meal.getID());
                    cartModel.setQuantity(1);
                    cartModel.setTotalPrice(Float.parseFloat(meal.getPrice()));
                    //CartModel cartModel = new CartModel(meal.getID(),meal.getMealName(),meal.getChefName() ,meal.getPrice(),1,Float.parseFloat(meal.getPrice()));

                    userCart.child(meal.getID())
                            .setValue(cartModel)
                            .addOnSuccessListener(aVoid -> {
                                onCartLoadFailed("Meal added to the cart");
                                //successToaster("Meal added to the cart");
                                countCartItem();
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    failingToaster(e.getMessage());
                                }
                            });

                }

                EventBus.getDefault().postSticky(new MyUpdateCartEvent());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void initSearchWidgets(){

        mealsListViewSearch = (SearchView) findViewById(R.id.mealsListViewSearch);

        mealsListViewSearch.setQueryHint("Search by meal's name or type");

        mealsListViewSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //called when the user presses enter
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            //called to search for what the client wrote
            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<Meal> filteredMeals = new ArrayList<Meal>();
                for(Meal meal : meals){
                    if((meal.getMealName().toLowerCase().contains(s.toLowerCase())) ||
                         meal.getMealType().toLowerCase().contains(s.toLowerCase())||
                          meal.getGastronomyType().toLowerCase().contains(s.toLowerCase())){
                        filteredMeals.add(meal);
                    }
                }

                MealsList adapter = new MealsList(PageMain.this, filteredMeals);
                //attaching adapter to the listview
                mealsListView.setAdapter(adapter);
                return false;
            }
        });

    }

    public void addComplaint() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_complaint, null);
        dialogBuilder.setView(dialogView);

        final EditText ComplaintChefName = (EditText) dialogView.findViewById(R.id.ComplaintChefName);
        final EditText complaintDate = (EditText) dialogView.findViewById(R.id.complaintDate);
        final EditText complaintText = (EditText) dialogView.findViewById(R.id.complaintText);
        final Button addComplaintBtn = (Button) dialogView.findViewById(R.id.addComplaintBtn);

        dialogBuilder.setTitle("Add complaint");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        addComplaintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {

                    String chefName = ComplaintChefName.getText().toString().trim();
                    String date = complaintDate.getText().toString().trim();
                    String text = complaintText.getText().toString().trim();

                    DatabaseReference complaintReference = FirebaseDatabase.getInstance().getReference().child("complaints");


                    //checking if the value is provided
                    if (!(TextUtils.isEmpty(chefName) & TextUtils.isEmpty(date) & TextUtils.isEmpty(text))) {

                        //getting a unique id using push().getKey() method
                        //it will create a unique id and we will use it as the Primary key for our Complaint
                        String id = reference.push().getKey();

                        //creating a Complaint Object
                        Complaint complaint = new Complaint(id, chefName, date, text);

                        //saving the complaint
                        complaintReference.child(id).setValue(complaint);

                        //setting edittext to blank again
                        ComplaintChefName.setText("");
                        complaintDate.setText("");
                        complaintText.setText("");

                        //displaying a success toast
                        successToaster("Complaint added");
                        b.dismiss();
                    } else {
                        //if the values are not given displaying a toast
                        failingToaster("Make sure everything is filled");
                        b.dismiss();

                    }
                }
            }
        });
    }

    public void successToaster(String msg) {
        Snackbar.make(pagemainlayout, msg, Snackbar.LENGTH_LONG).show();
        //Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    public void failingToaster(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    public void onStart() {
        super.onStart();
        countCartItem();
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
        countCartItem();
    }

    public void viewMealsList() {

        mealsReference = FirebaseDatabase.getInstance().getReference().child("meals");
        mealsReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SuspiciousIndentation")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clearing the previous artist list
                meals.clear();

                //listening through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if (postSnapshot.exists()){
                    for(DataSnapshot ds : postSnapshot.getChildren()) {
                        if ( ds.child("display").getValue(Boolean.class)  ) {
                            Meal meal = ds.getValue(Meal.class);
                            //adding product to the list
                            meals.add(meal);
                        }
                    }
                    }
                    else
                    mealLoadListener.onMealLoadFailed("Cannot find these meals");
                }
                //creating adapter
                MealsList mealsAdapter = new MealsList(PageMain.this, meals);
                //attaching adapter to the listview
                mealsListView.setAdapter(mealsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mealLoadListener.onMealLoadFailed(error.getMessage());
            }
        });
    }

    @Override
    public void onMealLoadSuccess(List<Meal> mealModelList) {
        //MyUpdateCartEvent adapter = new  MyUpdateCartEvent(this, mealModelList);
        //mealsListView.setAdapter((ListAdapter) adapter);
    }

    @Override
    public void onMealLoadFailed(String message) {
        Snackbar.make(pagemainlayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onCartLoadSuccess(List<CartModel> cartModelList) {
        int cartSum=0;
        for(CartModel cartModel : cartModelList){
            cartSum += cartModel.getQuantity();
            badge.setNumber(cartSum);
        }
    }

    @Override
    public void onCartLoadFailed(String message) {
        Snackbar.make(pagemainlayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        countCartItem();
    }

    private void countCartItem() {
        List<CartModel> cartModels = new ArrayList<>();

        DatabaseReference userCartFinal = FirebaseDatabase.getInstance().getReference().child("cart").child(fUser.getUid());
        userCartFinal.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot cartSnapshot : snapshot.getChildren()){
                    CartModel cartModel = cartSnapshot.getValue(CartModel.class);
                    cartModel.setKey(cartModel.getKey());
                    cartModels.add(cartModel);
                }
                onCartLoadSuccess(cartModels);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
