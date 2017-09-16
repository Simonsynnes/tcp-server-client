package MathClientServer;

import java.io.IOException;
import java.net.ServerSocket;

public class MathMultiServer {

    int port;
    boolean listening;

    public static void main(String args[]) {
        MathMultiServer mathMultiServer = new MathMultiServer(5558, true);
        try {
            mathMultiServer.startListening();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MathMultiServer(int port, boolean listening) {
        this.port = port;
        this.listening = listening;
    }

    public void startListening() throws IOException {
        try (ServerSocket server = new ServerSocket(port)) {
            while (listening) {
                new Thread(new MathClientThread(server.accept())).start();
            }

        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + port + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }

}