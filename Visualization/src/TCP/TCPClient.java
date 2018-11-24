package TCP;

/*
 * 
 * Modified code from https://gist.github.com/rostyslav
 * 
 * 
 * 
 */

/*
 * 
 *  A class for the client side of the TCP connection
 *  
 * 
 * 
 * Written by Jacob Olsson
 * 
 * 
 */

/*
 * 
 *  A class for the servser side of the TCP connection
 * 
 *
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.Scanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TCPClient {

	private static Socket socket;
	private static OutputStream os;
	private static OutputStreamWriter osw;
	private static BufferedWriter bw;
	private PostClass post = new PostClass();
		
	public void start() {
		
		Scanner scanner = new Scanner(System.in);
		
		while(true) {
			if(scanner.hasNextLine()) {
				
				String input = scanner.nextLine();
				
				this.send(input);
				
			}
		}
		
	}
	
	public void setup() {
		
		String ip = "";
		int port = 8888;
		int index = 1;
		System.out.println("IP-Adress: ");
		
		Scanner scanner = new Scanner(System.in);
		
		while(index < 3) {
			if(scanner.hasNextLine()) {
				if(index == 1) {
					ip = scanner.nextLine();
					System.out.println("Port: ");
					index++;
				}else if(index == 2) {
					port = scanner.nextInt();
					index++;
				}
			}	
		}
		this.connect(ip, port);
		this.start();
		
	}

	 	public SocketChannel client = null;
	    public InetSocketAddress isa = null;
	    public RecvThread rt = null;

	    public void connect(String ipadress , int port) {
	        int result = 0;
	        try {
	            client = SocketChannel.open();
	            isa = new InetSocketAddress(ipadress, port);
	            client.connect(isa);
	            client.configureBlocking(false);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	    }

	    public void send(String message) {
	        ByteBuffer bytebuf = ByteBuffer.allocate(1024);
	        int nBytes = 0;
	        try {
	            bytebuf = ByteBuffer.wrap(message.getBytes("UTF-8"));
	            nBytes = client.write(bytebuf);
	            System.out.println("Wrote " + nBytes + " bytes to the server");
	        } catch (Exception e) {
	             System.out.println("Could not send message");
	        }  
	    }

	    public void receiveMessage() {
	        rt = new RecvThread("Receive THread", client);
	        rt.start();
	    }

	    public void interruptThread() {
	        rt.val = false;
	    }

	    public class RecvThread extends Thread {

	        public SocketChannel sc = null;
	        public boolean val = true;

	        public RecvThread(String str, SocketChannel client) {
	            super(str);
	            sc = client;
	        }

	        public void run() {

	            System.out.println("Inside receivemsg");
	            int nBytes = 0;
	            ByteBuffer buf = ByteBuffer.allocate(2048);
	            try {
	                while (val) {
	                    while ((nBytes = client.read(buf)) > 0) {
	                        buf.flip();
	                        Charset charset = Charset.forName("us-ascii");
	                        CharsetDecoder decoder = charset.newDecoder();
	                        CharBuffer charBuffer = decoder.decode(buf);
	                        String result = charBuffer.toString();
	                        System.out.println(result);
	                        buf.flip();

	                    }
	                }

	            } catch (IOException e) {
	                e.printStackTrace();

	            }


	        }
	    }

	    public JsonArray getFromNetwork(String nodeName) throws Exception {
	    	
			post.addPostParamter("action", "lookup");
			post.addPostParamter("name", nodeName);

			post.URL = "http://api.lakerolmaker.com/network_lookup.php";
			
			String reponse = post.post();
			
	    	JsonParser jsonparser = new JsonParser();
	    	
	    	JsonElement root = jsonparser.parse(reponse);
	    	
	    	JsonArray obj = root.getAsJsonArray();

	    	return obj;
	    	
		}

}
