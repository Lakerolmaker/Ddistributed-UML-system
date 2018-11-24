package TCP;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;

/*
 * 
 * Modified code from https://gist.github.com/rostyslav
 * 
 * 
 * 
 */

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/*
 * 
 *  A class for the server side of the TCP connection
 * 
 *
 */

public class TCPServer {

	public Selector sel = null;
	public ServerSocket server = null;
	private SocketChannel socket = null;
	String result = null;
	public PostClass post = new PostClass();

	public void initializeServer() throws Exception {
		int port = findFreePort();
		InetAddress adress = InetAddress.getLocalHost();
		server = new ServerSocket(port, 10, adress);
		System.out.println("Server running on - " + this.getIp() + " : " + this.getPort());
	}
	
	public String getIp() {
		return server.getInetAddress().getHostAddress();
	}
	
	public int getPort() {
		return server.getLocalPort();
	}

	public void start(RunnableArg<String> invocation) throws Exception {

		Runnable serverCode = new Runnable() {

			@Override
			public void run() {

				while (true) {

					try {

						Socket connectionSocket = server.accept();

						InputStream strm = connectionSocket.getInputStream();
						InputStreamReader in = new InputStreamReader(strm);
						BufferedReader br = new BufferedReader(in);
						 	 
						String dataString = br.readLine();
						

						invocation.setArg(dataString);
						invocation.run();

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}

		};

		new Thread(serverCode).start();
	}

	public void writeMessage(SocketChannel socket, String ret) {

		if (ret.equals("quit") || ret.equals("shutdown")) {
			return;
		}
		File file = new File(ret);
		try {

			RandomAccessFile rdm = new RandomAccessFile(file, "r");
			FileChannel fc = rdm.getChannel();
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			fc.read(buffer);
			buffer.flip();

			Charset set = Charset.forName("UTF-8");
			CharsetDecoder dec = set.newDecoder();
			CharBuffer charBuf = dec.decode(buffer);
			System.out.println(charBuf.toString());
			buffer = ByteBuffer.wrap((charBuf.toString()).getBytes());
			int nBytes = socket.write(buffer);
			System.out.println("nBytes = " + nBytes);
			result = null;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String readMessage(SelectionKey key) throws Exception {
		int nBytes = 0;
		socket = (SocketChannel) key.channel();
		ByteBuffer buf = ByteBuffer.allocate(1024);
		try {
			nBytes = socket.read(buf);
			buf.flip();
			Charset charset = Charset.forName("UTF-8");
			CharsetDecoder decoder = charset.newDecoder();
			CharBuffer charBuffer = decoder.decode(buf);
			System.out.println("Recievied " + nBytes + " bytes");
			return charBuffer.toString();
		} catch (Exception e) {
			throw new Exception("Failed to read message");
		}

	}

	private boolean isDone(SelectionKey key) {
		int nBytes = 0;
		socket = (SocketChannel) key.channel();
		ByteBuffer buf = ByteBuffer.allocate(1024);
		try {
			nBytes = socket.read(buf);
			if (nBytes > 0) {
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			return true;
		}
	}

	private static int findFreePort() {
		ServerSocket socket = null;
		try {
			socket = new ServerSocket(0);
			socket.setReuseAddress(true);
			int port = socket.getLocalPort();
			try {
				socket.close();
			} catch (IOException e) {
				// Ignore IOException on close()
			}
			return port;
		} catch (IOException e) {
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
				}
			}
		}
		throw new IllegalStateException("Could not find a free TCP/IP port to start embedded Jetty HTTP Server on");
	}

	public void addToNetwork(String name) throws Exception {

		String ip = this.server.getInetAddress().getHostAddress();
		String port = String.valueOf(this.server.getLocalPort());

		post.addPostParamter("action", "insert");
		post.addPostParamter("name", name);
		post.addPostParamter("ip", ip);
		post.addPostParamter("port", port);

		post.URL = "http://api.lakerolmaker.com/network_lookup.php";

		post.post();
	}

	public void getFromNetwork() {

	}

}
