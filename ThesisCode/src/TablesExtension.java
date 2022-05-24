import java.util.Random;


/**
 * This class can create tables 1-8 of the paper of Kocaga and Sen
 * It prints the results in the console in the same format as in the paper
 * @author Floris Haverman 
 *
 *	(Medium interesting class)
 */
public class TablesExtension {
	
	public static void main(String[] args) {
		long startTime, endTime;
		startTime = System.currentTimeMillis();
		
		
//		createTable10();
		createTable9();

		
//		createTable1();

//		createTable2(); 
		
//		createTable3();
		
//		createTable4(true);
		

		
//		createTable5(true, 0.25);
		
		/*
		System.out.println();
		System.out.println();
		System.out.println();
		
		createTable5(true, 0.8);

		System.out.println();
		System.out.println();
		System.out.println();
		
		createTable5(true, 1);
		 */
//		createTable6(true, 0.25);
		
//		createTable7(true, 1);
		
//		createTable8(true);
		
//		Table.createAllTables();
		
//		createTable7(true, 0.25);
		
		System.out.println();
		System.out.println();
		System.out.println();
		
//		createTable7(true, 0.5);
//		createTable8(true, 0.5);
		
		System.out.println();
		System.out.println();
		System.out.println();
		
//		createTable7(true, 0.8);
//		createTable8(true, 0.8);

		System.out.println();
		System.out.println();
		System.out.println();
		
//		createTable7(true, 1);
//		createTable8(true, 1);

		

		 endTime = System.currentTimeMillis();

		System.out.println("That took " + (endTime - startTime) + " milliseconds");
	}
	/**
	 * Create table
	 */
	public static void createTable9() {
		int S, Sc;
		double T, L, rate1, rate2, Bc, Bn;
		T= 0.1;
		L = 0.5;
		boolean case1 = true;
		
		Random r =  new Random(1234);
		
		//Set the system parameters
		rate1 = 10;
		rate2 = 10;
		S = 14;
		Sc = 3;
		Bc = 0.99;
		Bn = 0.8;
		
		//Constant net demand during lead time
		int c =  10;
		double p = 0.5;
		boolean minimiseSc = true;
		
		for (T = 0; T < 0.47; T+=0.05) {
			T = F.round(T, 2);
			double[]  slSim;
			double pTemp;
			double rate2Temp;
			System.out.print(rate1 + " , ");
			System.out.print(rate2+ " , ");
			System.out.print(L + " , ");
			System.out.print(T + " , ");
			System.out.print(p + " , ");
			pTemp = p;
			double  slNoRat= Extension.getOptimalForNoRat(L, T, rate1, F.round((c- rate1 * L) /(L - pTemp* T), 2), Bc, Bn, r, case1, pTemp);
			System.out.print(slNoRat + " , ");
			
			pTemp = p;
			slSim = Extension.runSimulationBrute(L, T, rate1, F.round((c- rate1 * L) /(L - pTemp* T), 2), Bc, Bn, r, case1, minimiseSc, pTemp);
			System.out.print(slSim[0] + " , ");
			System.out.print(slSim[1] + " , ");
			System.out.print(F.round(100 * (slNoRat - slSim[0])/ slNoRat,2)+ " , ");
			
			pTemp = 1;
			double tempT = p * T;
			rate2Temp =  F.round((c- rate1 * L) /(L - tempT), 2);
			slSim = Extension.runSimulationBrute(L, p*T, rate1, rate2Temp, Bc, Bn, r, case1, minimiseSc, pTemp);
			System.out.print(tempT + " , ");
			System.out.print(rate2Temp + " , ");
			
			System.out.print(slSim[0] + " , ");
			System.out.print(slSim[1] + " , ");
			System.out.print(F.round(100 * (slNoRat - slSim[0])/ slNoRat,2)+ " , ");
			System.out.println();
		}
		
	}
	
