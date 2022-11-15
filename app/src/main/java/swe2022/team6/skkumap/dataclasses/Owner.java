package swe2022.team6.skkumap.dataclasses;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.grpc.Context;
import swe2022.team6.skkumap.MainActivity;
import swe2022.team6.skkumap.Utilities.FileUtil;

public class Owner extends Application {
    private static final String TAG = "Owner";
    
    private static volatile Owner singleton = null;

    private UserSetting mUs;

    public UserSetting getmUs() {
        return mUs;
    }

    private TimeTable mTt;
    private File userFile;
    private String uid;
    private FirebaseFirestore db;
    private DocumentSnapshot doc;
    private StorageReference sref;

    public static Owner getInstance() {
        if (singleton == null) {
            synchronized (Owner.class) {
                if (singleton == null)
                    singleton = new Owner();
            }
        }
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        mUs = new UserSetting();
        mTt = new TimeTable();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    public void addClassToTimeTable(String name, String classroom, int day, int startHour, int startMinute, int endHour, int endMinute) {
        mTt.addClass(name, classroom, day, startHour, startMinute, endHour, endMinute);
    }

    public void printTimeTable() {
        mTt.printTable();
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setFirebase(FirebaseFirestore db, StorageReference st) throws IOException {
        final String userFilePath = getFilesDir().getAbsolutePath() + this.uid + ".json";
        this.userFile = FileUtil.getFile(userFilePath);
        this.db = db;
        this.sref = st;
        db.collection("users").document(uid)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful())  {
                                    DocumentSnapshot temp = task.getResult();
                                    if (temp.exists()) {
                                        doc = temp;
                                        Log.d(TAG, "onComplete: query successful " + doc.getId());
                                    }
                                    else {
                                        Log.d(TAG, "onComplete: query failed");

                                        Gson gson = new Gson();
                                        if (userFile == null) {
                                            try {
                                                gson.toJson(singleton, new FileWriter(userFilePath));
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        Map<String, String> newDoc = new HashMap<>();
                                        newDoc.put("uid", uid);
                                        newDoc.put("settingUrl", "");
                                        newDoc.put("timetableUrl", "");

                                        db.collection("users").document(uid)
                                                .set(newDoc)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                    }
                                                });
                                    }
                                }
                            }
                        });
    }
}
