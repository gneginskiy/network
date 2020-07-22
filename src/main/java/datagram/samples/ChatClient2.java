package datagram.samples;

import lombok.SneakyThrows;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

public class ChatClient2 {
    private static final int PACKET_SIZE = 1000;
    private static final int TO_PORT = 8989;
    private static final int FROM_PORT = 8990;

    public static void main(String[] args) throws SocketException {
        runReceiverThread();
        Scanner sc = new Scanner(System.in);
        DatagramSocket senderSocket = new DatagramSocket();
        while (true) {
            sendMessage(senderSocket, sc.nextLine(), TO_PORT);
        }
    }

    private static void runReceiverThread() {
        Thread listener = new Thread(() -> {
            try {
                DatagramSocket receiverSocket = new DatagramSocket(FROM_PORT);
                while (true) {
                    DatagramPacket datagramPacket = new DatagramPacket(new byte[PACKET_SIZE], PACKET_SIZE);
                    receiverSocket.receive(datagramPacket);
                    System.out.println("sender: " + new String(datagramPacket.getData()).replace("\u0000", ""));
//            System.out.println("received from address: " + datagramPacket.getAddress());
//            System.out.println("received from port: " + datagramPacket.getPort());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        listener.start();
    }

    @SneakyThrows
    private static void sendMessage(DatagramSocket datagramSocket, String message, int toPort) {
        byte[] data = message.getBytes();
        DatagramPacket datagramPacket = new DatagramPacket(
                data, data.length, InetAddress.getLocalHost(), toPort);
        datagramSocket.send(datagramPacket);
        // System.out.println("you: "+message);
    }
}
