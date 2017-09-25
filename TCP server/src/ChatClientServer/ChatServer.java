package ChatClientServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
/*
*  This class is responsible for listening for incoming connection.
*  When a client is connecting and accepected, it is returning a new socket and thread (ChatServerThread).
*
*
* */
public class ChatServer implements Runnable {

    private ChatServerThread clients[] = new ChatServerThread[50];
    private HashMap<Integer, String> userNames = new HashMap<Integer, String>();
    private ServerSocket server = null;
    private boolean listening = true;
    private int clientCount = 0;

    public ChatServer(int port) {

        try {
            System.out.println("Binding to port " + port + ", please wait...");
            server = new ServerSocket(port);
            System.out.println("Server started: " + server);
            start();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    //Listens for incoming connections
    public void run() {
        while (listening) {
            try {
                System.out.println("Waiting for client...");
                addThread(server.accept()); //Returns new thread and socket
            } catch (IOException e) {
                System.out.println("Acceptance Error: " + e);
            }
        }
    }

    public void start() {
    }

    public void stop() {
    }
    //Finding client by ID
    private int findClient(int ID) {
        for (int i = 0; i < clientCount; i++) {
            if (clients[i].getID() == ID) {
                return i;
            }
        }
        return -1;
    }
    //Sends messages to all connected clients in the array
    public synchronized void handle(int ID, String input) {
        if (input.equals(".bye")) {
            clients[findClient(ID)].send(".bye");
            remove(ID);
        } else {
            String userName = userNames.get(ID); //Finds username through the HashMap.
            for (int i = 0; i < clientCount; i++) {
                if(ID != clients[i].getID()) {
                    clients[i].send(userName + ": " + input);
                }
            }
        }
    }

    //Saves input as username and stores it in the HashMap
    public synchronized boolean handleLogin(int ID, String input) {
        String userName = input;
        userNames.put(ID, input);
        return true;
    }

    //Removes client by ID
    public synchronized void remove(int ID) {
        int pos = findClient(ID);
        if (pos >= 0) {
            ChatServerThread toTerminate = clients[pos];
            System.out.println("Removing client thread " + ID + " at " + pos);
            if (pos < clientCount - 1) {
                for (int i = pos + 1; i < clientCount; i++) {
                    clients[i + 1] = clients[i];
                }
            }
            clientCount--;
            try {
                toTerminate.close();
            } catch (IOException e) {
                System.out.println("Error closing thread: " + e);
            }
            toTerminate.stop();
        }
    }

    //Adds new thread when client is accepted, returning a new socket
    private void addThread(Socket socket) {

        if (clientCount < clients.length) {
            System.out.println("Client accepted: " + socket);
            clients[clientCount] = new ChatServerThread(this, socket); //Added in the array of connected clients
            try {
                clients[clientCount].open();
                clients[clientCount].start(); //Starts thread
                clientCount++;
            } catch (IOException e) {
                System.out.println("Error opening thread: " + e);
            }
        } else {
            System.out.println("Client refused: maximum " + clients.length + " reached.");
        }
    }


    public static void main(String args[]) {
        ChatServer chatServer = new ChatServer(5558);
        chatServer.run();
    }
}