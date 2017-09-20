package ChatClientServer;

import java.io.*;
import java.net.Socket;
import java.util.regex.Pattern;

public class ChatServerThread extends Thread {

    private Socket socket = null;
    private ChatServer server = null;
    private int ID = -1;
    private DataInputStream streamIn = null;
    private DataOutputStream streamOut = null;

    public ChatServerThread(ChatServer server, Socket socket) {
        super();
        this.server = server;
        this.socket = socket;
        ID = socket.getPort();
    }

    public void send(String msg) {
        try {
            streamOut.writeUTF(msg);
            streamOut.flush();
        } catch (IOException e) {
            System.out.println(" Error sending: " + e.getMessage());
            stop();
        }
    }

    public int getID() {
        return ID;
    }

    public void run() {
        System.out.println("Server Thread " + ID + " running.");
        loginPhase();
        messagePhase();
    }

    public void loginPhase() {
        while(true) {
            try {
                if(server.handleLogin(ID, streamIn.readUTF())) {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void messagePhase() {
        while (true) {
            try {
                server.handle(ID, streamIn.readUTF());
            } catch (IOException e) {
                System.out.println(ID + " ERROR reading: " + e.getMessage());
                server.remove(ID);
                stop();
            }
        }
    }

    public void open() throws IOException {
        streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        streamOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }
    public void close() throws IOException {
        if (socket != null) socket.close();
        if (streamIn != null) streamIn.close();
        if (streamOut != null) streamOut.close();
    }
}
