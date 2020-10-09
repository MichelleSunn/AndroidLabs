package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    SharedPreferences prefs = null;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lab3);

        editText = findViewById(R.id.editText);
        prefs = getSharedPreferences("fileName", Context.MODE_PRIVATE);
        String savedStr = prefs.getString("savedEmail", "");
        editText.setText(savedStr);

        Button login = findViewById(R.id.login);
        login.setOnClickListener(click -> {
            Intent goToProfile = new Intent(this, ProfileActivity.class);
            goToProfile.putExtra("typedEmail", editText.getText().toString());
            startActivity(goToProfile);
        });
    }

    private void saveSharePrefs (String toString) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("savedEmail", toString);
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("savedEmail", editText.getText().toString());
        saveSharePrefs(editText.getText().toString());
        editor.commit();
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

