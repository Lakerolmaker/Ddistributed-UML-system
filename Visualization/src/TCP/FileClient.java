package TCP;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/*
 * 
 *  A class for sending a file over TCP
 *  
 * 
 * 
 */

public class FileClient {

	private Socket s;

	public FileClient(String host, int port) {
		try {
			s = new Socket(host, port);
			sendFile(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendFile(Socket sr) throws IOException {
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		InputStream is = null;
		try {
			is = sr.getInputStream();
			fos = new FileOutputStream("C:\\Users\\duyka\\imafucktard.zip");
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

	public static void main(String[] args) {
		FileClient fc = new FileClient("localhost", 1988);
	}

}