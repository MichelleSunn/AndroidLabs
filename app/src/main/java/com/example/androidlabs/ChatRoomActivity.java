package com.example.androidlabs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;


public class ChatRoomActivity extends AppCompatActivity {

    private ArrayList<Message> messages = new ArrayList<>();
    private MyListAdapter myAdapter;
    Message message;
    SQLiteDatabase db;
    DatabaseOpener dbOpener;
    Cursor results;
    Boolean isTablet;
    DetailsFragment dFragment;
    public static final int EMPTY_ACTIVITY = 345;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        //dFragment = new DetailsFragment();
        Button send = findViewById(R.id.sendBtn);
        Button receive = findViewById(R.id.receiveBtn);
        EditText typeHere = findViewById(R.id.typeHere);
        ListView myList = findViewById(R.id.theListView);
        isTablet = findViewById(R.id.theFrameLayout) != null;

        loadDataFromDatabase();
        myAdapter = new MyListAdapter();
        myList.setAdapter(myAdapter);
        //Print Log
        printCursor(results, 1);

        send.setOnClickListener(click -> {
            String msg = typeHere.getText().toString();
            //add to the database and get the new ID
            ContentValues newRowValues = new ContentValues();
            //put string message in the MESSAGE column
            newRowValues.put(DatabaseOpener.COL_MESSAGE, msg);
            //put type in the TYPE column
            newRowValues.put(DatabaseOpener.COL_TYPE, -1);
            //insert in the database
            long newId = db.insert(DatabaseOpener.TABLE_NAME, null, newRowValues);
            //create new message with newId
            message = new Message(msg, -1, newId);
            //add new message to list
            messages.add(message);
            myAdapter.notifyDataSetChanged();
            typeHere.setText("");
        });

        receive.setOnClickListener(click -> {
            String msg = typeHere.getText().toString();
            //add to the database and get the new ID
            ContentValues newRowValues = new ContentValues();
            //put string message in the MESSAGE column
            newRowValues.put(DatabaseOpener.COL_MESSAGE, msg);
            //put type in the TYPE column
            newRowValues.put(DatabaseOpener.COL_TYPE, 1);
            //insert in the database
            long newId = db.insert(DatabaseOpener.TABLE_NAME, null, newRowValues);
            //create new message with newId
            message = new Message(msg, 1, newId);
            //add new message to list
            messages.add(message);
            myAdapter.notifyDataSetChanged();
            typeHere.setText("");
        });

        myList.setOnItemLongClickListener((P, b, pos, id) -> {
            Message selectedMsg = messages.get(pos);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Delete Message")
                    .setMessage("Do you want to delete this message")
                    .setPositiveButton("Yes", (click, arg) -> {
                        deleteMsg(selectedMsg);
                        messages.remove(selectedMsg);
                        myAdapter.notifyDataSetChanged();
                        if(isTablet) {
                            getSupportFragmentManager().beginTransaction().remove(dFragment).commit();
                        }
                    })
                    .setNegativeButton("No", (click, arg) -> {
                    })
                    .create().show();
            return true;
        });

        myList.setOnItemClickListener((list, view, position, id) -> {

            Bundle dataToPass = new Bundle();
            dataToPass.putString("Message", messages.get(position).toString());
            dataToPass.putLong("id", id);
            dataToPass.putInt("isSend", messages.get(position).getType());
            if(isTablet){
                dFragment = new DetailsFragment();
                dFragment.setArguments(dataToPass);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.theFrameLayout, dFragment)
                        .commit();
            }
            else{
                Intent nextActivity = new Intent(this, emptyActivity.class);
                nextActivity.putExtras(dataToPass);
                startActivityForResult(nextActivity,EMPTY_ACTIVITY);
            }

        });
    }

    protected void deleteMsg(Message msg) {
        db.delete(DatabaseOpener.TABLE_NAME, DatabaseOpener.COL_ID + "= ?", new String[]{Long.toString(msg.getId())});
    }

    private void loadDataFromDatabase() {
        //get a database
        dbOpener = new DatabaseOpener(this);
        db = dbOpener.getWritableDatabase();
        //query all the results from the database
        String[] columns = {DatabaseOpener.COL_ID, DatabaseOpener.COL_MESSAGE, DatabaseOpener.COL_TYPE};
        results = db.query(false, DatabaseOpener.TABLE_NAME, columns, null, null, null, null, null, null);
        //find the column indices
        int idColIndex = results.getColumnIndex(DatabaseOpener.COL_ID);
        int msgColIndex = results.getColumnIndex(DatabaseOpener.COL_MESSAGE);
        int typeColIndex = results.getColumnIndex(DatabaseOpener.COL_TYPE);
        //iterate over the results
        while (results.moveToNext()) {
            long id = results.getLong(idColIndex);
            String typeText = results.getString(msgColIndex);
            int type = results.getInt(typeColIndex);
            //add new message to array list
            messages.add(new Message(typeText, type, id));
        }
    }

    private void printCursor(Cursor results, int version) {
        String printEachRow = printEachRow();
        Log.d("PrintCursor","The database version number: "+DatabaseOpener.VERSION_NUM);
        Log.d("PrintCursor","The number of columns: "+results.getColumnCount());
        Log.d("PrintCursor","The name of the columns: "+Arrays.toString(results.getColumnNames()));
        Log.d("PrintCursor","The number of rows: "+results.getCount());
        Log.d("PrintCursor","Each row of results: "+printEachRow);
    }

    private String printEachRow() {
        String res = "";
        if (results.moveToFirst()) {
            while (results.moveToNext()) {
                for (String s : results.getColumnNames()) {
                    res += results.getString(results.getColumnIndex(s)) + "\n";
                }
            }
        }return res;
    }
    private class MyListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return messages.size();
        }

        @Override
        public Message getItem(int position) {
            return messages.get(position);
        }

        @Override
        public long getItemId(int position) {
            return (long)position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View newView = convertView;
            TextView row;

            message = getItem(position);

            if(message.type == message.TYPE_SEND){

                newView = inflater.inflate(R.layout.send_msg, parent, false);
                row = newView.findViewById(R.id.sendMsg);
                String typedText = getItem(position).toString();
                row.setText(typedText);
            }
            if(message.type == message.TYPE_RECEIVE){

                newView = inflater.inflate(R.layout.receive_msg, parent, false);
                row = newView.findViewById(R.id.receiveMsg);
                String typedText = getItem(position).toString();
                row.setText(typedText);
            }return newView;
        }
    }

    private class Message{
        static final int TYPE_SEND = -1;
        static final int TYPE_RECEIVE = 1;
        protected long id;
        protected String typeText;
        protected int type;

        public Message(String typeText, int type, long id) {
            this.typeText = typeText;
            this.type = type;
            this.id = id;
        }

        Message(String typeText, int type){
           this(typeText, type, 0);
        }

        public String toString(){
            return typeText;
        }

        public long getId() {
            return id;
        }

        public int getType() { return type;}
    }
    }
