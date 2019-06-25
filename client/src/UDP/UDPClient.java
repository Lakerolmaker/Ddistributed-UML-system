package UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {

	private DatagramSocket socket = null;

	// : Broadcast a message to all nodes in the network.
	// : Returns the reply from the server
	public String broadcast(String broadcastMessage, InetAddress address, int port) {
		try {
			socket = new DatagramSocket();
			socket.setBroadcast(true);

			byte[] buffer = broadcastMessage.getBytes();

			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
			socket.send(packet);

			DatagramPacket replyData = new DatagramPacket(new byte[256], 256);

			socket.receive(replyData);

			String replyMessage = getMessage(replyData);

			// : Closes the socket connection
			socket.close();

			// : Returns the message from the server
			return replyMessage;
		} catch (IOException e) {
			return null;
		}
	}
	
	public void broadcast_withoutReply(String broadcastMessage, InetAddress address, int port) {
		try {
			socket = new DatagramSocket();
			socket.setBroadcast(true);

			byte[] buffer = broadcastMessage.getBytes();

			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
			socket.send(packet);

		} catch (IOException e) {
			
		}
	}

	// get's the message inside the packet
	private String getMessage(DatagramPacket data) {
		return new String(data.getData()).trim();
	}

}
