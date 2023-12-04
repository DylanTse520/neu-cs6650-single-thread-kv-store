// UDPServer.java: A key-value store server program using UDP socket programming.

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;

public class UDPServer {

    HashMap<Integer, Integer> keyValueStore;

    public UDPServer() {
        this.keyValueStore = new HashMap<>();
    }

    public static void main(String[] args) throws IOException {
        // Init the server
        UDPServer udpServer = new UDPServer();
        // Prompt user to input connection information
        Helper.ServerLog("Input the port number of the server as <port number>, for example: " + "\"9086\".");
        // To enable user input from console using BufferReader
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        // Reading connection info using readLine
        String input = reader.readLine();
        // Wait and accept a connection
        Helper.ServerLog("Connecting to port " + input + ".");

        // Create the UDP socket
        DatagramSocket socket = new DatagramSocket(Integer.parseInt(input));
        try {
            while (!socket.isClosed()) {
                // Get input bytes from client
                byte[] receiveData = new byte[1024];
                // Create the datagram packet
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                // The datagram packet receives the input from client
                socket.receive(receivePacket);
                // Decode the received message
                String receivedMessage = Helper.decodeData(receiveData);

                // Client address
                InetAddress address = receivePacket.getAddress();
                // Client port
                int port = receivePacket.getPort();
                // Process the input
                String response = Helper.handleClientInput(receivedMessage, udpServer.keyValueStore,
                        address + ":" + port);
                // Get response bytes for client
                byte[] sendData = response.getBytes();
                // Create the datagram packet
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, port);
                // Send the response
                socket.send(sendPacket);
            }
        } finally {
            // When done, close the connection and exit
            socket.close();
            reader.close();
        }
    }

}