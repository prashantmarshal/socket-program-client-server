import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.Vector;
public class Server {
	List <String> clientlist = new ArrayList <String>();
	static Vector clientSocket;
	static Vector id;
	public Server() throws Exception {
		ServerSocket servsock1 = new ServerSocket(10004);
		while(true) {
        	try {
        		Socket clientSock = servsock1.accept();
        		String name = clientSock.getInetAddress().getHostAddress();  // name has ip address
			if (clientlist.indexOf(name) == -1)
				clientlist.add(name);
			int cnumber = clientlist.indexOf(name);
        		System.out.println("Client connected: " + name + "\n" + "Client ID.: " + String.valueOf(cnumber + 1));
        		Conversion object = new Conversion(clientSock, servsock1, name);
        	} catch (Exception e) {
 	           System.out.println(e);
 	        }
        }
	}

    public static void main (String [] args ) throws Exception {
    	Server obj = new Server();
    }
       

class Conversion extends Thread {
	/*InputStream is;
	BufferedOutputStream bos;
	FileOutputStream fos;*/
	String name2;
	Socket socket;
	ServerSocket servsock; 
        Conversion(Socket clientSock, ServerSocket servsock1, String name) throws Exception {
	
			name2 = name;
	       	socket = clientSock;
        	servsock = servsock1;
        	start();
        }

        public void run() {	
        	try {
      //  System.out.println("I guess the work is done which is server has placed the file somewhere");
		int filesize=1022386;
        int bytesRead;
        int currentTot = 0;
        byte [] bytearray  = new byte [filesize];
        InputStream is = socket.getInputStream();
        FileOutputStream fos = new FileOutputStream("temp.jpg");
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        bytesRead = is.read(bytearray,0,bytearray.length);
        currentTot = bytesRead;
        do {
           bytesRead =
              is.read(bytearray, currentTot, (bytearray.length-currentTot));
           if(bytesRead >= 0) currentTot += bytesRead;
        } while(bytesRead > -1);
        bos.write(bytearray, 0 , currentTot);
        bos.flush();
        bos.close();
        	} catch (IOException e) {
  	           System.out.println(e);
  	        }
        // now converter pick up the file

        // System.out.println("Converting client's file..");
        Document document = new Document();
        String input = "temp.jpg";
        String output = "temp.pdf";
        try {
          FileOutputStream fos2 = new FileOutputStream(output);
          PdfWriter writer = PdfWriter.getInstance(document, fos2);
          writer.open();
          document.open();
          Image img = Image.getInstance(input);
          img.scalePercent(50);
          document.add(img);
          document.close();
          writer.close();
        }
        catch (Exception e) {
          e.printStackTrace();
        }
        // System.out.println("File has been converted..");
        // until here is the converter code
        // the reply from server starts
	    try {
        	//..Socket socket2 = servsock.accept();
        	// System.out.println("Server sending back the converted file..");
        	Socket socket2 = new Socket(name2, 6666);
        	File myFile = new File("temp.pdf");
        	byte[] mybytearray = new byte[(int) myFile.length()];
        	BufferedInputStream bis = new BufferedInputStream(new FileInputStream(myFile));
        	bis.read(mybytearray, 0, mybytearray.length);
        	OutputStream os = socket2.getOutputStream();
        	os.write(mybytearray, 0, mybytearray.length);
        	os.flush();        	
        	bis.close();
        	socket2.close();
        	System.out.println("Connection with the client is closed");
        //	servsock.close();
        }  catch (IOException e) {
	           System.out.println(e);
	        }
        //servsock.close();
      }
}
}

