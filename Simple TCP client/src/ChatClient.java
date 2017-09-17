import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ChatClient implements Runnable {

    private Socket socket = null;
    private Thread thread = null;
    private DataInputStream console = null;
    private DataOutputStream streamOut = null;
    private ChatClientThread client = null;

    public ChatClient(String serverName, int serverPort) {
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
        while(thread != null) {
            try {
                streamOut.writeUTF(console.readLine());
                streamOut.flush();
            } catch (IOException e) {
                System.out.println("Sending error: " + e.getMessage());
                stop();
            }
        }
    }

    public void handle(String msg) {
        if(msg.equals(".bye")) {
            System.out.println("Good bye. Press RETURN to exit...");
            stop();
        } else {
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
        try {
            if (console != null) console.close();
            if (streamOut != null) streamOut.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            System.out.println("Error closing");
            client.close();
            client.stop();
        }
    }


    public static void main(String[] args)  {
        ChatClient client = new ChatClient("localhost", 5558);
    }
}
