import java.math.BigDecimal;
import java.math.MathContext;

public class IntegralsExtension {
	
	
	
	
	/**
	 * Calculates a integral using the Simpson's method.
	 * @param L The system parameter for supply lead time
	 * @param T The system parameter for demand lead time
	 * @param rate1 The rate of class 1 customers
	 * @param rate2 The rate of class 2 customers
	 * @param S The order up to level
	 * @param Sc The critical level
	 * @param a The start of the integral
	 * @param b The end of the integral
	 * @param n The number of parts the interval is divided in
	 * @param firstInt A boolean to indicate which of the two integrals should be calculated
	 * @param case1 A boolean to indicate if class 1 is the critical class (true) of not (false)
	 * @return The value for the required integral
	 */
	public static double IntSimpson(double L, double T, double rate1, double rate2, int S, int Sc,double a, double b,int n, boolean firstInt, boolean case1, double p){                       
	       int i,z;                                                       
	       double h,s;                                                    

	       n=n+n;
	       if (firstInt) {
	    	   s= firstIntergralFunction(L, T, rate1, rate2, S, Sc, a, case1) *
		    		   firstIntergralFunction(L, T, rate1, rate2, S, Sc, b, case1);
	       }else {
	    	   s= secondIntergralFunction(L, T, rate1, rate2, S, Sc, a, case1,p) *
		    		   secondIntergralFunction(L, T, rate1, rate2, S, Sc, b, case1,p);
	       }
	       
	       h = (b-a)/n;                                        
	       z = 4;

	       for(i = 1; i<n; i++){
	    	   if (firstInt) {
	    		   double temp = firstIntergralFunction(L, T, rate1, rate2, S, Sc, a+i*h, case1);
	 	          s = s + z * temp;

		       }else {
			      s = s + z * secondIntergralFunction(L, T, rate1, rate2, S, Sc, a+i*h, case1, p);

		      }
	          z = 6 - z;
	       }
	       return (s * h)/3;
	 }
	
	/**
	 * Multiplies the first and the second part of the integral
	 * @param L The system parameter for supply lead time
	 * @param T The system parameter for demand lead time
	 * @param rate1 The rate of class 1 customers
	 * @param rate2 The rate of class 2 customers
	 * @param S The order up to level
	 * @param Sc The critical level
	 * @param x The values for which the function should be calculated
	 * @param case1 A boolean to indicate if class 1 is the critical class (true) of not (false)
	 * @return the value of the function
	 */
	public static double firstIntergralFunction(double L, double T, double rate1, double rate2, int S, int Sc, double x, boolean case1) {
		double f1 = new BigDecimal(Math.pow((rate1 + rate2), S - Sc) * Math.exp(-x *(rate1 + rate2)) * Math.pow(x, S - Sc - 1)).divide(new BigDecimal(F.factorial2(S - Sc - 1)), new MathContext(10)).doubleValue();
		
		double secondPart = 0;
		if (case1) {
			secondPart = secondPart(L,rate1, Sc, x);
		}else {
			secondPart = secondPart(L,rate2, Sc, x);
		}
		
//		System.out.println("This is the first part "+ firstPart + " this is the second part " + secondPart);
		return f1  * secondPart;
	}
	
	/**
	 * Multiplies the first and the second part of the integral
	 * @param L The system parameter for supply lead time
	 * @param T The system parameter for demand lead time
	 * @param rate1 The rate of class 1 customers
	 * @param rate2 The rate of class 2 customers
	 * @param S The order up to level
	 * @param Sc The critical level
	 * @param x The values for which the function should be calculated
	 * @param case1 A boolean to indicate if class 1 is the critical class (true) of not (false)
	 * @return the value of the function
	 */
	public static double secondIntergralFunction(double L, double T, double rate1, double rate2, int S, int Sc, double x, boolean case1, double p) {
		double rate = (rate1 + (1-p) * rate2) * x + p * rate2 * (L -T);
		//double firstPart = functionf2(L, T, rate1, rate2, S, Sc, x) ;
		BigDecimal one = new BigDecimal( (rate1 + (1-p)* rate2) * Math.exp(- rate) * Math.pow(rate, S - Sc - 1));
		double firstPart =   one.divide(new BigDecimal(F.factorial2(S - Sc - 1)), new MathContext(10)).doubleValue();
		
		double secondPart = 0;
		if (case1) {
			secondPart = secondPart(L,rate1, Sc, x);
		}else {
			secondPart = secondPart(L,rate2, Sc, x);
		}		
		return firstPart  * secondPart;
	}
	
	
	
	/**
	 * This method calculates the sum part which needs to be calculated in both integrals
	 * @param L The system parameter for supply lead time
	 * @param T The system parameter for demand lead time
	 * @param rate1 The rate of class 1 customers
	 * @param rate2 The rate of class 2 customers
	 * @param S The order up to level
	 * @param Sc The critical level
	 * @param x The values for which the function should be calculated
	 * @param case1 A boolean to indicate if class 1 is the critical class (true) of not (false)
	 * @return the value of the function
	 */
	public static double secondPart(double L, double rate, int Sc, double x) {
		double secondPart = 0;
		//Use the rate of the critical class  
		for (int i = 0; i < Sc; i++) {
			secondPart += F.pdfPoission(i, rate * (L - x));
		}
		return  secondPart;
	}

}
