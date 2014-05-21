/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dh.cs;


/**
 *
 * @author hades
 */
import java.net.*;
import java.io.*;
import java.math.BigInteger;
import java.util.*;

public class DHServer {
    private ArrayList<DHServerThread> threadClient;//inisialisasi arraylist
   private static BigInteger p;
   private static BigInteger a;
    public DHServer() {
		
		threadClient = new ArrayList<DHServerThread>();//membuat objek threadClient
	}

    public void StartServer()
    {
        //set Global parameter
          setP(DH.getRandomPrime(12));
          setA(DH.getPrimitiveRoot(getP()));
        ServerSocket serverSocket = null;
     
                
        try {
            serverSocket = new ServerSocket(4444);//menginsi port socket
        } catch (IOException e) {
            System.err.println("Could not listen on port: 4444.");
            System.exit(-1);
        }

        while (true)
        {
            try {
                DHServerThread client;//membuat object thread client
                client = new DHServerThread(serverSocket.accept(), this);
                client.setDaemon(true);
             
             
              //threadClient.add(client);//mmenyambungkan semua thread client yang ada dalam threadclient
              threadClient.add(client);
                
                threadClient.get(threadClient.size()-1).start();//menjalankan thread
                System.out.println("Jumlah Client: "+threadClient.size());//menampilkan jumlah client
                
            //threadClient.add(new DHServerThread(serverSocket.accept()));
            //threadClient.get(threadClient.size()-1).start();
	    
            //new DHServerThread(serverSocket.accept()).start();
            }
            catch (Exception e){
                   
        }
            //serverSocket.close();
        }
    }
    
    public static void main(String[] args) throws Exception {
		System.out.println("waiting for connections");
                
		new DHServer().StartServer();//membuat objek baru KKMMultiserver,menjalangkan fungsi StartServer
             
	}
        
  
  
   void broadcast( String msg) {//karena fungsi ini dipanggil berkali2 maka saya buat fungsi sendiri
		for (DHServerThread t : threadClient) {//untuk semua thread pada threadClient
			
				t.out.println(msg);//pesan akan dikirim ke semua thread kecuali thread pengirim
			
                      
		}
	}

    /**
     * @return the p
     */
    public BigInteger getP() {
        return p;
    }

    /**
     * @param p the p to set
     */
    public void setP(BigInteger p) {
        this.p = p;
    }

    /**
     * @return the a
     */
    public BigInteger getA() {
        return a;
    }

    /**
     * @param a the a to set
     */
    public void setA(BigInteger a) {
        this.a = a;
    }

class DHServerThread extends Thread {
    private Socket socket = null;
    private DHServer server;
    private String name;					// untuk membedakan klien satu dan yang lain
    private String alias;
    private PrintWriter out;
  
    
   
    public DHServerThread(Socket socket, DHServer server) {
	super("KKMultiServerThread");//membuat objek thread baru
	this.socket = socket;//inisialisasi socket
        this.server = server;//inisialisasi socket
    }
        
    public void run() {

	try {
           System.out.println("someone connected");
				//input output data
				out = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                                
				// identifikasi thread memasukkan nama client
				 out.println("enter your name.");
				name = in.readLine();
                                alias = "@" + name;
				System.out.println("it's "+name);
				out.println("Server: welcome "+name);
				server.broadcast("Server : " + name + " entered the room");//pesan ini akan dibroadcast pada seluruh thread kecuali thread pengirim

                                  
                                   server.broadcast("Server: User online: "+threadClient.size());
                                   this.out.println("Server Who's online?");
                                   if(threadClient.size()!=1){
                                   for (DHServerThread t : threadClient) {//untuk semua thread pada threadClient
						if(t!=this)
                                                {this.out.println(t.name);//pesan akan dikirim ke semua thread kecuali thread pengirim    
                                    
                                                }
                                   }
                                   }
                                   else{this.out.println("Server: You are alone");}
                                   out.println("GP "+getP()+" "+getA());
				//proses chat
				String line;
				while ((line = in.readLine()) != null && !line.equals("Bye")) {//selama input tidak Bye akan terus 
                                    if(line.startsWith("@")){
                                        String[] words = line.split("\\s", 2);
                                        if (words.length > 1 && words[1] != null) {
                                        words[1] = words[1].trim();
                                        
                                         if (!words[1].isEmpty()) {
                                 
                                        for (DHServerThread t : threadClient) {
                                        if(t.alias.equals(words[0])){
                                         String msg = "Shared "+name + " " + words[1];
					//System.out.println(msg);
                                        t.out.println(msg);
                                        this.out.println(words[0] + " received your public key");
                                        }
                                        }
                                       
                                     }
                                   }
                                }
                                    else{
                                    String msg = name + ":" + line;
					System.out.println(msg);
					server.broadcast( msg);
                                    }
				}
                                System.out.println(name + " leave the room");
                               server.broadcast( name + " leave the room");
                               
                               threadClient.remove(this);
                               
                               out.println("Server :you quit");
                               System.out.println("User online: "+threadClient.size());
                             server.broadcast("User online: "+threadClient.size());
		
	    

	    out.close();
	    in.close();
	    socket.close();

	} catch (IOException e) {
	    //e.printStackTrace();
            threadClient.remove(this);
            System.out.println("Someone disconected");
            server.broadcast("User online: "+threadClient.size());
            System.out.println("User online: "+threadClient.size());
        }
    }
    }
}
