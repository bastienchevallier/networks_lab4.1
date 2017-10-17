import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.rmi.UnknownHostException;
import java.net.DatagramPacket;


class Messenger implements Runnable{
	private final Charset CONVERTER = StandardCharsets.UTF_8;

	public Messenger(){
	}

	public void run(){
		byte[] data=new byte[1024];
		//TODO datagram size?
		DatagramPacket datagram = new DatagramPacket(data,data.length);
		//TODO infinite loop?
		while(!GroundLayer.getStop()){
			try{
				GroundLayer.getDatagramSocket().receive(datagram);
				String payload = new String(datagram.getData(),this.CONVERTER);
				String source = new String(datagram.getAddress().getHostAddress());
				while(GroundLayer.getAboveLayer()==null){
				}
				GroundLayer.getAboveLayer().receive(payload,source);
				
			}catch(IOException e){
				System.err.println(e.getMessage());
			}
		}

	}
}

public class GroundLayer {
	private static Boolean stop=false;
	private static DatagramSocket socket;
	/**
	 * This {@code Charset} is used to convert between our Java native String
	 * encoding and a chosen encoding for the effective payloads that fly over the
	 * network.
	 */
	private static final Charset CONVERTER = StandardCharsets.UTF_8;

	/**
	 * This value is used as the probability that {@code send} really sends a
	 * datagram. This allows to simulate the loss of packets in the network.
	 */
	public static double RELIABILITY = 1.0;
	private static Layer AboveLayer;

	public static boolean start(int localPort) {
		try{
			socket = new DatagramSocket(localPort);
			Messenger _messenger = new Messenger();
			Thread thread = new Thread(_messenger);
			thread.start();

			return true;
		}catch(SocketException e){
			System.err.println(e.getMessage());
			return false;
		}catch(IOException e){
			System.err.println(e.getMessage());
			return false;
		}


	}

	public static void deliverTo(Layer layer) {
		setAboveLayer(layer);
	}


	public static void send(String payload, String destinationHost,
			int destinationPort) {
		try {
			if(Math.random()<RELIABILITY) {
				InetAddress HostAddress = InetAddress.getByName(destinationHost);
				byte[] buffer = payload.getBytes(CONVERTER);
				DatagramPacket _payload = new DatagramPacket(buffer,buffer.length,HostAddress,destinationPort);
				socket.send(_payload);

			}
		}catch(SocketException e) {
			System.err.println("Exception throws by the socket : " + e.getMessage());
		}catch(IOException e) {
			System.err.println("Wrong destinationHost : " + e.getMessage());
		} 
	}

	public static void close() {
		stop=true;
		socket.close();
		System.err.println("GroundLayer closed");
	}

	public static Layer getAboveLayer() {
		return AboveLayer;
	}

	public static DatagramSocket getDatagramSocket(){
		return socket;
	}
	
	public static Boolean getStop(){
		return stop;
	}
	public static void setAboveLayer(Layer aboveLayer) {
		AboveLayer = aboveLayer;
	}

}