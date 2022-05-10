import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Random;

public class F {
	
	public static long factorial(int number) {
        long result = 1;

        for (int factor = 2; factor <= number; factor++) {
            result *= factor;
        }

        return result;
    }
	
	 // Returns Factorial of N
    public static BigInteger factorial2(int N)
    {
        // Initialize result
        BigInteger f = new BigInteger("1"); // Or BigInteger.ONE
 
        // Multiply f with 2, 3, ...N
        for (int i = 2; i <= N; i++)
            f = f.multiply(BigInteger.valueOf(i));
 
        return f;
    }
	
	
	public static double round(double d , int p) {
		return Math.round(d* Math.pow(10, p)) / Math.pow(10, p);
	}

	public static double pdfPoissionComplex(int i, double lambda) {
		BigDecimal nbr = new BigDecimal(i);
		BigDecimal rate = new BigDecimal(lambda);
		BigDecimal e = new BigDecimal(Math.E);
		BigDecimal one = e.pow((int) lambda); //Cast to int could give problems is lambda is not in integer.
		BigDecimal two = rate.pow(i);
		
//		 return Math.exp(- lambda) * Math.pow(lambda, i) / F.factorial(i);
		BigDecimal result = two.divide(new BigDecimal(F.factorial(i)).multiply(one)) ;
		return result.doubleValue();
				 
	}
	
	public static double pdfPoission(int i, double lambda) {
//		System.out.println(Math.exp(- lambda));
//		System.out.println(Math.pow(lambda, i));
//		System.out.println(F.factorial2(i));
		
		BigDecimal one = new BigDecimal(Math.exp(- lambda) * Math.pow(lambda, i));
		BigDecimal two = one.divide(new BigDecimal(F.factorial2(i)), new MathContext(10));
//		System.out.println(two.doubleValue());
		
//		System.out.println(new BigDecimal(Math.exp(- lambda) * Math.pow(lambda, i)));
		 return two.doubleValue();
	}
	
	
	
	public static double cdfPoisson(int k, double lambda) {
		BigDecimal sumPart = new BigDecimal(0);
		
		MathContext mc = new MathContext(10);
		for (int i = 0; i < k; i++) {
			System.out.println("For i "+ i + " the value is " + new BigDecimal(lambda).pow(i).divide(new BigDecimal(F.factorial2(i)), mc ));
			sumPart.add(new BigDecimal(lambda).pow(i).divide(new BigDecimal(F.factorial2(i)), mc ));
//			sumPart += Math.pow(lambda, i) / F.factorial(i);
		}
		
//		return sumPart * Math.exp(-lambda);
		System.out.println(sumPart.divide(new BigDecimal(Math.E).pow((int) lambda), mc));
		return sumPart.divide(new BigDecimal(Math.E).pow((int) lambda), mc).doubleValue();

	}

	
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
	
	public static double getExponetialRandom(Random r,double rate) {
	    return - Math.log(r.nextDouble()) / rate;
	}
}
