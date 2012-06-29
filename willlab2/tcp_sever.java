
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class tcp_sever {
	
	final private static int port = 7000;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ServerSocket ss = new ServerSocket(port);
			byte[] buf = new byte[100];
			System.out.println("Server is online, Waiting for Client require...");
			Socket s = ss.accept();
			InputStream is = s.getInputStream();
			int len = is.read(buf);
			String str = new String(buf, 0, len);
			OutputStream os = s.getOutputStream();
			System.out.println("From Client : " + str);
			os.write(str.toUpperCase().getBytes());
			System.out.println("To Client   : " + str.toUpperCase());
			is.close();
			os.close();
			s.close();
			ss.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
