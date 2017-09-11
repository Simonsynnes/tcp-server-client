import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    public static void main(String[] args) throws IOException {

        if (args.length != 2) {
            System.err.println(
                    "Usage: java EchoClient <host name> <port number>");
            System.exit(1);
        }
        //Hosting server on own computer
        String hostName = "localhost";

        try {
            Socket socket = new Socket(hostName, 5558);
            //Setting output stream
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            //Setting input streams
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader std = new BufferedReader(new InputStreamReader(System.in));
            {
                //Track user input and print it
                String userInput;
                while ((userInput = std.readLine()) != null) {
                    out.println(userInput);
                    System.out.println("echo: " + in.readLine());
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);
        }
    }
}
