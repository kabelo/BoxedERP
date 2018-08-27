package za.co.boxedcode.boxederp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Booking extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    private TextView Fullname;
    private TextView Email;
    private TextView fromDate;
    private TextView toDate;
    private DatePickerDialog.OnDateSetListener dtListner;
    private DatePickerDialog.OnDateSetListener dtToListner;

    private ImageView imgFromDate;
    private ImageView imgToDate;

    private Spinner make_spinner;
    private Spinner model_spinner;
    private Spinner location;

    private EditText vehicleColor;
    private EditText Registration;
    private EditText Brand;
    private EditText Model;

    private Button btnBook;
    private static final String[]makes = {"BMW"};
    private static final String[]models = {"3 Series"};
    private static final String[]locations = {"OR Tambo","Lanseria","Polokwane"};
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase firebaseDb = FirebaseDatabase.getInstance();
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        setupUI(findViewById(R.id.drawer_layout));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);


        vehicleColor = (EditText)findViewById(R.id.txtColor);
        Registration = (EditText)findViewById(R.id.txtRegistration);

        if(user == null){
            finish();
            startActivity(new Intent(getApplicationContext(),Login.class));
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        Fullname = (TextView) headerView.findViewById(R.id.txtFullname);
        Fullname.setText(user.getDisplayName().toString());

        Email = (TextView) headerView.findViewById(R.id.txtEmail);
        Email.setText(user.getEmail().toString());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        fromDate = (TextView)findViewById(R.id.txtFromDate);
        Date cDate = new Date();

        String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
        fromDate.setText(fDate);

        imgFromDate = (ImageView) findViewById(R.id.fromCalendar);
        imgFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dtDialog = new DatePickerDialog(
                        Booking.this,
                        android.R.style.Theme_DeviceDefault_Dialog,
                        dtListner,
                        year,month,day
                );
                dtDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dtDialog.show();

            }
        });

        toDate = (TextView)findViewById(R.id.txtToDate);
        String strToDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
        toDate.setText(strToDate);


        dtListner = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String strMonth;
                if(month < 10){
                    strMonth = "0" + String.valueOf(month);
                }else{
                    strMonth = "0" + String.valueOf(month);
                }
                fromDate.setText(year + "-" + strMonth + "-" + dayOfMonth);

            }
        };

imgToDate = (ImageView)findViewById(R.id.imgToDate);

        imgToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dtDialog = new DatePickerDialog(
                        Booking.this,
                        dtToListner,
                        year,month,day
                );
                dtDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dtDialog.show();

            }
        });


        dtToListner = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String strMonth = String.valueOf(month);
                if(month < 10){
                    strMonth = "0" + String.valueOf(month);
                }

                toDate.setText(year + "-" + strMonth + "-" + dayOfMonth);
                toDate.clearFocus();
                make_spinner.hasFocus();
            }
        };

        make_spinner = (Spinner)findViewById(R.id.spVehicleMake);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(Booking.this,
                android.R.layout.simple_spinner_item,makes);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        make_spinner.setAdapter(adapter);

        model_spinner = (Spinner)findViewById(R.id.spVehicleModel);

        ArrayAdapter<String>model_adapter = new ArrayAdapter<String>(Booking.this,
                android.R.layout.simple_spinner_item,models);

        model_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        model_spinner.setAdapter(model_adapter);

        location = (Spinner)findViewById(R.id.spLocations);

        ArrayAdapter<String>location_adapter = new ArrayAdapter<String>(Booking.this,
                android.R.layout.simple_spinner_item,locations);

        location_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        location.setAdapter(location_adapter);


        btnBook =  (Button)findViewById(R.id.btnBook);

        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validate input

                //book parking
                bookParking();
            }
        });
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
                    hideKeyboard(Booking.this);
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


    private void bookParking(){

        progressDialog.setMessage("Submitting Booking. Please Wait.");
        progressDialog.show();
        String user_id = user.getUid().toString();
        String from_date = fromDate.getText().toString();
        String to_date = toDate.getText().toString();
        String registration = Registration.getText().toString();
        String vcolor = vehicleColor.getText().toString();
        String brand = make_spinner.getSelectedItem().toString();
        String model = model_spinner.getSelectedItem().toString();

        String Location = location.getSelectedItem().toString();
        String current_date = fromDate.getText().toString();

        String key = FirebaseDatabase.getInstance().getReference("bookings").push().getKey();

        BookingManager bookingManager = new BookingManager(
                user_id,
                from_date,
                to_date,
                registration,
                brand,
                model,
                vcolor,
                Location,
                current_date,
                key
        );

        FirebaseDatabase.getInstance().getReference("bookings")
                .child(key)
                .setValue(bookingManager).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.hide();
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Booking Successful",Toast.LENGTH_LONG).show();
                   // startActivity(new Intent(getApplicationContext(),UserProfileActivity.class));
                }else{
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
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
        getMenuInflater().inflate(R.menu.booking, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

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
}
