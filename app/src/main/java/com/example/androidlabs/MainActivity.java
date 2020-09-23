package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.AlteredCharSequence;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ResourceBundle;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_linear);

        EditText editText = findViewById(R.id.editText);
        String userInput = editText.getText().toString();

        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, getResources().getString(R.string.toast_message),Toast.LENGTH_LONG).show();
        });

        Switch sw = findViewById(R.id.switchID);

        sw.setOnCheckedChangeListener((clicked, newState) -> {
            String state;
            if(newState){
                state = getResources().getString(R.string.on);
            }else state = getResources().getString(R.string.off);
            Snackbar.make(editText, getResources().getString(R.string.switch_message) + " " + state, Snackbar.LENGTH_SHORT)
                    .setAction(getResources().getString(R.string.undo), click ->clicked.setChecked(!newState)).show();
        });
    }
}

