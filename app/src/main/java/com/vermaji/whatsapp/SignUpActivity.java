package com.vermaji.whatsapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.vermaji.whatsapp.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {
     ActivitySignUpBinding binding;
     private FirebaseAuth auth;
     private FirebaseDatabase database;
     private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        dialog = new ProgressDialog(SignUpActivity.this);
        dialog.setTitle("Creating account");
        dialog.setMessage("We are creating your account");

        auth = FirebaseAuth.getInstance();
        database  = FirebaseDatabase.getInstance();

        binding.idBtnSignUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dialog.show();
                auth.createUserWithEmailAndPassword(
                        binding.idEmail.getText().toString(),binding.idPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        dialog.dismiss();
                        if (task.isSuccessful()){
                            Users user = new Users(binding.idUserName.getText().toString(),
                                    binding.idEmail.getText().toString(),binding.idPassword.getText().toString());
                            String id = task.getResult().getUser().getUid();
                            database.getReference().child("Users").child(id).setValue(user);
                            Toast.makeText(SignUpActivity.this,"User created successfull",Toast.LENGTH_SHORT).show();
                        }else
                            Toast.makeText(SignUpActivity.this, task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        binding.idTextHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this,SignInActivity.class);
                startActivity(intent);
            }
        });


    }
}