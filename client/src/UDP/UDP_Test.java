package UDP;

import java.net.InetAddress;

import TCP.RunnableArg;

public class UDP_Test {

	public static void main(String[] args) throws Exception {
		UDP udp = new UDP();
		udp.server.start(5400, new  RunnableArg<String>() {
			@Override
			public void run() {
				System.out.println("Recived message : " + this.getData());
			}
			
		});

		String reply = udp.client.broadcast("hello", InetAddress.getByName("255.255.255.255") , 5400);
		System.out.println("Reply from server : " + reply);
	}
	
	
}
