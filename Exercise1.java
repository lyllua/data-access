import java.io.*;

public class Exercise1 {
    public static void main(String[] args) {
        File input = new File("entrada.txt");
        File reversed = new File("invertido.txt");

        try (
                BufferedReader br = new BufferedReader(new FileReader(input));
                BufferedWriter bw = new BufferedWriter(new FileWriter(reversed))
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                for (int i = line.length() - 1; i >= 0; i--) {
                    bw.write(line.charAt(i));
                }
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
