import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ConnectedLayer implements Layer {
	String destinationHost;
	int ConnectionId;
	int destinationPort;
	int pckt_number;
	
	public ConnectedLayer(String destinationHost, int destinationPort, int ConnectionId ) {

		this.destinationHost = destinationHost;
		this.destinationPort = destinationPort;
		this.pckt_number = 0;
		this.ConnectionId = ConnectionId;
		
		//Configuration of the wrapped payload v.0
		String wrapped_payload = Integer.toString(ConnectionId)+";"+Integer.toString(pckt_number) + ";" +"--Hello--";
		this.send(wrapped_payload);
	}

	public void send(String payload) {
		if(pckt_number!=0) {
			pckt_number ++;
			String wrapped_payload = Integer.toString(ConnectionId)+ ";" + Integer.toString(pckt_number) + ";" + payload;
		}
		GroundLayer.send(payload, destinationHost, destinationPort);
		pckt_number ++;
	}


	public void receive(String payload, String source) {
		String[] parsed_payload = payload.split(";",3);
		int pckt_numb = Integer.parseInt(parsed_payload[1]);
		String rcvd_payload = parsed_payload[2];
		if (pckt_numb==pckt_number && !rcvd_payload.equals("--ACK--")) {
			String ack = parsed_payload[0]+parsed_payload[1]+"--ACK--";
			GroundLayer.send(ack, destinationHost, destinationPort);
		}else {
			
		}
	}


	public void deliverTo(Layer above) {
	}

	public void close() {

	}
}
