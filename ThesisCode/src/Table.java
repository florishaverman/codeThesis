import java.util.Random;


/**
 * This class can create tables 1-8 of the paper of Kocaga and Sen
 * It prints the results in the console in the same format as in the paper
 * @author Floris Haverman 
 *
 *	(Medium interesting class)
 */
public class Table {
	
	public static void main(String[] args) {
		long startTime, endTime;
		startTime = System.currentTimeMillis();
		
//		createTable1();

//		createTable2(); 
		
//		createTable3();
		
//		Table.createTable4(true);
		
//		Table.createTable5(true); 

//		createTable6(true);
		
//		createTable7(true);
		
//		createTable8(true);
		
//		Table.createAllTables();
		int S, Sc, rate1, rate2;
		double T, L, Bc, Bn;
		boolean case1 = true;
		
//		Random r =  new Random(9);
		
		/* This small bit of code was used to examine the probability of the service level being just over 99%, 
		 * thus obtaining a different value then the replication paper.
		Bc = 0.99;
		Bn = 0.8;
		rate1 = 1;
		rate2 = 1; 
		L = 0.5;
		T= 0.1;
		int counter = 0;
		for (int i = 1; i < 100; i ++) {			
			double temp = ServiceLevel.getSimServiceLevelCritical(L, T, rate1, 6, 6, 1, new Random(i), case1);
			System.out.println(temp);
			if (temp >= 0.99) counter++;
//			Table.createLine45(L, T, rate1, 6, Bc, Bn, new Random(i), case1, true);
		}
		System.out.println("This is the counter " + counter);
		*/

		 endTime = System.currentTimeMillis();

		System.out.println("That took " + (endTime - startTime) + " milliseconds");
	}
	/**
	 * Create table 1
	 */
	public static void createTable1() {
		int S, Sc, rate1, rate2;
		double T, L;
		T= 0.1;
		L = 0.5;
		boolean case1 = true;
		
		Random r =  new Random(1234);
		
		//Set the system parameters
		rate1 = 1;
		rate2 = 4;
		S = 5;
		Sc = 3;
		for (rate1 = 1; rate1 < 13; rate1++) {
			S = rate1 + 4;
			Table.createLine(L, T, rate1, rate2, S, Sc, r, case1);
			


		}
		S = 8;
		for (rate1 = 2; rate1 < 9; rate1++) {
			Sc = rate1 - 1;
			Table.createLine(L, T, rate1, rate2, S, Sc, r, case1);
		}
	}

	/**
	 * Create table 2
	 */
	public static void createTable2() {
		int S, Sc, rate1, rate2;
		double T, L;
		T= 0.1;
		L = 0.5;
		boolean case1 = true;
		
		Random r =  new Random(1234);
		
		rate1 = 4;
		rate2 = 1; 
		S = 5;
		Sc = 2;
		
		for (rate1 = 4; rate1 < 10; rate1++) {
			S = rate1 + 1;
			Table.createLine(L, T, rate1, rate2, S, Sc, r, case1);
		}
		
		S = 7;
		for (rate1 = 5; rate1 < 9; rate1++) {
			Sc = rate1 - 4;
			Table.createLine(L, T, rate1, rate2, S, Sc, r, case1);
		}
		
	}
	
	/**
	 * Create table 3
	 */
	public static void createTable3() {
		int S, Sc, rate1, rate2;
		double T, L;
		boolean case1 = true;
		
		Random r =  new Random(1234);
		
		S = 7;
		Sc = 2;
		rate1 = 6;
		rate2 = 2; 
		L = 0.5;
		T= 0.1;
		
		
		for (S = 7; S < 12; S++) {
			Table.createLine(L, T, rate1, rate2, S, Sc, r, case1);
		}
		
		S = 5;
		Sc = 2;
		rate1 = 1;
		rate2 = 1; 
		L = 1;
		T= 0.5;
		for (rate1 = 1; rate1 < 6; rate1++) {
			Table.createLine(L, T, rate1, rate2, S, Sc, r, case1);
		}
		rate1 = 1;
		for (rate2 = 1; rate2 < 6; rate2++) {
			Table.createLine(L, T, rate1, rate2, S, Sc, r, case1);
		}
		
		S = 14;
		Sc = 3;
		rate1 = 10;
		rate2 = 4; 
		L = 0.5;
		T= 0.1;
		for (T = 0.1; T < 0.55; T += 0.1) {
			Table.createLine(L, T, rate1, rate2, S, Sc, r, case1);
		}
	}
	
