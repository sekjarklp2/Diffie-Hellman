package algoritmaDH;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;

public class DH {

    public static BigInteger getRandomPrime(int nDigit) {
        return BigInteger.probablePrime(nDigit, new SecureRandom());
    }
    
    // Pre-condition: p is prime and 1 < a < p.
    // Post-condition: returns true iff a is a primitive root of p.
    public static boolean primitiveRoot(int a, BigInteger prime)
    {
        int p = prime.intValue();
        
        int val = a, i;
        
        // Calculate each modular exponent a^2, a^3... mod p.
        for(i=2; i<p; i++) {
            val = val*a%p;
            if(val == 1)
                break;
        }
        
        // If the exponent is p-1, it's a primitive root
        return (i == p-1);
    }
    
    public static BigInteger getPrimitiveRoot(BigInteger prime)
    {
        int p = prime.intValue();
        ArrayList<BigInteger> array = new ArrayList<BigInteger>();        
        
        for(int a=1; a<p; a++)
        {
            if(primitiveRoot(a,prime))
            {
                BigInteger bigA = BigInteger.valueOf(a);
                array.add(bigA);
            }
        }
        
        Collections.shuffle(array);
        return (array.get(0));
        
    }
    
    public static BigInteger getPrivateKey(BigInteger prime)
    {
        return prime.divide(BigInteger.valueOf((int)(Math.random()*100)));
    }
    
    public static BigInteger getPublicKet(BigInteger a, BigInteger Xa, BigInteger prime)
    {
        return a.modPow(Xa, prime);
    }
    
    public static BigInteger getSessionKey(BigInteger Xa, BigInteger Xb, BigInteger a, BigInteger prime)
    {
        return a.modPow((Xa.multiply(Xb)), prime);
    }
    
}
