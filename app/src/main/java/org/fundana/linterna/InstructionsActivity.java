package org.fundana.linterna;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by carlosvalls on 1/21/17.
 */

public class InstructionsActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.use_instructions);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
