import java.math.BigDecimal;
import java.math.MathContext;

/**
 * This class contains methods to calculate the integral 
 * needed in the calculations of the approximate service level for the critical class 
 * @author Floris Haverman 
 *
 *	(Not an interesting class)
 */
class Function{ 
	
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
	public static double IntSimpson(double L, double T, double rate1, double rate2, int S, int Sc,double a, double b,int n, boolean firstInt, boolean case1){                       
	       int i,z;                                                       
	       double h,s;                                                    

	       n=n+n;
	       if (firstInt) {
	    	   s= firstIntergralFunction(L, T, rate1, rate2, S, Sc, a, case1) *
		    		   firstIntergralFunction(L, T, rate1, rate2, S, Sc, b, case1);
	       }else {
	    	   s= secondIntergralFunction(L, T, rate1, rate2, S, Sc, a, case1) *
		    		   secondIntergralFunction(L, T, rate1, rate2, S, Sc, b, case1);
	       }
	       
	       h = (b-a)/n;                                        
	       z = 4;

	       for(i = 1; i<n; i++){
	    	   if (firstInt) {
	    		   double temp = firstIntergralFunction(L, T, rate1, rate2, S, Sc, a+i*h, case1);
	 	          s = s + z * temp;

		       }else {
			      s = s + z * secondIntergralFunction(L, T, rate1, rate2, S, Sc, a+i*h, case1);

		      }
	          z = 6 - z;
	       }
	       return (s * h)/3;
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
	public static double secondPart(double L, double T, double rate1, double rate2, int S, int Sc, double x, boolean case1) {
		double secondPart = 0;
		//Use the rate of the critical class  
		for (int i = 0; i < Sc; i++) {
			if (case1) {
				secondPart += F.pdfPoission(i, rate1 * (L - x));

			}else {
				secondPart += F.pdfPoission(i, rate2 * (L - x));
			}
		}
		return  secondPart;
	}
	
	/**
	 * Calculates the value of f1(S, Sc, y) in the paper of Kocaga en Sen.
	 * @param L The system parameter for supply lead time
	 * @param T The system parameter for demand lead time
	 * @param rate1 The rate of class 1 customers
	 * @param rate2 The rate of class 2 customers
	 * @param S The order up to level
	 * @param Sc The critical level
	 * @param x The values for which the function should be calculated
	 * @return the value of the function
	 */
	public static double functionf1(double L, double T, double rate1, double rate2, int S, int Sc, double x) {
		BigDecimal one = new BigDecimal(Math.pow((rate1 + rate2), S - Sc) * Math.exp(- (rate1 + rate2)* x) * Math.pow(x, S - Sc - 1));
		
		return  one.divide(new BigDecimal(F.factorial2(S - Sc - 1)), new MathContext(10)).doubleValue();

//		return  Math.pow((rate1 + rate2), S - Sc) * Math.exp(- (rate1 + rate2)* x) * Math.pow(x, S - Sc - 1) / F.factorial(S - Sc - 1);
	}
	/**
	 * Calculates the value of f2(S, Sc, y) in the paper of Kocaga en Sen.
	 * @param L The system parameter for supply lead time
	 * @param T The system parameter for demand lead time
	 * @param rate1 The rate of class 1 customers
	 * @param rate2 The rate of class 2 customers
	 * @param S The order up to level
	 * @param Sc The critical level
	 * @param x The values for which the function should be calculated
	 * @return the value of the function
	 */
	public static double functionf2(double L, double T, double rate1, double rate2, int S, int Sc, double x) {
		BigDecimal one = new BigDecimal(rate1 * Math.exp(- (rate1 * x+ rate2 *  (L - T) )) * Math.pow(rate1 * x + rate2 * (L- T), S - Sc - 1));
		
		return  one.divide(new BigDecimal(F.factorial2(S - Sc - 1)), new MathContext(10)).doubleValue();
//		return  rate1 * Math.exp(- (rate1 * x+ rate2 *  (L - T) )) * Math.pow(rate1 * x + rate2 * (L- T), S - Sc - 1) / F.factorial(S - Sc - 1);
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
		double firstPart = functionf1(L, T, rate1, rate2, S, Sc, x) ;
		double secondPart = secondPart(L, T, rate1, rate2, S, Sc, x, case1);
		
//		System.out.println("This is the first part "+ firstPart + " this is the second part " + secondPart);
		return firstPart  * secondPart;
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
	public static double secondIntergralFunction(double L, double T, double rate1, double rate2, int S, int Sc, double x, boolean case1) {
		double firstPart = functionf2(L, T, rate1, rate2, S, Sc, x) ;
		double secondPart = secondPart(L, T, rate1, rate2, S, Sc, x, case1);
		
		return firstPart  * secondPart;
	}
	
	
	   
}  	