
package algoritmaDH;

import java.net.*;
import java.io.*;
import java.math.BigInteger;
import java.util.*;

public class DHServer {

    //inisialisasi arraylist
    private ArrayList<DHServerThread> threadClient;
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
            //menginisalisasi port socket
            serverSocket = new ServerSocket(4444);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 4444.");
            System.exit(-1);
        }

        while (true)
        {
            try {
                //membuat object thread client
                DHServerThread client;
                client = new DHServerThread(serverSocket.accept(), this);
                client.setDaemon(true);
                //threadClient.add(client);//mmenyambungkan semua thread client yang ada dalam threadclient
                threadClient.add(client);
                //menjalankan thread
                threadClient.get(threadClient.size()-1).start();
                //menampilkan jumlah client
                System.out.println("Jumlah Client: "+threadClient.size());
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
        //membuat objek baru KKMMultiserver,menjalangkan fungsi StartServer
        new DHServer().StartServer();
             
	}
  
    //karena fungsi ini dipanggil berkali2 maka saya buat fungsi sendiri
    void broadcast( String msg) {
        //untuk semua thread pada threadClient
        for (DHServerThread t : threadClient) {
            //pesan akan dikirim ke semua thread kecuali thread pengirim
            t.out.println(msg);
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
        // untuk membedakan klien satu dan yang lain
        private String name;
        private String alias;
        private PrintWriter out;

        public DHServerThread(Socket socket, DHServer server) {
            //membuat objek thread baru
            super("KKMultiServerThread");
            this.socket = socket;
            this.server = server;
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

                //pesan ini akan dibroadcast pada seluruh thread kecuali thread pengirim
                server.broadcast("Server : " + name + " entered the room");
                server.broadcast("Server: User online: "+threadClient.size());
                this.out.println("Server Who's online?");
                if(threadClient.size()!=1) {
                    //untuk semua thread pada threadClient
                    for (DHServerThread t : threadClient) {
                        if(t!=this) {
                            //pesan akan dikirim ke semua thread kecuali thread pengirim
                            this.out.println(t.name);    
                        }
                   }
                }
                else{this.out.println("Server: You are alone");}
                out.println("GP "+getP()+" "+getA());

                //proses chat
                String line;
                //selama input tidak Bye akan terus 
                while ((line = in.readLine()) != null && !line.equals("Bye")) {
                    if(line.startsWith("@")){
                        String[] words = line.split("\\s", 2);

                        if (words.length > 1 && words[1] != null) {
                            words[1] = words[1].trim();
                            if (!words[1].isEmpty()) {
                                for (DHServerThread t : threadClient) {
                                    if(t.alias.equals(words[0])) {
                                        String msg = "Shared "+name + " " + words[1];
                                        //System.out.println(msg);
                                        t.out.println(msg);
                                        this.out.println(words[0] + " received your public key");
                                    }
                                }
                            }
                        }
                    }
                    else {
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
