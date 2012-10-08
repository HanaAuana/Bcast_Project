import java.nio.ByteBuffer;
import rf.RF;
import java.util.Random;


public class Sender implements Runnable {
	
	

	private RF rfLayer;
	private byte[] buffer;
	private int mac;
	
	public Sender(RF thisRFLayer, int thisMAC){
		rfLayer = thisRFLayer;
		mac = thisMAC;
		buffer = new byte[10];
	}

	
	public void run() {
		
		System.out.println("Sending");
		
		
		short shortMAC = (short)mac;
		ByteBuffer packetBB = ByteBuffer.wrap(buffer); //Uses a ByteBuffer to insert a short into the buffer for writing to the packet 
		packetBB.putShort(0,shortMAC);                 //  From http://docs.oracle.com/javase/6/docs/api/java/nio/ByteBuffer.html
		                                               // Same with the long insert below
		
		while(true){
			
			long timestamp = rfLayer.clock();  //Gets the current time according to the RF layer
			packetBB.putLong(2,timestamp);  //Puts the timestamp into the packet using a ByteBuffer
			
			int bytesSent = rfLayer.transmit(packetBB.array()); //Sends the packet, and collects the value of how many bytes were sent
			
			if(bytesSent == 10){ //Checks that the whole packet was sent
				
				System.out.print("Sent packet: " + mac +" "+ timestamp); //Prints out the sent packet
				System.out.print("      [ ");
				for(byte b : buffer){
					System.out.print(" " +(b & 0xFF));
					
				}
				System.out.println(" ]");
				
			}
			else{ //Whole packet was not sent, throw an error
				System.err.println("Sent the wrong sized packet, expected 10 bytes, sent: " + bytesSent + " bytes");
			}
			
			Random rand = new Random(); //For use in the random sleep time
			
			try { //Eclipse told me to use a try-catch block
				Thread.sleep(rand.nextInt(7000)); //Sleeps for a (random) number of milliseconds from 0-7000 (7 seconds)
			} catch (InterruptedException e) {    // From http://docs.oracle.com/javase/1.4.2/docs/api/java/lang/Thread.html#sleep(long)
				e.printStackTrace();
			}

		}
		
	}
			

}
