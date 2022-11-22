package reseau;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
/*
 * www.codeurjava.com
 */
public class client {

 public static void main(String[] args) throws IOException {
	 
	 Socket echoSocket = null ;
	 PrintWriter out = null ;
	 BufferedReader in= null;
 
	 try{
		 echoSocket = new Socket("localhost", 555) ;
		 out = new PrintWriter(echoSocket.getOutputStream(),true) ;
		  in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream())) ;
		 }
		 catch(UnknownHostException e){
		 System.out.println(" Destination localhost inconnue ") ;
		 System.exit(-1) ;
		 }
		 catch(IOException e){
		 System.out.println(" now to investigate this IO issue ") ;
		 System.exit(-1) ;
		 }
	 BufferedReader stdIn = new BufferedReader ( new InputStreamReader(System.in)) ;
	 
	 String userInput ;
	 
	 while ((userInput = stdIn.readLine())!= null) {
		 out.println(userInput);
		 System.out.println("echo:" + in.readLine()) ;
	 }
	 
	 out.close();
	 in.close();
	 stdIn.close();
	 echoSocket.close();
}
}