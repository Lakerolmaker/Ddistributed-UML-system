package Network;

import java.io.File;

import LakerLibrary.$;

import TCP.RunnableArg;

public class Network_Test {

	public static void main(String[] args) throws Exception {
		
		Network network1 = new Network("client1");
		network1.setSameDeive(true);
		network1.connectToNetwork();
		network1.startTextServer(new RunnableArg<String>() {

			@Override
			public void run() {
				System.out.println("Recived : " + this.getData());

			}

		});
		
		network1.startFileServer(new RunnableArg<File>() {

			@Override
			public void run() {
				System.out.println("Recived File : " + this.getData().getName());

			}

		});
		
		Network network2 = new Network("client2");
		network2.setSameDeive(true);
		network2.connectToNetwork();  
		System.out.println("Sending");
		
		network2.sendToNodes("client1", "hi");
		
		File file = new File("exmpty.txt");
		network2.sendToNodes("client1", file);
		
	}

}
