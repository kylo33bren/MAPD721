package com.test.recipeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class activity_favorite_recipes extends AppCompatActivity {
    private List<Recipe> lstFavorites;
    private RecyclerView recyclerView;
    private DatabaseReference db_ref;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private TextView emptyView;
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_recipes);
        this.setTitle("Favorite Recipes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        progressBar =findViewById(R.id.progressbar);
        emptyView= findViewById(R.id.empty_view);
        auth = FirebaseAuth.getInstance();

        show_favorites();
    }

    private void show_favorites() {
        Helper.showLoader(activity_favorite_recipes.this,"Please wait we are fetching favorite recepies . . .");
        String uid =   auth.getCurrentUser().getUid();

        db_ref = FirebaseDatabase.getInstance().getReference().child("Favorites").child(uid);
        db_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Helper.stopLoader();
                lstFavorites = new ArrayList<>();
                HashMap favorites = (HashMap) dataSnapshot.getValue();
                if (favorites != null) {
                    for (Object recipe : favorites.keySet()) {
                        String title = (String) dataSnapshot.child(recipe.toString()).child("title").getValue();
                        String img = (String) dataSnapshot.child(recipe.toString()).child("img").getValue();
                        lstFavorites.add(new Recipe(recipe.toString(), title, img, 0, 0));
                    }
                }
                progressBar.setVisibility(View.GONE);
                recyclerView =findViewById(R.id.recycleview_favorites);
                if(lstFavorites.isEmpty()){
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }
                else{
                    recyclerView.setLayoutManager(new GridLayoutManager(activity_favorite_recipes.this, 1));
                    viewholder_favorites myAdapter = new viewholder_favorites(activity_favorite_recipes.this, lstFavorites);
                    recyclerView.setAdapter(myAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}