	/**
	 * Create table
	 */
	public static void createTable10() {
		int S, Sc;
		double T, L, rate1, rate2, Bc, Bn;
		T= 0.1;
		L = 0.5;
		boolean case1 = true;
		
		Random r =  new Random(1234);
		
		//Set the system parameters
		rate1 = 50;
		rate2 = 20;
		S = 14;
		Sc = 3;
		Bc = 0.99;
		Bn = 0.8;
		
		//Constant net demand during lead time
		int c =  50;
		double p = 0.5;
		boolean minimiseSc = true;
		
		for (p = 0; p < 1.01; p+=0.1) {
			p = F.round(p, 2);
			double[]  slSim;
			double tempT;
			System.out.print(rate1 + " , ");
			System.out.print(F.round((c- rate1 * L) /(L - p* T), 2)+ " , ");
			System.out.print(L + " , ");
			System.out.print(T + " , ");
			System.out.print(p + " , ");
			tempT= 0.1;
			double  slNoRat= Extension.getOptimalForNoRat(L, tempT, rate1, F.round((c- rate1 * L) /(L - p* tempT), 2), Bc, Bn, r, case1, p);
			System.out.print(slNoRat + " , ");
			tempT= 0.1;
			slSim = Extension.runSimulationBrute(L, tempT, rate1, F.round((c- rate1 * L) /(L - p* tempT), 2), Bc, Bn, r, case1, minimiseSc, p);
			System.out.print(slSim[0] + " , ");
			System.out.print(slSim[1] + " , ");
			System.out.print(F.round(100 * (slNoRat - slSim[0])/ slNoRat,2)+ " , ");
			
			tempT= 0.2;
			slSim = Extension.runSimulationBrute(L, tempT, rate1, F.round((c- rate1 * L) /(L - p* tempT), 2), Bc, Bn, r, case1, minimiseSc, p);
			System.out.print(slSim[0] + " , ");
			System.out.print(slSim[1] + " , ");
			System.out.print(F.round(100 * (slNoRat - slSim[0])/ slNoRat,2)+ " , ");
			
			tempT= 0.4;
			slSim = Extension.runSimulationBrute(L, tempT, rate1, F.round((c- rate1 * L) /(L - p* tempT), 2), Bc, Bn, r, case1, minimiseSc, p);
			System.out.print(slSim[0] + " , ");
			System.out.print(slSim[1] + " , ");
			System.out.print(F.round(100 * (slNoRat - slSim[0])/ slNoRat,2)+ " , ");
			System.out.println();
		}
		
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
		
		double p = 0.8;
		
		//update the rates to match original case	
		for (rate1 = 1; rate1 < 13; rate1++) {
			S = rate1 + 4;
			TablesExtension.createLine(L, T, rate1, rate2, S, Sc, r, case1, p);
			
		}
		S = 8;
		for (rate1 = 2; rate1 < 9; rate1++) {
			Sc = rate1 - 1;
			TablesExtension.createLine(L, T, rate1, rate2, S, Sc, r, case1, p);
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
		
		double p = 0.5;
		
		for (rate1 = 4; rate1 < 10; rate1++) {
			S = rate1 + 1;
			TablesExtension.createLine(L, T, rate1, rate2, S, Sc, r, case1, p);
		}
		
		S = 7;
		for (rate1 = 5; rate1 < 9; rate1++) {
			Sc = rate1 - 4;
			TablesExtension.createLine(L, T, rate1, rate2, S, Sc, r, case1, p);
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
		
		double p = 1;
		
		for (S = 7; S < 12; S++) {
			System.out.print(p +" , " + L+" , " + T +" , ");
			TablesExtension.createLine(L, T, rate1, rate2, S, Sc, r, case1, p);
		}
		
		S = 5;
		Sc = 2;
		rate1 = 1;
		rate2 = 1; 
		L = 1;
		T= 0.5;
		for (rate1 = 1; rate1 < 6; rate1++) {
			System.out.print(p +" , " + L+" , " + T +" , ");
			TablesExtension.createLine(L, T, rate1, rate2, S, Sc, r, case1,p);
		}
		rate1 = 1;
		for (rate2 = 1; rate2 < 6; rate2++) {
			System.out.print(p +" , " + L+" , " + T +" , ");
			TablesExtension.createLine(L, T, rate1, rate2, S, Sc, r, case1, p);
		}
		
		S = 14;
		Sc = 3;
		rate1 = 10;
		rate2 = 4; 
		L = 0.5;
		T= 0.1;
		for (T = 0.1; T < 0.55; T += 0.1) {
			System.out.print(p +" , " + L+" , " + T +" , ");
			TablesExtension.createLine(L, T, rate1, rate2, S, Sc, r, case1, p);
		}
		
		S = 12;
		Sc = 3;
		rate1 = 8;
		rate2 = 4; 
		L = 0.5;
		T= 0.1;
		for (p = 0.0; p < 1.01; p += 0.2) {
			p = F.round(p, 4);
			System.out.print(p +" , " + L+" , " + T +" , ");
			TablesExtension.createLine(L, T, rate1, rate2, S, Sc, r, case1, p);
		}
	}
	
	/**
	 * Create table 4
	 * @param minimiseSc A boolean to indicate if we want to minimize Sc
	 */
	public static void createTable4(boolean minimiseSc, double p) {
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
//		double p = 0.8;
		for (int i = 1; i < 11; i ++) {
			
			TablesExtension.createLine45(L, T, rate1, i, Bc, Bn, r, case1, minimiseSc,p);

		}
	}
	
	/**
	 * Create table 5
	 * @param minimiseSc A boolean to indicate if we want to minimize Sc
	 */
	public static void createTable5(boolean minimiseSc, double p) {
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
//		double p = 0.5;
		
		for (int i = 0; i < 8; i ++) {
			
			TablesExtension.createLine45(L, T, ratec, raten, valuesSl[i], Bn, r, case1, minimiseSc,p);

		}
	}
	
	/**
	 * Create table 6
	 * @param minimiseSc A boolean to indicate if we want to minimize Sc
	 */
	public static void createTable6(boolean minimiseSc, double p) {
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
			
			TablesExtension.createLin678(L, F.round(T,2), ratec, raten, Bc, Bn, r, case1, minimiseSc, p);
						
		}
	}
	
	/**
	 * Create table 7
	 * @param minimiseSc A boolean to indicate if we want to minimize Sc
	 */
	public static void createTable7(boolean minimiseSc, double p) {
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
			raten =  ratec * L /(L - p* T);
			TablesExtension.createLin678(L, T, ratec, F.round(raten, 2), Bc, Bn, r, case1, minimiseSc, p);
						
		}
	}
	