	/**
	 * Create table 4
	 * @param minimiseSc A boolean to indicate if we want to minimize Sc
	 */
	public static void createTable4(boolean minimiseSc) {
		int S, Sc, rate1, rate2;
		double T, L, Bc, Bn;
		boolean case1 = true;
		
		Random r =  new Random(1234);
		
		Bc = 0.99;
		Bn = 0.8;
		rate1 = 1;
		rate2 = 1; 
		L = 0.5;
		T= 0.1;
		for (int i = 1; i < 11; i ++) {
			
			Table.createLine45(L, T, rate1, i, Bc, Bn, r, case1, minimiseSc);

		}
	}
	
	/**
	 * Create table 5
	 * @param minimiseSc A boolean to indicate if we want to minimize Sc
	 */
	public static void createTable5(boolean minimiseSc) {
		int S, Sc, ratec, raten;
		double T, L, Bc, Bn;
		boolean case1 = true;
		
		Random r =  new Random(1234);
		
		Bc = 0.99;
		Bn = 0.8;
		ratec = 5;
		raten = 10; 
		L = 2;
		T= 0.5;
		
		double[] valuesSl = new double[] {0.9, 0.925, 0.95, 0.97, 0.98, 0.985, 0.99, 0.995};
		for (int i = 0; i < 8; i ++) {
			
			Table.createLine45(L, T, ratec, raten, valuesSl[i], Bn, r, case1, minimiseSc);

		}
	}
	
	/**
	 * Create table 6
	 * @param minimiseSc A boolean to indicate if we want to minimize Sc
	 */
	public static void createTable6(boolean minimiseSc) {
		int ratec, raten;
		double T, L, Bc, Bn;
		boolean case1 = true;
		
		String d = " , ";
		
		Random r =  new Random(1234);
		
		Bc = 0.99;
		Bn = 0.8;
		ratec = 10;
		raten = 10; 
		L = 0.5;
		T= 0.0;
		
		for ( T = 0; T < 0.51; T += 0.05) {
			
			Table.createLin678(L, F.round(T,2), ratec, raten, Bc, Bn, r, case1, minimiseSc);
						
		}
	}
	
	/**
	 * Create table 7
	 * @param minimiseSc A boolean to indicate if we want to minimize Sc
	 */
	public static void createTable7(boolean minimiseSc) {
		int ratec;
		double raten;
		double T, L, Bc, Bn;
		boolean case1 = true;
				
		Random r =  new Random(1234);
		
		Bc = 0.99;
		Bn = 0.8;
		ratec = 10;
		raten = 10; 
		L = 0.5;
		T= 0.0;
		
		for ( T = 0; T < 0.49; T += 0.05) {
			T = F.round(T,2);
			raten = ratec * L /(L - T);
			Table.createLin678(L, T, ratec, F.round(raten, 2), Bc, Bn, r, case1, minimiseSc);
						
		}
	}
	
