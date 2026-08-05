package com.inhatc.android_dbsqlite2;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SQLiteDatabase myDB;            // Database object
    ArrayList<String> aryMBRList;   // ArrayList object
    ArrayAdapter<String> adtMembers;// ArrayAdapter object
    ListView lstView;               // ListView object
    String strRecord = null;        // Record data
    ContentValues insertValue;      // ContentValues object
    Cursor allRCD;                  // Cursor object
    Button btnInsert, btnUpdate, btnDelete, btnSearch; // Button object
    EditText edtCarType, edtCarPower; // EditText object
    String strSQL = null;           // String object to store Query

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        edtCarType = (EditText)findViewById(R.id.editCarType);
        edtCarPower = (EditText)findViewById(R.id.editCarPower);
        btnInsert = (Button)findViewById(R.id.btnInsert);
        btnInsert.setOnClickListener(this);
        btnUpdate = (Button)findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(this);
        btnDelete = (Button)findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(this);
        btnSearch = (Button)findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);

        lstView = (ListView)findViewById(R.id.lstMember);
        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] strData = null;
                strData = aryMBRList.get(position).split("\t\t");

                edtCarType.setText(strData[0]);
                edtCarPower.setText(strData[1]);
            }
        });

        //Create DB(DB name: CarInformation)
        myDB = this.openOrCreateDatabase("CarInformation", MODE_PRIVATE, null);
        myDB.execSQL("Drop table if exists Carlist");

        //Create Table(Table name: Carlist)
        myDB.execSQL("Create table Carlist (" +
                "_id integer primary key autoincrement, " +
                "CarType text not null, " + "CarPower text not null);");

        //Insert Data("BMW","3200") into Carlist table
        myDB.execSQL("Insert into Carlist " +
                "(CarType, CarPower) values ('BMW 528i', '2800');");

        //Insert Data into Carlist table
        insertValue = new ContentValues();
        insertValue.put("CarType", "Benz 320");
        insertValue.put("CarPower", "3200");
        myDB.insert("Carlist", null, insertValue);

        getDBData(null); //Get DB data from Carlist

        if(myDB != null) myDB.close(); //Close DB

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void getDBData(String strWhere) {
        //Get DB data from Carlist table
        allRCD = myDB.query("Carlist", null, strWhere, null, null, null, null);

        //Create arrayList
        aryMBRList = new ArrayList<String>();
        if (allRCD != null) {
            if (allRCD.moveToFirst()) {
                do {
                    strRecord = allRCD.getString(1) + "\t\t" + allRCD.getString(2);
                    aryMBRList.add(strRecord);
                } while (allRCD.moveToNext());
            }
        }
        adtMembers = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice, aryMBRList);

        //Create ListView
        lstView.setAdapter(adtMembers);
        lstView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    @Override
    public void onClick(View view) {
        //Create DB(DB name: CarInformation)
        myDB = this.openOrCreateDatabase("CarInformation", MODE_PRIVATE, null);

        if(view == btnInsert){
            //Insert record into Carlist
            insertValue = new ContentValues();
            insertValue.put("CarType", edtCarType.getText().toString());
            insertValue.put("CarPower", edtCarPower.getText().toString());
            myDB.insert("Carlist", null, insertValue);
            getDBData(null);
        } else if(view == btnUpdate){
            //Insert record into Carlist
            strSQL = "UPDATE Carlist SET CarPower = '" + edtCarPower.getText().toString() +
                    "' WHERE CarType = '" + edtCarType.getText().toString() + "';";
            myDB.execSQL(strSQL);
            getDBData(null);
        } else if(view == btnDelete){
            strSQL = "DELETE FROM Carlist WHERE CarType = '" + edtCarType.getText().toString() + "';";
            myDB.execSQL(strSQL);
            getDBData(null);
        } else if(view == btnSearch){
            //Insert record into Carlist
            strSQL = "CarType = '" + edtCarType.getText().toString() + "'";
            getDBData(strSQL);
        }

        if(myDB != null) myDB.close(); //Close DB
    }
}