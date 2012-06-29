
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class udp_receive {
	final private static int port = 8000;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			DatagramSocket ds = new DatagramSocket(port);
			System.out.println("Waiting for DatagramPacket...");
			
			byte[] buf = new byte[100];
			DatagramPacket dp = new DatagramPacket(buf, 100);
			ds.receive(dp);
			
			String str = new String(buf, 0, dp.getLength());
			System.out.println("From Sender : " + str);
			
			DatagramPacket dp_send = new DatagramPacket(str.toUpperCase().getBytes(),
					str.length(), dp.getAddress(), dp.getPort());
			System.out.println("To Sender   : " + str.toUpperCase());
			ds.send(dp_send);
			
			ds.close();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
