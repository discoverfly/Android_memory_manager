
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class File_Client {
	final private static int port = 7000;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Socket s = new Socket(InetAddress.getByName(null), port);
			System.out.print("Please Input File Name : ");

			InputStreamReader isr = new InputStreamReader(System.in);
			BufferedReader br = new BufferedReader(isr);
			String str = br.readLine();
			
			OutputStream os = s.getOutputStream();
			os.write(str.getBytes());
			
			
			InputStream is = s.getInputStream();
			BufferedReader br_server = new BufferedReader(new InputStreamReader(is));
			String state = br_server.readLine();
			if (state.equals("200")) {
				String file_name = br_server.readLine();
				System.out.println("State       : Succeed.");
				System.out.println("File Name   : "+file_name);
				System.out.println("File Length : "+br_server.readLine()+" bytes");
				int data;
				FileOutputStream fos = new FileOutputStream(file_name);
				while((data=is.read()) != -1) {
	            	fos.write(data);
	            }
			}
			else {
				System.out.println("State       : Error.");
				System.out.println("No Such File.");
			}
			
			is.close();
			os.close();
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
