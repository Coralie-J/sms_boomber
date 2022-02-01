package com.example.smsbomber.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smsbomber.R;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter {

    public class MessageItemView extends RecyclerView.ViewHolder{

        private TextView tv_message;

        public MessageItemView(@NonNull View itemView) {
            super(itemView);
            this.tv_message = itemView.findViewById(R.id.message);
        }
    }

    private ArrayList<String> messages;

    public MessageAdapter(ArrayList<String> messages){
        this.messages = messages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
        return new MessageAdapter.MessageItemView(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String message = this.messages.get(position);
        ((MessageAdapter.MessageItemView) holder).tv_message.setText((position+1) + " - " + message);
    }

    @Override
    public int getItemCount() {
        return this.messages.size();
    }
}
