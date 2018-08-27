package za.co.boxedcode.boxederp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReadWriteActivity extends AppCompatActivity {


    Button btnSave;
    TextView txtResult;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mConditionRef = mRootRef.child("condition");
    private static final String TAG = "ReadWriteActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_write);

        //txtResult = (TextView)findViewById(R.id.txtResult);
        btnSave = (Button)findViewById(R.id.btnSave);
        txtResult = (TextView)findViewById(R.id.txtResult);

    }

    protected void onStart(){
        super.onStart();

        mConditionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String rText = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + rText);
                txtResult.setText(rText);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Error OnCancelled"  + databaseError.getDetails().toString());
            }
        });

    }
}
