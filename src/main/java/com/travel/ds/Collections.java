package com.travel.ds;

import java.util.Comparator;

public final class Collections {
    private Collections() {
    }

    public static <T extends Comparable<? super T>> void sort(List<T> list) {
        sort(list, Comparator.naturalOrder());
    }

    public static <T> void sort(List<T> list, Comparator<? super T> comparator) {
        // In-place insertion sort keeps implementation simple and dependency-free.
        for (int i = 1; i < list.size(); i++) {
            T value = list.get(i);
            int j = i - 1;
            while (j >= 0 && comparator.compare(list.get(j), value) > 0) {
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, value);
        }
    }

    public static void reverse(List<?> list) {
        int left = 0;
        int right = list.size() - 1;
        while (left < right) {
            swap(list, left, right);
            left++;
            right--;
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void swap(List<?> list, int i, int j) {
        List raw = list;
        Object tmp = raw.get(i);
        raw.set(i, raw.get(j));
        raw.set(j, tmp);
    }
}
