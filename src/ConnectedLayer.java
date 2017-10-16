/**
 * @author Bastien Chevallier
 * @author Jules Yates
 */


public class ConnectedLayer implements Layer {
	String destinationHost;
	int ConnectionId;
	int destinationPort;
	int pckt_number;
	Layer LayerAbove;
	
	public ConnectedLayer(String destinationHost, int destinationPort, int ConnectionId ) {

		this.destinationHost = destinationHost;
		this.destinationPort = destinationPort;
		this.pckt_number = 0;
		this.ConnectionId = ConnectionId;
		//The connected layer is the layer above the GroundLayer
		GroundLayer.deliverTo(this);
		//First message
		this.send("--HELLO--");
	}
	
	@Override
	public void send(String payload) {
		String wrapped_payload;
		wrapped_payload = Integer.toString(ConnectionId) + ";" + Integer.toString(pckt_number) + ";" + payload;
		pckt_number++;
		GroundLayer.send(wrapped_payload, destinationHost, destinationPort);
	}

	@Override
	public void receive(String payload, String source) {
		String[] parsed_payload = payload.split(";",3);
		int Connect_Id = Integer.parseInt(parsed_payload[0]);
		int pckt_numb = Integer.parseInt(parsed_payload[1]);
		String rcvd_payload = parsed_payload[2];
		
		//TODO Add the verification of ConnectionID and packet number?
		if(rcvd_payload.equals("--HELLO--")) {
			String ack = Integer.toString(Connect_Id) + ";" + Integer.toString(pckt_numb) + ";"+"--ACK--";
			GroundLayer.send(ack, destinationHost, destinationPort);
		}else if (!rcvd_payload.equals("--ACK--")) {
			String ack = Integer.toString(Connect_Id) + ";" + Integer.toString(pckt_numb) + ";"+"--ACK--";
			LayerAbove.receive(rcvd_payload, source);
			GroundLayer.send(ack, destinationHost, destinationPort);
		}
	}

	@Override
	public void deliverTo(Layer above) {
		LayerAbove = above;
	}
	
	@Override
	public void close() {
		GroundLayer.close();
	}
}
