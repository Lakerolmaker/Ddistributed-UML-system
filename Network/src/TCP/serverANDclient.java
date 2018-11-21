package TCP;

import java.util.Scanner;


/*
 * 
 *  A test class for setting up a tcp connection
 * 
 * 
 */

public class serverANDclient {

	public static void main(String[] args) throws Exception {
		
		TCP tcp = new TCP();
		
		System.out.println("TCP service");
		System.out.println("server : To start a TCP server");
		System.out.println("client : To start a TCP client");
		
		Scanner scanner = new Scanner(System.in);
		
		 while(true) {
     		
     			while(scanner.hasNextLine()) {
     				
     				String input = scanner.nextLine();
     				
     				if(input.equals("server")) {
     					
     					
     					System.out.println("Hostname :");
     					String server = scanner.next();
     					
     					System.out.println("port :");
     					int port = scanner.nextInt();

     					tcp.server.initializeServer(server, port);
     					
     					tcp.server.start(new RunnableArg<String>() {

							@Override
							public void run() {
								
								
							}
     						
						});
     					
     				}else if(input.equals("client")) {
     					tcp.client.setup();
     				}
     				
     			}
     			
		 }//: end while(true)
		
	}
	
	
	
}
