package MultiServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
/*
*  This class is reponsible for communicating with the client from the server side.
*
* */
public class ClientThread implements Runnable {
        private Socket socket = null;

        public ClientThread(Socket socket) {
            this.socket = socket;
        }

        public void run() {

            try {
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                //Reading from and writing to the socket
                System.out.println(in.readLine()); //Reading information from socket
                try {
                    Thread.sleep(4000); //Pause for 3 seconds
                } catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                out.println("Who's there?"); //Sending/writing information to socket
                //Saving name as username
                String username = in.readLine();
                System.out.println(username);
                try {
                    Thread.sleep(4000);
                } catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                out.println(username + " who?");
                System.out.println(in.readLine());
                try {
                    Thread.sleep(4000);
                } catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                out.println("Congrats!");
                socket.close(); //Closes socket
            }

            catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
