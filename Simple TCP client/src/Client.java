import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    Socket socket = new Socket();
    PrintWriter out = new PrintWriter(socket.getInputStream(), true);
    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    BufferedReader std = new BufferedReader(new InputStreamReader(System.in));

}
