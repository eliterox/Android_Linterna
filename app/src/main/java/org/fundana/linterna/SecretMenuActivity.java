package org.fundana.linterna;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by carlosvalls on 12/23/16.
 */

public class SecretMenuActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secret_menu);

        // Opciones del menu de configuracion
        String[] menuList = new String[] { "Configurar mensaje de emergencia", "Contactos de emergencia"};
        // Opciones del menu de ayuda
        String[] helpList = new String[] { "Instrucciones de configuracion","Instrucciones de uso"};

        // Creamos un Adapter para acceder a los datos de nuestro listado
        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuList);
        ListAdapter adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, helpList);


        // Enlazamos el adapter con la vista
        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(adapter);
        ListView listView2 = (ListView) findViewById(R.id.list_help);
        listView2.setAdapter(adapter2);


        // Listener que define que se ejecuta al seleccionar un item de la lista
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                if (position == 0){
                                                    Intent intent = new Intent();
                                                    intent.setClass(getApplicationContext(), AlertMessage.class);
                                                    startActivity(intent);
                                                }
                                                if (position == 1){
                                                    //Toast.makeText(getApplicationContext(), "Toast de Prueba 2", Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent();
                                                    intent.setClass(getApplicationContext(), ContactList.class);
                                                    startActivity(intent);
                                                }
                                            }
                                        }
        );

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                if (position == 0) {
                                                    //Toast.makeText(getApplicationContext(), "Toast de Prueba 2", Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent();
                                                    intent.setClass(getApplicationContext(), ConfigInstructionsActivity.class);
                                                    startActivity(intent);
                                                }
                                                if (position == 1) {
                                                    //Toast.makeText(getApplicationContext(), "Toast de Prueba 2", Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent();
                                                    intent.setClass(getApplicationContext(), InstructionsActivity.class);
                                                    startActivity(intent);
                                                }
                                            }
                                        }
        );



    }
}
