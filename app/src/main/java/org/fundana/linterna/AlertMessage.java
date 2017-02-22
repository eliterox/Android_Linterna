package org.fundana.linterna;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by carlosvalls on 12/27/16.
 */

public class AlertMessage extends AppCompatActivity {

    EditText message;
    Button saveButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configure_message);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        message = (EditText) findViewById(R.id.message);
        saveButton = (Button) findViewById(R.id.save_button);

        // Validamos si no tenemos un mensaje previamente guardado para mostrar en el campo de texto
        SharedPreferences sharedPref = getSharedPreferences("userMessage", Context.MODE_PRIVATE);
        String savedMsg = sharedPref.getString("userMessage", "");


        if (savedMsg.compareTo("") != 0){
            message.setText(savedMsg);
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMessage(v);
            }
        });



    }

    public void saveMessage(View view){
        SharedPreferences sharedPref = getSharedPreferences("userMessage", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("userMessage", message.getText().toString());
        editor.apply();
        editor.commit();
        Toast.makeText(getApplicationContext(), "Guardado Correctamente", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
