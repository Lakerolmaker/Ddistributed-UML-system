package TCP;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * 
 *  A class for receiving a file over TCP
 *  
 * 
 * 
 */

public class FileServer extends Thread {

	private ServerSocket ss;
	private static Socket clientSock;

	public FileServer(int port) {
		try {
			ss = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while (true) {
			try {
				clientSock = ss.accept();
				saveFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void saveFile() throws IOException {
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		InputStream is = null;
		try {
			is = clientSock.getInputStream();
			fos = new FileOutputStream("C:\\Users\\duyka\\lmao.zip");
			bos = new BufferedOutputStream(fos);
			int c = 0;
			byte[] b = new byte[2048];
			while ((c = is.read(b)) > 0) {
				bos.write(b, 0, c);
			}
		} finally {
			if (is != null)
				is.close();
			if (bos != null)
				bos.close();
		}
	}

	public static void main(String[] args) throws IOException {
		FileServer fs = new FileServer(1988);
		fs.run();

	}

}