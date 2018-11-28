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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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

		System.out.println("TCP server running on -" + this.getIp() + ":" + this.getPort());
	}

	public void startFileServer(RunnableArg<File> invocation) {

		Runnable serverCode = new Runnable() {

			@Override
			public void run() {

				while (true) {
					try {
						Socket clientSock = server.accept();
						File newfile = saveFile(clientSock);
						invocation.addArgs(newfile);
						invocation.run();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}

		};

		serverCode.run();

		System.out.println("TCP file-server running on -" + this.getIp() + ":" + this.getPort());

	}

	@SuppressWarnings("finally")
	private File saveFile(Socket clientSock) throws IOException {
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		OutputStream os = null;
		File newfile = null;
		try {
			newfile = new File("newfile.zip");
			byte b[] = new byte[(int) newfile.length()];
			fis = new FileInputStream(newfile);
			bis = new BufferedInputStream(fis);
			bis.read(b, 0, b.length);
			os = clientSock.getOutputStream();
			os.write(b, 0, b.length);
			os.flush();
		} finally {
			if (bis != null)
				bis.close();
			if (os != null)
				os.close();
			if (clientSock != null)
				clientSock.close();
			return newfile;
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
