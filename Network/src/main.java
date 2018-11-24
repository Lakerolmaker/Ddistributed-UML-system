import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import TCP.RunnableArg;
import TCP.TCP;

public class main {

	public static void main(String[] args) throws Exception {
	
		TCP tcp = new TCP();
		
		tcp.server.initializeServer();
		
		tcp.server.start(new RunnableArg<String>() {

			@Override
			public void run() {
			
				try {
					
					JsonArray clients  = tcp.client.getFromNetwork("client");
					
					for (int i = 0; i < clients.size(); i++) {
						
						JsonObject client = clients.get(i).getAsJsonObject();
						
						String ip = client.get("ip").getAsString();
						int port = client.get("port").getAsInt();
						
						tcp.client.connect(ip, port);
						tcp.client.send("file");
						
					}
					
				
				} catch (Exception e) {}
				
			}
			
		});

		tcp.server.post.addPostParamter("master_node", "true");
		tcp.server.addToNetwork("visualizer");
		
	}

}
