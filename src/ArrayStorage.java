import java.util.Arrays;

public class ArrayStorage {
    private static final int CAPACITY = 10000;
    Resume[] storage = new Resume[CAPACITY];
    private int countResumes;

    public void clear() {
        Arrays.fill(storage, 0, countResumes, null);
        countResumes = 0;
    }

    public void save(Resume resume) {
        for (int i = 0; i < countResumes; i++) {
            if (resume.toString().equals(storage[i].getUuid())) {
                System.out.println("This resume already added.");
                return;
            }
        }
        storage[countResumes] = resume;
        countResumes++;
    }

    public Resume get(String uuid) {
        for (int i = 0; i < countResumes; i++) {
            if (uuid.equals(storage[i].getUuid())) {
                return storage[i];
            }
        }
        return null;
    }

    public void delete(String uuid) {
        for (int i = 0; i < countResumes; i++) {
            if (uuid.equals(storage[i].getUuid())) {
                countResumes--;
                System.arraycopy(storage, i + 1, storage, i, countResumes - i);
                storage[countResumes] = null;
                System.out.println("The resume was successfully deleted.");
                return;
            }
        }
        System.out.println("The entered resume was not found for deletion.");
    }

    public Resume[] getAll() {
        return Arrays.copyOf(storage, countResumes);
    }

    public int getSize() {
        return countResumes;
    }
}