	/**
	 * Create table 8
	 * @param minimiseSc A boolean to indicate if we want to minimize Sc
	 */
	public static void createTable8(boolean minimiseSc, double p) {
		double T, L, Bc, Bn, raten, ratec;
		boolean case1 = true;
				
		Random r =  new Random(1234);
		
		Bc = 0.99;
		Bn = 0.8;
		ratec = 10;
		raten = 10; 
		L = 0.5;
		T= 0.0;
		int constantRate = 10;
		
		for ( T = 0; T < 0.51; T += 0.05) {
			T = F.round(T,2);
			raten = F.round(constantRate / (2* L- p * T) ,2);
			ratec = raten;
			TablesExtension.createLin678(L, T, ratec, raten, Bc, Bn, r, case1, minimiseSc, p);
			
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
	public static void createLin678(double L, double T, double rate1, double rate2, double Bc, double Bn, Random r, boolean case1, boolean minimiseSc, double p) {
		String d = " , ";
		
		case1= true;
		double[]  slSim = Extension.runSimulationBrute(L, T, rate1, rate2, Bc, Bn, r, case1, minimiseSc, p);
		double  slNoRat= Extension.getOptimalForNoRat(L, T, rate1, rate2, Bc, Bn, r, case1, p);
		System.out.print(rate1+ d);	
		System.out.print(rate2+ d);	
		System.out.print(L+ d);	
		System.out.print(F.round(T,2)+ d);	
		System.out.print(slNoRat + d);
		System.out.print(slSim[0] + d);
		System.out.print(slSim[1] + d);
		System.out.print(F.round(100 * (slNoRat - slSim[0])/ slNoRat,2)+ d);

		case1= false;
		slSim = Extension.runSimulationBrute(L, T, rate1, rate2, Bc, Bn, r, case1, minimiseSc, p);
		slNoRat= Extension.getOptimalForNoRat(L, T, rate1, rate2, Bc, Bn, r, case1, p);
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
	public static void createLine45(double L, double T, int rate1, int i, double Bc, double Bn, Random r, boolean case1, boolean minimiseSc, double p) {
		case1= true;

		double[]  slSim = Extension.runSimulationBrute(L, T, rate1, i, Bc, Bn, r, case1, minimiseSc, p);
		double[]  slAprox = Extension.getOptimalForAprox(L, T, rate1, i, Bc, Bn, r, case1, minimiseSc, p);
		double  slNoRat= Extension.getOptimalForNoRat(L, T, rate1, i, Bc, Bn, r, case1, p);
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
		slSim = Extension.runSimulationBrute(L, T, i, rate1, Bc, Bn, r, case1, minimiseSc, p);
		slAprox = Extension.getOptimalForAprox(L, T, i,rate1, Bc, Bn, r, case1, minimiseSc, p);
		slNoRat= Extension.getOptimalForNoRat(L, T, i, rate1, Bc, Bn, r, case1, p);
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
	public static void createLine(double L, double T, int rate1, int rate2, int S, int Sc, Random r, boolean case1, double p) {
		case1 = true;
		
		double rate1new = rate1 + (1-p) * rate2;
		double rate2new = p* rate2;	

		
		double slSim = F.round(Extension.getSimServiceLevelCritical(L, T, rate1, rate2, S, Sc, r, case1, p),4);
		double slAprox = F.round(Extension.getAproxServiceLevelCritical(L, T, rate1new, rate2new, S, Sc, case1,p),4);
		System.out.print(rate1 + " , ");
		System.out.print(rate2 + " , ");
		System.out.print(S + " , ");
		System.out.print(Sc + " , ");
		System.out.print(F.round(Extension.getServiceLevelNonCritical(L, T, rate1new, rate2new, S, Sc,p),4) + " , ");
		System.out.print(slSim + " , ");
		System.out.print(slAprox + " , ");
		System.out.print(F.round( 100 * (slSim - slAprox)/ slSim, 2));
		System.out.print(" , ");
		case1 = false;
		slSim = F.round(Extension.getSimServiceLevelCritical(L, T, rate2, rate1, S, Sc, r, case1, p),4);
		slAprox = F.round(Extension.getAproxServiceLevelCritical(L, T, rate2new, rate1new, S, Sc, case1,p),4);
		System.out.print(F.round(Extension.getServiceLevelNonCritical(L, T, rate2new, rate1new, S, Sc,p),4) + " , ");
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
		
		double p = 0.8;
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
		
		createTable4(true, p);
		
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		
		createTable5(true, p); 
		
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		
		createTable6(true, p);
		
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		createTable7(true, p);
		
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		
		createTable8(true, p);
		



		 endTime = System.currentTimeMillis();

		System.out.println("That took " + (endTime - startTime) + " milliseconds");
	}
}
