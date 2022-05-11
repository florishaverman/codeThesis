import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Random;

/**
 * This class contains some useful functions, like generating random numbers or to calculate a factorial 
 * This class does not 
 * @author Floris Haverman 
 *
 *	(Not an interesting class)
 */
public class F {
	
	/**
	 * This function calculates the factorial for a given value
	 * @param number the factorial you want to calculate
	 * @return The factorial of the given number
	 */
	public static long factorial(int number) {
        long result = 1;

        for (int factor = 2; factor <= number; factor++) {
            result *= factor;
        }

        return result;
    }
	
	/**
	 * Returns the factorial of a number in BigInteger, thus allowing for bigger factorials
	 * @param N integer
	 * @return The factorial of the given number 
	 */
    public static BigInteger factorial2(int N)
    {
        // Initialize result
        BigInteger f = new BigInteger("1"); // Or BigInteger.ONE
 
        // Multiply f with 2, 3, ...N
        for (int i = 2; i <= N; i++)
            f = f.multiply(BigInteger.valueOf(i));
 
        return f;
    }
	
	/**
	 * A method to round a number to a certain amount of decimals
	 * @param d The number you want to round
	 * @param p The amount of decimal places
	 * @return The rounded number
	 */
	public static double round(double d , int p) {
		return Math.round(d* Math.pow(10, p)) / Math.pow(10, p);
	}

	
	/**
	 * This method calculates the pdf of the Poisson distribution.
	 * The method used BigDecimal to allow for larger rates.
	 * @param i 
	 * @param lambda The rate of the distribution
	 * @return the probability of getting i from a Poisson distribution with rate lambda
	 */
	public static double pdfPoission(int i, double lambda) {
		BigDecimal one = new BigDecimal(Math.exp(- lambda) * Math.pow(lambda, i));
		BigDecimal two = one.divide(new BigDecimal(F.factorial2(i)), new MathContext(10));
		return two.doubleValue();
	}
	
	
	/**
	 * Return a Poisson distributed random variable with a given rate.
	 * @param r The Random object which is used to create randomness
	 * @param mean The mean of the distribution
	 * @return A random draw from the distribution
	 */
	public static int getPoissonRandom(Random r,double mean) {
	    double L = Math.exp(-mean);
	    int k = 0;
	    double p = 1.0;
	    do {
	        p = p * r.nextDouble();
	        k++;
	    } while (p > L);
	    return k - 1;
	}
	
	/**
	 * Returns a exponentially distributed random variable
	 * @param r The Random object which is used to create randomness
	 * @param rate The rate of the distribution 
	 * @return A random draw from the distribution
	 */
	public static double getExponetialRandom(Random r,double rate) {
	    return - Math.log(r.nextDouble()) / rate;
	}
}
