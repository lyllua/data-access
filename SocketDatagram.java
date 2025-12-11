import java.net.*;

public class ProgramA_Datagram {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket();

        String message = "token";
        InetAddress address = InetAddress.getByName("localhost");
        byte[] data = message.getBytes();

        DatagramPacket packetToSend = new DatagramPacket(data, data.length, address, 5555);
        socket.send(packetToSend);

        byte[] buffer = new byte[1024];
        DatagramPacket packetToReceive = new DatagramPacket(buffer, buffer.length);
        socket.receive(packetToReceive);

        String response = new String(packetToReceive.getData(), 0, packetToReceive.getLength());
        System.out.println("Program A finished. Response: " + response);

        socket.close();
    }
}