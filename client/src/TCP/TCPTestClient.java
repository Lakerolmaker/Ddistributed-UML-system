package TCP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/*
 * 
 *  A class for testing the TCP 
 * 
 * 
 */


public class TCPTestClient {
    private Socket socket;
    private Scanner scanner;
    
    private TCPTestClient(InetAddress serverAddress, int serverPort) throws Exception {
        this.socket = new Socket(serverAddress, serverPort);
        this.scanner = new Scanner(System.in);
    }
    
    private void start() throws IOException {
        String input;        
        
        while (true) {
            input = scanner.nextLine();
            PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
           
            out.println(input);
            out.flush();
              
        }
                
    }
    
    public static void main(String[] args) throws Exception {
        TCPTestClient client = new TCPTestClient(InetAddress.getByName("127.0.0.1"), 53530);
        
        System.out.println("\r\nConnected to Server: " + client.socket.getInetAddress());
        client.start();                
    }
}