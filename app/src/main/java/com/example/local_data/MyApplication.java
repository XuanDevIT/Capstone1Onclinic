package com.example.local_data;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import com.example.onclinic.R;

public class MyApplication extends Application {
    public static final String CHANNEL_ID = "CHANNEL_1";
    public static final String CHANNEL_ID_2 = "CHANNEL_2";

    @Override
    public void onCreate() {
        super.onCreate();
        DataLocalManager.init(getApplicationContext());
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        //Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);//hieu ung rung
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Uri sound = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.sound_notification_custom);
            //vibrator.vibrate(VibrationEffect.createOneShot(1000,VibrationEffect.DEFAULT_AMPLITUDE));
            AudioAttributes attributes = new AudioAttributes.Builder().
                    setUsage(AudioAttributes.USAGE_NOTIFICATION).build();

            //channel 1
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.setSound(uri, attributes);
            channel.setVibrationPattern(new long[]{500,100,100,100});

            //channel 2
            CharSequence name_2 = getString(R.string.channel_name_2);
            String description_2 = getString(R.string.channel_description_2);
            int importance_2 = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel_2 = new NotificationChannel(CHANNEL_ID_2, name_2, importance_2);
            channel_2.setDescription(description_2);
            channel_2.setSound(sound, attributes);
            channel_2.setVibrationPattern(new long[]{1000,2000,1000,2000,1000,2000});
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if(notificationManager!=null) {
                notificationManager.createNotificationChannel(channel);
                notificationManager.createNotificationChannel(channel_2);
            }
        }
        //else vibrator.vibrate(1000);
    }
}
