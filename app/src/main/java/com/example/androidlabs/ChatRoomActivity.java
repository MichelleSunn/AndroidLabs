package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;


public class ChatRoomActivity extends AppCompatActivity {

    private ArrayList<Message> messages = new ArrayList<>();
    private MyListAdapter myAdapter;
    Message message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        Button send = findViewById(R.id.sendBtn);
        Button receive = findViewById(R.id.receiveBtn);
        EditText typeHere = findViewById(R.id.typeHere);

        ListView myList = findViewById(R.id.theListView);
        myAdapter = new MyListAdapter();

        send.setOnClickListener(click -> {
            message = new Message(typeHere.getText().toString(), -1);
            messages.add(message);

            myAdapter.notifyDataSetChanged();
            typeHere.setText("");
        });

        receive.setOnClickListener(click -> {
            message = new Message(typeHere.getText().toString(),1);
            messages.add(message);

            myAdapter.notifyDataSetChanged();
            typeHere.setText("");
        });
        myList.setAdapter(myAdapter);

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

            if(message.avatar == message.TYPE_SEND){

                newView = inflater.inflate(R.layout.send_msg, parent, false);
                row = newView.findViewById(R.id.sendMsg);
                String typedText = getItem(position).toString();
                row.setText(typedText);
            }
            if(message.avatar == message.TYPE_RECEIVE){

                newView = inflater.inflate(R.layout.receive_msg, parent, false);
                row = newView.findViewById(R.id.receiveMsg);
                String typedText = getItem(position).toString();
                row.setText(typedText);
            }

            return newView;

        }
    }

    private class Message{
        static final int TYPE_SEND = -1;
        static final int TYPE_RECEIVE = 1;

        String typeText;
        int avatar;

        Message(String typeText, int avatar){
            this.typeText = typeText;
            this.avatar = avatar;
        }

        public String toString(){
            return typeText;
        }
    }
    }
