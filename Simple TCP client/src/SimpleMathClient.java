import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * A simple TCP client that can be used to test the Math Server in A4 DataKomm
 * assignment.
 *
 * @author Girts Strazdins, gist@ntnu.no
 */
public class SimpleMathClient {

    BufferedReader reader;
    PrintWriter writer;
    Scanner input;

    public static void main(String args[]) {
        SimpleMathClient client = new SimpleMathClient();
        client.run();
    }

    public boolean run() {
        try {
            // Connect to server. Set you IP and port here
            Socket socket = new Socket("localhost", 5558);

            // Set up stream handlers
            InputStream in = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in));
            OutputStream out = socket.getOutputStream();
            // Create PrintWriter with autoFlush=true
            writer = new PrintWriter(out, true);
            input = new Scanner(System.in);

            // Run all the tests
            return runTests();
            
        } catch (IOException ex) {
            System.out.println("Socket error: " + ex.getMessage());
            return false;
        }
    }

    /**
     * Send a command to server (add newline), read one line from the server,
     * compare the response with expected response
     *
     * @param testName
     * @param cmdToServer
     * @param expectedResponse
     * @return
     * @throws IOException
     */
    private boolean test(String testName, String cmdToServer, String expectedResponse) throws IOException {
        System.out.print("Test: " + testName);
        if (cmdToServer != null) {
            writer.println(cmdToServer);
        }
        if (expectedResponse != null) {
            String response = reader.readLine();
            if (response == null || !response.equals(expectedResponse)) {
                System.out.println("   --- FAILED! Expected: "
                        + expectedResponse + ", received: " + response);
                return false;
            }
        }
        System.out.println("   +++ PASSED");

        return true;
    }

    private boolean runTests() throws IOException {
        if (!test("Initial value 0", null, "0")) {
            return false;
        }
        if (!test("Simple +5", "+5", "5")) {
            return false;
        }
        if (!test("Minus", "-27", "-22")) {
            return false;
        }
        if (!test("Division", "/11", "-2")) {
            return false;
        }
        if (!test("Multiply by zero", "*0", "0")) {
            return false;
        }
        if (!test("Add", "+44", "44")) {
            return false;
        }
        if (!test("Division with remainder", "/18", "2")) {
            return false;
        }
        if (!test("Division that results in zero", "/8", "0")) {
            return false;
        }
        if (!test("Add", "+44", "44")) {
            return false;
        }
        if (!test("Division by zero", "/0", "0")) {
            return false;
        }
        if (!test("Add large", "+9999", "9999")) {
            return false;
        }
        if (!test("Overflow", "+444", "9999")) {
            return false;
        }
        if (!test("Invalid operation", "/-8", "9999")) {
            return false;
        }
        if (!test("Multiply by zero", "*0", "0")) {
            return false;
        }
        if (!test("Add", "+42", "42")) {
            return false;
        }
        if (!test("Invalid operation", "What's the meaning of life, universe and everything?", "42")) {
            return false;
        }
        if (!test("Quit", "q", null)) {
            return false;
        }

        System.out.println("All tests passed!");
        return true;
    }
}
