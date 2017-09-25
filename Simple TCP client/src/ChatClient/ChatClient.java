package ChatClient;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
/*
*  This class is responsible for communicating with the server thread.
*  It writes user input to the socket and sends it to the server.
*
* */
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
            socket = new Socket(serverName, serverPort); //Establishes connection to server
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
    //Writes message to socket
    public void sendMessage(String msg) {
        try {
            streamOut.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Prints new messages to GUI
    public void handle(String msg) {
        if(msg.equals(".bye")) {
            System.out.println("Good bye. Press RETURN to exit...");
            stop();
        } else {
            String[] txt = msg.split("\\|");
            GUI.getMessage(txt[0], txt[1]);
            System.out.println(msg);
        }
    }
    //Sets input and output streams
    public void start() throws IOException {
        console = new DataInputStream(System.in);
        streamOut = new DataOutputStream(socket.getOutputStream());
        if(thread == null) {
            client = new ChatClientThread(this, socket); //Creates new thread for listening for incoming messages
            thread = new Thread(this);
            thread.start();
        }
    }

    //Closes input and output streams
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
