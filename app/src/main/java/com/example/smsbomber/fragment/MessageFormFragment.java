package com.example.smsbomber.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.smsbomber.MainActivity;
import com.example.smsbomber.R;


public class MessageFormFragment extends Fragment {


    public MessageFormFragment() {
        super();
    }

    public static MessageFormFragment newInstance(int val) {
        MessageFormFragment fragment = new MessageFormFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_message_form, container, false);
        Button bouton_add = layout.findViewById(R.id.btn_add);

        bouton_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edit_message = layout.findViewById(R.id.edit_message);
                if (! edit_message.getText().toString().trim().isEmpty()){
                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.addMessage(edit_message.getText().toString());
                }
            }
        });


        return layout;
    }

}