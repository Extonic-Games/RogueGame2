package me.extain.game.desktop;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;

public class Test {

    private Socket socket;
    private SocketAddress address;

    private static boolean isRunning = false;

    public static void main(String[] args) {
        isRunning = true;
        new Test();
    }

    public Test() {
            new Thread() {
              public void run() {
                  createSocket();
                  connectToSocket();
                  //sendToSocket("ping");
              }
            }.start();
        }

    public void createSocket() {
        address = (new InetSocketAddress("192.168.1.1", 445));
        socket = new Socket();
        try {
            socket.setKeepAlive(true);
            socket.setSoTimeout(1000);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void connectToSocket() {
        if (socket != null) {
            try {
                socket.connect(address);
                System.out.println("Connected to socket");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendToSocket(String message) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
            bw.write(message);
            bw.flush();
            System.out.println("Sent message to socket");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
