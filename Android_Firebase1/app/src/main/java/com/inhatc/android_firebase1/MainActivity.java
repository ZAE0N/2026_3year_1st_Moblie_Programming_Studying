package com.inhatc.android_firebase1;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseDatabase myFirebase;                // Firebase Object
    DatabaseReference myDB_Reference = null;    // Firebase DB Reference

    HashMap<String, Object> Customer_Value = null;
    CustomerInfo objCustomerInfo = null;

    TextView txtFirebase;                       // TextView object
    EditText edtCustomerName;                   // EditText object
    Button btnInsert;                           // Button object
    String strHeader = "Customer Information";  // Firebase Key
    String strCName = null;
    String strCPhone_No = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        txtFirebase = (TextView) findViewById(R.id.txtFirebase);
        edtCustomerName = (EditText) findViewById(R.id.editCustomerName);

        btnInsert = (Button) findViewById(R.id.btnInsert);
        btnInsert.setOnClickListener(this);

        myFirebase = FirebaseDatabase.getInstance();     // Get FirebaseDatabase Instance
        myDB_Reference = myFirebase.getReference();     // Get Firebase Reference

        Customer_Value = new HashMap<>();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnInsert){
            strCName = edtCustomerName.getText().toString();
            if (!strCName.equals("")) {
                Customer_Value.put("Name", strCName);

                mSet_FirebaseDatabase(true);     // App -> Firebase DB
                mGet_FirebaseDatabase();         // Firebase DB -> App
            }
        }
        edtCustomerName.setText("");
    }

    // Data : App -> Firebase DB
    private void mSet_FirebaseDatabase(boolean bFlag) {
        // bFlag = true(add) / false(delete)
        if (bFlag){
            myDB_Reference.child(strHeader).child(strCName).setValue(Customer_Value);
        }
    }

    // Data : Firebase DB -> App
    private void mGet_FirebaseDatabase() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                txtFirebase.setText("Firebase Value : ");

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String strKey = postSnapshot.getKey().toString();

                    txtFirebase.append("\n -Name: " + strKey);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError dbError) {
                Log.w("Tag: ", "Failed to read value", dbError.toException());
            }
        };

        Query sortbyName = FirebaseDatabase.getInstance().getReference()
                .child(strHeader).orderByChild(strCName);
        sortbyName.addListenerForSingleValueEvent(postListener);
    }
}