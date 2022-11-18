package swe2022.team6.skkumap.dataclasses;

import android.app.Activity;
import android.app.Application;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
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
import com.google.firebase.storage.UploadTask;
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

    public String uid;
    public FirebaseFirestore db;
    public DocumentSnapshot doc;
    public StorageReference sref;

    public Activity getmActivity() {
        return mActivity;
    }

    public void setmActivity(Activity mActivity) {
        this.mActivity = mActivity;
    }

    private Activity mActivity=null;

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


}
