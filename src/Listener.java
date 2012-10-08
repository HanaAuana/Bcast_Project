
import java.nio.ByteBuffer;
import rf.RF;


public class Listener  implements Runnable{
	

	private RF rfLayer;
	private byte[] buffer;
	
	public Listener(RF thisRFLayer){
		rfLayer = thisRFLayer;
	}

	
	public void run() {
		
		System.out.println("Listening");
		while(true){
			
			buffer = rfLayer.receive(); //Wait to receive a packet from the RF layer
			
			if(buffer.length == 10){ //Received a full packet
				
				System.out.print("Received: [ "); //Print out the received packet
				for(byte b : buffer){
					System.out.print(" " +(b & 0xFF));
					
				}
				System.out.println(" ]");
				
				byte[] addressBytes = new byte[2]; //Sectioning the buffer into two parts, the address and the body (containing the timestamp)
				byte[] bodyBytes = new byte[8];
				
				addressBytes[0] = buffer[0]; //Grabs the first two bytes (The senders MAC address) and puts them in a separate buffer for later
				addressBytes[1] = buffer[1];
				
				for(int i = 2; i < 10; i++){ //Adds the remaining bytes into a separate ByteBuffer. (Probably could have used just 1?)
					
					bodyBytes[i-2] = buffer[i];
					
				}
				
				ByteBuffer addressBB = ByteBuffer.wrap(addressBytes);  //Uses the built in ByteBuffer class to make it easier to access the byte[] buffer     
				ByteBuffer bodyBB = ByteBuffer.wrap(bodyBytes);        // From http://docs.oracle.com/javase/6/docs/api/java/nio/ByteBuffer.html
				 
				short address = addressBB.getShort();                 //Grabs the two pieces of information from the packet
				long time = bodyBB.getLong();
				
				System.out.println("Host "+ address+ " says the time is: "+ time);
				
				
			}
			else{ //Did not recieve a 10 byte packet, throws an error
				
				System.err.println("Packet was not the right size, expected 10 bytes, recieved: " + buffer.length + " bytes");
			}
			
			
		}
		
		
	}

}
