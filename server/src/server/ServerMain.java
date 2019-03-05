package server;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {

	public static int port = 1324;

	public static String TCP = "TCP";

	public static String UDP = "UDP";
	
	public static int bytesReceived = 0;

	public static void main(String[] args) throws IOException {
		if (args == null || args.length == 0) {
			System.out.println("Please insert protocol (TCP/UDP) as argument");
			System.exit(0);
		}

		if (args[0].equals(ServerMain.TCP)) {
			ServerMain.initializeTcpServer();
		}

		if (args[0].equals(ServerMain.UDP)) {
			ServerMain.initializeUdpServer();
		}
	}

	public static void initializeTcpServer() throws IOException {

		try (ServerSocket server = new ServerSocket(port)) {

			while (true) {
				System.out.println("Waiting for clients...");

				Socket client = server.accept();
				String clientAddress = client.getInetAddress().getHostAddress();
				System.out.println("\r\nNew connection from " + clientAddress);

				DataInputStream input = new DataInputStream(client.getInputStream());

				int length = input.readInt(); // read length of incoming message
				if (length > 0) {
					byte[] messageBuf = new byte[length];

					long startTime = System.currentTimeMillis();
					
					input.readFully(messageBuf, 0, messageBuf.length); // read the message

		            long finalTime = System.currentTimeMillis();
		            
					String message = new String(messageBuf);
					ServerMain.writeToFile("input.txt", message);
					
					System.out.println("Message received from client: " + clientAddress);
					
					System.out.println("Size of message: "+ message.length());
					
					System.out.println("Time: " + (finalTime - startTime) + "ms");
				}
				System.out.println("Closing connection");

				// close connection
				client.close();
				input.close();
			}
		}
	}

	public static void initializeUdpServer() throws IOException{
		try (DatagramSocket serverSocket = new DatagramSocket(port)) {
			System.out.println("Waiting for clients...");
			
        	byte[] messageBuf = new byte[1024];
        	
            DatagramPacket messagePacket = new DatagramPacket(messageBuf, messageBuf.length);
			while (true) {
	            serverSocket.receive(messagePacket);
	            
	            bytesReceived += messagePacket.getLength();
	            
				System.out.println("Size of message: "+ ServerMain.bytesReceived);
			}
		}

	}

	public static void writeToFile(String fileName, String message) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
	    writer.write(message);
	    writer.close();
	}
}
