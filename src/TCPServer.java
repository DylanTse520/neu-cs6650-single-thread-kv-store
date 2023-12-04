// TCPServer.java: A key-value store server program using TCP socket programming.

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class TCPServer {

    HashMap<Integer, Integer> keyValueStore;

    public TCPServer() {
        this.keyValueStore = new HashMap<>();
    }

    public static void main(String[] args) throws IOException {
        // Init the server
        TCPServer tcpServer = new TCPServer();
        // Prompt user to input connection information
        Helper.ServerLog("Input the port number of the server as <port number>, for example: " + "\"9086\".");
        // To enable user input from console using BufferReader
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        // Reading connection info using readLine
        String input = reader.readLine();
        // Register service on port
        ServerSocket server;
        try {
            server = new ServerSocket(Integer.parseInt(input));
        } catch (IOException e) {
            // Prompt user with input error
            Helper.ServerLog("Cannot connect to the given port number, please check your input and" + " try again.");
            throw new RuntimeException(e);
        }
        // Wait and accept a connection
        Helper.ServerLog("Waiting for connection on port " + input + ".");
        Socket client = server.accept();
        Helper.ServerLog("Connected on port " + input + ".");
        // Get the input reader with buffered reader and input stream
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        // Get the output writer with the output stream
        PrintWriter out = new PrintWriter(client.getOutputStream());

        try {
            // Get input from client
            String line;
            while ((line = in.readLine()) != null) {
                String response = Helper.handleClientInput(line, tcpServer.keyValueStore,
                        client.getInetAddress() + ":" + client.getLocalPort());
                // Send response to client
                out.println(response);
                out.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // When done, close the connection and exit
            client.close();
            reader.close();
        }
    }

}