package com.askhmer.lockscreen.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.askhmer.lockscreen.model.Reciver;

public class SmsBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = SmsBroadcastReceiver.class.getSimpleName();
    public static final String SMS_CONTENT = "sms_content";
    private Reciver reciver;

    public SmsBroadcastReceiver(Context context) {
        this.reciver = (Reciver) context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs;
            String sender;
            if (bundle != null) {
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        // Here you have the sender(phone number)
                        sender = msgs[i].getOriginatingAddress();
                        String msgBody = msgs[i].getMessageBody();
                        String verifyNumber = msgBody.replaceAll("[^\\.0123456789]", "");

                        // you have the sms content in the msgBody
                        if(sender.equals("MEDAYI")|| sender == "MEDAYI") {
                            reciver.setTextToTextView(verifyNumber);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }
}