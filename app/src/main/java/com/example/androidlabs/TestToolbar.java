package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class TestToolbar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);

        Toolbar tBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tBar);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.item1:
                Toast.makeText(this,"You clicked item 1",Toast.LENGTH_SHORT ).show();
                break;
            case R.id.item2:
                Toast.makeText(this,"You clicked item 2",Toast.LENGTH_SHORT ).show();
                break;
            case R.id.item3:
                Toast.makeText(this,"You clicked item 3",Toast.LENGTH_SHORT ).show();
                break;
            case R.id.item4:
                Toast.makeText(this,"You clicked on the overflow menu",Toast.LENGTH_SHORT ).show();
                break;
        }
        return true;

    }


}