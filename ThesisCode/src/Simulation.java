import java.util.Random;

/**
 * This class 
 * @author Floris Haverman 
 *
 *	(Interesting class)
 */
public class Simulation {
	public static void main(String[] args) {
		System.out.println("Hello world");
		long startTime, endTime;
		
		int S, Sc, rate1, rate2;
		double T, L, Bc, Bn;
		boolean case1 = true;
		
		Random r =  new Random(1234);
		
		S = 7;
		Sc = 2;
		rate1 = 1;
		rate2 = 1; 
		L = 0.5;
		T= 0.1;
		Bc = 0.99;
		Bn = 0.8;
		 boolean minimiseSc = true;
		
		rate1= 5;
		rate2= 10;
		L = 2;
		T =0.5;
		Bn= 0.8;
		Bc= 0.9;
		
		/*
		for (int i =1; i < 11; i ++) {
//			double[]  sl = Simulation.runSimulationBrute(L, T, rate1, i, Bc, Bn, r, case1);
			double[]  sl = Simulation.getOptimalForAprox(L, T, rate1, i, Bc, Bn, r, case1, true);
//			double[]  sl = Simulation.getOptimalForNoRat(L, T, rate1, i, Bc, Bn, r, case1);
//			System.out.println("Optimal is with S "+ sl[0] + " and Sc "+ sl[1]);
			System.out.print("Smin is " + Simulation.getSmin(L, T, i, rate2, Bc, Bn) + " ;");
			System.out.println(i +" "+ sl[0] + " "+ sl[1]);
		}
		
		
		case1= false;
		double[] sl;
		for (int i =1; i <11; i ++) {
			sl = Simulation.runSimulationBrute(L, T, i, rate2, Bc, Bn, r, case1, minimiseSc);
//			sl = Simulation.getOptimalForAprox(L, T, i, rate2, Bc, Bn, r, case1, minimiseSc);
	//		sl = Simulation.getOptimalForNoRat(L, T, rate1, i, Bc, Bn, r, case1);
	//		System.out.println("Optimal is with S "+ sl[0] + " and Sc "+ sl[1]);
			System.out.print("Smin is " + Simulation.getSmin(L, T, i, rate2, Bc, Bn) + " ;");
			System.out.println(i +" "+ sl[0] + " "+ sl[1]);
		}
		*/
		
		
		 startTime = System.currentTimeMillis();

//		Table.createTable4(true);
//		Table.createTable5(true); //Takes a long time to run

		 endTime = System.currentTimeMillis();

		System.out.println("That took " + (endTime - startTime) + " milliseconds");
		
		startTime = System.currentTimeMillis();
		
//		Table.createTable4(false);
//		Table.createTable5(false); //Takes a long time to run

		endTime = System.currentTimeMillis();
		
		System.out.println("That took " + (endTime - startTime) + " milliseconds");
		 
		
		
	}
	
