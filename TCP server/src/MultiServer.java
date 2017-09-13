import java.io.IOException;
import java.net.ServerSocket;

public class MultiServer {

    public static void main(String[] args) {

        //Port number to listen to
        int portNumber = 5558;
        boolean listening = true;
        try
                //Listens for new connections. Returns new socket object for each connection (and new thread).
            (ServerSocket server = new ServerSocket(portNumber)) {
                while(listening)

                { new Thread(new ServerThread(server.accept())).start(); //Accept incoming client connection and return socket object
                }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}
