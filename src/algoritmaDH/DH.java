package algoritmaDH;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DH {
    private static ArrayList<BigInteger> factors = new ArrayList<>();
    private static ArrayList<Integer> primeFactors;
    /**
    * Menghitung sebuah nDigit BigInteger prima 
    *
    * @param nBit banyaknya digit dari bilangan yang akan dibuat
    * @return            nDigit BigInteger Prima
    */
    public static BigInteger getRandomPrime(int nBit) {
        return BigInteger.probablePrime(nBit, new SecureRandom());
    }
    
    private static boolean testPrimitiveRoot(BigInteger a, BigInteger p)
    {   
        BigInteger val;
        for(int i=0;i<factors.size();++i) {
            val = a.modPow(factors.get(i), p);
            if (val.compareTo(BigInteger.ONE) == 0) return false;
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
        BigInteger a, q = p.subtract(BigInteger.ONE); // q= etf
        // mencari faktor-faktor dari p-1 aka etf
        factors.add(BigInteger.ONE);
        for(int i=0;i<primeFactors.size();++i) {
            BigInteger prime = BigInteger.valueOf(primeFactors.get(i));
            BigInteger temp = q, pembagi = prime;
            if (prime.multiply(prime).compareTo(temp) > 0) break; // jika p*p > q, berarti sudah tidak memenuhi
            while(temp.mod(prime).compareTo(BigInteger.ZERO) == 0) { // selama temp%p == 0
                temp = temp.divide(prime);
                factors.add(temp); // hasil bagi = faktornya
                factors.add(pembagi);
                pembagi = pembagi.multiply(pembagi);
            }
        }
        for(a=BigInteger.ONE;a.compareTo(p) < 0;a=a.add(BigInteger.ONE))
        {
            if(testPrimitiveRoot(a,p))
               break;
        }
        factors.clear();
        return a;
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
    /**
     * Mengisi primeFactors dengan bilangan prima <= 1000000
     */
    public static void getPrimeFactors() { // sieve of eratosthenes
        primeFactors = new ArrayList<>();
        boolean flag[] = new boolean[1000001];
        for(int i=2;i<=1000000;++i)
            if (flag[i] == false) {
                primeFactors.add(i);
                for(int j=i+i;j<=1000000;j+=i)
                    flag[j] = true;
            }
    }
}
