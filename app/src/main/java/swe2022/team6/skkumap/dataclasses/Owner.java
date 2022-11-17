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
    private String uid;
    private FirebaseFirestore db;
    private DocumentSnapshot doc;
    private StorageReference sref;

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

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setFirebase(FirebaseFirestore db, StorageReference st) throws IOException {
        // user's file path is absolute and definite
        final String userFilePath = getFilesDir().getAbsolutePath() + this.uid + ".json";
        this.userFile = FileUtil.getFile(userFilePath);

        // access to the db should always be possible
        this.db = db;
        this.sref = st;

        // fetching or adding user's own document
        db.collection("users").document(uid)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                DocumentSnapshot temp = task.getResult();

                                if (temp.exists()) {
                                    doc = temp;
                                    Log.d(TAG, "onComplete: query successful " + doc.getId());
                                } else {
                                    Log.d(TAG, "onComplete: query failed");

                                    Map<String, String> newDoc = new HashMap<>();
                                    newDoc.put("uid", uid);
                                    newDoc.put("OwnerFileUrl", "");

                                    db.collection("users").document(uid)
                                            .set(newDoc)
                                            .addOnCompleteListener(newTask -> {
                                                if (task.isSuccessful()) {
                                                    doc = task.getResult();
                                                    Log.d(TAG, "onComplete: query is finally successful " + doc.getId());
                                                }
                                                else {
                                                    Log.e(TAG, "setFirebase: db error");
                                                }
                                            });
                                }
                            }
                        });

        if (this.userFile == null) {
            Gson gson = new Gson();

            final String OwnerFileUrl = doc.getString("OwnerFileUrl");
            assert OwnerFileUrl != null;
            if (OwnerFileUrl.equalsIgnoreCase("")) {
                try {
                    gson.toJson(singleton, new FileWriter(userFilePath));
                    userFile = FileUtil.getFile(userFilePath);
                    if (userFile == null) {
                        Log.e(TAG, "setFirebase: god damn it!!!");
                    }
                    else {
                        Uri userFileUri = Uri.fromFile(userFile);
                        StorageReference userFileRef = sref.child(userFileUri.getLastPathSegment());
                        UploadTask uploadTask = userFileRef.putFile(userFileUri);

                        Task<Uri> UriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful())
                                    throw task.getException();
                                return userFileRef.getDownloadUrl();
                            }
                        })
                        .addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    Uri downloadUri = task.getResult();
                                }
                                else {

                                }
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else {

        }
    }
}
