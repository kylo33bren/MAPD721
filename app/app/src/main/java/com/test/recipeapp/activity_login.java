package com.test.recipeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class activity_login extends AppCompatActivity implements View.OnClickListener {

    TextInputLayout email,password;
    Button btn_signin, btn_register;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = auth.getCurrentUser();
        if(mFirebaseUser != null) {
            startActivity(new Intent(activity_login.this,MainActivity.class));
            finish();
        }


        email = (TextInputLayout) findViewById(R.id.txt_email);
        password = (TextInputLayout) findViewById(R.id.txt_password);
        btn_signin = findViewById(R.id.btn_signin);
        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);
        btn_signin.setOnClickListener(this);
        email.getEditText().setText("");
        password.getEditText().setText("");

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_signin:

                if (email.getEditText().getText().toString().equals("")) {
                    email.getEditText().setError("Please enter email");
                    return;

                }
                else if (password.getEditText().getText().toString().equals("")) {
                    password.getEditText().setError("Please enter password");
                    return;
                }
                else{

                    Helper.showLoader(activity_login.this,"Please wait . . .");
                    auth.signInWithEmailAndPassword(email.getEditText().getText().toString(), password.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                fetch_profile(email.getEditText().getText().toString());
                            } else {
                                Helper.stopLoader();
                                Toast.makeText(activity_login.this, task.getException().toString(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });


                }

                break;

            case R.id.btn_register:
                 startActivity(new Intent(activity_login.this, activity_register.class));

                break;

            default:
                break;
        }
    }


    public void fetch_profile(String email){

       DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        Query query = databaseReference.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.getValue() != null) {


                    Helper.stopLoader();
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {

                        finish();
                        Helper.PutData(activity_login.this,"name",childSnapshot.child("name").getValue(String.class));
                        startActivity(new Intent(activity_login.this,MainActivity.class));

                    }
                }
                else{
                    Toast.makeText(activity_login.this,"Not found",Toast.LENGTH_LONG).show();


                   Helper.stopLoader();
                }





            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
               Helper.stopLoader();
            }
        });
    }



}