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


public class GroundLayer {

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
    return false;
  }

  public static void deliverTo(Layer layer) {
		setAboveLayer(layer);
  }

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