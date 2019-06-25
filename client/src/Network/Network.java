package Network;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.google.gson.Gson;

import TCP.RunnableArg;
import TCP.TCP;
import TCP.TCPClient;
import TCP.TCPServer;
import UDP.UDP;
import UDP.UDPServer;

public class Network {

	private Node self;

	private UDP udp = new UDP();
	private TCPServer nameReciver = new TCPServer();
	private TCPServer txtServer = new TCPServer();
	private TCPServer fileServer = new TCPServer();

	private Gson jsonParser = new Gson();
	private ArrayList<Node> Nodes = new ArrayList<Node>();

	// : This can be switched on to have different nodes on the same device.
	private Boolean sameDevice = false;
	private int numberOfInstancesOnDevice = 10;

	// : Creates the network session
	public Network(String name) throws Exception {
		nameReciver.initializeServer();
		txtServer.initializeServer();
		fileServer.initializeServer();
		this.self = createSelf(name);
	}
	// : Set's up the name server, which holds the serves identity.
	private Node createSelf(String name) throws Exception {
		return new Node(name, nameReciver.getAdress(), txtServer.getAdress(), fileServer.getAdress());
	}

	public void setSameDeive(Boolean sameDevice) {
		this.sameDevice = sameDevice;
	}

	public void setInstancesOnSameDevice(int number) {
		this.numberOfInstancesOnDevice = number;
	}

	public void connectToNetwork() throws Exception {
			startNameReciver();
			startNameServer(3434);
			annoucePresence(3434);
			//: Wait's for the other nodes to respond.
			sleep(1000);
	}

	public void startTextServer(RunnableArg<String> invocation) {
		txtServer.startTextServer(invocation);
	}

	public void startFileServer(RunnableArg<File> invocation) {
		fileServer.startFileServer(invocation);
	}

	TCPClient client = new TCPClient();
	public void sendToNodes(String nodeName, String msg) {
		for (Node node : getNodes(nodeName)) {
			client.connect(node.getMsgServer());
			client.send(msg);
		}
	}
	
	public void sendToAllNodes(String msg) {
		for (Node node : Nodes) {
			client.connect(node.getMsgServer());
			client.send(msg);
		}
	}
	
	public void sendToNodes(String nodeName , File file) {
		for (Node node : getNodes(nodeName)) {
			client.connect(node.getFileServer());
			client.send(file);
		}
	}
	
	public void sendToAllNodes(File file) {
		for (Node node : Nodes) {
			client.connect(node.getFileServer());
			client.send(file);
		}
	}

	private ArrayList<Node> getNodes(String name) {
		ArrayList<Node> retrived_nodes = new ArrayList<Node>();
		for (Node node : Nodes) {
			if(node.name.equals(name)) {
				retrived_nodes.add(node);
			}
		}
		return retrived_nodes;
	}

	// : Adds a node to the list of nodes, if the node isn't in the list
	private void addNode(Node newnode) {
		if (same_node(self, newnode)) {  
			return;
		}
		for (Node node : Nodes) {
			if (same_node(node, newnode)) {
				return;
			}
		}
		Nodes.add(newnode);
		printNode(newnode);
	}

	private boolean same_node(Node node1, Node node2) {
		return node1.toString().equals(node2.toString());
	}
	
	public void printNode(Node node) {
		System.out.println("Node info recived : " + node.toString());
	}

	// : Sends out a broadcast, of the address of the name server to the network to
	// announce yourself
	private void annoucePresence(int port) throws UnknownHostException, IOException {
		if (this.sameDevice) {
			for (int i = 0; i < this.numberOfInstancesOnDevice; i++) {
				udp.client.broadcast_withoutReply(jsonParser.toJson(this.self), InetAddress.getByName("255.255.255.255"), port);
				port++;
			}
		} else {
			udp.client.broadcast(jsonParser.toJson(this.self), InetAddress.getByName("255.255.255.255"), port);
		}
	}

	// : Listens for nodes sharing their information
	private void startNameReciver() throws Exception {
		nameReciver.startTextServer(new RunnableArg<String>() {
			@Override
			public void run() {
				Node node = jsonParser.fromJson(this.getData(), Node.class);
				addNode(node);
			}
		});
	}

	// : Listens for other nodes announcing themselves.
	// : If a node announces themselves,a message is sent with the information of
	// ourself
	private void startNameServer(int port) {
		try {
			udp.server.start(port, new RunnableArg<String>() {
				@Override
				public void run() {
					String json = this.getData();
					Node node = jsonParser.fromJson(json, Node.class);
					addNode(node);
					sendSelfData(node);
				}
			});
		} catch (SocketException e) {
			// : Is the port is already in use and the same device is on
			if ((e.getMessage().equals("Address already in use (Bind failed)")) && (this.sameDevice)) {
				System.err.println("Port " + port + " allready in use, testing next one");
				startNameServer(port + 1);
			} else {
				System.err.println(e.getMessage());
			}
		}
	}

	private void sendSelfData(Node node) {
		TCP nodeTCP = new TCP();
		nodeTCP.client.connect(node.getNameServer());
		nodeTCP.client.send(jsonParser.toJson(self));
	}
	
	private void sleep(int time) {
		try        
		{
		    Thread.sleep(time);
		} 
		catch(InterruptedException ex) 
		{
		    Thread.currentThread().interrupt();
		}
	}

}
