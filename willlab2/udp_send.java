
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class udp_send {
	final private static int port = 8000;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			DatagramSocket ds = new DatagramSocket();
			System.out.print("Please Input the String : ");

			InputStreamReader isr = new InputStreamReader(System.in);
			BufferedReader br = new BufferedReader(isr);
			String str = br.readLine(); 
			
			DatagramPacket dp_send = new DatagramPacket(str.getBytes(), str.length(), 
					InetAddress.getByName(null), port);
			ds.send(dp_send);
			
			byte[] buf = new byte[100];
			DatagramPacket dp_recv = new DatagramPacket(buf, 100);
			ds.receive(dp_recv);
			
			System.out.println("Input    : " + str);
			System.out.println("Receiver : " + new String(buf, 0, dp_recv.getLength()));
			
			ds.close();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
