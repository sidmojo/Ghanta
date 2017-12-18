package com.example.siddharthmajumdar.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Mehul Garg on 23-09-2017.
 */

public class Alarm_Receiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {

        // fetch the extra string from intent
        String get_your_string = intent.getExtras().getString("extra");
        String abc = intent.getExtras().getString("extra1");

        // create an intent to the ringtone service
        Intent service_intent = new Intent(context, RingTonePlayingService.class);

        // pass the extra string from Main Activity to the Ring Tone Playing Service
        service_intent.putExtra("extra", get_your_string);


        // start the ringtone service
        context.startService(service_intent);

    }
}
