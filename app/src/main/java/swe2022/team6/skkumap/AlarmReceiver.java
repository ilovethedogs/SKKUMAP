package swe2022.team6.skkumap;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

public class AlarmReceiver extends BroadcastReceiver {

    Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel0")
                .setSmallIcon(R.drawable.skkumap_logo)
                .setContentTitle("You have to move!")
                .setContentText("You have 10 minutes until SW Engineering class")
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_HIGH)
                ;//TODO tap action
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(200, builder.build());
    }

}
