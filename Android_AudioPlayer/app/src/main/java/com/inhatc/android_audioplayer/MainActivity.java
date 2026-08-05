package com.inhatc.android_audioplayer;

import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.Manifest;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MediaPlayer objMP;                  // MediaPlayer object
    private EditText audioSrcFile;              // EditText object
    private CheckBox chkLoopCTRL;               // CheckBox object
    private Button btnLoad, btnPlay, btnStop;   // Button object

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        audioSrcFile = (EditText)findViewById(R.id.editSrcFile);

        chkLoopCTRL = (CheckBox)findViewById(R.id.chkLoop);
        chkLoopCTRL.setOnClickListener(this);

        btnLoad = (Button)findViewById(R.id.btnLoad);
        btnLoad.setOnClickListener(this);

        btnPlay = (Button)findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(this);

        btnStop = (Button)findViewById(R.id.btnStop);
        btnStop.setOnClickListener(this);

        // Check if we have write permission
        mVerifyStoragePermissions(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    //Permission Verification
    public static void mVerifyStoragePermissions(Activity activity) {
        int iWrite_Permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int iRead_Permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        if (iWrite_Permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
        if (iRead_Permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
        //Grant access to all files
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if(!Environment.isExternalStorageManager()){
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                activity.startActivity(intent);
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view == btnLoad){                      //Audio File Load
            if(!LoadAudioFile(audioSrcFile.getText().toString())){
                Toast.makeText(getApplicationContext(), "Audio File Load Fail !",
                        Toast.LENGTH_LONG).show();
                return;
            }

            audioSrcFile.setEnabled(false); // EditText audioSrcFile disable
            btnPlay.setEnabled(true);
            btnStop.setEnabled(true);
            chkLoopCTRL.setEnabled(true);
            btnLoad.setEnabled(false);
            Toast.makeText(getApplicationContext(), "File : " +
                            audioSrcFile.getText().toString() + " Load Success !",
                    Toast.LENGTH_LONG).show();
            return;
        } else if(view == btnPlay) {
            if(PlayPauseAudio() != true ){ //MediaPlayer Play
                btnPlay.setText("Pause");  //btnPlay.Text = "Pause"
            }else{
                btnPlay.setText("Play");   //btnPlay.Text = "Play"
            }
        } else if(view == btnStop) {
            objMP.stop();                  //MediaPlayer Stop
            audioSrcFile.setEnabled(true); //audioSrcFile Active
            btnPlay.setText("Play");
            chkLoopCTRL.setChecked(false);
            btnPlay.setEnabled(false);
            btnStop.setEnabled(false);
            chkLoopCTRL.setEnabled(false);
            btnLoad.setEnabled(true);
        } else if(view == chkLoopCTRL) {
            if (chkLoopCTRL.isChecked()) {   //chkLoopCTRL.Checked = true
                objMP.setLooping(true);    //Loop Set
                Toast.makeText(getApplicationContext(), "Loop Set Status",
                        Toast.LENGTH_SHORT).show();
            } else {
                objMP.setLooping(false);   //Loop Reset
                Toast.makeText(getApplicationContext(), "Loop Reset Status",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Play Pause Audio
    private boolean PlayPauseAudio(){
        if(!objMP.isPlaying()){
            objMP.start();
            Toast.makeText(getApplicationContext(), "Play",
                    Toast.LENGTH_SHORT).show();
            return false;
        }else{                             // in Play status
            objMP.pause();                 // Pause
            Toast.makeText(getApplicationContext(), "Pause", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    //Load Audio file
    private boolean LoadAudioFile(String path){ //Audio File Load
        objMP = new MediaPlayer();              // Create MediaPlayer object
        try{
            objMP.setDataSource(path);
            objMP.prepare();                    // Audio File ready
            return true;
        }catch(Exception e){                    //Audio File Load Fail
            Toast.makeText(getApplicationContext(), e.getMessage(),
                    Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    //onDestroy() method
    public void onDestroy(){                    //Activity Destroy
        super.onDestroy();
        if(objMP != null) objMP.release();      // MediaPlayer object Release
        objMP = null;
    }
}