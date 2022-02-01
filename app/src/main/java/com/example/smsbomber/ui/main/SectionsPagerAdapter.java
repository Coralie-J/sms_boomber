package com.example.smsbomber.ui.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.smsbomber.fragment.ContactFragment;
import com.example.smsbomber.fragment.ListeMessageFragment;
import com.example.smsbomber.fragment.MessageFormFragment;


public class SectionsPagerAdapter extends FragmentStateAdapter {

    public SectionsPagerAdapter(FragmentManager fm, Lifecycle lifecycle) {
        super(fm, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        Fragment fragment = null;

        switch (position){
            case 0:
                fragment = ContactFragment.newInstance(position + 1);
                break;
            case 1:
                fragment = MessageFormFragment.newInstance(position + 1);
                break;
            case 2:
                fragment = ListeMessageFragment.newInstance(position + 1);
                break;
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}