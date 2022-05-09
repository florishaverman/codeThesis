import java.util.Random;

public class ServiceLevel {
	public static void main(String[] args) {
		
		//Demand lead time
		double T = 0.1;
		
		// Supply lead time
		double L = 0.5;
		
		//Rate class 1 and class 2
		int rate1= 3;
		int rate2= 4;
		
		// Base stock level
		int S =7;
		
		// Critical level
		int Sc = 3;
		
		System.out.println("Hello world");
		
		

		
//		System.out.println("The service level is");
//		System.out.println(ServiceLevel.getServiceLevelNonCritical(L, T, rate1, rate2, S, Sc));
		
//		System.out.println("The service level  for the critical class is");

//		System.out.println(ServiceLevel.getAproxServiceLevelCritical(L, T, rate1, rate2, S, Sc));

		
		
		System.out.println();
		System.out.println("Start of the list");

		for (rate1 = 1; rate1 < 13; rate1++) {
			S = rate1 + 4;
			System.out.print("The service level for the non-critical and critical class with rate 1: " +rate1+ ", rate 2: " +rate2+ ", S: " +S + ", Sc: " +Sc + "  ");
			System.out.print(F.round(ServiceLevel.getServiceLevelNonCritical(L, T, rate1, rate2, S, Sc),4));
			System.out.print(" , ");			
			System.out.println(F.round( ServiceLevel.getAproxServiceLevelCritical(L, T, rate1, rate2, S, Sc), 4));

		}
		/*
		// Call class function                                           
		Function function;                                   
		function = new Function();
		
		// ENTER the desired values of a, b and n !!!
		double a = 0 ;                                           
	    double b = 1 ;
	    int n =1000000 ;
	    // Applies simpson method to function
	    double result = function.IntSimpson(a,b,n);

	    // Show results
	    System.out.println("Integral is: " + result);
	      */	       		   	      

		
	}
	
	public static double getServiceLevelNonCritical(double L, double T, int rate1, int rate2, int S, int Sc) {
		double serviceLevel = 0;
		
		for (int i = 0; i < S - Sc; i++) {
			//System.out.println(pdfPoission(i, (rate1 * L + rate2 * (L - T))));
		
			serviceLevel += F.pdfPoission(i, (rate1 * L + rate2 * (L - T)));
		}
		
		
		return serviceLevel;
	}
	
	
	public static double getAproxServiceLevelCritical(double L, double T, int rate1, int rate2, int S, int Sc) {
		//Parameters
		//n is the number of partitions
		int n = 10000;
		
		double serviceLevel = 0;
		
		for (int i = 0; i < S - Sc; i++) {
		
			serviceLevel += F.pdfPoission(i, (rate1 * L + rate2 * (L - T)));
		}
		
		return serviceLevel + Function.IntSimpson(L, T, rate1, rate2, S, Sc, 0, (L - T), n, true) 
					+ Function.IntSimpson(L, T, rate1, rate2, S, Sc, (L - T), L,  n, false);
	}
	
	
	
	
		
}
