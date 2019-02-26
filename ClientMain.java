package client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class ClientMain {

	public static String host = "127.0.0.1";
	public static int port = 1324;
	public static String TCP = "TCP";
	public static String UDP = "UDP";

	public static void main(String[] args) throws UnknownHostException, IOException {
		if (args == null || args.length == 0) {
			System.out.println("Please insert protocol (TCP/UDP) as argument");
			System.exit(0);
		}

		if (args[0].equals(ClientMain.TCP)) {
			ClientMain.sendTcpMessage();
		}

		if (args[0].equals(ClientMain.UDP)) {
			ClientMain.sendUdpMesasge();
		}

	}

	public static void sendTcpMessage() throws UnknownHostException, IOException {

		try (Socket socket = new Socket(host, port);
				Scanner input = new Scanner(System.in);
				Scanner responseStream = new Scanner(socket.getInputStream())) {

			DataOutputStream requestStream = new DataOutputStream(socket.getOutputStream());

			String filePath = input.next();

			byte[] message = Files.readAllBytes(Paths.get(filePath));
			requestStream.writeInt(message.length); // write length of the
													// message
			requestStream.write(message);
		}
	}

	public static void sendUdpMesasge() throws IOException {

		try (DatagramSocket socket = new DatagramSocket(); Scanner input = new Scanner(System.in);) {

			String filePath = input.next();
			
			byte[] messageBuf = Files.readAllBytes(Paths.get(filePath));
			
			DatagramPacket messagePacket = new DatagramPacket(messageBuf, messageBuf.length, new InetSocketAddress(host, port));
			socket.send(messagePacket);

		}
	}

}
