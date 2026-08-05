package com.inhatc.android_multimedia;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MediaPlayer objMP;      //MediaPlayer object
    private Button btnPlay;         //Button object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        btnPlay = (Button)this.findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(this);

        objMP = MediaPlayer.create(this, R.raw.music);  //Create Media Player

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onClick(View view) {
        if (objMP != null){
            Music_Player();
        } else {
            objMP = MediaPlayer.create(MainActivity.this, R.raw.music);
            Music_Player();
        }
    }

    private void Music_Player(){
        if (!objMP.isPlaying()){
            objMP.start();
            btnPlay.setText("Stop");

        }else{
            btnPlay.setText("Play");
            objMP.stop();
            objMP.release();
            objMP = null;
        }
    }
}