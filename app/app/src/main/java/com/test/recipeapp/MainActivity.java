package com.test.recipeapp;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private SliderLayout sliderLayout;
    private List<Recipe> lstRecipe = new ArrayList<>();
    private List<Recipe> searchRecipe;
    private JSONArray jsonArray;
    SlidingRootNav slidingRootNav;
    TextView txt_name,txt_recipe_title;
    ArrayList<CategoryRecipeModel> sliderRecipeList;
    private RecyclerView myrv;
    private ProgressBar progressBar;
    private TextView  emptyView;
    ImageView btn_search;
    TextInputLayout txt_input;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        sliderLayout = findViewById(R.id.slider);
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(3333);
        txt_recipe_title=findViewById(R.id.recipe_title_id);
        slidingRootNav = new SlidingRootNavBuilder(this).withMenuLayout(R.layout.drawer).withMenuOpened(false).inject();
        progressBar = findViewById(R.id.progressbar2);
        emptyView= findViewById(R.id.empty_view2);
        myrv = findViewById(R.id.recyclerview);
        txt_input=findViewById(R.id.txt_input);

        btn_search=findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                } catch (Exception e) {
                }
                if(!txt_input.getEditText().getText().toString().toString().equals("")) {
                    progressBar.setVisibility(View.VISIBLE);
                    myrv.setAlpha(0);
                    searchRecipe(txt_input.getEditText().getText().toString());
                    txt_recipe_title.setText(txt_input.getEditText().getText().toString());
                }
                else
                    Toast.makeText(MainActivity.this, "Type something...", Toast.LENGTH_LONG).show();
            }
        });
        myrv.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
        findViewById(R.id.iv_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slidingRootNav.openMenu();
            }
        });

        findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slidingRootNav.closeMenu();
            }
        });


        findViewById(R.id.tv_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(MainActivity.this, activity_login.class));
                FirebaseAuth.getInstance().signOut();

            }
        });
        findViewById(R.id.btn_fav).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, activity_favorite_recipes.class));

            }
        });
        txt_name=  findViewById(R.id.txt_name);
        txt_name.setText(Helper.GetData(MainActivity.this,"name"));
        sliderRecipeList=new ArrayList<>();
        sliderRecipeList.add(new CategoryRecipeModel("Starter",R.drawable.starter_img));
        sliderRecipeList.add(new CategoryRecipeModel("Breakfast",R.drawable.breakfast_image));
        sliderRecipeList.add(new CategoryRecipeModel("Soup",R.drawable.soup_img));


        for (CategoryRecipeModel recipeModel:sliderRecipeList) {

            TextSliderView textSliderView = new TextSliderView(MainActivity.this);
            // initialize a SliderLayout
            textSliderView
                    .description(recipeModel.getCat_name())
                    .image(recipeModel.getImage())
                    .setScaleType(BaseSliderView.ScaleType.Fit);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", recipeModel.getCat_name());


            textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(BaseSliderView slider) {
                    txt_recipe_title.setText(slider.getDescription());

                    searchRecipe(slider.getDescription().toLowerCase(Locale.ROOT));
                    /** open detail page activity and pass the clicked recipe object in the intent */
                   /* Intent it=new Intent(MainActivity.this, RecipeDetailsActivity.class);
                    it.putExtra("model",recipeModel);
                    startActivity(it);*/
                }
            });

            sliderLayout.addSlider(textSliderView);

        }

        getRandomRecipes();

        }

    private void getRandomRecipes() {
        txt_recipe_title.setText("Random Recipes");
        String URL = " https://api.spoonacular.com/recipes/random?number=30&instructionsRequired=true&apiKey=c5d9a0a4a69148378098010e59624a37";
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            jsonArray = (JSONArray) response.get("recipes");
                            Log.i("the res is:", String.valueOf(jsonArray));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1;
                                jsonObject1 = jsonArray.getJSONObject(i);
                                lstRecipe.add(new Recipe(jsonObject1.optString("id"),jsonObject1.optString("title"), jsonObject1.optString("image"), Integer.parseInt(jsonObject1.optString("servings")), Integer.parseInt(jsonObject1.optString("readyInMinutes"))));
                            }
                            progressBar.setVisibility(View.GONE);
                            viewholder_recipes myAdapter = new viewholder_recipes(MainActivity.this, lstRecipe);
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
                        progressBar.setVisibility(View.GONE);
                        myrv.setAlpha(0);
                        emptyView.setVisibility(View.VISIBLE);
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }
    private void searchRecipe(String search) {
        searchRecipe = new ArrayList<Recipe>();
        String URL="https://api.spoonacular.com/recipes/search?query=" + search + "&number=30&instructionsRequired=true&apiKey=c5d9a0a4a69148378098010e59624a37";
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            jsonArray = (JSONArray) response.get("results");
                            Log.i("the search res is:", String.valueOf(jsonArray));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1;
                                jsonObject1 = jsonArray.getJSONObject(i);
                                searchRecipe.add(new Recipe(jsonObject1.optString("id"),jsonObject1.optString("title"), "https://spoonacular.com/recipeImages/" + jsonObject1.optString("image"), Integer.parseInt(jsonObject1.optString("servings")), Integer.parseInt(jsonObject1.optString("readyInMinutes"))));
                            }
                            progressBar.setVisibility(View.GONE);
                            if(searchRecipe.isEmpty()){
                                myrv.setAlpha(0);
                                emptyView.setVisibility(View.VISIBLE);
                            }
                            else{
                                emptyView.setVisibility(View.GONE);
                                viewholder_recipes myAdapter = new viewholder_recipes(MainActivity.this, searchRecipe);
                                myrv.setAdapter(myAdapter);
                                myrv.setItemAnimator(new DefaultItemAnimator());
                                myrv.setAlpha(1);
                            }
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