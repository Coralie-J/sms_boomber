package com.example.smsbomber.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smsbomber.Contacts;
import com.example.smsbomber.R;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter {

    public class ItemView extends RecyclerView.ViewHolder{

        private TextView tv_name;
        private TextView tv_num;
        private TextView tv_stats;

        public ItemView(@NonNull View itemView) {
            super(itemView);
            this.tv_name = itemView.findViewById(R.id.tv_name);
            this.tv_num = itemView.findViewById(R.id.tv_numero);
            this.tv_stats = itemView.findViewById(R.id.tv_stats);
        }
    }

    private ArrayList<Contacts> contacts;

    public ContactAdapter(ArrayList<Contacts> contacts) {
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_itemcontact, parent, false);
        return new ItemView(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Contacts contact = this.contacts.get(position);

        ((ItemView) holder).tv_name.setText(contact.getName());
        ((ItemView) holder).tv_num.setText(contact.getPhonenumber());
        ((ItemView) holder).tv_stats.setText("Nombre de messages envoyés : " + contact.getNb_messages());
    }

    @Override
    public int getItemCount() {
        return this.contacts.size();
    }
}
