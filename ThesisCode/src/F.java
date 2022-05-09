import java.util.Random;

public class F {
	
	public static long factorial(int number) {
        long result = 1;

        for (int factor = 2; factor <= number; factor++) {
            result *= factor;
        }

        return result;
    }
	
	
	public static double round(double d , int p) {
		return Math.round(d* Math.pow(10, p)) / Math.pow(10, p);
	}

	public static double pdfPoission(int i, double lambda) {
		 return Math.exp(- lambda) * Math.pow(lambda, i) / F.factorial(i);
				 
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
}
