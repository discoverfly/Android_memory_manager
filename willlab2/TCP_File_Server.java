
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TCP_File_Server extends Thread {
	final private static int port = 7000;
	final private static String line = System.getProperty("line.separator");
	private Socket s;
	private static int count = 1;
	
	public TCP_File_Server(Socket s)
	{
		this.s = s;
	}
	
	public void run() {
		try {
			System.out.println("Client "+count+" is connected.");
			InputStream is = s.getInputStream();
			
			byte[] buf = new byte[100];
			int len = is.read(buf);
			String str = new String(buf, 0, len);
			
			if (!str.equals("!q")) {
				File f = new File(str);
				
				OutputStream os = s.getOutputStream();
				if (f.exists()) {
					//file
					if (f.isFile()) {
						os.write("200".getBytes()); //file success
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
						System.out.println("Client "+ count +" got File "+str+".");
					}
					//directory
					else if (f.isDirectory()) {
						File[] files = f.listFiles();
						os.write("201".getBytes()); //directory success
						os.write(line.getBytes()); //line
						for (int i=0; i<files.length; i++) {
							File file = files[i];
							if (file.isDirectory()) {
								os.write("<".getBytes());
								os.write(file.getName().getBytes());
								os.write(">".getBytes());
								os.write(line.getBytes()); //line
							}
							else {
								//os.write("F:".getBytes());
								os.write(file.getName().getBytes());
								os.write(" -- ".getBytes());
								os.write(String.valueOf(file.length()).getBytes());
								os.write(" bytes".getBytes());
								os.write(line.getBytes()); //line
							}
						}
						System.out.println("Client "+ count +" got Directory <"+str+"> info.");
					}
				}
				else {
					os.write("404".getBytes()); //not find file
					os.write(line.getBytes()); //line
					System.out.println("File "+str+" is not exist.");
				}
				os.close();
			}
			is.close();
			s.close();
			
			System.out.println("Client "+ count++ +" is disconnected.\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ServerSocket ss = new ServerSocket(port);
			System.out.println("Server is online, Waiting for Client require...");
			while (true) {
				Socket s = ss.accept();
		        new TCP_File_Server(s).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
