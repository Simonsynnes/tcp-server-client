import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
/*
*  This class is responsible for  listening for a incoming connection (single-threaded).
*
* */
public class Server {
    public static void main(String[] args) {

        //Port number to listen to
        int portNumber = 5558;

        try (
                ServerSocket server = new ServerSocket(portNumber); //Create Server socket object using port
                Socket client = server.accept(); //Accept incoming client connection and return socket object
                PrintWriter out = new PrintWriter(client.getOutputStream(), true); //Set output stream
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream())); //Set input stream
        ) {
            //Reading from and writing to the socket
                System.out.println(in.readLine()); //Reading information from socket
            try {
                Thread.sleep(3000); //Pause for 3 seconds
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
                out.println("Who's there?"); //Sending/writing information to socket
                //Saving name as username
                String username = in.readLine();
                System.out.println(username);
            try {
                Thread.sleep(3000);
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
                out.println(username + " who?");
                System.out.println(in.readLine());
            try {
                Thread.sleep(3000);
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
                out.println("Congrats!");
            }

            catch (IOException e) {
                System.out.println("Exception caught when trying to listen on port "
                        + portNumber + " or listening for a connection");
                System.out.println(e.getMessage());
            }
        }


            }



