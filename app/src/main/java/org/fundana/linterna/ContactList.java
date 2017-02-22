package org.fundana.linterna;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

/**
 * Created by carlosvalls on 12/27/16.
 */

public class ContactList extends AppCompatActivity {
    private static final int RESULT = 1337;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPref = getSharedPreferences("userContacts", Context.MODE_PRIVATE);
        String contact0_name = sharedPref.getString("contact0_name", "");
        String contact1_name = sharedPref.getString("contact1_name", "");
        String contact2_name = sharedPref.getString("contact2_name", "");
        String contact3_name = sharedPref.getString("contact3_name", "");



        // Opciones del menu
        String[] menuList = new String[] { "Contacto 1", "Contacto 2", "Contacto 3", "Contacto 4" };
        if (contact0_name.compareTo("") != 0){
            menuList[0] = contact0_name;
        }
        if (contact1_name.compareTo("") != 0){
            menuList[1] = contact1_name;
        }
        if (contact2_name.compareTo("") != 0){
            menuList[2] = contact2_name;
        }
        if (contact3_name.compareTo("") != 0){
            menuList[3] = contact3_name;
        }

        // Creamos un Adapter para acceder a los datos de nuestro listado
        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuList);

        // Enlazamos el adapter con la vista
        ListView listView = (ListView) findViewById(R.id.contact_list);
        listView.setAdapter(adapter);


        // Listener que define que se ejecuta al seleccionar un item de la lista
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                    Intent intent = new Intent();
                                                    intent.setClass(getApplicationContext(), ContactPicker.class);
                                                    intent.putExtra(EXTRA_MESSAGE,position);
                                                    startActivityForResult(intent,RESULT);
                                            }
                                        }
        );

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT:
                    SharedPreferences sharedPref = getSharedPreferences("userContacts", Context.MODE_PRIVATE);
                    String contact0_name = sharedPref.getString("contact0_name", "");
                    String contact1_name = sharedPref.getString("contact1_name", "");
                    String contact2_name = sharedPref.getString("contact2_name", "");
                    String contact3_name = sharedPref.getString("contact3_name", "");


                    // Opciones del menu
                    String[] menuList = new String[] { "Contacto 1", "Contacto 2", "Contacto 3", "Contacto 4" };
                    if (contact0_name.compareTo("") != 0){
                        menuList[0] = contact0_name;
                    }
                    if (contact1_name.compareTo("") != 0){
                        menuList[1] = contact1_name;
                    }
                    if (contact2_name.compareTo("") != 0){
                        menuList[2] = contact2_name;
                    }
                    if (contact3_name.compareTo("") != 0){
                        menuList[3] = contact3_name;
                    }
                    // Creamos un Adapter para acceder a los datos de nuestro listado
                    ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuList);

                    // Enlazamos el adapter con la vista
                    ListView listView = (ListView) findViewById(R.id.contact_list);
                    listView.setAdapter(adapter);

                    break;
            }

        } else {
            Log.e("MainActivity", "Failed to pick contact");
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
