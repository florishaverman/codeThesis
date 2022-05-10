import java.util.Random;

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
		
		 startTime = System.currentTimeMillis();

		
		System.out.print("Smin is " + Simulation.getSmin(L, T, rate1, rate2, Bc, Bn) + " ;");

//		double[]  sl = Simulation.runSimulationBrute(L, T, rate1, rate2, Bc, Bn, r, case1, true);
		double[]  sl = Simulation.getOptimalForAprox(L, T, rate1, rate2, Bc, Bn, r, case1, true);
//		double[]  sl = Simulation.getOptimalForNoRat(L, T, rate1, rate2, Bc, Bn, r, case1);
//		System.out.println("Optimal is with S "+ sl[0] + " and Sc "+ sl[1]);
//		System.out.print("Smin is " + Simulation.getSmin(L, T, rate1, rate2, Bc, Bn) + " ;");
		System.out.println(Bc +" "+ sl[0] + " "+ sl[1]);
		
		 endTime = System.currentTimeMillis();

		System.out.println("That took " + (endTime - startTime) + " milliseconds");
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
	
	public static double[] runSimulationBrute(double L, double T, double rate1, double rate2, double Bc, double Bn, Random r, boolean case1, boolean minimiseSc) {
		int Smin = Simulation.getSmin(L, T, rate1, rate2, Bc, Bn);
		for (int i= Smin; i < 50; i++) {
			if (minimiseSc) {
				for (int j = 0; j <= i - Smin ; j++) {
					double[]  sl = ServiceLevel.getSimServiceLevelCritical(L, T, rate1, rate2, i, j, r, case1, true);
//					System.out.println("Sl for crit "+ sl[0] + " sl for non crit "+ sl[1] + "with S "+ i + " and Sc "+ j);
					if (sl[0] >= Bc && sl[1] >= Bn) {
						return new double [] {i,j};
					}
				}
			} else {
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
	
public static double[] getOptimalForNoRat(double L, double T, double rate1, double rate2, double Bc, double Bn, Random r, boolean case1) {
		
		for (int i= Simulation.getSmin(L, T, rate1, rate2, Bc, Bn); i < 100; i++) {
//			double[] sl =  ServiceLevel.getSimServiceLevelCritical(L, T, rate1, rate2, i, 0, r, case1, true);
			

//			System.out.println("Sl for crit "+ sl[0] + " sl for non crit "+ sl[1] + "with S "+ i + " and Sc "+ j);
//			if (sl[0] >= Bc && sl[1] >= Bn) return new double [] {i,0};
			
			
			double sl =  ServiceLevel.getServiceLevelNonCritical(L, T, rate1, rate2, i, 0);
			if (sl > Bc) return new double [] {i,0};
		}
		
		return null;
	}
	

public static double[] getOptimalAll(double L, double T, int rate1, int rate2, double Bc, double Bn, Random r, boolean case1, boolean minimiseSc) {
	double[] toReturn = new double[5];
	boolean lookingSim = true;
	boolean lookingAprox = true;
	boolean lookingNo = true;
	
	int Smin = Simulation.getSmin(L, T, rate1, rate2, Bc, Bn);
	for (int i= Smin; i < 10; i++) {
		if (minimiseSc) {
			for (int j = 0; j <= i - Smin ; j++) {
				if (!(lookingSim || lookingAprox || lookingNo )) return toReturn;
				if (lookingSim) {
					double[]  sl = ServiceLevel.getSimServiceLevelCritical(L, T, rate1, rate2, i, j, r, case1, true);
//					System.out.println("Sl for crit "+ sl[0] + " sl for non crit "+ sl[1] + "with S "+ i + " and Sc "+ j);
					if (sl[0] >= Bc && sl[1] >= Bn) {
						toReturn[3] = i;
						toReturn[4] = j;
					}
				}
				
				if (lookingAprox) {
					double  slCrit = ServiceLevel.getAproxServiceLevelCritical(L, T, rate1, rate2, i, j, case1);
					double  slNonCrit = ServiceLevel.getServiceLevelNonCritical(L, T, rate1, rate2, i, j);
					double[] sl = new double[] {slCrit, slNonCrit};
					//System.out.println("Sl for crit "+ sl[0] + " sl for non crit "+ sl[1] + "with S "+ i + " and Sc "+ j);
					if (sl[0] >= Bc && sl[1] >= Bn) {
						toReturn[1] = i;
						toReturn[2] = j;
					}
				}
				
				if (lookingNo) {
					double[] sl =  ServiceLevel.getSimServiceLevelCritical(L, T, rate1, rate2, i, 0, r, case1, true);
					if (sl[0] >= Bc && sl[1] >= Bn) {
						toReturn[0] = i;
					}
				}
				
			}
		} else {
			for (int j = i - 1; j > 0; j--) {
				if (!(lookingSim || lookingAprox || lookingNo )) return toReturn;
				if (lookingSim) {
					double[]  sl = ServiceLevel.getSimServiceLevelCritical(L, T, rate1, rate2, i, j, r, case1, true);
//					System.out.println("Sl for crit "+ sl[0] + " sl for non crit "+ sl[1] + "with S "+ i + " and Sc "+ j);
					if (sl[0] >= Bc && sl[1] >= Bn) {
						toReturn[3] = i;
						toReturn[4] = j;
					}
				}
				
				if (lookingAprox) {
					double  slCrit = ServiceLevel.getAproxServiceLevelCritical(L, T, rate1, rate2, i, j, case1);
					double  slNonCrit = ServiceLevel.getServiceLevelNonCritical(L, T, rate1, rate2, i, j);
					double[] sl = new double[] {slCrit, slNonCrit};
					//System.out.println("Sl for crit "+ sl[0] + " sl for non crit "+ sl[1] + "with S "+ i + " and Sc "+ j);
					if (sl[0] >= Bc && sl[1] >= Bn) {
						toReturn[1] = i;
						toReturn[2] = j;
					}
				}
				
				if (lookingNo) {
					double[] sl =  ServiceLevel.getSimServiceLevelCritical(L, T, rate1, rate2, i, 0, r, case1, true);
					if (sl[0] >= Bc && sl[1] >= Bn) {
						toReturn[0] = i;
					}
				}
				
			}
		}
	}
	
	return null;
}


public static int getSmin(double L, double T, double rate1, double rate2, double Bc, double Bn) {
	
	for (int Smin= 1; Smin < 100; Smin++) {
		if (ServiceLevel.getServiceLevelNonCritical(L, T, rate1, rate2, Smin, 0) >= Bn) {
			return Smin;
		}
	}
	return 1;
}

}