	/**
	 * Create table 8
	 * @param minimiseSc A boolean to indicate if we want to minimize Sc
	 */
	public static void createTable8(boolean minimiseSc) {
		double T, L, Bc, Bn, raten, ratec;
		boolean case1 = true;
				
		Random r =  new Random(1234);
		
		Bc = 0.99;
		Bn = 0.8;
		ratec = 10;
		raten = 10; 
		L = 0.5;
		T= 0.0;
		
		for ( T = 0; T < 0.51; T += 0.05) {
			T = F.round(T,2);
			raten = F.round(10 / (1-T) ,2);
			ratec = raten;
			Table.createLin678(L, T, ratec, F.round(raten, 2), Bc, Bn, r, case1, minimiseSc);
						
		}
	}

	/**
	 * Prints a line in the format of tables 6-8.
	 * @param L The system parameter for supply lead time
	 * @param T The system parameter for demand lead time
	 * @param rate1 The rate of class 1 customers
	 * @param rate2 The rate of class 2 customers
	 * @param Bc The required service level of the critical class
	 * @param Bn The required service level of the non critical class
	 * @param r A Random object used to create randomness
	 * @param case1 A boolean to indicate if class 1 is the critical class (true) of not (false)
	 * @param minimiseSc A boolean to indicate if we want to minimize Sc
	 */
	public static void createLin678(double L, double T, double rate1, double rate2, double Bc, double Bn, Random r, boolean case1, boolean minimiseSc) {
		String d = " , ";
		
		case1= true;
		double[]  slSim = Simulation.runSimulationBrute(L, T, rate1, rate2, Bc, Bn, r, case1, minimiseSc);
		double  slNoRat= Simulation.getOptimalForNoRat(L, T, rate1, rate2, Bc, Bn, r, case1)[0];
		System.out.print(rate1+ d);	
		System.out.print(rate1+ d);	
		System.out.print(L+ d);	
		System.out.print(F.round(T,2)+ d);	
		System.out.print(slNoRat + d);
		System.out.print(slSim[0] + d);
		System.out.print(slSim[1] + d);
		System.out.print(F.round(100 * (slNoRat - slSim[0])/ slNoRat,2)+ d);

		case1= false;
		slSim = Simulation.runSimulationBrute(L, T, rate1, rate2, Bc, Bn, r, case1, minimiseSc);
		slNoRat= Simulation.getOptimalForNoRat(L, T, rate1, rate2, Bc, Bn, r, case1)[0];
		System.out.print(slSim[0] + d);
		System.out.print(slSim[1] + d);
		System.out.println(F.round(100 * (slNoRat - slSim[0])/ slNoRat,2));
	}

