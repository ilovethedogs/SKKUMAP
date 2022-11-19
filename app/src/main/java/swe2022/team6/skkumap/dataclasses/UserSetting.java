package swe2022.team6.skkumap.dataclasses;

import android.util.Log;

import java.io.IOException;

import swe2022.team6.skkumap.Utilities.FireBaseUtil;

public class UserSetting {
    private static final String TAG = "UserSetting";

    //TODO set 할 때 firebase에 업데이트
    private boolean notiActivate = false;
    private int notiHr = 0;
    private int notiMin = 15;
    private boolean notiLoc = false;
    private int notiMthd = 0;//0: vibration 1: sound 2: push

    public UserSetting() {
        this.notiActivate = false;
        this.notiHr = 0;
        this.notiMin = 15;
        this.notiLoc = false;
        this.notiMthd = 0;
    }


    public boolean isNotiActivate() {
        return notiActivate;
    }

    public void setNotiActivate(boolean notiActivate) {
        this.notiActivate = notiActivate;
        try {

            FireBaseUtil.syncUserSettingFile();
        }
        catch (IOException e) {
            Log.e(TAG, "setNotiActivate: dd");
        }
        FireBaseUtil.syncUserSettingDb();
    }

    public int getNotiHr() {
        return notiHr;
    }

    public void setNotiHr(int notiHr) {
        this.notiHr = notiHr;
    }

    public int getNotiMin() {
        return notiMin;
    }

    public void setNotiMin(int notiMin) {
        this.notiMin = notiMin;
    }

    public boolean isNotiLoc() {
        return notiLoc;
    }

    public void setNotiLoc(boolean notiLoc) {
        this.notiLoc = notiLoc;
    }

    public int getNotiMthd() {
        return notiMthd;
    }

    public void setNotiMthd(int notiMthd) {
        this.notiMthd = notiMthd;
    }


}
