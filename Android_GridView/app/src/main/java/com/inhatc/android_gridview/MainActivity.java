package com.inhatc.android_gridview;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.nio.file.Files;

public class MainActivity extends AppCompatActivity {

    GridView objGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        objGridView = (GridView)findViewById(R.id.gridView1);
        objGridView.setAdapter(new ImageAdapter(this));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private class ImageAdapter extends BaseAdapter {

        private Context mContext;
        private Integer[] mThumbIds = {
                R.drawable.img_1, R.drawable.img_2, R.drawable.img_3,
                R.drawable.img_4, R.drawable.img_5, R.drawable.img_6,
                R.drawable.img_7, R.drawable.img_8, R.drawable.img_9
        };
        public ImageAdapter(Context objContxt) { mContext = objContxt; }
        public int getCount() { return mThumbIds.length; }
        public Object getItem(int position) { return null; }
        public long getItemId(int position) { return 0; }

        //Create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView objImgView;    //ImageView object

            if (convertView == null) { //
                objImgView = new ImageView(mContext);
                objImgView.setLayoutParams(new GridView.LayoutParams(300, 200));
                objImgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                objImgView.setPadding(8, 8, 8, 8);
            } else {
                objImgView = (ImageView) convertView;
            }
            objImgView.setImageResource(mThumbIds[position]);
            return objImgView;
        }
    }
}