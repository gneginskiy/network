package datagram.samples;

import lombok.SneakyThrows;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class DatagramSenderSample {

    @SneakyThrows
    public static void main(String[] args) {
        DatagramSocket datagramSocket = new DatagramSocket();
        Scanner sc = new Scanner(System.in);

        while (true) {
            sendMessage(datagramSocket, sc.nextLine());
        }
    }

    private static void sendMessage(DatagramSocket datagramSocket, String message) throws IOException {
        int toPort = 8989;
        byte[] data = message.getBytes();
        DatagramPacket datagramPacket = new DatagramPacket(
                data, data.length, InetAddress.getLocalHost(), toPort);
        datagramSocket.send(datagramPacket);
    }
}
