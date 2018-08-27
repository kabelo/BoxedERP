package za.co.boxedcode.boxederp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity implements View.OnClickListener {


    //defining views
    private Button buttonLogin;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView linkRegister;
    //firebase auth object
    private FirebaseAuth firebaseAuth;
    private boolean DEBUG = true;
    //progress dialog
    private ProgressDialog progressDialog;
    private static final String TAG = "LoginActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupUI(findViewById(R.id.login));
        //getting firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        if(DEBUG){
            firebaseAuth.signOut();
        }
        //if the objects getcurrentuser method is not null
        //means user is already logged in
        if(firebaseAuth.getCurrentUser() != null){
            //close this activity
            finish();
            startActivity(new Intent(getApplicationContext(), Booking.class));
        }

        //initializing views
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        linkRegister = (TextView)findViewById(R.id.linkRegister);

        if(DEBUG){
           editTextEmail.setText("p.kabelo@gmail.com");
           editTextPassword.setText("123456");
        }

        progressDialog = new ProgressDialog(this);

        //attaching click listener
        buttonLogin.setOnClickListener(this);
    }



    //method for user login
    private void userLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();


        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Logging In. Please Wait.");
       progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        //if the task is successfull
                        if(task.isSuccessful()){
                            //start the profile activity
                            Log.d(TAG, "Login Success");
                            startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));

                        }else{
                            Toast.makeText(Login.this,"Login Failed Please Try Again..", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    private void loadApp(){
        Log.d(TAG, "Loading App");
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        progressDialog.setMessage("Fetching Data...");
//        progressDialog.show();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        final String userType;

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()){

                    UserDetail userDetail = new UserDetail();
                    userDetail.setUser_type(data.child(user.getUid()).getValue(UserDetail.class).getUser_type());
                    startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
//                    switch (Usertype.trim()) {

//                        case "1": //error
//                            switch (id_no.trim()){

//                                case "":
//                                    startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
//                                    break;
//                                default:
//                                    startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
//                                    break;
//                            }
//                            break;
//                        case "2": //error
//                            Toast.makeText(Login.this, "Admin", Toast.LENGTH_LONG).show();
//                            break;
//                        default:
//                            break;
//                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view == buttonLogin){
            userLogin();
        }
    }


    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideKeyboard(Login.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    public void beginRegister(View view){
        startActivity(new Intent(getApplicationContext(), Register.class));
    }
}
