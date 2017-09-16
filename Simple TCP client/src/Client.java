import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {



    public static void main(String[] args)  {

        Scanner reader = new Scanner(System.in);


        //Hosting server on own computer
        String hostName ="localhost";
        int portNumber = 5558;

        try {
            Socket socket = new Socket(hostName, portNumber);
            //Setting output stream
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            //Setting input stream
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //Reading user input
            BufferedReader std = new BufferedReader(new InputStreamReader(System.in));
            {
                    String text = reader.next();
                    out.println(text); //Sending/writing information through socket
                    System.out.println(in.readLine()); //Reading information from socket
                    String username = "Simon";
                    out.println(username);
                    System.out.println(in.readLine());
                    out.println(username + " who passed task 1");
                    System.out.println(in.readLine());
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
