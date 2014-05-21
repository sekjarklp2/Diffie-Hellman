/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dh.cs;

/**
 *
 * @author hades
 */

import java.io.*;
import java.math.BigInteger;
import java.net.*;

public class DHClient {
    
    // deklarasi socket
    private static Socket socket;
    
    // deklarasi I/O stream
    private static BufferedReader in;
    private static PrintWriter out;
    
    // deklarasi boolean untuk status, diletakkan dalam scope private static agar dapat diakses keseluruhan class
    private static boolean active;
    // deklarasi Global parameter
    private static BigInteger p;
    private static BigInteger a;
    private static BigInteger Xa;
    private static BigInteger Ya;
    private static BigInteger Yb;
    private static BigInteger Ka;
    public static void main(String[] args) throws IOException{
        
        // inisialisasi socket dan I/O
        socket = null;
        out = null;
        in = null;

        try{
            // mengkoneksikan client dengan socket yang sudah ada di port 4444 untuk host: localhost
            socket = new Socket("localhost",4444);

            // menghubungkan I/O stream dengan socket
            out = new PrintWriter(socket.getOutputStream(),true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: localhost.");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for " + "the connection to: localhost.");
            System.exit(1);
        }
        
        // deklarasi input stream untuk membaca input dari console
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String fromUser;
        
         // inisialisasi awal adalah true dimana setiap client terhubung berarti client aktif
        active = true;
        
        // class threads diinstansiasi kemudian dijalankan agar setiap thread yg terhubung dpt merima pesan dari server
        new threads().start();
        
        // selama thread masih aktif akan meminta input dari console
        while(active){
            fromUser = stdIn.readLine();
            if (fromUser != null) {
                  if(fromUser.startsWith("@")) {
                    fromUser= fromUser+" "+DHClient.getYa();
                    out.println(fromUser);
                }
                    // inputan dari console nantinya akan ditampilkan dari PrintWriter stream pada thread
                  else{out.println(fromUser);}
            }
            
        }
 
        // socket dan I/O stream ditutup
        out.close();
        in.close();
        stdIn.close();
        socket.close();
    }

    /**
     * @return the p
     */
    public static BigInteger getP() {
        return p;
    }

    /**
     * @param aP the p to set
     */
    public static void setP(BigInteger aP) {
        p = aP;
    }

    /**
     * @return the a
     */
    public static BigInteger getA() {
        return a;
    }

    /**
     * @param aA the a to set
     */
    public static void setA(BigInteger aA) {
        a = aA;
    }

    /**
     * @return the Xa
     */
    public static BigInteger getXa() {
        return Xa;
    }

    /**
     * @param aXa the Xa to set
     */
    public static void setXa(BigInteger aXa) {
        Xa = aXa;
    }

    /**
     * @return the Ya
     */
    public static BigInteger getYa() {
        return Ya;
    }

    /**
     * @param aYa the Ya to set
     */
    public static void setYa(BigInteger aYa) {
        Ya = aYa;
    }

    /**
     * @return the Yb
     */
    public static BigInteger getYb() {
        return Yb;
    }

    /**
     * @param aYb the Yb to set
     */
    public static void setYb(BigInteger aYb) {
        Yb = aYb;
    }

    /**
     * @return the Ka
     */
    public static BigInteger getKa() {
        return Ka;
    }

    /**
     * @param aKa the Ka to set
     */
    public static void setKa(BigInteger aKa) {
        Ka = aKa;
    }
    
    private static class threads extends Thread {
        
        // tidak membutuhkan constructor, hanya berjalan menjalan fungsi run setiap diinstansiasi di fungsi utama
        public void run(){
            try{
                String fromServer;
 
                // membaca input dari server
                while ((fromServer = in.readLine()) != null) {
                    if (fromServer.startsWith("GP")){
                        String[] words = fromServer.split("\\s", 3);
                                        if (words.length > 1 && words[1] != null) {
                                        words[1] = words[1].trim();
                                        words[2] = words[2].trim();
                                        DHClient.setP(new BigInteger(words[1]));
                                        DHClient.setA(new BigInteger(words[2]));
                                        
                                         if (!words[1].isEmpty()) {
                                             setXa(DH.getSecretKey(DHClient.getP()));
                                             setYa(DH.getPublicKey(DHClient.getA(),DHClient.getXa(),DHClient.getP()));
                                             System.out.println("Secret Key: "+Xa+" Public Key: "+Ya);
                                         }
                                        }
                    }
                    else if (fromServer.startsWith("Shared")){
                        String[] words = fromServer.split("\\s", 3);
                                        if (words.length > 1 && words[1] != null) {
                                        words[1] = words[1].trim();
                                        words[2] = words[2].trim();
                                        DHClient.setYb(new BigInteger(words[2]));
                                        
                                         if (!words[1].isEmpty()) {
                                             DHClient.setKa(DH.getSessionKey(DHClient.getXa(), DHClient.getYb(), DHClient.getP()));
                                           
                                             System.out.println("Session key: "+DHClient.getKa());
                                         }
                                        }
                    }
                    // cek apabila input dari server adalah "<Server> Exit." maka berhenti membaca dan keluar
                    else if (fromServer.equals("Server : you quit")){
                        break;
                    }
                    // pesan dari server akan ditampilkan pada console
                    else{System.out.println(fromServer);  } 
                }
                // ketika sudah berhenti membaca maka status thread diubah jadi false sehingga tidak dapat lagi membaca input dari user pada console
                active = false;
            } catch(IOException e){
                
            } 
            
        }
    }
}
