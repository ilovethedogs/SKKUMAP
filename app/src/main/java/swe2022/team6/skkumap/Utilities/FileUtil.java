package swe2022.team6.skkumap.Utilities;

import android.util.Log;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import swe2022.team6.skkumap.dataclasses.Owner;
import swe2022.team6.skkumap.dataclasses.UserSetting;

public class FileUtil {
    private static final String TAG = "FileUtil";

    private static Owner owner1 = Owner.getInstance();
    private static final String userSettingFilePath = owner1.getFilesDir().getAbsolutePath() + '/' + owner1.uid + "_setting.json";
    private static final String userTtFilePath = owner1.getFilesDir().getAbsolutePath() + '/' + owner1.uid + "_tt.json";
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
        Owner owner = Owner.getInstance();
        Gson gson = new Gson();
        try {
            Log.e("TAG",owner.uid+"");
//            Log.e("TAG",owner.mTt.toString()+"");
//            Log.e("TAG",owner.mUs.getNotiMthd()+"");
//            Log.e("TAG", gson.toJson(owner.getmUs(), new TypeToken<UserSetting>(){}.getType()));
            FileWriter fw = new FileWriter(userSettingFilePath);
            gson.toJson(owner.mUs, fw);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exportUserTtFile() {
        Owner owner = Owner.getInstance();
        if (userTtFileObj.exists()) userTtFileObj.delete();
        Gson gson = new Gson();

        try {
            FileWriter fw = new FileWriter(userTtFileObj);
            gson.toJson(owner.getmTt(), fw);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exportUserSettingFileJoining() {
        Owner owner = Owner.getInstance();
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
        } catch (InterruptedException e) {
            Log.e(TAG, "exportFileJoining: ");
        }
    }

    public static void exportUserTtFileJoining() {
        Owner owner = Owner.getInstance();
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
        } catch (InterruptedException e) {
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
