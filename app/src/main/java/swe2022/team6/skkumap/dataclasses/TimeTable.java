package swe2022.team6.skkumap.dataclasses;

import android.util.Log;

import java.util.Comparator;
import java.util.Hashtable;
import java.util.Map;
import java.util.TreeMap;

public class TimeTable {
    private static final String TAG = "TimeTable";

    private Map<Class.Week, TreeMap<Class.classTime, Class>> mClassTable;
    private final ClassComparator comp;

    public TimeTable() {
        mClassTable = new Hashtable<>();
        comp = new ClassComparator();
        TreeMap<Class.classTime, Class> monMap = new TreeMap<>(comp);
        TreeMap<Class.classTime, Class> tueMap = new TreeMap<>(comp);
        TreeMap<Class.classTime, Class> wedMap = new TreeMap<>(comp);
        TreeMap<Class.classTime, Class> thuMap = new TreeMap<>(comp);
        TreeMap<Class.classTime, Class> friMap = new TreeMap<>(comp);
        TreeMap<Class.classTime, Class> satMap = new TreeMap<>(comp);
        TreeMap<Class.classTime, Class> sunMap = new TreeMap<>(comp);
        mClassTable.put(Class.Week.MON, monMap);
        mClassTable.put(Class.Week.TUE, tueMap);
        mClassTable.put(Class.Week.WED, wedMap);
        mClassTable.put(Class.Week.THU, thuMap);
        mClassTable.put(Class.Week.FRI, friMap);
        mClassTable.put(Class.Week.SAT, satMap);
        mClassTable.put(Class.Week.SUN, sunMap);
    }

    public void addClass(String name, String classroom, int day, int startHour, int startMinute, int endHour, int endMinute) {
        Class.Week key;
        switch (day) {
            case 1: key = Class.Week.TUE; break;
            case 2: key = Class.Week.WED; break;
            case 3: key = Class.Week.THU; break;
            case 4: key = Class.Week.FRI; break;
            case 5: key = Class.Week.SAT; break;
            case 6: key = Class.Week.SUN; break;
            default: key = Class.Week.MON;
        };

        Class newClass = new Class(name, classroom, key, startHour, startMinute, endHour, endMinute);

        Map.Entry<Class.classTime, Class> precedingClass = mClassTable.get(key).lowerEntry(newClass.getBegTime());
        Map.Entry<Class.classTime, Class> followingClass = mClassTable.get(key).higherEntry(newClass.getBegTime());

        boolean firstCondition = precedingClass == null || Class.classTime.lesser(newClass.getBegTime(), precedingClass.getValue().getEndTime());
        boolean secondCondition = followingClass == null || Class.classTime.lesser(newClass.getEndTime(), followingClass.getValue().getBegTime());
        Log.d(TAG, "addClass: " + firstCondition + ' ' + secondCondition);
        if (firstCondition && secondCondition) {
            mClassTable.get(key).put(newClass.getBegTime(), newClass);
        }
    }

    // edit what?
    public void editClass(Class target, Class newClass) {
        mClassTable.get(target.getDay()).remove(target.getBegTime());
        mClassTable.get(target.getDay()).put(newClass.getBegTime(), newClass);
    }

    public void removeClass(Class target) {
        mClassTable.get(target.getDay()).remove(target.getBegTime());
    }

    public String toString() {
        return "";
    }

    public void printTable() {
        System.out.println("MON");
        for (Map.Entry<Class.classTime, Class> c : mClassTable.get(Class.Week.MON).entrySet()) {
            System.out.println(c.getValue().toString());
            System.out.println();
        }
        System.out.println();

        System.out.println("TUE");
        for (Map.Entry<Class.classTime, Class> c : mClassTable.get(Class.Week.TUE).entrySet()) {
            System.out.println(c.getValue().toString());
            System.out.println();
        }
        System.out.println();

        System.out.println("WED");
        for (Map.Entry<Class.classTime, Class> c : mClassTable.get(Class.Week.WED).entrySet()) {
            System.out.println(c.getValue().toString());
        }
        System.out.println();

        System.out.println("THU");
        for (Map.Entry<Class.classTime, Class> c : mClassTable.get(Class.Week.THU).entrySet()) {
            System.out.println(c.getValue().toString());
            System.out.println();
        }
        System.out.println();

        System.out.println("FRI");
        for (Map.Entry<Class.classTime, Class> c : mClassTable.get(Class.Week.FRI).entrySet()) {
            System.out.println(c.getValue().toString());
            System.out.println();
        }
        System.out.println();

        System.out.println("SAT");
        for (Map.Entry<Class.classTime, Class> c : mClassTable.get(Class.Week.SAT).entrySet()) {
            System.out.println(c.getValue().toString());
            System.out.println();
        }
        System.out.println();

        System.out.println("SUN");
        for (Map.Entry<Class.classTime, Class> c : mClassTable.get(Class.Week.SUN).entrySet()) {
            System.out.println(c.getValue().toString());
            System.out.println();
        }
        System.out.println();
    }

    static class ClassComparator implements Comparator<Class.classTime> {
        @Override
        public int compare(Class.classTime o1, Class.classTime o2) {
            int selector = o1.getHour() ^ o2.getHour();
            return (o1.getHour() - o2.getHour()) * (selector & 0x1)
                    + (o1.getMinute() - o2.getMinute()) * (selector ^ 0x1);
        }
    }
}
