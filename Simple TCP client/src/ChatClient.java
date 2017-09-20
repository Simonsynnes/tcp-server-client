import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class    ChatClient implements Runnable {

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
        String userName = null;
        try {
            userName = console.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            streamOut.writeUTF(userName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(thread != null) {

            String userInput = null;

            try {
                userInput = console.readLine();
            } catch (IOException e) {
                break;
            }
            try {
                if (userInput != null) {
                    streamOut.writeUTF(userInput);
                    streamOut.flush();
                }

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


    public static void main(String[] args)  {
        ChatClient client = new ChatClient("localhost", 5558);
    }
}
