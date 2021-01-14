package main.java;

import java.util.Comparator;

/**
 * Customer comparator to filter the records to match required condition.
 *
 * @author Ziru Hang
 * @version 1.0
 * @since 02-12-2020
 */
public class NewestReadingComparator implements Comparator {
    /**
     * @param o1 The original object to be compared.
     * @param o2 The object want to compared with the original one.
     * @return int Returns -1 when both objects are readings and the compare one is later the the original one;
     * returns 1 when both are readings and the compare one it early than the original one.
     */
    @Override
    public int compare(Object o1, Object o2) {
        Readings r1 = (Readings) o1;
        Readings r2 = (Readings) o2;
        if (r1.getDate().before(r1.getDate())) {
            return -1;
        } else
            return 1;
    }
}
