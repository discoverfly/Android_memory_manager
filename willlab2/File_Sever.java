
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class File_Sever {
	
	final private static int port = 7000;
	final private static String line = System.getProperty("line.separator");

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
			
			File f = new File(str);
			OutputStream os = s.getOutputStream();
			if (f.exists() && f.isFile()) {
				os.write("200".getBytes()); //success
				os.write(line.getBytes()); //line
				os.write(f.getName().getBytes()); //file name
				os.write(line.getBytes()); //line
				os.write(String.valueOf(f.length()).getBytes()); //file length
				os.write(line.getBytes()); //line
				
				FileInputStream fis = new FileInputStream(f);
				int data;
				while((data = fis.read()) != -1) {
					os.write(data);
				}
				System.out.println("File "+str+" is transported.");
			}
			else {
				os.write("404".getBytes()); //not find file
				os.write(line.getBytes()); //line
				System.out.println("File "+str+" is not exist.");
			}
			
			is.close();
			os.close();
			s.close();
			ss.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
