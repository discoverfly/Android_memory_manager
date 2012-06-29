


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class lab2 {
	final private static int port = 7000;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Socket s = new Socket(InetAddress.getByName(null), port);
			System.out.print("Please Input the String : ");

			InputStreamReader isr = new InputStreamReader(System.in);
			BufferedReader br = new BufferedReader(isr);
			String str = br.readLine(); 
			
			OutputStream os = s.getOutputStream();
			os.write(str.getBytes());
			InputStream is = s.getInputStream();
			byte[] buf = new byte[100];
			int len = is.read(buf);
			System.out.println("Input  : " + str);
			System.out.println("Server : " + new String(buf, 0, len));
			
			is.close();
			os.close();
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
