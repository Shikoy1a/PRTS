package com.travel.ds;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomDataStructuresTest {

    @Test
    void arrayListBasicOperationsShouldWork() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(1, 3);

        assertEquals(3, list.size());
        assertEquals(3, list.get(1));
        assertEquals(0, list.indexOf(1));
        assertEquals(0, list.lastIndexOf(1));

        assertEquals(3, list.remove(1));
        assertTrue(list.remove((Object) 2));
        assertArrayEquals(new Object[]{1}, list.toArray());
    }

    @Test
    void linkedListShouldSupportListAndDequeSemantics() {
        LinkedList<String> list = new LinkedList<>();
        list.add("b");
        list.addFirst("a");
        list.addLast("c");

        assertEquals("a", list.peekFirst());
        assertEquals("c", list.peekLast());
        assertEquals("a", list.removeFirst());
        assertEquals("c", list.removeLast());
        assertEquals("b", list.peek());
        assertEquals(1, list.size());
    }

    @Test
    void hashMapShouldPutGetUpdateAndRemove() {
        HashMap<String, Integer> map = new HashMap<>();

        assertNull(map.put("x", 1));
        assertEquals(1, map.get("x"));
        assertEquals(1, map.put("x", 2));
        assertEquals(2, map.get("x"));
        assertTrue(map.containsKey("x"));

        assertNull(map.put(null, 7));
        assertEquals(7, map.get(null));

        assertEquals(2, map.size());
        assertEquals(2, map.remove("x"));
        assertFalse(map.containsKey("x"));
        assertEquals(1, map.size());
    }

    @Test
    void hashSetShouldKeepUniqueElements() {
        HashSet<String> set = new HashSet<>();

        assertTrue(set.add("a"));
        assertFalse(set.add("a"));
        assertTrue(set.add("b"));
        assertTrue(set.contains("a"));
        assertTrue(set.remove("a"));
        assertFalse(set.contains("a"));
        assertEquals(1, set.size());
    }

    @Test
    void priorityQueueShouldReturnElementsInAscendingOrder() {
        PriorityQueue<Integer> queue = new PriorityQueue<>();
        queue.offer(5);
        queue.offer(1);
        queue.offer(3);

        assertEquals(1, queue.poll());
        assertEquals(3, queue.poll());
        assertEquals(5, queue.poll());
        assertNull(queue.poll());
    }

    @Test
    void arrayDequeShouldSupportCircularBufferAndDisallowNull() {
        ArrayDeque<Integer> deque = new ArrayDeque<>();
        deque.addLast(1);
        deque.addLast(2);
        deque.addFirst(0);

        assertTrue(deque.contains(1));
        assertFalse(deque.contains(null));
        assertEquals(0, deque.removeFirst());
        assertEquals(2, deque.removeLast());
        assertEquals(1, deque.peekFirst());

        assertThrows(NullPointerException.class, () -> deque.addFirst(null));
        assertThrows(NullPointerException.class, () -> deque.addLast(null));
    }

    @Test
    void collectionsSortAndReverseShouldWork() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(3);
        list.add(1);
        list.add(2);

        Collections.sort(list);
        assertArrayEquals(new Object[]{1, 2, 3}, list.toArray());

        Collections.reverse(list);
        assertArrayEquals(new Object[]{3, 2, 1}, list.toArray());
    }
}

