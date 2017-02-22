package org.fundana.linterna;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

/**
 * Created by carlosvalls on 12/27/16.
 */

public class ContactPicker extends Activity {
    private static final int RESULT_PICK_CONTACT = 85500;

    private TextView contactName;
    private TextView phone;
    private Button save_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_picker);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        int position = 0;

        if(b!=null)
        {
            position =(Integer) b.get(EXTRA_MESSAGE);
        }
        final int pos = position;

        SharedPreferences sharedPref = getSharedPreferences("userContacts", Context.MODE_PRIVATE);
        String contact0_name = sharedPref.getString("contact0_name", "");
        String contact0_phone = sharedPref.getString("contact0_phone", "");
        String contact1_name = sharedPref.getString("contact1_name", "");
        String contact1_phone = sharedPref.getString("contact1_phone", "");
        String contact2_name = sharedPref.getString("contact2_name", "");
        String contact2_phone = sharedPref.getString("contact2_phone", "");
        String contact3_name = sharedPref.getString("contact3_name", "");
        String contact3_phone = sharedPref.getString("contact3_phone", "");



        contactName = (TextView) findViewById(R.id.nameTextView);
        phone = (TextView) findViewById(R.id.phoneTextView);
        save_button = (Button) findViewById(R.id.save_contact);

        if (contact0_name.compareTo("") != 0 && position == 0){
            contactName.setText(contact0_name);
            phone.setText(contact0_phone);
        }
        if (contact1_name.compareTo("") != 0 && position == 1){
            contactName.setText(contact1_name);
            phone.setText(contact1_phone);
        }
        if (contact2_name.compareTo("") != 0 && position == 2){
            contactName.setText(contact2_name);
            phone.setText(contact2_phone);
        }
        if (contact3_name.compareTo("") != 0 && position == 3){
            contactName.setText(contact3_name);
            phone.setText(contact3_phone);
        }


        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveContact(v,pos);
            }
        });

    }

    public void pickContact(View v)
    {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    contactPicked(data);
                    break;
            }

        } else {
            Log.e("MainActivity", "Failed to pick contact");
        }
    }

    private void contactPicked(Intent data) {
        Cursor cursor = null;
        try {
            String phoneNo = null ;
            String name = null;
            Uri uri = data.getData();
            cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();

            int  phoneIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int  nameIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

            phoneNo = cursor.getString(phoneIndex);
            name = cursor.getString(nameIndex);

            contactName.setText(name);
            phone.setText(phoneNo);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void saveContact(View v,int position){
        SharedPreferences sharedPref = getSharedPreferences("userContacts", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        switch (position){
            case 0: editor.putString("contact0_name", contactName.getText().toString());
                    editor.putString("contact0_phone", phone.getText().toString());
                    break;
            case 1: editor.putString("contact1_name", contactName.getText().toString());
                    editor.putString("contact1_phone", phone.getText().toString());
                    break;
            case 2: editor.putString("contact2_name", contactName.getText().toString());
                    editor.putString("contact2_phone", phone.getText().toString());
                    break;
            case 3: editor.putString("contact3_name", contactName.getText().toString());
                    editor.putString("contact3_phone", phone.getText().toString());
                    break;
        }
        editor.apply();
        editor.commit();
        Toast.makeText(getApplicationContext(), "Guardado Correctamente", Toast.LENGTH_LONG).show();
        setResult(RESULT_OK);
        finish();
        return;
    }



}

