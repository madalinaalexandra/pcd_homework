package server;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {

	public static int port = 1324;

	public static String TCP = "TCP";

	public static String UDP = "UDP";

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
					input.readFully(messageBuf, 0, messageBuf.length); // read the message
					
					String message = new String(messageBuf);
					ServerMain.writeToFile("input.txt", message);
					
					System.out.println("Message received from client: " + clientAddress);
					System.out.println("Size of message: "+ message.length());
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
			while (true) {
				System.out.println("Waiting for clients...");
				
				byte[] buf = new byte[4096];
	            DatagramPacket messagePacket = new DatagramPacket(buf, buf.length);
	            serverSocket.receive(messagePacket);
	            
	            InetAddress address = messagePacket.getAddress();
	            int port = messagePacket.getPort();
	            messagePacket = new DatagramPacket(buf, buf.length, address, port);
	            
	            String message = new String(messagePacket.getData(), 0, messagePacket.getLength());
	            
				System.out.println("Message received from client: "  + address.getHostAddress());

				System.out.println("Size of message: "+ message.length());
			}
		}

	}

	public static void writeToFile(String fileName, String message) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
	    writer.write(message);
	    writer.close();
	}
}
