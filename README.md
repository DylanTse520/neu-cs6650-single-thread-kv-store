# Project 1: Single-Threaded Socket Key-Value Store

## Assignment overview

The purpose of the assignment is to build a key-value store server program that can receive requests from a single client to perform three basic operations: PUT, GET, and DELETE. The server must be single-threaded and must respond to only one request at a time. The key-value store must be implemented using a Hash Map. The scope of the assignment is to implement the server program using two different communication protocols, UDP and TCP. The client and server programs must use sockets for communication, and the user can choose to run the file with either UDP or TCP. The implementation is done in Java and the code should be well-factored, well-commented, and split into multiple functions or classes as appropriate. The client and server must take specific command line arguments, be robust to server failure, be able to handle malformed packets, and log requests and responses in a human-readable format. The client and server should run forever until forcibly killed by an external signal. The server must display requests received and its responses. The client must fulfill the requirements of logging, time-stamping, and robustness.

## Technical impression

During the completion of the assignment, my first impression was: I already know how to do socket programming in Java with the previous homework. But this project asks us to do the socket programming with TCP and UDP. So how do I do the socket programming in TCP and UDP. Then after my research I know that typically the sockets communication in Java are completed by TCP, so for TCP part I just need to polish the program to complete the robustness requirement. For the UDP part, I searched through the Internet and know that it can be completed by Datagram package. So all I need to do is to replace the sockets in the TCP project with the Datagram. Then my second impression is to figure out how to complete the “Other notes” part. This part is a bit confusing because it asks us to first populate the store with data and then complete minimum of 5 operation with each operation. So I asked a question on Piazza to clarify and decided to first have the client program read a file to do the pre-population and then read another file to complete the minimum operation requirement. In the end I still need to offer the ability to allow users to interact with the program through terminal.

## Run the code
Under current directory, open a terminal and use javac to compile 5 programs with the following commands:

```shell
javac TCPServer.java
javac TCPClient.java
javac UDPServer.java
javac UDPClient.java
javac Helper.java
```

(a) To use the TCP Server and TCP Client, start the TCP server with the following command:

```shell
java TCPServer
9086
```

Keep the current terminal, open one more terminal and navigate to the same directory.
Start the TCP client with the following commands:

```shell
java TCPClient
localhost 9086
```

Now the client should be able to make connection with the server.
To put new records in the store, try "PUT 1 10". Note that both the key and the value should be integers.
To get a value by a key, try "GET 1".
To delete a key, try "DELETE 1".

(b) To use the UDP Server and UDP Client, start the UDP server with the following command:

```shell
java UDPServer
9086
```

Keep the current terminal, open one more terminal and navigate to the same directory.
Start the UDP client with the following commands:

```shell
java UDPClient
localhost 9086
```

Now the client should be able to make connection with the server.
The commands are similar.

The PREPOPULATION file is for storing the pre-population records.
The MINIMUM_OPERATION file is for storing the operations to be completed by the clients.