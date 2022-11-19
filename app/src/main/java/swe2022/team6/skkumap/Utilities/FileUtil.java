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

    private static final String userSettingFilePath = owner.getFilesDir().getAbsolutePath() + '/' + owner.uid + "_setting.json";
    private static final String userTtFilePath = owner.getFilesDir().getAbsolutePath() + '/' + owner.uid + "_tt.json";
    private static final File userSettingFileObj = new File(userSettingFilePath);
    private static final File userTtFileObj = new File(userTtFilePath);

    public static File getSettingFileObj() {
        return userSettingFileObj;
    }

    public static File getTtFileObj() {
        return userTtFileObj;
    }

    public static void exportUserSettingFile() {
        if (userSettingFileObj.exists()) userSettingFileObj.delete();
        Gson gson = new Gson();
        try {
            gson.toJson(owner.getmUs(), new FileWriter(userSettingFilePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exportUserTtFile() {
        if (userTtFileObj.exists()) userTtFileObj.delete();
        Thread thread = new Thread() {
            @Override
            public void run() {
                Gson gson = new Gson();

                try {
                    gson.toJson(owner.getmTt(), new FileWriter(userTtFilePath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    public static void exportUserSettingFileJoining() {
        if (userSettingFileObj.exists()) userSettingFileObj.delete();
        Thread thread = new Thread() {
            @Override
            public void run() {
                Gson gson = new Gson();

                try {
                    gson.toJson(owner.getmUs(), new FileWriter(userSettingFilePath));
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

    public static void exportUserTtFileJoining() {
        if (userTtFileObj.exists()) userTtFileObj.delete();
        Thread thread = new Thread() {
            @Override
            public void run() {
                Gson gson = new Gson();

                try {
                    gson.toJson(owner.getmTt(), new FileWriter(userTtFilePath));
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

    /*
    public static boolean importFile() {
        if (!userFileObj.exists()) return false;
        Thread thread = new Thread() {
            @Override
            public void run() {
                Gson gson = new Gson();
            }
        };
        return true;
    }

    public static boolean importFileJoining() {
        return true;
    }
     */

    public static String getUserSettingFilePath() {
        return userSettingFilePath;
    }

    public static String getUserTtFilePath() {
        return userTtFilePath;
    }
}
