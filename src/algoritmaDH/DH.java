package algoritmaDH;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DH {
    /**
    * Menghitung sebuah nDigit BigInteger prima 
    *
    * @param nDigit banyaknya digit dari bilangan yang akan dibuat
    * @return            nDigit BigInteger Prima
    */
    public static BigInteger getRandomPrime(int nDigit) {
        return BigInteger.probablePrime(nDigit, new SecureRandom());
    }
    
    private static boolean testPrimitiveRoot(BigInteger a, BigInteger p)
    {   
        BigInteger i, val;
        Set<BigInteger> hasil = new HashSet();
        for(i=BigInteger.ONE; i.compareTo(p) < 0; i=i.add(BigInteger.ONE)) {
            val = a.modPow(i, p);
            if (hasil.contains(val)) return false;
            else hasil.add(val);
        }
        return true;
    }
    /**
    * Mendapatkan sebuah primitive root dari prime
    *
    * @param p bilangan prima yang ingin dicari salah satu primitive rootnya
    * @return            salah satu primitive root dari p
    */
    public static BigInteger getPrimitiveRoot(BigInteger p)
    {
        ArrayList<BigInteger> array = new ArrayList<>();   
        for(BigInteger a=BigInteger.ONE;a.compareTo(p) < 0;a=a.add(BigInteger.ONE))
        {
            if(testPrimitiveRoot(a,p))
                array.add(a);
            if (array.size() == 8) break;
        }
        
        int idx = (int)(Math.random()*array.size());
        return array.get(idx);
    }
    /**
    * Menghitung secret key user tersebut
    *
    * @param p bilangan prima untuk menjadi patokan menghitung secret key
    * @return            BigInteger secret key
    */
    public static BigInteger getSecretKey(BigInteger p)
    {
        return p.divide(BigInteger.valueOf((int)(Math.random()*100)));
    }
    /**
    * Menghitung nilai dari public key yang akan dikirimkan ke lawan komunikasi
    *
    * @param a BigInteger a primitive root dari p
    * @param secretKey BigInteger secret key user tsb
    * @param p BigInteger p
    * @return            BigInteger hasil dari a^secretKey mod p
    */
    public static BigInteger getPublicKey(BigInteger a, BigInteger secretKey, BigInteger p)
    {
        return a.modPow(secretKey, p);
    }
    /**
    * Menghitung nilai dari session key yang digunakan
    *
    * @param secretKey BigInteger private key user tsb
    * @param publicKey BigInteger public key yang diterima dari lawan komunikasi
    * @param p BigInteger p yang di generate user
    * @return            BigInteger hasil dari publicKey^privateKey mod p
    */
    public static BigInteger getSessionKey(BigInteger secretKey, BigInteger publicKey, BigInteger p)
    {
        return publicKey.modPow(secretKey, p);
    }   
}
