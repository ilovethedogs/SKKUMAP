package swe2022.team6.skkumap.dataclasses;

public class UserSetting {
    private boolean notiActivate = false;
    private int notiHr = 0;
    private int notiMin = 0;
    private boolean notiLoc = false;

    public boolean isNotiActivate() {
        return notiActivate;
    }

    public void setNotiActivate(boolean notiActivate) {
        this.notiActivate = notiActivate;
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

    private int notiMthd = 0;//0: vibration 1: sound 2: push
}
