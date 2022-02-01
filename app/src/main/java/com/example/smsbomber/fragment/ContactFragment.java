package com.example.smsbomber.fragment;


import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smsbomber.MainActivity;
import com.example.smsbomber.Contacts;
import com.example.smsbomber.R;



public class ContactFragment extends Fragment {

    public ContactFragment() {
        super();
    }

    public static ContactFragment newInstance(int index) {
        return new ContactFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MainActivity mainActivity = (MainActivity) getActivity();
        View layout = inflater.inflate(R.layout.fragment_contact, container, false);

        RecyclerView recyclerView = layout.findViewById(R.id.recycler_contacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mainActivity.getContactAdapter());

        return layout;
    }

}
