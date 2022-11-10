package com.example.multicastchat;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.*;
import java.util.ResourceBundle;

public class ClientController implements Initializable {

    public ScrollPane dialogScrollPane;
    public Pane mainPane;
    public TextArea dialogTextArea;
    public TextField inputTextField;
    public TextField username;
    public Button disconnectButton;
    public Button sendButton;

    @FXML
    protected void onDisconnectButtonClick() {
        System.exit(0);
    }

    @FXML
    protected void onSendClick() {
        if (!inputTextField.getText().isBlank()) {
            if (username.getText().isBlank()) {
                username.setText("Anonymous");
            }
            int outPort = 4446;
            byte[] message = (username.getText().trim() + ": " + inputTextField.getText().trim()).getBytes();

            try (DatagramSocket socket = new DatagramSocket()) {
                InetAddress address = InetAddress.getLocalHost();
                socket.send(new DatagramPacket(message, 0, message.length, address, outPort));
            } catch (IOException e) {
                e.printStackTrace();
            }
            inputTextField.setText("");
        }
    }

    Runnable task = () -> {
        byte[] buf = new byte[256];
        int inPort = 12540;

        try (MulticastSocket socket = new MulticastSocket(inPort)) {
            InetAddress ip = InetAddress.getByName("234.235.236.237");
            InetSocketAddress group = new InetSocketAddress(ip, inPort);
            NetworkInterface netIf = NetworkInterface.getByName("Ethernet");
            socket.joinGroup(group, netIf);

            while (true) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                dialogTextArea.appendText(new String(packet.getData(), 0, packet.getLength()) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new Thread(task).start();
    }
}