package algoritmaDH;

import java.math.BigInteger;
import java.security.SecureRandom;

public class DH {

    public static BigInteger getRandomPrime(int nDigit) {
        return BigInteger.probablePrime(nDigit, new SecureRandom());
    }
    
    public static BigInteger getPrimitiveRoot(int prime)
    {
        return null;
    }
    
}
