
package com.test.recipeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class activity_recipe_detail extends AppCompatActivity {

    private TextView title, ready_in, servings, healthy, instructions;
    private ImageView img;
    private DatabaseReference mRootRef;
    private FirebaseAuth mAuth;
    private JSONArray jsonArray;
    private List<Ingredient> ingredientsLst = new ArrayList<Ingredient>();
    private RecyclerView myrv;
    private FloatingActionButton fab;
    private boolean like = false;
    Button btn_nutrition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_recipe);
        getSupportActionBar().hide();
        final Intent intent = getIntent();
        final String recipeId = Objects.requireNonNull(intent.getExtras()).getString("id");
        mAuth = FirebaseAuth.getInstance();
        final String uid = mAuth.getCurrentUser().getUid();
        mRootRef = FirebaseDatabase.getInstance().getReference().child("Favorites").child(uid).child(recipeId);
        img = findViewById(R.id.recipe_img);
        title = findViewById(R.id.recipe_title);
        ready_in = findViewById(R.id.recipe_ready_in);
        servings = findViewById(R.id.recipe_servings);
        healthy = findViewById(R.id.recipe_healthy);
        instructions = findViewById(R.id.recipe_instructions);
        fab = findViewById(R.id.floatingActionButton);
        getRecipeData(recipeId);
        btn_nutrition=findViewById(R.id.btn_nutritions);
        btn_nutrition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_recipe_detail.this,activity_nutrition_detail.class);
                intent.putExtra("id",recipeId);
                startActivity(intent);
            }
        });
        mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i("mRootRef", String.valueOf(dataSnapshot));
                if (dataSnapshot.getValue() != null) {
                    fab.setImageResource(R.drawable.ic_favorite_black_24dp);
                    like = true;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                like = !like;
                mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (like) {
                            fab.setImageResource(R.drawable.ic_favorite_black_24dp);
                            Map favorites = new HashMap();
                            favorites.put("img", intent.getExtras().getString("img"));
                            favorites.put("title", intent.getExtras().getString("title"));
                            mRootRef.setValue(favorites);
                        } else {
                            try {
                                fab.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                                mRootRef.setValue(null);
                            } catch (Exception e) {
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });
        myrv = (RecyclerView)findViewById(R.id.recipe_ingredients_rv);
        myrv.setHasFixedSize(true);
        myrv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void getRecipeData(final String recipeId) {
        Helper.showLoader(activity_recipe_detail.this,"Please wait we are fetching recipe details . . .");
        String URL = " https://api.spoonacular.com/recipes/" + recipeId + "/information?apiKey=c5d9a0a4a69148378098010e59624a37";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Helper.stopLoader();

                            try {
                                Picasso.with(activity_recipe_detail.this).load((String) response.get("image")).into(img);
                            }
                            catch (Exception e){
                                img.setImageResource(R.drawable.ic_launcher_background);
                            }
                            title.setText((String) response.get("title"));
                            ready_in.setText(Integer.toString((Integer) response.get("readyInMinutes")));
                            servings.setText(Integer.toString((Integer) response.get("servings")));
                            if ((boolean) response.get("veryHealthy")) {
                                healthy.setText("Healthy");
                            }
                            if ((boolean) response.get("vegetarian")) {
                            }
                            try{
                                if(response.get("instructions").equals("")){
                                    throw new Exception("No Instructions");
                                }
                                else
                                    instructions.setText(Html.fromHtml((String) response.get("instructions")));
                            }
                            catch(Exception e){
                                String msg= "Unfortunately, the recipe you were looking for not found, to view the original recipe click on the link below:" + "<a href="+response.get("spoonacularSourceUrl")+">"+response.get("spoonacularSourceUrl")+"</a>";
                                instructions.setMovementMethod(LinkMovementMethod.getInstance());
                                instructions.setText(Html.fromHtml(msg));
                            }
                            jsonArray = (JSONArray) response.get("extendedIngredients");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1;
                                jsonObject1 = jsonArray.getJSONObject(i);
                                ingredientsLst.add(new Ingredient(jsonObject1.optString("name"), jsonObject1.optString("image")));
                            }
                            viewholder_recipe_ingredient myAdapter = new viewholder_recipe_ingredient(getApplicationContext(), ingredientsLst);
                            myrv.setAdapter(myAdapter);
                            myrv.setItemAnimator(new DefaultItemAnimator());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("the res is error:", error.toString());
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }
}