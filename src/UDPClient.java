// TCPClient.java: A key-value store client program using TCP socket programming.

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Scanner;

public class UDPClient {
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

        // Create the UDP socket
        DatagramSocket socket = new DatagramSocket();
        // Set timeout for the socket
        socket.setSoTimeout(100);
        // Get the host address
        InetAddress address = InetAddress.getByName(host);
        // Get the server port
        int portNum = Integer.parseInt(port);
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
                // Get input bytes
                byte[] sendData = ((++requestId) + "#PUT " + line).getBytes();
                // Create the datagram packet
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, portNum);
                // Send data to server
                socket.send(sendPacket);
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
                // Get input bytes
                byte[] sendData = ((++requestId) + "#" + line).getBytes();
                // Create the datagram packet
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, portNum);
                // Send data to server
                socket.send(sendPacket);
            }
            sc.close();
            Helper.ClientLog("Finished minimum operations.");

            // Prompt user with input instructions
            Helper.ClientLog("Input your requests. Separate operation name (PUT, GET or DELETE), key (integers only) " + "and/or value (integers only) by space. For example: PUT 1 10; GET 1; DELETE 1.");
            // Process user input
            String line;
            while ((line = reader.readLine()) != null) {
                // Get input bytes
                byte[] sendData = ((++requestId) + "#" + line).getBytes();
                // Create the datagram packet
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, portNum);
                try {
                    // Send data to server
                    socket.send(sendPacket);

                    while (true) {
                        // Get response bytes from server
                        byte[] receiveData = new byte[1024];
                        // Create the datagram packet
                        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        // The datagram packet receives the response from server
                        socket.receive(receivePacket);
                        // Decode the received message
                        String receivedMessage = Helper.decodeData(receiveData);
                        // Split the response ID from the response part
                        String[] responseParts = receivedMessage.split("#");
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
            socket.close();
            reader.close();
        }
    }
}