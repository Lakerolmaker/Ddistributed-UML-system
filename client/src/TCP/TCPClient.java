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
 */

public class TCPClient {

	public Socket socket = null;
	private PostClass post = new PostClass();
	ZIP zip = new ZIP();

	public void connect(String ipadress, int port) {
		try {
			socket = new Socket(ipadress, port);
		} catch (Exception e) {
			System.err.println("Could not connect to TCP server. - " + ipadress + ":" + port);
		}
	}
	
	public void connect(InetSocketAddress adress) {
		try {
			socket = new Socket(adress.getAddress(), adress.getPort());
		} catch (Exception e) {
			System.err.println("Could not connect to TCP server. - " + adress.getAddress() + ":" + adress.getPort());
		}
	}

	public void connect(String ipadress, int port, Runnable run) {
		try {
			socket = new Socket(ipadress, port);
			if (socket.isConnected()) {
				run.run();
			}
		} catch (Exception e) {
			System.err.println("Could not connect to TCP server. - " + ipadress + ":" + port);
		}
	}

	public String send(String message) {

		OutputStreamWriter out;
		try {
			// Send the message to the server
			OutputStream os = socket.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			BufferedWriter bw = new BufferedWriter(osw);
			osw.write(message + "\n");
			osw.flush();
			
			//Get the return message from the server
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String returnMessage = br.readLine();
            return returnMessage;
			
		} catch (IOException e) {
			return "";
		}
	}

	public void send(File file) {

		// : if the file is directory , it is first ziped and then sent.
		if (file.isDirectory()) {

			File compressedFile = null;
			try {
				compressedFile = zip.compress(file);
				this.send_file(compressedFile);
			} catch (Exception e) {
				System.err.println("TCP-Client - Could not send file");
			} finally {
				// : deletes the ziped file.
				compressedFile.delete();
			}

			// : if it is a file it is sent normally.
		} else if (file.isFile()) {
			try {
				send_file(file);
			} catch (Exception e) {
				System.err.println("TCP-Client - Could not send file");
			}
		}

	}

	private void send_file(File file) throws IOException {
		BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
		try (DataOutputStream d = new DataOutputStream(out)) {
			d.writeUTF(file.getName());
			Files.copy(file.toPath(), d);
		}
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
	}
}
