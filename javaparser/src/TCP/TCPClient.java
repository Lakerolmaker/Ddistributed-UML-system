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
import java.io.DataOutputStream;
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
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Scanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


/*
 * 
 *  A class for the client side of the TCP connection
 * 
 *
 */

public class TCPClient {
	
	public Socket socket = null;
	private PostClass post = new PostClass();
	ZIP zip = new ZIP();
	private RunnableArg<String> connectInvocation;
	public boolean showOutput = false;

	public void connect(String ipadress, int port) throws UnknownHostException, IOException {
		socket = new Socket(ipadress, port);
	}

	public void send(String message) {

		OutputStreamWriter out;
		try {
			// Send the message to the server
			OutputStream os = socket.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			BufferedWriter bw = new BufferedWriter(osw);
			osw.write(message + "\n");
			osw.flush();
			if (showOutput)
				System.out.println("TCP-Client - Wrote " + message.getBytes().length + " bytes to the server");
		} catch (IOException e) {
		}
	}

	public void sendFile(File file){

		// : if the file is directory , it is ziped and sent.
		if (file.isDirectory()) {

			File compressedFile = null;
			try {
				compressedFile = zip.compress(file);
				send_a_file(compressedFile);
			}catch(Exception e){
				System.err.println("TCP-Client - Could not send file");
			}finally {
				// : deletes the ziped file.
				compressedFile.delete();
			}

			// : if it is a normal file it is send normally.
		} else if (file.isFile()) {
			try {
				send_a_file(file);
			}catch(Exception e){
				System.err.println("TCP-Client - Could not send file");
			}
		}

	}

	private void send_a_file(File file) throws IOException {
		BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
		try (DataOutputStream d = new DataOutputStream(out)) {
			d.writeUTF(file.getName());
			Files.copy(file.toPath(), d);
		}
		if (showOutput)
			System.out.println("TCP-client - Sent a file ");
	}

	public Socket getSocket() {
		return this.socket;
	}

	public String getIP() throws IOException {
		return getSocket().getInetAddress().toString();
	}

	public int getport() {
		return this.getSocket().getPort();
	}

	public void connectTNetwork(String nodeName) throws Exception {

		post.addPostParamter("action", "lookup");
		post.addPostParamter("name", nodeName);

		post.URL = "http://api.lakerolmaker.com/network_lookup.php";

		String reponse = post.post();

		JsonParser jsonparser = new JsonParser();

		JsonElement root = jsonparser.parse(reponse);

		JsonArray obj = root.getAsJsonArray();

		JsonObject client = obj.get(0).getAsJsonObject();

		String ip = client.get("ip").getAsString();
		int port = client.get("port").getAsInt();

		this.connect(ip, port);
		runInvoation();
	}

	public void runInvoation() {
		if (this.connectInvocation != null) {
			this.connectInvocation.run();
		}
	}

	public void onConnect(RunnableArg<String> invocation) {

			this.connectInvocation = invocation;
	}

	public boolean isConnected() {
		if(this.socket != null)
			return this.socket.isConnected();
		else
			return false;			
	}

	public void showOutput(Boolean value) {
		this.showOutput = value;
	}

}
