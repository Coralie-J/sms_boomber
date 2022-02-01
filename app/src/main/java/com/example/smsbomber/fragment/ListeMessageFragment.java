package com.example.smsbomber.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smsbomber.MainActivity;
import com.example.smsbomber.R;


public class ListeMessageFragment extends Fragment {

    public ListeMessageFragment() {
        super();
    }

    public static ListeMessageFragment newInstance(int val) {
        return new ListeMessageFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_liste_message, container, false);
        MainActivity mainActivity = (MainActivity) getActivity();

        RecyclerView recyclerView = layout.findViewById(R.id.recycler_message);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mainActivity.getMessageAdapter());
        return layout;
    }
}