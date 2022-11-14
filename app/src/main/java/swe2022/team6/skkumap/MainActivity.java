package swe2022.team6.skkumap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;

import swe2022.team6.skkumap.databinding.ActivityMainBinding;
import swe2022.team6.skkumap.dataclasses.Owner;
import swe2022.team6.skkumap.dataclasses.TimeTable;
import swe2022.team6.skkumap.fragments.MapFragments;
import swe2022.team6.skkumap.fragments.SettingFragments;
import swe2022.team6.skkumap.fragments.TimetableFragments;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private final TimetableFragments ttFrag = new TimetableFragments();
    private final SettingFragments setFrag = new SettingFragments();
    private final MapFragments mapFrag = new MapFragments();

    private FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportFragmentManager().beginTransaction().replace(binding.frameContainer.getId(), setFrag).commit();
        Owner owner = Owner.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            owner.setUid(user.getUid());
            firebaseStorage = FirebaseStorage.getInstance();
            try {
                owner.setFirebase(FirebaseFirestore.getInstance(), FirebaseStorage.getInstance().getReference());
            } catch (IOException e) {
                e.printStackTrace();
            }
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