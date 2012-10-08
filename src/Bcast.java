

import java.util.Random;
import rf.RF;


public class Bcast {

	public static final int LENGTH_IN_BYTES = 10;
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Random gen = new Random(); //Creates a Random for making up MAC addresses
		int mac;
		
		
		if(args.length > 0){  //Check if a commandline argument was given, if so, use this as the MAC address
			int temp = Integer.parseInt(args[0]);
			if(temp >= 0 && temp < 65536){ //Checks if the given MAC address is a positive 16bit integer
				

				mac = temp; 
				System.out.println("Using MAC: "+ mac);
				
			}
			else{ //Given MAC is not in the valid range
				
				
				mac = gen.nextInt(65536);
				System.out.println("Assigned MAC is not acceptable, using a random MAC: "+ mac);
			}
			
			
		}
		else{ //No command line argument given, use a random MAC address
			

			mac = gen.nextInt(65536);
			System.out.println("No MAC specified, using a random MAC: "+ mac);
			
		}
		
		//short shortMAC = (short) mac;
		
		
		RF rfLayer = new RF(null,null); //Creates an RF layer for use in the sender and listener
		Listener listen = new Listener(rfLayer); //Creates a new listener
		Sender send = new Sender(rfLayer, mac); //Creates a new sender
		
		new Thread(listen).start(); //Runs the listener in a thread
		new Thread(send).start(); // RUns the sender in another thread so both will work independantly

	}

}
