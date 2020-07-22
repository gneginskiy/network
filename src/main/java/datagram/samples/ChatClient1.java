package datagram.samples;

import lombok.SneakyThrows;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;


public class ChatClient1 {
    private static final int PACKET_SIZE = 1000;
    private static final int TO_PORT = 8990;
    private static final int FROM_PORT = 8989;

    public static void main(String[] args) throws Throwable{
        DatagramSocket receiverSocket = new DatagramSocket(FROM_PORT);
        Thread listener = new Thread(() -> {
            while (true) {
                DatagramPacket datagramPacket = new DatagramPacket(new byte[PACKET_SIZE], PACKET_SIZE);
                try {
                    receiverSocket.receive(datagramPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("sender: " + new String(datagramPacket.getData()).replace("\u0000", ""));
//            System.out.println("received from address: " + datagramPacket.getAddress());
//            System.out.println("received from port: " + datagramPacket.getPort());
            }
        });
        listener.start();

        Scanner sc = new Scanner(System.in);
        DatagramSocket senderSocket = new DatagramSocket();

        while (true) {
            sendMessage(senderSocket, sc.nextLine(), TO_PORT);
        }

    }

    @SneakyThrows
    private static void sendMessage(DatagramSocket datagramSocket, String message, int toPort) {
        byte[] data = message.getBytes();
        DatagramPacket datagramPacket = new DatagramPacket(
                data, data.length, InetAddress.getLocalHost(), toPort);
        datagramSocket.send(datagramPacket);
        //System.out.println("you: "+message);
    }
}
