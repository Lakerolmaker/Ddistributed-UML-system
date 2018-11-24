package TCP;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
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
				Socket clientSock = ss.accept();
				saveFile(clientSock);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void saveFile(Socket clientSock) throws IOException {
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		OutputStream os = null;
		try {
			File myFile = new File("D:\\Download\\yuki.zip");
			byte b[] = new byte[(int) myFile.length()];
			fis = new FileInputStream(myFile);
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
		}
	}

	public static void main(String[] args) {
		FileServer fs = new FileServer(1988);
		fs.run();
	}

}