package com.example.androidlabs;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class DetailsFragment extends Fragment {

    private Bundle dataFromActivity;
    private long id;
    private AppCompatActivity parentActivity;
    private boolean isSend;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dataFromActivity = getArguments();
        id = dataFromActivity.getLong("id");
        isSend = dataFromActivity.getInt("isSend") == -1;
        // Inflate the layout for this fragment
        View result =  inflater.inflate(R.layout.fragment_details, container, false);
        //show the message
        TextView message = (TextView)result.findViewById(R.id.messageHere);
        message.setText(dataFromActivity.getString("Message"));
        //show the id
        TextView idView = (TextView)result.findViewById(R.id.MsgID);
        idView.setText("ID = " + id);
        //show check box
        CheckBox cb = (CheckBox)result.findViewById(R.id.MsgCheckBox);
        cb.setChecked(isSend);
        // get the hide button, and add a click listener
        Button hideButton = (Button)result.findViewById(R.id.hideButton);
        hideButton.setOnClickListener(click ->{
            parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
        });

        return result;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //context will either be FragmentExample for a tablet, or EmptyActivity for phone
        parentActivity = (AppCompatActivity)context;
    }


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DetailsFragment() {
        // Required empty public constructor
    }

}