package com.example.myapplication2;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.widget.Toast;


public class SmsListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();
                    try {
                        if (senderNum.contains("MOB_NUMBER")) {
                            Toast.makeText(context,"",Toast.LENGTH_SHORT).show();

                            Intent intentCall = new Intent(context, MainActivity.class);
                            intentCall.putExtra("message", currentMessage.getMessageBody());

                            PendingIntent pendingIntent= PendingIntent.getActivity(context, 0, intentCall, PendingIntent.FLAG_UPDATE_CURRENT);
                            pendingIntent.send();
                        }
                    } catch (Exception e) {
                    }
                }
            }
        } catch (Exception e) {
        }
    }

}
