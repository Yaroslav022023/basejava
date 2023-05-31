package com.basejava.webapp;

import com.basejava.webapp.model.Resume;
import com.basejava.webapp.storage.SortedArrayStorage;

/**
 * Test for your com.basejava.webapp.storage.ArrayStorage implementation
 */
public class MainTestArrayStorage {
    static final SortedArrayStorage ARRAY_STORAGE = new SortedArrayStorage();

    public static void main(String[] args) {
        Resume r1 = new Resume();
        r1.setUuid("uuid1");
        Resume r2 = new Resume();
        r2.setUuid("uuid2");;
        Resume r3 = new Resume();
        r3.setUuid("uuid3");
        Resume r4 = new Resume(); // Переменная для теста вставки в метод save() в класс SortedArrayStorage, как сортирует Arrays.binarySearch.
        r4.setUuid("uuid25");

        ARRAY_STORAGE.save(r1);
        ARRAY_STORAGE.save(r2);
        ARRAY_STORAGE.save(r3);
        ARRAY_STORAGE.save(r4); // Переменная для теста вставки в метод save() в класс SortedArrayStorage, как сортирует Arrays.binarySearch.

        System.out.println("Get r1: " + ARRAY_STORAGE.get(r1.getUuid()));
        System.out.println("Size: " + ARRAY_STORAGE.getSize());

        System.out.println("Get dummy: " + ARRAY_STORAGE.get("dummy"));

        /////// Тест вывода массива sortedStorage в классе SortedArrayStorage:
        System.out.println("\nТест вывода массива sortedStorage в классе SortedArrayStorage: ");
        for (int i = 0; i < ARRAY_STORAGE.countResumes; i++) {
            System.out.print(ARRAY_STORAGE.sortedStorage[i] + ", ");
        }
        System.out.println();
        //////

        printAll();
        ARRAY_STORAGE.delete(r1.getUuid());
        printAll();
        ARRAY_STORAGE.update(r1); // for 'update' method in ArrayStorage
        printAll();
        ARRAY_STORAGE.clear();
        printAll();

        System.out.println("Size: " + ARRAY_STORAGE.getSize());
    }

    static void printAll() {
        System.out.println("\nGet All");
        for (Resume r : ARRAY_STORAGE.getAll()) {
            System.out.println(r);
        }
    }
}