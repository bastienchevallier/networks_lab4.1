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
		String wrapped_payload;
		if(pckt_number==0) {
			wrapped_payload = Integer.toString(ConnectionId)+ ";" + Integer.toString(pckt_number) + ";" + payload;
		}else {
			pckt_number++;
			wrapped_payload = Integer.toString(ConnectionId)+ ";" + Integer.toString(pckt_number) + ";" + payload;
		}
		GroundLayer.send(wrapped_payload, destinationHost, destinationPort);
	}


	public void receive(String payload, String source) {
		String[] parsed_payload = payload.split(";",3);
		int pckt_numb = Integer.parseInt(parsed_payload[1]);
		String rcvd_payload = parsed_payload[2];
		if (pckt_numb==pckt_number+1 && !rcvd_payload.equals("--ACK--")) {
			String ack = parsed_payload[0]+parsed_payload[1]+"--ACK--";
			GroundLayer.send(ack, destinationHost, destinationPort);
		}else {
			//Do something
		}
	}


	public void deliverTo(Layer above) {
		//TODO To verify
		GroundLayer.deliverTo(above);
	}

	public void close() {
		//TODO To verify
		GroundLayer.close();
	}
}
