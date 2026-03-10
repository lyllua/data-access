import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class Exercise2 {

    public static void main(String[] args) throws IOException {

        Scanner sc = new Scanner(System.in);
        RandomAccessFile f = new RandomAccessFile("scores.dat", "rw");

        System.out.println("New player id: ");
        int id = sc.nextInt();
        System.out.println("Score: ");
        int points = sc.nextInt();

        insert(f, id, points);

        System.out.println("Id to search: ");
        int searchId = sc.nextInt();

        search(f, searchId);

        f.close();
        sc.close();
    }

    public static void insert(RandomAccessFile f, int id, int points) throws IOException {
        f.seek(f.length());
        f.writeInt(id);
        f.writeInt(points);
    }

    public static void search(RandomAccessFile f, int searchId) throws IOException {
        f.seek(0);

        while (f.getFilePointer() < f.length()) {
            int id = f.readInt();
            int points = f.readInt();

            if (id == searchId) {
                System.out.println("Score: " + points);
                return;
            }
        }

        System.out.println("Id not found");
    }
}
