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
	Layer AboveLayer;
	DatagramSocket socket;
	
	public void Messenger(){
		this.socket = GroundLayer.socket;
		this.AboveLayer = GroundLayer.AboveLayer;
	}
	
	public void run(){
		byte[] data=new byte[1024];
		//TODO datagram size?
		DatagramPacket datagram = new DatagramPacket(data,1024);
		//TODO infinite loop?
		while(true){
			try{
				socket.receive(datagram);
				String payload = new String(datagram.getData(),this.CONVERTER);
				String source = new String(datagram.getAddress().getAddress(),this.CONVERTER);
				AboveLayer.receive(payload,source);
			}catch(IOException e){
				System.err.println(e.getMessage());
			}
		}
		
	}
}

public class GroundLayer {
	public static DatagramSocket socket;
	public static Layer AboveLayer;
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
			
			return true;
		}catch(SocketException e){
			System.out.println(e.getMessage());
			return false;
		}


	}

<<<<<<< HEAD
  public static void deliverTo(Layer layer) {
		setAboveLayer(layer);
  }
=======
	public static void deliverTo(Layer layer) {
		AboveLayer=
	}
>>>>>>> branch 'master' of https://github.com/bastienchevallier/networks_lab4.1.git

<<<<<<< HEAD
  public static void send(String payload, String destinationHost,
      int destinationPort) {
	  try {
		  if(Math.random()<RELIABILITY) {
			  InetAddress HostAddress = InetAddress.getByName(destinationHost);
			  DatagramPacket _payload = new DatagramPacket(payload.getBytes(),payload.length(),HostAddress,destinationPort);
			  DatagramSocket socket = new DatagramSocket();
			  socket.send(_payload);
			  socket.close();
		  }
	  }catch(SocketException e) {
		  System.err.println("Exception throws by the socket : " + e.getMessage());
	  }catch(IOException e) {
		  System.err.println("Wrong destinationHost : "+e.getMessage());
	  } 
  }
=======
	public static void send(String payload, String destinationHost,
			int destinationPort) {
	}
>>>>>>> branch 'master' of https://github.com/bastienchevallier/networks_lab4.1.git

	public static void close() {
		
		System.err.println("GroundLayer closed");
	}

public static Layer getAboveLayer() {
	return AboveLayer;
}

public static void setAboveLayer(Layer aboveLayer) {
	AboveLayer = aboveLayer;
}

}