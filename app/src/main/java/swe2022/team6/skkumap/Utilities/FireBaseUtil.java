package swe2022.team6.skkumap.Utilities;

import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import swe2022.team6.skkumap.dataclasses.Owner;

public class FireBaseUtil {
    private static final String TAG = "FireBaseUtil";
    
    private static Owner owner = Owner.getInstance();

    public static void setFirebase() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            owner.uid = user.getUid();

            Thread thread = new Thread() {
                @Override
                public void run() {
                    owner.db = FirebaseFirestore.getInstance();
                    owner.sref = FirebaseStorage.getInstance().getReference();

                    // fetching or adding user's own document
                    owner.db.collection("users").document(owner.uid)
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot temp = task.getResult();

                                    if (temp.exists()) {
                                        owner.doc = temp;
                                        Log.d(TAG, "onComplete: query successful " + owner.doc.getId());
                                    } else {
                                        Log.d(TAG, "onComplete: query failed");

                                        Map<String, String> newDoc = new HashMap<>();
                                        newDoc.put("uid", owner.uid);
                                        newDoc.put("OwnerFileUrl", "");

                                        owner.db.collection("users").document(owner.uid)
                                                .set(newDoc)
                                                .addOnCompleteListener(newTask -> {
                                                    if (task.isSuccessful()) {
                                                        owner.doc = task.getResult();
                                                        Log.d(TAG, "onComplete: query is finally successful " + owner.doc.getId());
                                                    }
                                                    else {
                                                        Log.e(TAG, "setFirebase: db error");
                                                    }
                                                });
                                    }
                                }
                            });
                }
            };
            thread.start();

            try {
                thread.join();
            }
            catch (InterruptedException e) {
                Log.e(TAG, "setFirebase: error setting firebase");
            }
        }
        else {
            Log.e(TAG, "setFirebase: firebase user error");
        }
    }

    public void syncUserFile() throws IOException {
        final File[] userFile = {FileUtil.getFileObj()};

        if (userFile[0] == null) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    Gson gson = new Gson();

                    final String OwnerFileUrl = owner.doc.getString("OwnerFileUrl");
                    if(OwnerFileUrl != null && OwnerFileUrl.equalsIgnoreCase(""))
                    {
                        try {
                            FileUtil.exportFileJoining();
                            userFile[0] = FileUtil.getFileObj();
                            if (userFile[0] == null) {
                                Log.e(TAG, "setFirebase: god damn it!!!");
                            } else {
                                Uri userFileUri = Uri.fromFile(userFile[0]);
                                StorageReference userFileRef = owner.sref.child(userFileUri.getLastPathSegment());
                                UploadTask uploadTask = userFileRef.putFile(userFileUri);

                                Task<Uri> UriTask = uploadTask.continueWithTask(task -> {
                                            if (!task.isSuccessful())
                                                throw task.getException();
                                            return userFileRef.getDownloadUrl();
                                        })
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                Uri downloadUri = task.getResult();
                                            } else {

                                            }
                                        });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            thread.start();
        }
        else {

        }
    }

    public void syncDb() {

    }
}
