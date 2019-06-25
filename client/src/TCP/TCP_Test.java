package TCP;

import java.io.File;

public class TCP_Test {
	
	public static void main(String[] args) throws Exception {

		TCP tcp = new TCP();	
		tcp.server.initializeServer();
		tcp.server.startTextServer(new RunnableArg<String>() {

			@Override
			public void run() {
				System.out.println("Recived : " + this.getData());
			}
		});
		
	
		tcp.client.connect(tcp.server.getAdress());
		String returned_msg = tcp.client.send("yo");
		System.out.println("Returned : " + returned_msg);
	
	}
}
