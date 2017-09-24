package ChatClient;

import ChatClient.ChatClient;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatClientThread extends Thread {

    private Socket socket = null;
    private ChatClient client = null;
    private DataInputStream streamIn = null;
    private boolean listening = true;

    public ChatClientThread (ChatClient client, Socket socket) {
        this.socket = socket;
        this.client = client;
        open();
        start();
    }

    public void open() {
        try {
            streamIn = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("Error getting input stream: " + e);
            client.stop();
        }
    }

    public void close() {
        try {
            if (streamIn != null) streamIn.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("Error closing input stream: " + e);
        }
    }

    public void run() {
        while (listening) {
            try {
                client.handle(streamIn.readUTF());
            } catch (IOException e) {
                System.out.println("Listening error: " + e.getMessage());
                client.stop();
            }
        }
    }

    public void setListening(boolean value) {
        listening = value;
    }
}
