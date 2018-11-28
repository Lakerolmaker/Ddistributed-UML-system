package TCP;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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

	public SocketChannel client = null;
	public InetSocketAddress isa = null;
	private PostClass post = new PostClass();
	ZIP zip = new ZIP();
	
	public void connect(String ipadress, int port) {
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

	public void sendFile(File file) throws IOException{
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		OutputStream os = null;
		try {
			byte b[] = new byte[(int) file.length()];
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);
			bis.read(b, 0, b.length);
			os = getSocket().getOutputStream();
			os.write(b, 0, b.length);
			os.flush();
		} finally {
			if (bis != null)
				bis.close();
			if (os != null)
				os.close();
			if (getSocket() != null)
				getSocket().close();
		}
	}
	
	public Socket getSocket() {
		return this.client.socket();
	}
	
	public String getIP() throws IOException {
		return this.client.getLocalAddress().toString();
	}
	
	public int getport() {
		return this.getSocket().getPort();
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
