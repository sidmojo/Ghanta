package com.example.siddharthmajumdar.alarm;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Random;


public class MainActivity extends AppCompatActivity {


    AlarmManager alarm_manager;
    TimePicker alarm_timepicker;
    TextView update_text;
    Context context;
    PendingIntent pending_intent;
    Uri Alarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
    public static final String PREFS = "examplePrefs";
    public static final String PREFS1 = "examplePrefs";
    public static final String PREFS2 = "examplePrefs";
    public static final String PREFS3 = "examplePrefs";
    String abc="";
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.context = this;

        // initialize alarm manager
        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // initialize our time picker
        alarm_timepicker = (TimePicker) findViewById(R.id.timePicker);

        // initialize out update box
        update_text = (TextView) findViewById(R.id.update_text);
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
        // create an instance of an calendar

        final Calendar calendar = Calendar.getInstance();

        // initialize buttons
        Button alarm_on = (Button) findViewById(R.id.alarm_on);
        Button alarm_off = (Button) findViewById(R.id.alarm_off);


        //create an intent to Alarm receiver class
        final Intent my_intent = new Intent(this.context,Alarm_Receiver.class);

        SharedPreferences example = getSharedPreferences(PREFS, 0);
        String f = example.getString("message","not found");
        if(f.equals("not found"))
        {
            // method that updates text
            set_alarm_text("Did you set the alarm ?");
        }
        else if(f.equals("Alarm off!"))
        {
            set_alarm_text("Did you set the alarm ?");
        }
        else
        {
            set_alarm_text(f);
        }

        // create an on click listener for buttons
        alarm_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get the int values of the hour and minute
                int hour = alarm_timepicker.getCurrentHour();
                int minute = alarm_timepicker.getCurrentMinute();



                    // setting calendar instance with the hour and minute we picked on time picker
                    calendar.set(Calendar.HOUR_OF_DAY, alarm_timepicker.getCurrentHour());
                calendar.set(Calendar.MINUTE, alarm_timepicker.getCurrentMinute());
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                // convert int to string

                String hour_string = String.valueOf(hour);
                String minute_string = String.valueOf(minute);

                if(hour>12)
                {
                    hour_string = String.valueOf(hour - 12);
                }
                if(hour==0)
                {
                    hour_string = "00";
                }
                if(minute<10)
                {
                    minute_string = "0" + String.valueOf(minute);
                }

                // method that updates text
                String h = "Alarm set at " + hour_string + ":" + minute_string +".";
                set_alarm_text(h);

                SharedPreferences exampleprefs = getSharedPreferences(PREFS, 0);
                SharedPreferences.Editor editor = exampleprefs.edit();
                editor.putString("message", h);
                editor.commit();

                // put in extra string in my intent to tell clock you pressed on button
                my_intent.putExtra("extra", "alarm on");


                // create a pending intent that delays the intnet until the specified calendar time
                pending_intent = PendingIntent.getBroadcast(MainActivity.this, 0, my_intent, PendingIntent.FLAG_UPDATE_CURRENT);

                // set alarm manager
                alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);
            }
        });

        alarm_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                final View mView = getLayoutInflater().inflate(R.layout.custom_dialog, null);
                final TextView question = (TextView) mView.findViewById(R.id.textView);
                final EditText answer =  mView.findViewById(R.id.edit_text);
                Button ok = (Button) mView.findViewById(R.id.button);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                char operator[]={'+','-','*'};

                Random r1 = new Random();
                char op = operator[r1.nextInt(3 - 0) + 0];

                Random r2 = new Random();
                int i1 = r2.nextInt(10 - 1) + 1;

                Random r3 = new Random();
                int i2 = r3.nextInt(10 - 1) + 10;

                int i3=0;

                switch(op)
                {
                    case '+':
                        i3 = i1 + i2;
                        break;
                    case '-':
                        i3 = i1 - i2;
                        break;
                    case '*':
                        i3 = i1 * i2;
                        break;
                    default:
                }

                final String abc1 = String.valueOf(i3);
                question.setText(i1+" "+op+" "+i2+" = ?");

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (answer.getText().toString().isEmpty()) {
                            Toast.makeText(MainActivity.this, "Please enter the answer to shut the alarm.", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {

                            String abc = answer.getText().toString();

                            if(abc.equals(abc1)) {

                                set_alarm_text("Alarm off!");

                                SharedPreferences example1 = getSharedPreferences(PREFS, 0);
                                SharedPreferences.Editor editor = example1.edit();
                                editor.putString("message", "Alarm off!");
                                editor.commit();

                                // put in extra string in my intent to tell clock you pressed off button
                                my_intent.putExtra("extra", "alarm off");

                                pending_intent = PendingIntent.getBroadcast(MainActivity.this, 0, my_intent, PendingIntent.FLAG_UPDATE_CURRENT);


                                // cancel the pending intent
                                alarm_manager.cancel(pending_intent);

                                // stop the ringtone
                                sendBroadcast(my_intent);

                                dialog.dismiss();
                                NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.cancel(0);
                            }
                            else  {
                                Toast.makeText(MainActivity.this, "Wrong Answer! Try Again!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });
    }



    private void set_alarm_text(String output) {

        update_text.setText(output);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this.context,SettingsActivity.class);
            this.context.startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
