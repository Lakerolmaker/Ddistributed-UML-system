import TCP.RunnableArg;
import TCP.TCP;

public class main {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		TCP tcp = new TCP();
		
		tcp.server.initializeServer();
		
		tcp.server.start(new RunnableArg<String>() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				 System.out.println("called");
			}
		});
		
		tcp.client.start();
	}

}
