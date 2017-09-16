package MathClientServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.regex.Pattern;

public class MathClientThread implements Runnable {

    private Socket socket;
    private int sum;
    private boolean calculating;

    public MathClientThread(Socket socket) {
        this.socket = socket;
        calculating = true;
    }

    public void run() {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            sum = 0;
            out.println(sum);

            // Do all the calculations
            while (calculating) {
                String fullString = in.readLine();
                int intValue = 0;
                if (fullString.equals("q")) {
                    calculating = false;
                } else {

                    //checks if its a letter string, then it will jump over all the calculations

                    if (!Pattern.matches(".*[a-zA-Z].*", fullString)) {

                        //Calculations

                        intValue = Integer.parseInt(fullString.substring(1, fullString.length()));
                        if (intValue >= 0) {
                            String operator = fullString.substring(0, 1);
                            if (operator.equals("+")) {
                                sum = sum + intValue;
                            } else if (operator.equals("-")) {
                                sum = sum - intValue;
                            } else if (operator.equals("/")) {
                                if (intValue == 0) {
                                    sum = 0;
                                } else {
                                    sum = sum / intValue;
                                }
                            } else if (operator.equals("*")) {
                                sum = sum * intValue;
                            }
                        }
                    }

                    //Sets the max limit to 9999
                    if (sum > 9999) {
                        sum = 9999;
                    }

                    //prints out the current sum
                    out.println(sum);
                }
            }
        } catch (IOException e) {

        }
    }
}
