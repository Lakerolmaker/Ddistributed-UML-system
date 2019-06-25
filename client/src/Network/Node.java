package Network;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class Node {

	public String name;

	// : Name server.
	public String hostname_name;
	public int port_name;

	// : Message server.
	public String hostname_txt;
	public int port_txt;

	// : File server.
	public String hostname_file;
	public int port_file;

	public Node(String name, InetSocketAddress name_reciver, InetSocketAddress txt_server,
			InetSocketAddress file_server) {
		this.name = name;
		this.hostname_name = name_reciver.getHostName();
		this.port_name = name_reciver.getPort();
		this.hostname_txt = txt_server.getHostName();
		this.port_txt = txt_server.getPort();
		this.hostname_file = file_server.getHostName();
		this.port_file = file_server.getPort();
	}

	public String toString() {
		return name + " - msg_server " + hostname_txt + ":" + port_txt;
	}
	
	public InetSocketAddress getNameServer() {
		return new InetSocketAddress(hostname_name, port_name);
	}

	public InetSocketAddress getMsgServer() {
		return new InetSocketAddress(hostname_txt, port_txt);
	}

	public InetSocketAddress getFileServer() {
		return new InetSocketAddress(hostname_file, port_file);
	}

}
