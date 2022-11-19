package swe2022.team6.skkumap;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;

import swe2022.team6.skkumap.Utilities.FireBaseUtil;
import swe2022.team6.skkumap.databinding.ActivityMainBinding;
import swe2022.team6.skkumap.dataclasses.Owner;
import swe2022.team6.skkumap.dataclasses.TimeTable;
import swe2022.team6.skkumap.fragments.MapFragments;
import swe2022.team6.skkumap.fragments.SettingFragments;
import swe2022.team6.skkumap.fragments.TimetableFragments;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private ActivityMainBinding binding;

    private final TimetableFragments ttFrag = new TimetableFragments();
    private final SettingFragments setFrag = new SettingFragments();
    private final MapFragments mapFrag = new MapFragments();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // on start, display the time table
        getSupportFragmentManager().beginTransaction().replace(binding.frameContainer.getId(), ttFrag).commit();

        // set up user data
        Owner owner = Owner.getInstance();
        owner.setmActivity(this);
        FireBaseUtil.setFirebase();
        Log.d(TAG, "onCreate: uid " + owner.uid);
        try {
            FireBaseUtil.syncUserSettingFile();
            FireBaseUtil.syncUserTtFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        createNotificationChannel();//푸시알림 채널 설정
        setPushNoti();//푸시알림 설정

        binding.navigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_timetable:
                    getSupportFragmentManager().beginTransaction().replace(binding.frameContainer.getId(), ttFrag).commit();
                    return true;
                case R.id.action_settings:
                    getSupportFragmentManager().beginTransaction().replace(binding.frameContainer.getId(), setFrag).commit();
                    return true;
                case R.id.action_navigation:
                    getSupportFragmentManager().beginTransaction().replace(binding.frameContainer.getId(), mapFrag).commit();
                    return true;
            }
            return false;
        });


    }


    //그냥 앱 안 켜져있을대 아무때나 푸시알림
    public void setPushNoti() {
        Log.e("TAG","push noti set");
        Intent intent=new Intent(MainActivity.this,AlarmReceiver.class);
        PendingIntent pendingIntent= PendingIntent.getBroadcast(MainActivity.this,0,intent,PendingIntent.FLAG_IMMUTABLE );

        AlarmManager alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);

        long timeAtButtinClick=System.currentTimeMillis();

        long tenSecondsInMillis=1000*10;

        alarmManager.set(AlarmManager.RTC_WAKEUP,timeAtButtinClick+tenSecondsInMillis,pendingIntent);
    }

    void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel0", "notiname", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("channel description");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}