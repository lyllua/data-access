import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ProgramB_SocketStream {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(5555);
        Socket socket = serverSocket.accept();

        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        serverSocket.close();
    }
}