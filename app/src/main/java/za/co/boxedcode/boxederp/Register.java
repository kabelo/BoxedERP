package za.co.boxedcode.boxederp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.ProgressDialog;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;


public class Register extends AppCompatActivity implements View.OnClickListener{

    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //initialize firebase
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null){
            //close this activity
            finish();
            //Go to dashboard
            startActivity(new Intent(getApplicationContext(), ReadWriteActivity.class));
        }


        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        buttonRegister = (Button) findViewById(R.id.buttonSignup);
        progressDialog = new ProgressDialog(this);

        buttonRegister.setOnClickListener(this);

    }

    public void onClick(View view){
        if(view == buttonRegister){
           registerUser();
        }
    }

    private void registerUser(){
        String email = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Registering. Please Wait.");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            addUserInfo();
                            Toast.makeText(Register.this, "Registration Successful...", Toast.LENGTH_LONG);
                            startActivity(new Intent(getApplicationContext(), Login.class));
                        }else{
                            Toast.makeText(Register.this,"Registration Failed. Please Try Again.",Toast.LENGTH_LONG).show();
                        }

                   }
                });

    }


    private void addUserInfo(){

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        progressDialog.setMessage("Updating Profile. Please Wait.");
        progressDialog.show();

        UserProfileChangeRequest updateProfile = new UserProfileChangeRequest.Builder()
                .setDisplayName("")
                .build();

        user.updateProfile(updateProfile)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            //set additional fields
                            Date created_date = Calendar.getInstance().getTime();

                           UserInformation userInfo = new UserInformation(
                                    editTextEmail.getText().toString(),"", "", "", "1", "1", created_date.toString()
                            );

                            FirebaseDatabase.getInstance().getReference("users")
                                    .child(user.getUid())
                                    .setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressDialog.hide();
                                    if(task.isSuccessful()){
                                        return;
                                    }else{
                                        Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                        //return;
                                    }
                                }
                            });

                        }
                    }
                });

    }

    public void beginLogin(View view){
        startActivity(new Intent(getApplicationContext(), Login.class));
    }
}
