package com.simple.reaz.fffcom;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import co.gofynd.gravityview.GravityView;

public class Test extends AppCompatActivity {
 private ImageView imageView;
 private GravityView gravityView;
 private boolean suported =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        
        init();
        if(suported){
            gravityView.setImage(imageView,R.drawable.bg).center();
        }else {
            Bitmap bitmap=BitmapFactory.decodeResource(getResources(),R.drawable.bg);
            imageView.setImageBitmap(bitmap);

        }
    }

    private void init() {
        this.imageView=findViewById(R.id.panaroma);
        this.gravityView=GravityView.getInstance(getBaseContext());
        this.suported=gravityView.deviceSupported();
    }
    @Override
    protected void onResume() {
        super.onResume();
        gravityView.registerListener();
    }
    @Override
    protected void onStop() {
        super.onStop();
        gravityView.unRegisterListener();
    }
}
