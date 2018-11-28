package TCP;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

/*
 * 
 *  A old class for sending messages over TCP
 *  
 * 
 * 
 */


public class oldTCP {

	private static Socket socket;
	private static OutputStream os;
	private static OutputStreamWriter osw;
	private static BufferedWriter bw;

	public void connect(String ipAdress) {
		 try {
			 	String host = "172.20.10.6";
	            int port = 8888;
	            System.out.println("creating socket");
	            socket = new Socket(ipAdress, port);
	            	System.out.println("socket created");
	            	
	            	os = socket.getOutputStream();
	            	osw = new OutputStreamWriter(os);
	            	bw = new BufferedWriter(osw);
	            	
		} catch (Exception e) {
			System.out.println("could not connect to server");
		}
	            
	          
	}
	
	public void send(String data){
		try {

			//Send the message to the server
            OutputStream os = socket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            osw.write(data);
            osw.flush();
            
            System.out.println("Message sent to the server : " + data);

		} catch (Exception e) {
			System.out.println("Could not send Message");
		}
	}
	
	public void close() {
		//Closing the socket
        try
        {
            socket.close();
            System.out.println("connection closed");
        }
        catch(Exception e)
        {
        	System.out.println("could not close connection");
        }
	}
	
	
}