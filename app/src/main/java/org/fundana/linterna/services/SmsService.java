package org.fundana.linterna.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;

import org.fundana.linterna.location.GPSTracker;

/**
 * Created by carlosvalls on 1/14/17.
 */

public class SmsService extends IntentService {

    private GPSTracker gps;
    private double latitude;
    private double longitude;
    private Object lock = new Object();

    public static volatile boolean shouldContinue = true;

    public SmsService(){
        super("SmsService");
    }

    public static boolean isShouldContinue() {
        return shouldContinue;
    }

    public static void setShouldContinue(boolean shouldContinue) {
        SmsService.shouldContinue = shouldContinue;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        doStuff();
    }

    // Funcion para el envio de mensaje de emergencia
    // Esta funcion obtiene la latitud y la longitud de los sensores del equipo
    // Concatena el mensaje de emergencia guardado, junto con la cadena de caracteres necesaria para el API de google maps
    public void sendLocationSMS(String phoneNumber, String latitude, String longitude) {
        SharedPreferences sharedPref = getSharedPreferences("userMessage", Context.MODE_PRIVATE);
        String savedMsg = sharedPref.getString("userMessage", "");
        SmsManager smsManager = SmsManager.getDefault();
        StringBuffer smsBody = new StringBuffer(savedMsg);
        smsBody.append(" https://maps.google.com/maps?q=");
        smsBody.append(latitude);
        smsBody.append(",");
        smsBody.append(longitude);
        smsManager.sendTextMessage(phoneNumber, null, smsBody.toString(), null, null);


    }

    private void doStuff() {
        synchronized(lock) {
            while (shouldContinue == true) {
                gps = new GPSTracker(this);
                SharedPreferences sharedPref = getSharedPreferences("userContacts", Context.MODE_PRIVATE);
                // DEBUG:


                // check if GPS enabled
                if (gps.canGetLocation()) {

                    this.latitude = gps.getLatitude();
                    this.longitude = gps.getLongitude();
                }

                String contact0_phone = sharedPref.getString("contact0_phone", "");
                String contact1_phone = sharedPref.getString("contact1_phone", "");
                String contact2_phone = sharedPref.getString("contact2_phone", "");
                String contact3_phone = sharedPref.getString("contact3_phone", "");

                if (contact0_phone.compareTo("") != 0) {
                    sendLocationSMS(contact0_phone, String.valueOf(this.latitude), String.valueOf(this.longitude));
                }
                if (contact1_phone.compareTo("") != 0) {
                    sendLocationSMS(contact1_phone, String.valueOf(this.latitude), String.valueOf(this.longitude));
                }
                if (contact2_phone.compareTo("") != 0) {
                    sendLocationSMS(contact2_phone, String.valueOf(this.latitude), String.valueOf(this.longitude));
                }
                if (contact3_phone.compareTo("") != 0) {
                    sendLocationSMS(contact3_phone, String.valueOf(this.latitude), String.valueOf(this.longitude));
                }
                try {
                    lock.wait(120000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return;
    }
}
