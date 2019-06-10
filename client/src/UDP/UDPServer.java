package UDP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

import TCP.RunnableArg;

public class UDPServer {

	public void start(int port, RunnableArg<String> invocation) throws Exception {

		Runnable serverCode = new Runnable() {

			DatagramSocket socket = new DatagramSocket(port);
			DatagramPacket packet = new DatagramPacket(new byte[256], 256);

			@Override
			public void run() {

				while (true) {

					try {
						socket.receive(packet);
						String message = getMessage(packet);

						// : Adds the message to the invocation queue
						invocation.addArgs(message);

						InetAddress address = packet.getAddress();
						int port = packet.getPort();

						// : Sends a reply back to the client
						String reply = "ok";
						socket.send(getPacket(reply, address, port));

						// : Calls the server code
						invocation.run();
					} catch (Exception e) {
					}

				}

			}

		};

		new Thread(serverCode).start();
		System.out.println("UDP text-server running");

	}


	// get's the message inside the packet
	private String getMessage(DatagramPacket data) {
		return new String(data.getData()).trim();
	}

	// : Creates a packet from a message, address and port
	private DatagramPacket getPacket(String message, InetAddress address, int port) {
		byte[] byteSendMessage = message.getBytes();
		return new DatagramPacket(byteSendMessage, byteSendMessage.length, address, port);
	}

}
