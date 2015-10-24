import java.net.*;
import java.io.*;
import java.util.*;
public class Client {
 
     public static void main (String [] args ) throws IOException {
         try { 
				Scanner cin = new Scanner(System.in);
				System.out.println("Enter Server ip address: ");
				String sipadd = cin.nextLine();
				Socket connect = new Socket(sipadd, 10004);
				System.out.print("Enter full image name: ");
				String imagename = cin.nextLine();
				File transferFile = new File (imagename);
				byte [] bytearray  = new byte [(int)transferFile.length()];
				FileInputStream fin = new FileInputStream(transferFile);            
				BufferedInputStream bin = new BufferedInputStream(fin);
				bin.read(bytearray,0,bytearray.length);
				OutputStream os = connect.getOutputStream();
				System.out.println("Sending file to the server..");
				os.write(bytearray,0,bytearray.length);		// os sends bytearray
				os.flush();
				connect.close();
				/////////////////////////////////////////////////////////////////
				ServerSocket hope = new ServerSocket(6666);
				Socket connect2 = hope.accept();
				System.out.println("Receiving file from the server..");
				int filesize2=1022386;
				int bytesRead2;
				int currentTot2 = 0;
				// System.out.println("receiving file from the server..");
				//..Socket connect2 = new Socket("127.0.0.1",6666);
				byte [] bytearray2  = new byte [filesize2];
				InputStream is2 = connect2.getInputStream();
				FileOutputStream fos2 = new FileOutputStream("received.pdf");
				BufferedOutputStream bos2 = new BufferedOutputStream(fos2);
				bytesRead2 = is2.read(bytearray2,0,bytearray2.length);
				currentTot2 = bytesRead2;
		
				do {
					bytesRead2 = is2.read(bytearray2, currentTot2, (bytearray2.length-currentTot2));
		            if(bytesRead2 >= 0) 
		            	currentTot2 += bytesRead2;
		        } while(bytesRead2 > -1);
 
			    bos2.write(bytearray2, 0 , currentTot2);
			    bos2.flush();
			    bos2.close();
			    connect2.close();
				connect2.close();
				System.out.println("File transfer complete");
			}
			catch (IOException e) {
	           System.out.println(e);
	        }
		}
}

