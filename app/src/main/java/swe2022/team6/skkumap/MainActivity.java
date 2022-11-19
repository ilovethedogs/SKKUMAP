package swe2022.team6.skkumap;

import androidx.appcompat.app.AppCompatActivity;

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
        owner.addClassToTimeTable("class1", "room1", 0, 0, 0, 1, 1);
        owner.addClassToTimeTable("class2", "room2", 0, 1, 2, 2, 1);
        owner.addClassToTimeTable("class3", "room3", 0, 2, 2, 3, 1);
        owner.addClassToTimeTable("class3", "room3", 0, 2, 42, 3, 3);

        try {
            FireBaseUtil.syncUserSettingFile();
            FireBaseUtil.syncUserTtFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
}