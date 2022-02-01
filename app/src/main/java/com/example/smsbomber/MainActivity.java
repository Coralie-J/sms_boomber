package com.example.smsbomber;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
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
    public static int result_sms = 1;
    private static int result_contact = 1;

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

        restoreTasks();
        askForPermission();
        this.contactAdapter = new ContactAdapter(this.contacts);
        this.messageAdapter = new MessageAdapter(this.messages);

    }

    private void askForPermission() {
        String[] permissions = new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.SEND_SMS};
        ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_CODE);
        // ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantRes) {
        super.onRequestPermissionsResult(requestCode, permissions, grantRes);
        for (String permission: permissions) {
            if (permission.equals(Manifest.permission.READ_CONTACTS)) {
                if (grantRes.length > 0 && grantRes[0] == PackageManager.PERMISSION_GRANTED) {
                    MainActivity.result_contact = 0;
                    getPhoneContacts();
                    Log.i("TEST CONTACTS 3", this.contacts.toString());
                    this.contactAdapter.notifyDataSetChanged();
                } else {
                    AlertDialog.Builder popup = new AlertDialog.Builder(this);
                    popup.setMessage("L'application n'est pas autorisée à lire vos contacts");
                    popup.setNeutralButton("Ok", null);
                    AlertDialog show = popup.create();
                    show.show();
                }
            }

            else if (permission.equals(Manifest.permission.SEND_SMS)){
                if (grantRes.length > 1 && grantRes[1] == PackageManager.PERMISSION_GRANTED) {
                    MainActivity.result_sms = 0;
                    Log.i("TEST", "Permission SMS acceptée");
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
        if (this.messages.size() !=0 && this.contacts.size() != 0 && MainActivity.result_sms == 0) {
                for (int i=0; i< 6; i++) {
                    int indice_contact = random.nextInt(this.contacts.size());
                    int indice_message = random.nextInt(this.messages.size());
                    sendSMS(this.contacts.get(indice_contact).getPhonenumber(), this.messages.get(indice_message));
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
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String contacts_json = sharedPreferences.getString("contacts", null);
        String messages_json = sharedPreferences.getString("messages", null);
        Type type2 = new TypeToken<ArrayList<String>>() {}.getType();
        Gson gson = new Gson();

        if (MainActivity.result_contact == 0) {
            Type type = new TypeToken<ArrayList<Contacts>>() {}.getType();
            this.contacts = gson.fromJson(contacts_json, type);
        } else{
            editor.remove("contacts");
            editor.apply();
        }

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

    @SuppressLint("Range")
    public void getPhoneContacts() {
        ContentResolver contentResolver = getContentResolver();

        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if ((cursor != null ? cursor.getCount() : 0) > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    while (phoneCursor.moveToNext()) {
                        String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        Contacts contact = new Contacts(name, phoneNumber, 0);
                        this.contacts.add(contact);
                    }
                    phoneCursor.close();
                }
            }
        }
        if (cursor != null) {
            cursor.close();
        }
    }
}