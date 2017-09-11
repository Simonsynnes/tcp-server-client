import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: java Server <port number>");
            System.exit(1);
        }
        //Port number to listen to
        int portNumber = 5558;

        try (
            ServerSocket server = new ServerSocket(portNumber); //Create socket object using port
            Socket client = server.accept(); //Accept incoming client connection and return socket object
            PrintWriter out = new PrintWriter(client.getOutputStream(), true); //Set output stream
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream())); //Set input stream
        ) {
            //Reading from and writing to the socket
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                out.println(inputLine);
                if (inputLine.equals("Bye."))
                    break;
            }
        } catch (IOException e) {
                System.out.println("Exception caught when trying to listen on port" + portNumber +
                        "or listening for a connection");
                System.out.println(e.getMessage());
            }


        }

    }
