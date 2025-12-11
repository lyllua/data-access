import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ProgramA_SocketStream {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        StringBuilder textBuilder = new StringBuilder();

        System.out.println("Type text. Type 'CYC' to send:");

        while (true) {
            String input = scanner.nextLine();
            if (input.equals("CYC")) {
                break;
            }
            textBuilder.append(input).append("\n");
        }

        Socket socket = new Socket("localhost", 5555);
        PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

        output.print(textBuilder.toString());
        System.out.println("Data sent. Exiting...");

        socket.close();
    }
}