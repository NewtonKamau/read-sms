package com.example.myapplication2;


import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class MainActivity extends AppCompatActivity  {
    private static final int PERMISSION_REQUEST_READ_CONTACTS = 100;

    final int APP_PERMISSIONS_REQUEST_RESULT = 1;
    private static final Logger log = Logger.getLogger(MainActivity.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int PERMISSION_CHECK = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);

        if (PERMISSION_CHECK == PackageManager.PERMISSION_GRANTED){
            getAllSms();
        }else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, PERMISSION_REQUEST_READ_CONTACTS);
        }
        List<Sms> lst = getAllSms();
        listSms(lst);
    }


    public List<Sms> getAllSms() {
        List<Sms> lstSms = new ArrayList<Sms>();
        Sms objSms = new Sms();
        Uri message = Uri.parse("content://sms/");
        ContentResolver cr = getApplicationContext().getContentResolver();

        Cursor c = cr.query(message, null, null, null, null);
        int totalSMS = c.getCount();

        if (c.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {

                objSms = new Sms();
                objSms.setId(c.getString(c.getColumnIndexOrThrow("_id")));
                objSms.setAddress(c.getString(c
                        .getColumnIndexOrThrow("address")));
                objSms.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
                objSms.setReadState(c.getString(c.getColumnIndex("read")));
                objSms.setTime(c.getString(c.getColumnIndexOrThrow("date")));
                if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
                    objSms.setFolderName("inbox");
                } else {
                    objSms.setFolderName("sent");
                }

                lstSms.add(objSms);
                c.moveToNext();
            }

            Toast.makeText(this, "No message to show!", Toast.LENGTH_SHORT).show();

        }
        c.close();

        return lstSms;
    }

    private void listSms(List<Sms> smsList) {
        for (Sms sms : smsList) {
            log.info(" address: " + sms.getAddress() + "############# msg: " + sms.getMsg());
        }
    }


}
