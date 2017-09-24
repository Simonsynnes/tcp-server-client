package ChatClient;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient implements Runnable {

    private Socket socket = null;
    private Thread thread = null;
    private DataInputStream console = null;
    private DataOutputStream streamOut = null;
    private ChatClientThread client = null;
    private ChatClientGUI GUI;

    public ChatClient(String serverName, int serverPort, ChatClientGUI GUI) {

        this.GUI = GUI;

        System.out.println("Establishing connection. Please wait...");
        try {
            socket = new Socket(serverName, serverPort);
            System.out.println("Connected: " + socket);
            start();
        } catch (UnknownHostException uhe) {
            System.out.println("Host unknown; " + uhe.getMessage());
        } catch (IOException e) {
            System.out.println("Unexpected exception " + e.getMessage());
        }
    }

    public void run() {

    }

    public void sendMessage(String msg) {
        try {
            streamOut.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handle(String msg) {
        if(msg.equals(".bye")) {
            System.out.println("Good bye. Press RETURN to exit...");
            stop();
        } else {
            GUI.getMessage(msg);
            System.out.println(msg);
        }
    }

    public void start() throws IOException {
        console = new DataInputStream(System.in);
        streamOut = new DataOutputStream(socket.getOutputStream());
        if(thread == null) {
            client = new ChatClientThread(this, socket);
            thread = new Thread(this);
            thread.start();
        }
    }

    public void stop() {

        client.setListening(false);

        try {
            if (thread != null) thread = null;
            if (console != null) console.close();
            if (streamOut != null) streamOut.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            System.out.println("Error closing");
            client.close();
        }
    }

    public void setThread() {
        thread = null;
    }
}
