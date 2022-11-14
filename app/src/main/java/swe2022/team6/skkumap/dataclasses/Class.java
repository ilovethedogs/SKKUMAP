package swe2022.team6.skkumap.dataclasses;

import androidx.annotation.NonNull;

public class Class {
    private final String mName;
    private final String mClassroom;
    private final Week mDay;
    private final classTime mBegTime;
    private final classTime mEndTime;

    public Class() {
        mName = "";
        mClassroom = "";
        mDay = Week.MON;
        mBegTime = new classTime();
        mEndTime = new classTime();
    }

    public Class(String name, String classroom, Week day, int startHour, int startMinute, int endHour, int endMinute) {
        this.mName = name;
        this.mClassroom = classroom;
        this.mDay = day;

        this.mBegTime = new classTime();
        this.mEndTime = new classTime();

        this.mBegTime.setHour(startHour);
        this.mBegTime.setMinute(startMinute);

        if (startHour > endHour || (startHour == endHour && startMinute >= endMinute)) {
            this.mEndTime.setHour(startHour + 1);
        }
        else {
            this.mEndTime.setHour(endHour);
        }
        this.mEndTime.setMinute(endMinute);
    }

    @NonNull
    public String toString() {
        return    "Name     : " + this.mName + '\n'
                + "Classroom: " + this.mClassroom + '\n'
                + "Day      : " + this.mDay.toString() + '\n'
                + "Begin at : " + this.mBegTime.getHour() + ":" + this.mBegTime.getMinute() + '\n'
                + "End at   : " + this.mEndTime.getHour() + ":" + this.mEndTime.getMinute();
    }

    public String getName() {
        return mName;
    }

    public String getClassroom() {
        return mClassroom;
    }

    public Week getDay() {
        return mDay;
    }

    public classTime getBegTime() {
        return mBegTime;
    }

    public classTime getEndTime() {
        return mEndTime;
    }

    public enum Week {
        MON, TUE, WED, THU, FRI, SAT, SUN
    }

    public static class classTime {
        private int hour;
        private int minute;

        public classTime() {
            this.hour = 0;
            this.minute = 0;
        }

        public classTime(int h, int m) {
            setHour(h);
            setMinute(m);
        }

        public void setHour(int h) {
            if (h >= 0 && h <= 23) {
                this.hour = h;
            }
        }

        public void setMinute(int m) {
            if (m >= 0 && m <= 59) {
                this.minute = m;
            }
        }

        public int getHour() {
            return this.hour;
        }

        public int getMinute() {
            return this.minute;
        }

        public static boolean lesser(classTime lhs, classTime rhs) {
            return lhs.getHour() < rhs.getHour() || (lhs.getHour() == rhs.getHour() && lhs.getMinute() < rhs.getMinute());
        }

        public static boolean greater(classTime lhs, classTime rhs) {
            return lhs.getHour() > rhs.getHour() || (lhs.getHour() == rhs.getHour() && lhs.getMinute() > rhs.getMinute());
        }

        @NonNull
        @Override
        public String toString() {
            return hour + ":" + minute;
        }
    }
}