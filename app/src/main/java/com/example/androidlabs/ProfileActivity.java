package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class ProfileActivity extends AppCompatActivity {

    public static final String ACTIVITY_NAME = "PROFILE_ACTIVITY";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TOOLBAR = 2;
    ImageButton mImageButton;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivity(takePictureIntent);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageButton.setImageBitmap(imageBitmap);
        }
        if( requestCode == REQUEST_TOOLBAR && resultCode == 500){
            this.finish();
        }
        //Log.e(ACTIVITY_NAME, "In function: onActivityResult ");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mImageButton = findViewById(R.id.imageButton);
        Intent fromMain = getIntent();
        String email = fromMain.getStringExtra("typedEmail");
        EditText editEmail = findViewById(R.id.editText2);
        editEmail.setText(email);

        mImageButton.setOnClickListener(click ->{
            dispatchTakePictureIntent();
        });
        //Log.e(ACTIVITY_NAME, "In function: onCreate");

        Button goToChatBtn = findViewById(R.id.goToChat);
        goToChatBtn.setOnClickListener(click ->{
            Intent goToChat = new Intent(this, ChatRoomActivity.class);
            startActivity(goToChat);
        });

        Button weatherForecastBtn = findViewById(R.id.weatherForecast);
        weatherForecastBtn.setOnClickListener((click ->{
            Intent weatherForecast = new Intent(this, WeatherForecast.class);
            startActivity(weatherForecast);
        }));

        Button goToToolbar = findViewById(R.id.goToToolbarButton);
        goToToolbar.setOnClickListener(click ->{
            Intent toolbar = new Intent(this, TestToolbar.class);
            startActivityForResult(toolbar, REQUEST_TOOLBAR);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Log.e(ACTIVITY_NAME, "In function: onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Log.e(ACTIVITY_NAME, "In function: onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Log.e(ACTIVITY_NAME, "In function: onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Log.e(ACTIVITY_NAME, "In function: onDestroy");
    }
}