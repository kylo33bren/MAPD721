package com.test.recipeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class activity_nutrition_detail extends AppCompatActivity {


    TextView txt_calories,txt_protein,txt_fats,txt_carbs;
    private RecyclerView good_nutrition_recyclerview, bad_nutrition_recyclerview;
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_detail);
        this.setTitle("Nutrition Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        txt_calories=findViewById(R.id.txt_calories);
        txt_protein=findViewById(R.id.txt_protein);
        txt_fats=findViewById(R.id.txt_fats);
        txt_carbs=findViewById(R.id.txt_carbs);
        final Intent intent = getIntent();

        final String recipeId = Objects.requireNonNull(intent.getExtras()).getString("id");

        good_nutrition_recyclerview = findViewById(R.id.good_nutritions_recyclerview);
        good_nutrition_recyclerview.setLayoutManager(new GridLayoutManager(activity_nutrition_detail.this, 1));
        bad_nutrition_recyclerview = findViewById(R.id.bad_nutritions_recyclerview);
        bad_nutrition_recyclerview.setLayoutManager(new GridLayoutManager(activity_nutrition_detail.this, 1));


        getNutritionData(recipeId);

    }

    private void getNutritionData(final String recipeId) {
        Helper.showLoader(activity_nutrition_detail.this,"Please wait we are fetching nutrition details . . .");
        String URL = " https://api.spoonacular.com/recipes/" + recipeId + "/nutritionWidget.json?apiKey=c5d9a0a4a69148378098010e59624a37";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    Helper.stopLoader();
                    String calories  =(String) response.get("calories");
                    String protein  =(String) response.get("protein");
                    String fat  =(String) response.get("fat");
                    String carbs  =(String) response.get("carbs");

                    txt_calories.setText(calories);
                    txt_protein.setText(protein);
                    txt_fats.setText(fat);
                    txt_carbs.setText(carbs);


                  ///  Toast.makeText(activity_nutrition_detail.this, calories, Toast.LENGTH_SHORT).show();

                    ArrayList<Nutritions> good_list=new ArrayList<>();
                    ArrayList<Nutritions> bad_list=new ArrayList<>();

                    JSONArray jsonArrayGood = (JSONArray) response.get("good");
                    for (int i = 0; i < jsonArrayGood.length(); i++) {
                        JSONObject   jsonObject1 = jsonArrayGood.getJSONObject(i);
                        good_list.add(new Nutritions(jsonObject1.optString("title"), jsonObject1.optString("amount"), jsonObject1.optString("indented"), jsonObject1.optString("percentOfDailyNeeds")));
                    }

                    if(good_list.size()>0){
                        viewholder_nutritions myAdapter = new viewholder_nutritions(activity_nutrition_detail.this, good_list);
                        good_nutrition_recyclerview.setAdapter(myAdapter);
                        good_nutrition_recyclerview.setItemAnimator(new DefaultItemAnimator());

                    }

                    JSONArray jsonArrayBad = (JSONArray) response.get("bad");
                    for (int i = 0; i < jsonArrayBad.length(); i++) {
                        JSONObject   jsonObject1 = jsonArrayBad.getJSONObject(i);
                        bad_list.add(new Nutritions(jsonObject1.optString("title"), jsonObject1.optString("amount"), jsonObject1.optString("indented"), jsonObject1.optString("percentOfDailyNeeds")));
                    }
                    if(bad_list.size()>0){
                        viewholder_nutritions myAdapter = new viewholder_nutritions(activity_nutrition_detail.this, bad_list);
                        bad_nutrition_recyclerview.setAdapter(myAdapter);
                        bad_nutrition_recyclerview.setItemAnimator(new DefaultItemAnimator());

                    }

                  //  Toast.makeText(activity_nutrition_detail.this, ""+list.size(), Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){

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