	/**
	 * Determines the optimal parameters S and Sc using the simulation
	 * @param L The system parameter for supply lead time
	 * @param T The system parameter for demand lead time
	 * @param rate1 The rate of class 1 customers
	 * @param rate2 The rate of class 2 customers
	 * @param Bc The required service level of the critical class
	 * @param Bn The required service level of the non critical class
	 * @param r A Random object used to create randomness
	 * @param case1 A boolean to indicate if class 1 is the critical class (true) of not (false)
	 * @param minimiseSc A boolean to indicate if we want to minimize Sc
	 * @return The optimal S and Sc
	  */
	public static double[] runSimulationBrute(double L, double T, double rate1, double rate2, double Bc, double Bn, Random r, boolean case1, boolean minimiseSc) {
		//Get Smin to limit the feasible region
		int Smin = Simulation.getSmin(L, T, rate1, rate2, Bc, Bn);
		
		//Start searching for the small values of S and increase until you find parameters that yield the required service level
		for (int i= Smin; i < 100; i++) {
			
			if (minimiseSc) { //Search for the smallest S with also the smallest Sc, thus maximizing non critical service level
				for (int j = 0; j <= i - Smin ; j++) {
//					System.out.println("start simulation");
					double[]  sl = ServiceLevel.getSimServiceLevelCritical(L, T, rate1, rate2, i, j, r, case1, true);
//					System.out.println("end simulation with i " + i + " and j "+ j);

//					System.out.println("Sl for crit "+ sl[0] + " sl for non crit "+ sl[1] + "with S "+ i + " and Sc "+ j);
					if (sl[0] >= Bc && sl[1] >= Bn) {
						return new double [] {i,j};
					}
				}
			} else { //Search for the smallest S with the largest Sc, thus maximizing the critical service level
				for (int j = i - 1; j > 0; j--) {
					double[]  sl = ServiceLevel.getSimServiceLevelCritical(L, T, rate1, rate2, i, j, r, case1, true);
//					System.out.println("Sl for crit "+ sl[0] + " sl for non crit "+ sl[1] + "with S "+ i + " and Sc "+ j);
					if (sl[0] >= Bc && sl[1] >= Bn) {
						return new double [] {i,j};
					}
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Determines the optimal parameters S and Sc using the approximate expression for the critical class
	 * @param L The system parameter for supply lead time
	 * @param T The system parameter for demand lead time
	 * @param rate1 The rate of class 1 customers
	 * @param rate2 The rate of class 2 customers
	 * @param Bc The required service level of the critical class
	 * @param Bn The required service level of the non critical class
	 * @param case1 A boolean to indicate if class 1 is the critical class (true) of not (false)
	 * @param minimiseSc A boolean to indicate if we want to minimize Sc
	 * @return The optimal S and Sc
	 * 
	 * Works the same a runSimulationBrute() above
	 */
	public static double[] getOptimalForAprox(double L, double T, double rate1, double rate2, double Bc, double Bn, Random r, boolean case1, boolean minimiseSc) {
		int Smin = Simulation.getSmin(L, T, rate1, rate2, Bc, Bn);
		
		for (int i= Smin; i < 50; i++) {
			if (minimiseSc) {
				for (int j = 0; j <= i - Smin ; j++) {
					double  slCrit = ServiceLevel.getAproxServiceLevelCritical(L, T, rate1, rate2, i, j, case1);
					double  slNonCrit = ServiceLevel.getServiceLevelNonCritical(L, T, rate1, rate2, i, j);
					double[] sl = new double[] {slCrit, slNonCrit};
//					System.out.println("Sl for crit "+ sl[0] + " sl for non crit "+ sl[1] + " with S "+ i + " and Sc "+ j);
					if (sl[0] >= Bc && sl[1] >= Bn) {
						return new double [] {i,j};
					}
				}
			}else {
				for (int j = i - 1; j >= 0; j--) {
					double  slCrit = ServiceLevel.getAproxServiceLevelCritical(L, T, rate1, rate2, i, j, case1);
					double  slNonCrit = ServiceLevel.getServiceLevelNonCritical(L, T, rate1, rate2, i, j);
					double[] sl = new double[] {slCrit, slNonCrit};
//					System.out.println("Sl for crit "+ sl[0] + " sl for non crit "+ sl[1] + "with S "+ i + " and Sc "+ j);
					if (sl[0] >= Bc && sl[1] >= Bn) {
						return new double [] {i,j};
					}
				}
			}
			
		}
		
		return null;
	}


	
	
	/**
	 * Determines the optimal parameter S when no rationing is applied
	 * @param L The system parameter for supply lead time
	 * @param T The system parameter for demand lead time
	 * @param rate1 The rate of class 1 customers
	 * @param rate2 The rate of class 2 customers
	 * @param Bc The required service level of the critical class
	 * @param Bn The required service level of the non critical class
	 * @param case1 A boolean to indicate if class 1 is the critical class (true) of not (false)
	 * @return The optimal S 
	 */
public static double[] getOptimalForNoRat(double L, double T, double rate1, double rate2, double Bc, double Bn, Random r, boolean case1) {
		
		//In this case the service levels of both classes are the same, so we can use the exact one which is fastest and exact. 
		//Find the smallest S such that the service level requirements are met
		for (int i= Simulation.getSmin(L, T, rate1, rate2, Bc, Bn); i < 100; i++) {
	
			double sl =  ServiceLevel.getServiceLevelNonCritical(L, T, rate1, rate2, i, 0);
			if (sl > Bc) return new double [] {i,0};
		}
		
		return null;
	}
	

/**
 * Determines the minimal amount of stock needed to satisfy the non critical service level with no rationing
 * This Smin is used to decrease the feasible region, which is explained in more detail in the paper
 * @param L The system parameter for supply lead time
 * @param T The system parameter for demand lead time
 * @param rate1 The rate of class 1 customers
 * @param rate2 The rate of class 2 customer
 * @param Bc The required service level of the critical class
 * @param Bn The required service level of the non critical class
 * @return the minimal amount of stock needed to get the non critical service level with no rationing
 */
public static int getSmin(double L, double T, double rate1, double rate2, double Bc, double Bn) {
	
	for (int Smin= 1; Smin < 100; Smin++) {
		if (ServiceLevel.getServiceLevelNonCritical(L, T, rate1, rate2, Smin, 0) >= Bn) {
			return Smin;
		}
	}
	return 1;
}

}
