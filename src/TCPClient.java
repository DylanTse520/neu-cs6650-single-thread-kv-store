// TCPClient.java: A key-value store client program using TCP socket programming.

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;

public class TCPClient {
    public static void main(String[] args) throws IOException {
        // Prompt user to input connection information
        Helper.ClientLog("Input the hostname or IP address and the port number of the server in the form of " + "<hostname> <port number>, for example: \"localhost 9086\".");
        // To enable user input from console using BufferReader
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        // Reading connection info using readLine
        String input = reader.readLine();
        // Splitting the input line to get hostname or IP address and port number
        String[] parts = input.split(" ");
        // The first part should be the hostname or IP address
        String host = parts[0];
        // The second part should be the port number
        String port = parts[1];

        Socket client;
        try {
            // Open the connection to a server at the host and port
            client = new Socket(host, Integer.parseInt(port));
            Helper.ClientLog("Connected to server on " + host + ":" + port + ".");
        } catch (IOException e) {
            // Prompt user with input error
            Helper.ClientLog("Cannot connect to the given hostname or IP address and port number, please check your " + "input and try again.");
            throw new RuntimeException(e);
        }
        client.setSoTimeout(100);
        // Get the input reader with buffered reader and input stream
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        // Get the output writer with the output stream
        PrintWriter out = new PrintWriter(client.getOutputStream());

        try {
            // The request ID
            int requestId = 0;
            // Prepopulate server's key-value store
            Helper.ClientLog("Pre-populating server's key-value store.");
            // Read pre-population data
            File prePopulationFile = new File("src/PREPOPULATION");
            // Create the scanner
            Scanner sc = new Scanner(prePopulationFile);
            // Read each line
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                // Send data to server
                out.println((++requestId) + "#PUT " + line);
                out.flush();
            }
            sc.close();
            Helper.ClientLog("Finished pre-populating server's key-value store.");

            // Complete the minimum operation request
            Helper.ClientLog("Completing minimum operation request.");
            // Read minimum operation data
            File operationFile = new File("src/MINIMUM_OPERATION");
            // Create the scanner
            sc = new Scanner(operationFile);
            // Read each line
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                // Send data to server
                out.println((++requestId) + "#" + line);
                out.flush();
            }
            sc.close();
            Helper.ClientLog("Finished minimum operations.");

            // Prompt user with input instructions
            Helper.ClientLog("Input your requests. Separate operation name (PUT, GET or DELETE), key (integers only) " + "and/or value (integers only) by space. For example: PUT 1 10; GET 1; DELETE 1.");
            // Process user input
            String line;
            while ((line = reader.readLine()) != null) {
                // Send data to server
                out.println((++requestId) + "#" + line);
                out.flush();
                try {
                    // Get the response from server
                    String response;
                    while ((response = in.readLine()) != null) {
                        // Split the request ID from the request part
                        String[] responseParts = response.split("#");
                        if (Integer.parseInt(responseParts[0]) == requestId) {
                            // Print the server's response
                            Helper.ClientLog("Server responded: " + responseParts[1]);
                            break;
                        }
                    }
                } catch (SocketTimeoutException e) {
                    Helper.ClientLog("Did not receive response within 100ms and experienced timeout. Try again later.");
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // When done, close the connection and exit
            client.close();
            reader.close();
        }
    }
}