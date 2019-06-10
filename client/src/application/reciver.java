package application;

import java.net.InetAddress;
import java.util.List;

import UDP.UDP;

public class reciver {

	public static void main(String[] args) throws Exception {
		
		UDP udp = new UDP();
		
		List<InetAddress> hi = udp.server.listAllBroadcastAddresses();
		
		System.out.println(hi);
	}
}
