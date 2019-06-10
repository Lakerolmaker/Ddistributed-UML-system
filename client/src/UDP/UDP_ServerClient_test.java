package UDP;

import java.net.InetAddress;

import TCP.RunnableArg;

public class UDP_ServerClient_test {

	public static void main(String[] args) throws Exception {
		// launch(args);

		UDP udp = new UDP();
		
		udp.server.start(5400, new  RunnableArg<String>() {
			
			@Override
			public void run() {
				 System.out.println("Recived message : " + this.getArg());
			}
			
		});

		String reply = udp.client.broadcast("hello", InetAddress.getByName("127.0.0.1") , 5400);

		System.out.println("Reply from server : " + reply);
	}
	
	
}
