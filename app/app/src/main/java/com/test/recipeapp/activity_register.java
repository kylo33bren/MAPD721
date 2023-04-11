package com.test.recipeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class activity_register extends AppCompatActivity {
    Button btn_signup;
    TextInputLayout name,email,password,cpassword;
    FirebaseAuth auth;
    DatabaseReference db_ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();
        db_ref = FirebaseDatabase.getInstance().getReference().child("Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        name = (TextInputLayout) findViewById(R.id.txt_name);
        email = (TextInputLayout) findViewById(R.id.txt_email);
        password = (TextInputLayout) findViewById(R.id.txt_password);
        cpassword = (TextInputLayout) findViewById(R.id.txt_cpassword);
        btn_signup=findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getEditText().getText().toString().equals("")) {
                    Toast.makeText(activity_register.this, "Please Enter Name", Toast.LENGTH_SHORT).show();

                }
               else if (email.getEditText().getText().toString().equals("")) {
                    Toast.makeText(activity_register.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                }
                else if (!Helper.isEmailValid(email.getEditText().getText().toString())) {
                    Toast.makeText(activity_register.this, "Please Enter Valid Email", Toast.LENGTH_SHORT).show();
                }

               else if (password.getEditText().getText().toString().equals("")) {
                    Toast.makeText(activity_register.this, "Please Enter Password", Toast.LENGTH_SHORT).show();

                }

                else if (cpassword.getEditText().getText().toString().equals("")) {
                    Toast.makeText(activity_register.this, "Please Enter Confirm Password", Toast.LENGTH_SHORT).show();

                }
               else {
                    Helper.showLoader(activity_register.this,"Please wait . . .");
                    auth.createUserWithEmailAndPassword(email.getEditText().getText().toString(),password.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {


                            if (task.isSuccessful()) {


                                String user_id = auth.getCurrentUser().getUid();
                                Users upload = new Users(user_id,name.getEditText().getText().toString(),email.getEditText().getText().toString());

                                db_ref.child(db_ref.push().getKey()).setValue(upload).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Helper.stopLoader();
                                            finish();
                                            Toast.makeText(getApplicationContext(), "Successfully registered!", Toast.LENGTH_LONG).show();


                                            finish();

                                        }
                                        else{
                                            Helper.stopLoader();
                                            Toast.makeText(activity_register.this,task.getException().toString(),Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });


                            } else {
                                Helper.stopLoader();
                                Toast.makeText(activity_register.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }



            }
        });

    }
    public void refresh(){
        name.getEditText().setText("");
        email.getEditText().setText("");
        cpassword.getEditText().setText("");
        password.getEditText().setText("");
        name.requestFocus();
    }

    @Override
    public void onBackPressed()
    {
        finish();
        startActivity(new Intent(activity_register.this,activity_login.class));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                finish();
                return true;

            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}