package com.inhatc.android_eventhandler;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnCall;
    private EditText objET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        btnCall = (Button) findViewById(R.id.button);
        objET = (EditText) findViewById(R.id.editTextPhone);
        btnCall.setOnClickListener(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onClick(View view) {    // View는 해당 이벤트를 실행한 놈?을 불러옴(여기서는 btnCall)
        String strPhoneNo = objET.getText().toString();
        if(view == btnCall) {
            if(!strPhoneNo.isEmpty()) {
                Intent dialIntent = new Intent(Intent.ACTION_CALL,
                        Uri.parse("tel:" + strPhoneNo));
                dialIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(dialIntent);
            } else {
                objET.setText("Input Phone Number......");
            }
        }
    }
}