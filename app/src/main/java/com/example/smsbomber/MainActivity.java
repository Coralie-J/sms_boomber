package com.example.smsbomber;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.example.smsbomber.adapters.ContactAdapter;
import com.example.smsbomber.adapters.MessageAdapter;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.smsbomber.ui.main.SectionsPagerAdapter;
import com.example.smsbomber.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_CODE = 0;
    private ActivityMainBinding binding;
    private ArrayList<Contacts> contacts;
    private ArrayList<String> messages;
    private MessageAdapter messageAdapter;
    private ContactAdapter contactAdapter;
    public static int result_contacts = 1;
    public static int result_sms = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), getLifecycle());
        ViewPager2 viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;

        new TabLayoutMediator(tabs, viewPager, (tab, position) -> {
            switch (position){
                case 0:
                        tab.setText("Contacts");
                        break;
                case 1:
                        tab.setText("Save Message");
                        break;
                case 2:
                    tab.setText("Liste Message");
                    break;
            }
        }).attach();

        askForPermission();
        restoreTasks();
        this.contactAdapter = new ContactAdapter(this.contacts);
        this.messageAdapter = new MessageAdapter(this.messages);

    }

    private void askForPermission() {
        String[] permissions = new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.SEND_SMS};
        ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantRes) {
        super.onRequestPermissionsResult(requestCode, permissions, grantRes);
        for (String permission: permissions) {
            if (permission.equals(Manifest.permission.READ_CONTACTS)) {
                if (grantRes.length > 0 && grantRes[0] == PackageManager.PERMISSION_GRANTED) {
                    MainActivity.result_contacts = 0;
                } else {
                    AlertDialog.Builder popup = new AlertDialog.Builder(this);
                    popup.setMessage("L'application n'est pas autorisée à lire vos contacts");
                    popup.setNeutralButton("Ok", null);
                    AlertDialog show = popup.create();
                    show.show();
                }
            }
            else if (permission.equals(Manifest.permission.SEND_SMS)){
                if (grantRes.length > 0 && grantRes[0] == PackageManager.PERMISSION_GRANTED) {
                    MainActivity.result_sms = 0;
                } else {
                    AlertDialog.Builder popup = new AlertDialog.Builder(this);
                    popup.setMessage("L'application n'est pas autorisée à envoyer des messages");
                    popup.setNeutralButton("Ok", null);
                    AlertDialog show = popup.create();
                    show.show();
                }
            }
        }
    }

    public void addMessage(String message){
        this.messages.add(message);
        this.messageAdapter.notifyItemInserted(this.messages.size() - 1);
    }

    public ArrayList<String> getMessages() {
        return messages;
    }

    public ArrayList<Contacts> getContacts() {
        return contacts;
    }

    public ContactAdapter getContactAdapter() {
        return contactAdapter;
    }

    public MessageAdapter getMessageAdapter() {
        return messageAdapter;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("SMS Pause", "On pause");
        Random random = new Random();

        if (this.messages.size() !=0 && this.contacts.size() != 0) {
            if (MainActivity.result_sms == 0) {
                for (int i=0; i< 6; i++) {
                    int indice_contact = random.nextInt(this.contacts.size());
                    int indice_message = random.nextInt(this.messages.size());

                    // sendSMS(this.contacts.get(indice_contact).getPhonenumber(), this.messages.get(indice_message));
                }
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPreferences = getSharedPreferences("smsboomber", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String contact_string = gson.toJson(contacts);
        String messages_str = gson.toJson(messages);
        editor.putString("contacts", contact_string);
        editor.putString("messages", messages_str);
        editor.apply();
    }

    public void restoreTasks(){
        SharedPreferences sharedPreferences = getSharedPreferences("smsboomber", MODE_PRIVATE);
        String contacts_json = sharedPreferences.getString("contacts", null);
        String messages_json = sharedPreferences.getString("messages", null);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Contacts>>(){}.getType();
        Type type2 = new TypeToken<ArrayList<String>>(){}.getType();
        this.contacts = gson.fromJson(contacts_json, type);
        this.messages = gson.fromJson(messages_json, type2);

        if (this.contacts == null)
            this.contacts = new ArrayList<>();

        if (this.messages == null)
            this.messages = new ArrayList<>();
    }

    public void sendSMS(String phoneNumber, String message) {
        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_IMMUTABLE);
        SmsManager sms = SmsManager.getDefault();
        if (phoneNumber.startsWith("+337") || phoneNumber.startsWith("+336") || phoneNumber.startsWith("06") || phoneNumber.startsWith("07")){
            sms.sendTextMessage(phoneNumber, null, message, pi, null);
            for (Contacts contact : this.contacts){
                if (contact.getPhonenumber().equals(phoneNumber)) {
                    Log.i("SMS Pause", "Contact trouvé " + contact.getName());
                    contact.incrementeNbMessages();
                    this.contactAdapter.notifyDataSetChanged();
                }
            }
        }
    }
}