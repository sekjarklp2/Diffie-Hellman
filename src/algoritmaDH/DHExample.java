package algoritmaDH;

import java.math.BigInteger;

public class DHExample {
    public static void main(String[] args) {
        // tentukan bilangan prima yang digunakan dan primitive rootnya
        DH.getPrimeFactors();
        BigInteger p = DH.getRandomPrime(512);
        BigInteger a = DH.getPrimitiveRoot(p);
        System.out.println(p);
        // generetan Alice
        BigInteger Xa = DH.getSecretKey(p);
        BigInteger Ya = DH.getPublicKey(a, Xa, p);
        // Alice lalu ngirim public key ke Bob
        
        // generetan Bob
        BigInteger Xb = DH.getSecretKey(p);
        BigInteger Yb = DH.getPublicKey(a, Xb, p);
        // Bob menghitung session key dengan public key Alice
        BigInteger Kb = DH.getSessionKey(Xb, Ya, p);
        // Bob lalu ngirim public key ke Alice
        
        // Alice menghitung session key
        BigInteger Ka = DH.getSessionKey(Xa, Yb, p);
        
        // cetak apakah hasilnya sama
        System.out.println("Ka: "+Ka+" Kb: "+Kb);
    }
}