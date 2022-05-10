class Function{ 
		
	public static double secondPart(double L, double T, int rate1, int rate2, int S, int Sc, double x, boolean case1) {
		double secondPart = 0;
		for (int i = 0; i < Sc; i++) {
			if (case1) {
				secondPart += F.pdfPoission(i, rate1 * (L - x));
			}else {
				secondPart += F.pdfPoission(i, rate2 * (L - x));
			}
		}
		
		return  secondPart;
	}
	
	public static double functionf1(double L, double T, int rate1, int rate2, int S, int Sc, double x) {
		return  Math.pow((rate1 + rate2), S - Sc) * Math.exp(- (rate1 + rate2)* x) * Math.pow(x, S - Sc - 1) / F.factorial(S - Sc - 1);
	}
	
	public static double functionf2(double L, double T, int rate1, int rate2, int S, int Sc, double x) {
		return  rate1 * Math.exp(- (rate1 * x+ rate2 *  (L - T) )) * Math.pow(rate1 * x + rate2 * (L- T), S - Sc - 1) / F.factorial(S - Sc - 1);
	}
	
	
	
	public static double firstIntergralFunction(double L, double T, int rate1, int rate2, int S, int Sc, double x, boolean case1) {
		double firstPart = functionf1(L, T, rate1, rate2, S, Sc, x) ;
		double secondPart = secondPart(L, T, rate1, rate2, S, Sc, x, case1);
		
		return firstPart  * secondPart;
	}
	
	public static double secondIntergralFunction(double L, double T, int rate1, int rate2, int S, int Sc, double x, boolean case1) {
		double firstPart = functionf2(L, T, rate1, rate2, S, Sc, x) ;
		double secondPart = secondPart(L, T, rate1, rate2, S, Sc, x, case1);
		
		return firstPart  * secondPart;
	}
	
	public static double IntSimpson(double L, double T, int rate1, int rate2, int S, int Sc,double a, double b,int n, boolean firstInt, boolean case1){                       
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
	 	          s = s + z * firstIntergralFunction(L, T, rate1, rate2, S, Sc, a+i*h, case1);

		       }else {
			          s = s + z * secondIntergralFunction(L, T, rate1, rate2, S, Sc, a+i*h, case1);

		      }
	          z = 6 - z;
	       }
	       return (s * h)/3;
	 } 
	   
}  	