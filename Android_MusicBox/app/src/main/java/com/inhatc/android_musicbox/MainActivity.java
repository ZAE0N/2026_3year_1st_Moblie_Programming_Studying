package com.inhatc.android_musicbox;

import android.os.Bundle;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MediaPlayer objMP;                  // MediaPlayer object
    private CheckBox chkLoopCTRL;               // CheckBox object
    private Button btnPlay, btnStop;            // Button object
    private ListView lstMusicFile;              // Music file list
    private TextView txtTitle;                  // Title TextView
    String strMusicSource_Path;                 // Music file path
    ArrayList<String> arrayList_Music;          // Array list for storing music
    String strSelected_Music;                   // Select Music from ListView

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

        // Check if we have write permission
        mVerifyStoragePermissions(this);

        strMusicSource_Path = Environment.getExternalStorageDirectory().getPath() + "/";
        objMP = new MediaPlayer();                  // Create MediaPlayer object
        arrayList_Music = new ArrayList<String>();

        // Load Audio file & Make ArrayList
        mLoad_AudioFile();

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        chkLoopCTRL = (CheckBox)findViewById(R.id.chkLoop);
        chkLoopCTRL.setOnClickListener(this);

        btnPlay = (Button)findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(this);
        btnStop = (Button)findViewById(R.id.btnStop);
        btnStop.setOnClickListener(this);

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

    // Load Audio file: MP3 file in External Storage -> Arraylist Music
    private void mLoad_AudioFile(){ //Audio File Load
        File[] listFiles = new File(strMusicSource_Path).listFiles();
        String strFileName, strFileExtension;
        for(File file : listFiles){
            strFileName = file.getName();
            strFileExtension = strFileName.substring(strFileName.length() - 3);
            if(strFileExtension.equals("mp3") || strFileExtension.equals("m4r"))
                arrayList_Music.add(strFileName);
        }

        //Add mp3 music to lstMusicFile
        lstMusicFile = findViewById(R.id.lstMusic);
        ArrayAdapter<String> adapterMusic = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_single_choice, arrayList_Music);
        lstMusicFile.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lstMusicFile.setAdapter(adapterMusic);
        lstMusicFile.setItemChecked(0, true);
        lstMusicFile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                strSelected_Music = arrayList_Music.get(i);
                mSet_AudioFile();                   //Set Music Source file
                chkLoopCTRL.setEnabled(true);
                btnPlay.setEnabled(true);
                btnPlay.setClickable(true);
                btnStop.setEnabled(true);
                btnStop.setClickable(true);
            }
        });
        strSelected_Music = arrayList_Music.get(0);
        mSet_AudioFile();                   //Set Music Source file
    }

    //Set Music Source file
    private void mSet_AudioFile(){ //Audio File Load
        try{
            objMP = new MediaPlayer();              // Create MediaPlayer object
            objMP.setDataSource(strMusicSource_Path + strSelected_Music);
            objMP.prepare();
        }catch(Exception e) {                       //Audio File Load Fail
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        if(view == btnPlay) {
            if(!objMP.isPlaying()){        //MediaPlayer Play
                objMP.start();
                btnPlay.setText("Pause");  //btnPlay.Text = "Pause"
                btnPlay.setClickable(true);
                btnStop.setClickable(true);
                txtTitle.setText("Play Music : " + strSelected_Music);
            }else{
                objMP.pause();             // Pause
                btnPlay.setText("Play");   //btnPlay.Text = "Play"
                btnStop.setEnabled(true);
                btnStop.setClickable(true);
            }
        } else if(view == btnStop) {
            objMP.stop();                          //MediaPlayer Stop
            btnPlay.setText("Play");
            txtTitle.setText("My Music-Box");
            chkLoopCTRL.setEnabled(false);
            chkLoopCTRL.setChecked(false);
            btnPlay.setEnabled(false);
            btnPlay.setClickable(false);
            btnStop.setEnabled(false);
            btnStop.setClickable(false);
        } else if(view == chkLoopCTRL) {
            String strLoopStatus = null;
            if (chkLoopCTRL.isChecked()) {              //chkLoopCTRL.Checked = true
                objMP.setLooping(true);                 //Loop Set
                strLoopStatus = "Loop Set Status";
            } else {
                objMP.setLooping(false);                 //Loop Reset
                strLoopStatus = "Loop Reset Status";
            }
            Toast.makeText(getApplicationContext(), strLoopStatus, Toast.LENGTH_SHORT).show();
        }
    }

    //onDestroy() method
    public void onDestroy(){                    //Activity Destroy
        super.onDestroy();
        if(objMP != null) objMP.release();      // MediaPlayer object Release
        objMP = null;
    }
}