package com.travel.ds;

import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class CustomDataStructuresParityTest {

    @Test
    void arrayListShouldMatchJavaUtilArrayListCoreBehavior() {
        ArrayList<Integer> custom = new ArrayList<>();
        java.util.ArrayList<Integer> jdk = new java.util.ArrayList<>();

        custom.add(1);
        jdk.add(1);
        custom.add(null);
        jdk.add(null);
        custom.add(1, 2);
        jdk.add(1, 2);

        assertEquals(jdk.size(), custom.size());
        assertEquals(jdk.get(0), custom.get(0));
        assertEquals(jdk.get(1), custom.get(1));
        assertEquals(jdk.indexOf(null), custom.indexOf(null));
        assertEquals(jdk.lastIndexOf(1), custom.lastIndexOf(1));

        assertEquals(jdk.set(1, 3), custom.set(1, 3));
        assertEquals(jdk.remove(0), custom.remove(0));
        assertEquals(jdk.remove((Object) null), custom.remove((Object) null));

        assertArrayEquals(jdk.toArray(), custom.toArray());
        assertThrows(IndexOutOfBoundsException.class, () -> custom.get(99));
        assertThrows(IndexOutOfBoundsException.class, () -> custom.add(-1, 7));
    }

    @Test
    void linkedListShouldMatchJavaUtilLinkedListAsListAndDeque() {
        LinkedList<String> custom = new LinkedList<>();
        java.util.LinkedList<String> jdk = new java.util.LinkedList<>();

        custom.addFirst("b");
        jdk.addFirst("b");
        custom.addLast("c");
        jdk.addLast("c");
        custom.add(1, null);
        jdk.add(1, null);
        custom.addFirst("a");
        jdk.addFirst("a");

        assertEquals(jdk.peekFirst(), custom.peekFirst());
        assertEquals(jdk.peekLast(), custom.peekLast());
        assertEquals(jdk.indexOf(null), custom.indexOf(null));
        assertEquals(jdk.lastIndexOf("c"), custom.lastIndexOf("c"));

        assertEquals(jdk.removeFirst(), custom.removeFirst());
        assertEquals(jdk.removeLast(), custom.removeLast());
        assertEquals(jdk.poll(), custom.poll());
        assertEquals(jdk.peek(), custom.peek());

        custom.clear();
        assertThrows(java.util.NoSuchElementException.class, custom::removeFirst);
        assertThrows(java.util.NoSuchElementException.class, custom::removeLast);
    }

    @Test
    void hashMapShouldMatchJavaUtilHashMapCoreBehavior() {
        HashMap<String, Integer> custom = new HashMap<>();
        java.util.HashMap<String, Integer> jdk = new java.util.HashMap<>();

        assertEquals(jdk.put("k1", 1), custom.put("k1", 1));
        assertEquals(jdk.put("k2", 2), custom.put("k2", 2));
        assertEquals(jdk.put("k1", 3), custom.put("k1", 3));
        assertEquals(jdk.put(null, 4), custom.put(null, 4));

        assertEquals(jdk.get("k1"), custom.get("k1"));
        assertEquals(jdk.get(null), custom.get(null));
        assertEquals(jdk.containsKey("k2"), custom.containsKey("k2"));
        assertEquals(jdk.containsKey("missing"), custom.containsKey("missing"));

        assertEquals(jdk.remove("k2"), custom.remove("k2"));
        assertEquals(jdk.remove("missing"), custom.remove("missing"));
        assertEquals(jdk.size(), custom.size());

        assertEquals(jdk.getOrDefault("kX", 9), custom.getOrDefault("kX", 9));

        assertEquals(jdk.keySet().size(), custom.keySet().size());
        assertEquals(jdk.entrySet().size(), custom.entrySet().size());

        custom.clear();
        jdk.clear();
        assertEquals(jdk.isEmpty(), custom.isEmpty());
    }

    @Test
    void hashSetShouldMatchJavaUtilHashSetBehavior() {
        HashSet<String> custom = new HashSet<>();
        java.util.HashSet<String> jdk = new java.util.HashSet<>();

        assertEquals(jdk.add("a"), custom.add("a"));
        assertEquals(jdk.add("a"), custom.add("a"));
        assertEquals(jdk.add(null), custom.add(null));
        assertEquals(jdk.contains("a"), custom.contains("a"));
        assertEquals(jdk.contains(null), custom.contains(null));

        assertEquals(jdk.remove("a"), custom.remove("a"));
        assertEquals(jdk.remove("missing"), custom.remove("missing"));
        assertEquals(jdk.size(), custom.size());

        custom.clear();
        jdk.clear();
        assertEquals(jdk.isEmpty(), custom.isEmpty());
    }

    @Test
    void priorityQueueShouldMatchJavaUtilPriorityQueueBehavior() {
        PriorityQueue<Integer> custom = new PriorityQueue<>();
        java.util.PriorityQueue<Integer> jdk = new java.util.PriorityQueue<>();

        int[] values = {5, 1, 3, 4, 2};
        for (int v : values) {
            assertEquals(jdk.offer(v), custom.offer(v));
        }

        assertEquals(jdk.peek(), custom.peek());
        assertEquals(jdk.remove(3), custom.remove(3));

        while (!jdk.isEmpty()) {
            assertEquals(jdk.poll(), custom.poll());
        }
        assertNull(custom.poll());
        assertNull(custom.peek());

        assertThrows(NullPointerException.class, () -> custom.offer(null));
    }

    @Test
    void priorityQueueWithComparatorShouldBehaveLikeJavaUtil() {
        PriorityQueue<Integer> custom = new PriorityQueue<>(Comparator.reverseOrder());
        java.util.PriorityQueue<Integer> jdk = new java.util.PriorityQueue<>(Comparator.reverseOrder());

        int[] values = {5, 1, 3, 4, 2};
        for (int v : values) {
            custom.offer(v);
            jdk.offer(v);
        }

        while (!jdk.isEmpty()) {
            assertEquals(jdk.poll(), custom.poll());
        }
    }

    @Test
    void arrayDequeShouldMatchJavaUtilArrayDequeCoreBehavior() {
        ArrayDeque<Integer> custom = new ArrayDeque<>();
        java.util.ArrayDeque<Integer> jdk = new java.util.ArrayDeque<>();

        custom.addFirst(2);
        jdk.addFirst(2);
        custom.addLast(3);
        jdk.addLast(3);
        custom.addFirst(1);
        jdk.addFirst(1);

        assertEquals(jdk.peekFirst(), custom.peekFirst());
        assertEquals(jdk.peekLast(), custom.peekLast());
        assertEquals(jdk.removeFirst(), custom.removeFirst());
        assertEquals(jdk.removeLast(), custom.removeLast());

        custom.addLast(9);
        jdk.addLast(9);
        assertEquals(jdk.remove((Object) 9), custom.remove((Object) 9));
        assertEquals(jdk.contains(2), custom.contains(2));

        custom.clear();
        assertThrows(java.util.NoSuchElementException.class, custom::removeFirst);
        assertThrows(java.util.NoSuchElementException.class, custom::removeLast);
        assertThrows(NullPointerException.class, () -> custom.addFirst(null));
        assertThrows(NullPointerException.class, () -> custom.addLast(null));
    }

    @Test
    void collectionsUtilityShouldMatchJavaUtilCollectionsEffects() {
        ArrayList<Integer> custom = new ArrayList<>();
        java.util.ArrayList<Integer> jdk = new java.util.ArrayList<>();

        int[] values = {3, 1, 2, 5, 4};
        for (int v : values) {
            custom.add(v);
            jdk.add(v);
        }

        Collections.sort(custom);
        java.util.Collections.sort(jdk);
        assertArrayEquals(jdk.toArray(), custom.toArray());

        Collections.reverse(custom);
        java.util.Collections.reverse(jdk);
        assertArrayEquals(jdk.toArray(), custom.toArray());

        Collections.swap(custom, 1, 3);
        java.util.Collections.swap(jdk, 1, 3);
        assertArrayEquals(jdk.toArray(), custom.toArray());
    }

    @Test
    void iteratorsShouldExposeSameTraversalResultsAsJdkCounterparts() {
        ArrayList<String> customList = new ArrayList<>();
        java.util.ArrayList<String> jdkList = new java.util.ArrayList<>();
        LinkedList<String> customLinked = new LinkedList<>();
        java.util.LinkedList<String> jdkLinked = new java.util.LinkedList<>();

        String[] values = {"a", "b", "c"};
        for (String v : values) {
            customList.add(v);
            jdkList.add(v);
            customLinked.add(v);
            jdkLinked.add(v);
        }

        assertEquals(join(jdkList), join(customList));
        assertEquals(join(jdkLinked), join(customLinked));
    }

    private static String join(Iterable<?> items) {
        StringBuilder sb = new StringBuilder();
        for (Object item : items) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(Objects.toString(item));
        }
        return sb.toString();
    }
}
