package org.fundana.linterna;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import org.fundana.linterna.location.GPSTracker;
import org.fundana.linterna.services.SmsService;


public class MainActivity extends AppCompatActivity{

    ImageButton btnSwitch;
    Button sctButton;

    private Camera camera;
    private boolean isFlashOn;
    private boolean hasFlash;
    private static int count;
    private int serviceStatus = 0;

    private GPSTracker gps;


    private LocationManager locationManager;
    private LocationListener locationListener;

    Parameters params;
    MediaPlayer mp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Compatibilidad de permisos para Android >= 6.0
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);


        // Obtener el boton de la linterna en la interfaz y asignarlo a una variable
        btnSwitch = (ImageButton) findViewById(R.id.button_on_off);

        // Obtener el boton secreto de la interfaz y asignarlo a una variable
        sctButton = (Button) findViewById(R.id.secret_button);

        // Pedimos una locacion inicial para preparar el GPS
        gps = new GPSTracker(this);
        gps.getLocation();


        // First check if device is supporting flashlight or not
        hasFlash = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!hasFlash) {
            // Si el dispositivo no posee flash
            // Muestra mensaje de error y finaliza la ejecucion
            AlertDialog alert = new AlertDialog.Builder(MainActivity.this)
                    .create();
            alert.setTitle("Error");
            alert.setMessage("Sorry, your device doesn't support flash light!");
            alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // closing the application
                    finish();
                    System.exit(0);
                }
            });
            alert.show();
            return;
        }

        // Obtiene el control de la camara
        getCamera();

        // Muestra la imagen del boton
        toggleButtonImage();

        sctButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), SecretMenuActivity.class);
                startActivity(intent);
            }
        });


        // Listener del boton de la linterna
        btnSwitch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isFlashOn) {
                    // turn off flash
                    turnOffFlash();
                } else {
                    // turn on flash
                    turnOnFlash();
                }
            }
        });
    }


    // Get the camera
    private void getCamera() {
        if (camera == null) {
            try {
                camera = Camera.open();
                params = camera.getParameters();
            } catch (RuntimeException e) {
                Log.e("Camera Error.", e.getMessage());
            }
        }
    }


    // Procedimiento para encendido del flash
    private void turnOnFlash() {
        if (!isFlashOn) {
            if (camera == null || params == null) {
                return;
            }
            // play sound
            playSound();

            params = camera.getParameters();
            params.setFlashMode(Parameters.FLASH_MODE_TORCH);
            camera.setParameters(params);
            camera.startPreview();
            isFlashOn = true;

            // changing button/switch image
            toggleButtonImage();
        }

    }


    // Procedimiento para apagar el flash
    private void turnOffFlash() {
        if (isFlashOn) {
            if (camera == null || params == null) {
                return;
            }
            // Reproducir sonido
            playSound();

            params = camera.getParameters();
            params.setFlashMode(Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);
            camera.stopPreview();
            isFlashOn = false;

            // Cambiar el estado de la imagen
            toggleButtonImage();
        }
    }


    // Playing sound
    // will play button toggle sound on flash on / off
    private void playSound() {
        if (isFlashOn) {
            mp = MediaPlayer.create(MainActivity.this, R.raw.light_switch_off);
        } else {
            mp = MediaPlayer.create(MainActivity.this, R.raw.light_switch_on);
        }
        mp.setOnCompletionListener(new OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                // TODO Auto-generated method stub
                mp.release();
            }
        });
        mp.start();
    }

    /*
     * Toggle switch button images
     * changing image states to on / off
     * */
    private void toggleButtonImage() {
        if (isFlashOn) {
            btnSwitch.setImageResource(R.drawable.btn_switch_on);
        } else {
            btnSwitch.setImageResource(R.drawable.btn_switch_off);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)

                return;
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();

        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {
                    //TODO
                    count++;
                    if ((count>=4) && (this.serviceStatus == 0)) {

                        // Leemos los Shared Preferences para verificar que exista mensaje de emergencia y al menos un contacto definido
                        // antes de iniciar el servicio de mensajeria
                        SharedPreferences sharedPrefContacts = getSharedPreferences("userContacts", Context.MODE_PRIVATE);
                        SharedPreferences sharedPrefMessage = getSharedPreferences("userMessage", Context.MODE_PRIVATE);

                        String savedMsg = sharedPrefMessage.getString("userMessage", "");
                        String contact0_phone = sharedPrefContacts.getString("contact0_phone", "");
                        String contact1_phone = sharedPrefContacts.getString("contact1_phone", "");
                        String contact2_phone = sharedPrefContacts.getString("contact2_phone", "");
                        String contact3_phone = sharedPrefContacts.getString("contact3_phone", "");


                        if (savedMsg.compareTo("") != 0){
                            if (contact0_phone.compareTo("") != 0 || contact1_phone.compareTo("") != 0 || contact2_phone.compareTo("") != 0 || contact3_phone.compareTo("") != 0 ){

                                // Si se cumplen todas las condiciones se lanza el servicio de mensajeria
                                Toast.makeText(getApplicationContext(), "S.O.S Activado", Toast.LENGTH_LONG).show();
                                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                // Vibrate for 400 milliseconds
                                v.vibrate(400);
                                count = 0;
                                this.serviceStatus = 1;

                                SmsService.shouldContinue = true;
                                Intent i = new Intent(MainActivity.this, SmsService.class);
                                MainActivity.this.startService(i);
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Debe definirse al menos 1 contacto de emergencia", Toast.LENGTH_LONG).show();
                                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                // Vibrate for 100 milliseconds
                                v.vibrate(10);
                                v.vibrate(10);
                                count = 0;
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Mensaje de emergencia no configurado", Toast.LENGTH_LONG).show();
                            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            // Vibrate for 400 milliseconds
                            v.vibrate(10);
                            v.vibrate(10);
                            count = 0;
                        }
                    }
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    //TODO
                    count++;
                    if ((count>=4) && (this.serviceStatus == 1)) {
                        Toast.makeText(getApplicationContext(), "S.O.S Detenido", Toast.LENGTH_LONG).show();
                        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        // Vibrate for 400 milliseconds
                        v.vibrate(400);
                        count = 0;
                        this.serviceStatus = 0;


                        SmsService.shouldContinue = false;
                        Intent i = new Intent(MainActivity.this, SmsService.class);
                        MainActivity.this.stopService(i);
                    }
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // on pause turn off the flash
        turnOffFlash();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // on resume turn on the flash
        if(hasFlash)
            turnOffFlash();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // on starting the app get the camera params
        getCamera();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // on stop release the camera
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }


}
