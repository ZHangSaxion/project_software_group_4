package main.java;

import java.util.Comparator;

public class NewestReadingComparator implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        Readings r1 = (Readings) o1;
        Readings r2 = (Readings) o2;
        if(r1.getDate().before(r1.getDate())){
            return -1;
        }else
            return 1;
    }
}
