package za.co.boxedcode.boxederp;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class UserProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ProgressDialog progressDialog;
    private static final String TAG = "UpdateProfileActivity";
    private Button btnUpdate;
    private EditText email;
    private EditText firstname;
    private EditText surname;
    private EditText contact_no;
    private EditText id_no;
    private TextView txtHeaderEmail;
    private TextView txtHeaderFullname;
    private FirebaseAuth firebaseAuth;
    private ImageView bookParking;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase firebaseDb = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set content view
        setContentView(R.layout.activity_user_profile);
        //set keyboard hide listener
        setupUI(findViewById(R.id.drawer_layout));
        //initialize toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //initialize progress bar
        progressDialog = new ProgressDialog(this);

        ImageView bookParking = (ImageView)findViewById(R.id.bookParking);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        btnUpdate = (Button)findViewById(R.id.btnUpdate);

         if(user == null){
             finish();
             startActivity(new Intent(getApplicationContext(),Login.class));
         }

        email = (EditText) findViewById(R.id.email);
        email.setText(user.getEmail());

        firstname = (EditText) findViewById(R.id.firstname);
        if(user.getDisplayName() != null){
            firstname.setText(user.getDisplayName());
        }

        surname = (EditText)findViewById(R.id.surname);
        contact_no = (EditText) findViewById(R.id.contact_no);
        id_no = (EditText) findViewById(R.id.id_no);

        //ImageView bookParking = (ImageView) findViewById(R.id.favorite_icon);
        bookParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startActivity(new Intent(getApplicationContext(),Booking.class));
            }
        });

        //fetch user data
        progressDialog.setMessage("Fetching Data.");
        progressDialog.show();
        populateData();


        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_booking);
        View headerView = navigationView.getHeaderView(0);
        txtHeaderFullname = (TextView) headerView.findViewById(R.id.txtFullname);
        if(user.getDisplayName() != null){
            txtHeaderFullname.setText(user.getDisplayName().toString());
        }

        txtHeaderEmail = (TextView) headerView.findViewById(R.id.txtEmail);
        txtHeaderEmail.setText(email.getText().toString());

       btnUpdate.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               updateUser();
           }
       });
  }


  private void populateData(){

      DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("users");

      dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

              try{
                  for (DataSnapshot data : dataSnapshot.getChildren()){

                      UserInformation userInfo = new UserInformation();
                      userInfo.setSurname(data.child(user.getUid()).getValue(UserInformation.class).getSurname());
                      //Log.d(TAG,'User ID' + user.getUid());
                      Log.d(TAG,"User surname" + userInfo.getSurname());


                      userInfo.setContact_no(data.getValue(UserInformation.class).getContact_no().toString());
                      userInfo.setSurname(data.getValue(UserInformation.class).getSurname().toString());
                      userInfo.setId_no(data.getValue(UserInformation.class).getId_no().toString());

                      contact_no.setText(userInfo.getContact_no());
                      surname.setText(userInfo.getSurname());
                      id_no.setText(userInfo.getId_no());
                  }

              }catch (Exception e){
                Log.d(TAG,e.getMessage().toString());
                Toast.makeText(UserProfileActivity.this,"Could Not Load User Data. Please Try Again",Toast.LENGTH_LONG);
              }

              progressDialog.dismiss();

          }

          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {
              progressDialog.dismiss();
          }
      });
  }
    private void updateUser(){
        progressDialog.setMessage("Updating Profile. Please Wait.");
        progressDialog.show();

         UserProfileChangeRequest updateProfile = new UserProfileChangeRequest.Builder()
                .setDisplayName(firstname.getText().toString().trim())
                .build();

        user.updateProfile(updateProfile)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            //set additional fields
                            UserDetail userDetail = new UserDetail(firstname.getText().toString(),
                                                                surname.getText().toString(), id_no.getText().toString(), contact_no.getText().toString(), "1");

                            FirebaseDatabase.getInstance().getReference("users")
                            .child(user.getUid())
                            .setValue(userDetail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressDialog.hide();
                                    if(task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(),"Profile Updated",Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(getApplicationContext(),Booking.class));
                                    }else{
                                        Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                }
                            });


                        }
                    }
                });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_book) {
            //open booking activity
            startActivity(new Intent(getApplicationContext(), Booking.class));

        } else if (id == R.id.nav_history) {
            //open booking history activity
        } else if (id == R.id.nav_logout) {
            //firebase logout and open login activity
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signOut();
            startActivity(new Intent(getApplicationContext(), Login.class));

        } else if (id == R.id.nav_account){
            startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                    hideKeyboard(UserProfileActivity.this);
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

    public void bookParking(){
        startActivity(new Intent(getApplicationContext(), Booking.class));
    }
}
