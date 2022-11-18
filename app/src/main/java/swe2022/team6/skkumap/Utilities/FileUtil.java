package swe2022.team6.skkumap.Utilities;

import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import swe2022.team6.skkumap.dataclasses.Owner;

public class FileUtil {
    private static final String TAG = "FileUtil";
    
    private static Owner owner = Owner.getInstance();

    private static final String userFilePath = owner.getFilesDir().getAbsolutePath() + owner.uid + ".json";

    public static File getFileObj() throws IOException {
        final File[] result = {null};

        Thread thread = new Thread() {
            @Override
            public void run() {
                result[0] = new File(userFilePath);
            }
        };
        thread.start();

        try {
            thread.join();

            if (!result[0].exists()) {
                return null;
            }
            else {
                return result[0];
            }
        }
        catch (InterruptedException e) {
            Log.e(TAG, "getFile:");
            return null;
        }
    }

    public static void exportFile() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Gson gson = new Gson();

                try {
                    gson.toJson(owner, new FileWriter(userFilePath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    public static void exportFileJoining() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Gson gson = new Gson();

                try {
                    gson.toJson(owner, new FileWriter(userFilePath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();

        try {
            thread.join();
        }
        catch (InterruptedException e) {
            Log.e(TAG, "exportFileJoining: ");
        }
    }

    public static void importFile() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Gson gson = new Gson();

                try {
                    String path = String.valueOf(userFilePath);
                }
            }
        }
    }

    public static void importFileJoining() {

    }

    public static String getUserFilePath() {
        return userFilePath;
    }
}
