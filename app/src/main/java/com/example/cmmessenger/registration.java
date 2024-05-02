package com.example.cmmessenger;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cmmessenger.databinding.ActivityRegistrationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class registration extends AppCompatActivity {
    ActivityRegistrationBinding binding;
    FirebaseAuth auth;
    Uri imageURI;
    String imageuri;
    String emailPattern="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FirebaseDatabase database;
    FirebaseStorage storage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRegistrationBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();
        auth=FirebaseAuth.getInstance();

        binding.loginbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(registration.this,login.class);
                startActivity(intent);
                finish();
            }
        });

         binding.rgsignup.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 String namee=binding.rgUsername.getText().toString();
                 String emaill=binding.rgemail.getText().toString();
                 String Password=binding.rgpassword.getText().toString();
                 String cPassword=binding.rgrepassword.getText().toString();
                 String status="Hey I'm Using this Application";

                 if(TextUtils.isEmpty(namee)|| TextUtils.isEmpty(emaill)||TextUtils.isEmpty(Password)||TextUtils.isEmpty(cPassword)){
                     Toast.makeText(registration.this, "Please Enter Valid Information", Toast.LENGTH_SHORT).show();
                 } else if (!emaill.matches(emailPattern)) {
                     binding.rgemail.setError("Invalid email");
                 } else if (Password.length()<=6) {
                     binding.rgpassword.setError("Min length for password should be 6");
                 } else if (!Password.equals(cPassword)) {
                     binding.rgpassword.setError("Password Doesn't Match");
                 }else {
                     auth.createUserWithEmailAndPassword(emaill,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                         @Override
                         public void onComplete(@NonNull Task<AuthResult> task) {
                             if (task.isSuccessful()){
                                 String id=task.getResult().getUser().getUid();
                                 DatabaseReference reference=database.getReference().child("user").child(id);
                                 StorageReference storageReference=storage.getReference().child("Uplode").child(id);

                                 if(imageURI!=null){
                                     storageReference.putFile(imageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                         @Override
                                         public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                           if(task.isSuccessful()){
                                               storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                   @Override
                                                   public void onSuccess(Uri uri) {
                                                       imageuri=uri.toString();
                                                       Users users=new Users(id,namee,emaill,Password,imageuri,status);
                                                       reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                           @Override
                                                           public void onComplete(@NonNull Task<Void> task) {
                                                               if(task.isSuccessful()){
                                                                   Intent intent=new Intent(registration.this, MainActivity.class);
                                                                   startActivity(intent);
                                                                   finish();
                                                               }else {
                                                                   Toast.makeText(registration.this, "Error in creating the User", Toast.LENGTH_SHORT).show();
                                                               }
                                                           }
                                                       });
                                                   }
                                               });
                                           }
                                         }
                                     });
                                 }else {
                                     String status="Hey I'm Using this Application";
                                     imageuri="https://firebasestorage.googleapis.com/v0/b/cm-messenger-a54ab.appspot.com/o/coleccion.png?alt=media&token=b6ad79ba-3f6f-47af-9e76-c553872aa7a2";
                                     Users users=new Users(id,namee,emaill,Password,imageuri,status);
                                     reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                         @Override
                                         public void onComplete(@NonNull Task<Void> task) {
                                             if(task.isSuccessful()){
                                                 Intent intent=new Intent(registration.this, MainActivity.class);
                                                 startActivity(intent);
                                                 finish();
                                             }else {
                                                 Toast.makeText(registration.this, "Error in creating the User", Toast.LENGTH_SHORT).show();
                                             }
                                         }
                                     });
                                 }
                             }else {
                                 Toast.makeText(registration.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                             }
                         }
                     });
                 }
             }
         });

        binding.rgprofileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),10);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==10){
            if(data!=null){
                imageURI=data.getData();
                binding.rgprofileImg.setImageURI(imageURI);
            }
        }
    }
}