	/**
	 * Prints a line in the format of tables 4 and 5.
	 * @param L The system parameter for supply lead time
	 * @param T The system parameter for demand lead time
	 * @param rate1 The rate of class 1 customers
	 * @param rate2 The rate of class 2 customers
	 * @param Bc The required service level of the critical class
	 * @param Bn The required service level of the non critical class
	 * @param r A Random object used to create randomness
	 * @param case1 A boolean to indicate if class 1 is the critical class (true) of not (false)
	 * @param minimiseSc A boolean to indicate if we want to minimize Sc
	 */
	public static void createLine45(double L, double T, int rate1, int i, double Bc, double Bn, Random r, boolean case1, boolean minimiseSc) {
		case1= true;

		double[]  slSim = Simulation.runSimulationBrute(L, T, rate1, i, Bc, Bn, r, case1, minimiseSc);
		double[]  slAprox = Simulation.getOptimalForAprox(L, T, rate1, i, Bc, Bn, r, case1, minimiseSc);
		double  slNoRat= Simulation.getOptimalForNoRat(L, T, rate1, i, Bc, Bn, r, case1)[0];
//		System.out.print(Simulation.getSmin(L, T, rate1, i, Bc, Bn) + " ; ");
		System.out.print(i+ " , ");		
		System.out.print(Bc+ " , ");
		System.out.print(slNoRat + " , ");
		System.out.print(slAprox[0] + " , ");
		System.out.print(slAprox[1] + " , ");
		System.out.print(F.round(100 * (slNoRat - slAprox[0])/ slNoRat, 2) + " , ");
		
		System.out.print(slSim[0] + " , ");
		System.out.print(slSim[1] + " , ");
		System.out.print(F.round(100 * (slNoRat - slSim[0])/ slNoRat,2)+ " , ");
		
//		System.out.println(" ");
		
		//Second part of the table
		case1= false;
		slSim = Simulation.runSimulationBrute(L, T, i, rate1, Bc, Bn, r, case1, minimiseSc);
		slAprox = Simulation.getOptimalForAprox(L, T, i,rate1, Bc, Bn, r, case1, minimiseSc);
		slNoRat= Simulation.getOptimalForNoRat(L, T, i, rate1, Bc, Bn, r, case1)[0];
//		System.out.print(Simulation.getSmin(L, T, i, rate1, Bc, Bn) + " ; ");
//		System.out.print(i + " , ");
		System.out.print(slNoRat + " , ");
		System.out.print(slAprox[0] + " , ");
		System.out.print(slAprox[1] + " , ");
		System.out.print(F.round(100 * (slNoRat - slAprox[0])/ slNoRat, 2) + " , ");
		
		System.out.print(slSim[0] + " , ");
		System.out.print(slSim[1] + " , ");
		System.out.println(F.round(100 * (slNoRat - slSim[0])/ slNoRat,2));
	}
	/**
	 * Prints a line in the format of tables 1-3
	 * @param L The system parameter for supply lead time
	 * @param T The system parameter for demand lead time
	 * @param rate1 The rate of class 1 customers
	 * @param rate2 The rate of class 2 customers
	 * @param S The order up to level
	 * @param Sc The critical level
	 * @param r A Random object used to create randomness
	 * @param case1 A boolean to indicate if class 1 is the critical class (true) of not (false)
	 */
	public static void createLine(double L, double T, int rate1, int rate2, int S, int Sc, Random r, boolean case1) {
		case1 = true;
		double slSim = F.round(ServiceLevel.getSimServiceLevelCritical(L, T, rate1, rate2, S, Sc, r, case1),4);
		double slAprox = F.round(ServiceLevel.getAproxServiceLevelCritical(L, T, rate1, rate2, S, Sc, case1),4);
		System.out.print(rate1 + " , ");
		System.out.print(rate2 + " , ");
		System.out.print(S + " , ");
		System.out.print(Sc + " , ");
		System.out.print(F.round(ServiceLevel.getServiceLevelNonCritical(L, T, rate1, rate2, S, Sc),4) + " , ");
		System.out.print(slSim + " , ");
		System.out.print(slAprox + " , ");
		System.out.print(F.round( 100 * (slSim - slAprox)/ slSim, 2));
		System.out.print(" , ");
		case1 = false;
		slSim = F.round(ServiceLevel.getSimServiceLevelCritical(L, T, rate2, rate1, S, Sc, r, case1),4);
		slAprox = F.round(ServiceLevel.getAproxServiceLevelCritical(L, T, rate2, rate1, S, Sc, case1),4);
		System.out.print(F.round(ServiceLevel.getServiceLevelNonCritical(L, T, rate2, rate1, S, Sc),4) + " , ");
		System.out.print(slSim + " , ");
		System.out.print(slAprox + " , ");
		System.out.println(F.round( 100 * (slSim - slAprox)/ slSim, 2));
	}
	
	/**
	 * A method that creates tables 1-8 
	 * Takes about 20 minutes
	 */
	public static void createAllTables() {
		long startTime, endTime;
		startTime = System.currentTimeMillis();
		
		createTable1();
		
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();

		
		createTable2();
		
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		
		createTable3();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		
		Table.createTable4(true);
		
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		
		Table.createTable5(true); 
		
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		
		createTable6(true);
		
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		createTable7(true);
		
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		
		createTable8(true);
		



		 endTime = System.currentTimeMillis();

		System.out.println("That took " + (endTime - startTime) + " milliseconds");
	}
}
