package swe2022.team6.skkumap.Utilities;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import swe2022.team6.skkumap.dataclasses.Owner;
import swe2022.team6.skkumap.dataclasses.TimeTable;
import swe2022.team6.skkumap.dataclasses.UserSetting;

public class FireBaseUtil {
    private static final String TAG = "FireBaseUtil";

    public static boolean setFirebase() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Log.d(TAG, "setFirebase: starting set up firebase");

        if (user != null) {
            Owner owner = Owner.getInstance();
            owner.uid = user.getUid();
            owner.db = FirebaseFirestore.getInstance();
            owner.sref = FirebaseStorage.getInstance().getReference();
            Log.d(TAG, "run: starting");

            // fetching or adding user's own document
            owner.db.collection("users").document(owner.uid)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();

                            if (doc.exists()) {
                                Log.d(TAG, "onComplete: query successful " + doc.getId());
                            } else {
                                Log.d(TAG, "onComplete: query failed");

                                Map<String, String> newDoc = new HashMap<>();
                                newDoc.put("uid", owner.uid);
                                newDoc.put("ttFileUrl", "");
                                newDoc.put("settingFileUrl", "");

                                owner.db.collection("users").document(owner.uid)
                                        .set(newDoc)
                                        .addOnCompleteListener(newTask -> {
                                            if (newTask.isSuccessful()) {
                                                Log.d(TAG, "onComplete: query is finally successful " + doc.getId());
                                            }
                                            else {
                                                Log.e(TAG, "setFirebase: db error");
                                            }
                                        });
                            }
                        }
                        else {
                            Log.d(TAG, "run: task failed");
                        }
                    });

            Log.d(TAG, "setFirebase: returning true");
            return true;
        }
        else {
            Log.e(TAG, "setFirebase: firebase user error");
            return false;
        }
    }

    // db to local file
    public static void syncUserSettingFile() throws IOException {
        final File[] userFile = {FileUtil.getSettingFileObj()};

        Thread thread = new Thread() {
            @Override
            public void run() {
                Gson gson = new Gson();

                Owner owner = Owner.getInstance();

                owner.db.collection("users").document(owner.uid).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult();

                        String settingFileUrl = doc.getString("settingFileUrl");

                        if (settingFileUrl != null && settingFileUrl.equalsIgnoreCase("")) {
                            Log.d(TAG, "run: yes1");
                            // db에서 가져올 file이 없음......
                            FileUtil.exportUserSettingFile();
                            syncUserSettingDb();
                        }
                        else {
                            // download
                            Log.d(TAG, "run: yes2");
                            if (userFile[0].exists()) userFile[0].delete();
                            StorageReference userFileRef = owner.sref.child("setting/" + owner.uid + "_setting.json");
                            userFileRef.getFile(userFile[0])
                                    .addOnSuccessListener(taskSnapshot -> {
                                        try {
                                            Reader reader = new BufferedReader(new FileReader(FileUtil.getUserSettingFilePath()));
                                            owner.mUs = gson.fromJson(reader, UserSetting.class);
                                            reader.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    })
                                    .addOnFailureListener(e -> Log.e(TAG, "onFailure4: damn it"));
                        }
                    }
                });
            }
        };
        thread.start();
    }

    // local file to db
    public static void syncUserSettingDb() {
        final File[] userFile = {FileUtil.getSettingFileObj()};

        Thread thread = new Thread() {
            @Override
            public void run() {
                Owner owner = Owner.getInstance();

                if (userFile[0].exists()) {
                    if (userFile[0].delete()) {
                        userFile[0] = FileUtil.getSettingFileObj();
                    }
                }
                FileUtil.exportUserSettingFile();

                owner.db.collection("users").document(owner.uid).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult();
                        
                        if (doc.exists()) {
                            DocumentReference docRef = doc.getReference();
                            final String userFileUrl = doc.getString("settingFileUrl");

                            if (userFileUrl != null && !userFileUrl.equalsIgnoreCase("")) {
                                StorageReference target = owner.sref.child("setting/" + owner.uid + "_setting.json");

                                target.delete()
                                        .addOnSuccessListener(unused -> {
                                            Log.d(TAG, "run: yeryerterter");
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e(TAG, "run: ffwegwaegawegwe");
                                        });
                                docRef.update("settingFileUrl", "")
                                        .addOnSuccessListener(unused -> {
                                            Log.d(TAG, "run: ssssss");
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.d(TAG, "run: fwwfefefefe");
                                        });
                            }
                            
                            StorageReference userFileRef = owner.sref.child("setting/" + owner.uid + "_setting.json");
                            Uri file = Uri.fromFile(userFile[0]);
                            UploadTask uploadTask = userFileRef.putFile(file);

                            uploadTask.addOnFailureListener(e -> Log.e(TAG, "onFailure3: damn it"))
                                    .addOnSuccessListener(taskSnapshot -> docRef.update("settingFileUrl", userFileRef.getDownloadUrl().toString())
                                            .addOnSuccessListener(unused -> {
                                                Log.d(TAG, "run: update succeed");
                                            })
                                            .addOnFailureListener(e -> Log.d(TAG, "onFailure: update failed"))
                                    );
                        }
                    }
                });
            }
        };
        thread.start();
    }

    public static void syncUserTtFile() throws IOException {
        final File[] userFile = {FileUtil.getTtFileObj()};

        Thread thread = new Thread() {
            @Override
            public void run() {
                Owner owner = Owner.getInstance();

                Gson gson = new Gson();

                owner.db.collection("users").document(owner.uid).get().addOnCompleteListener(task -> {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        final String ttFileUrl = doc.getString("ttFileUrl");
                        if (ttFileUrl != null && ttFileUrl.equalsIgnoreCase("")) {
                            // db에서 가져올 file이 없음......
                            FileUtil.exportUserTtFile();
                            syncUserTtDb();
                        }
                        else {
                            // download
                            if (userFile[0].exists()) userFile[0].delete();
                            StorageReference userFileRef = owner.sref.child("tt/" + owner.uid + "_tt.json");
                            userFileRef.getFile(userFile[0])
                                    .addOnSuccessListener(taskSnapshot -> {
                                        try {
                                            Reader reader = new BufferedReader(new FileReader(FileUtil.getUserTtFilePath()));
                                            owner.mTt = gson.fromJson(reader, TimeTable.class);
                                            reader.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    })
                                    .addOnFailureListener(e -> Log.e(TAG, "onFailure1: damn it"));
                        }
                    }
                });
            }
        };
        thread.start();
    }

    public static void syncUserTtDb() {
        final File[] userFile = {FileUtil.getTtFileObj()};

        Thread thread = new Thread() {
            @Override
            public void run() {
                Owner owner = Owner.getInstance();

                if (!userFile[0].exists()) {
                    FileUtil.exportUserTtFile();
                }

                owner.db.collection("users").document(owner.uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot doc = task.getResult();

                        if (doc.exists()) {
                            DocumentReference docRef = doc.getReference();
                            final String userFileUrl = doc.getString("ttFileUrl");

                            if (userFileUrl != null && userFileUrl.equalsIgnoreCase("")) {
                                StorageReference userFileRef = owner.sref.child("tt/" + owner.uid + "_tt.json");
                                Uri file = Uri.fromFile(userFile[0]);
                                UploadTask uploadTask = userFileRef.putFile(file);

                                uploadTask.addOnFailureListener(e -> Log.e(TAG, "onFailure2: damn it"))
                                        .addOnSuccessListener(taskSnapshot -> docRef.update("ttFileUrl", userFileRef.getDownloadUrl().toString())
                                                .addOnSuccessListener(unused -> Log.d(TAG, "onSuccess: update succeed"))
                                                .addOnFailureListener(e -> Log.d(TAG, "onFailure: update failed"))
                                        );
                            }
                        }
                    }
                });
            }
        };
        thread.start();
    }
}
