package com.example.multicastchat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Server implements Runnable {

    public void multicast(String multicastMessage) {
        byte[] buf;
        int outPort = 6655;

        try (MulticastSocket socket = new MulticastSocket()) {
            InetAddress ip = InetAddress.getByName("230.0.0.0");
            buf = multicastMessage.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, ip, outPort);
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        int inPort = 4446;
        final byte[] data = new byte[256];
        try (DatagramSocket socket = new DatagramSocket(inPort)) {

            DatagramPacket packet = new DatagramPacket(data, data.length);
            while (true) {
                socket.receive(packet);
                multicast(new String(packet.getData(), 0, packet.getLength()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        (new Thread(new Server())).start();
    }
}
