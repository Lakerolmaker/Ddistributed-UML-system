package TCP;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;

/*
 * 
 * Modified code from https://gist.github.com/rostyslav
 * 
 * 
 * 
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.zip.ZipFile;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/*
 * 
 *  A class for the server side of the TCP connection 
 *
 */

public class TCPServer {

	public ServerSocket server = null;
	public PostClass post = new PostClass();
	ZIP zip = new ZIP();

	public TCPServer() {
		
	}
	
	public TCPServer(InetSocketAddress adress) {
		this.initializeServer(adress.getAddress(), adress.getPort());
	}
	
	
	//: finds a free port and then adds a socket to the ip-adress of the system
	public void initializeServer() throws Exception {
		int port = findFreePort();
		InetAddress adress = InetAddress.getLocalHost();
		server = new ServerSocket(port, 10, adress);
	}
	
	public void initializeServer(InetAddress address, int port){
		try {
			server = new ServerSocket(port, 10, address);
		} catch (IOException e) {
			System.err.println("Could not create server TCP on - " + address.toString() + ":" + port);
		}
	}

	public String getIp() {
		return server.getInetAddress().getHostAddress();
	}

	public int getPort() {
		return server.getLocalPort();
	}
	
	public InetSocketAddress getAdress() {
		return new InetSocketAddress(this.getIp(), this.getPort());
	}

	public void startTextServer(RunnableArg<String> invocation) {

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
						
						OutputStream os = connectionSocket.getOutputStream();
		                OutputStreamWriter osw = new OutputStreamWriter(os);
		                BufferedWriter bw = new BufferedWriter(osw);
		                String return_message = "ok" + "\n";
		                bw.write(return_message);
		                bw.flush();
						
						invocation.addData(dataString);
						invocation.run();

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};

		new Thread(serverCode).start();

		System.out.println("TCP text-server running on - " + this.getIp() + ":" + this.getPort());
	}

	public void startFileServer(RunnableArg<File> invocation) {

		Runnable serverCode = new Runnable() {

			@Override
			public void run() {

				while (true) {
					
					try {
						
						Socket socket = server.accept();
						File file = saveFile(socket);

						invocation.addData(file);
						invocation.run();

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}

		};

		new Thread(serverCode).start();
		System.out.println("TCP file-server running on - " + this.getIp() + ":" + this.getPort());

	}
	
	//: Saves a file to file when receiving it.
	public File saveFile(Socket socket) throws Exception {
		BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
		String fileName = null;
		File newFile = null;
		try (DataInputStream d = new DataInputStream(in)) {
			fileName = d.readUTF();
			newFile = new File(fileName);
			Files.copy(d, Paths.get(newFile.getPath()));
		} catch (Exception e) {
		}
		return newFile;
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
		throw new IllegalStateException("Could not find a free TCP/IP port");
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

}
