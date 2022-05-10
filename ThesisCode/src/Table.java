import java.util.Random;

public class Table {
	
	public static void main(String[] args) {
//		createTable1();
		
//		createTable2();
		
		createTable3();
	}
	
	public static void createTable1() {
		int S, Sc, rate1, rate2;
		double T, L;
		T= 0.1;
		L = 0.5;
		boolean case1 = true;
		
		Random r =  new Random(1234);
		
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
	 * Replicates table 3 from the paper.
	 * The table consists of 4 different parts in which a different parameter varies.
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
//		L = 1;
//		T= 0.5;
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
		System.out.print(" ; ");
		case1 = false;
		slSim = F.round(ServiceLevel.getSimServiceLevelCritical(L, T, rate2, rate1, S, Sc, r, case1),4);
		slAprox = F.round(ServiceLevel.getAproxServiceLevelCritical(L, T, rate2, rate1, S, Sc, case1),4);
		System.out.print(F.round(ServiceLevel.getServiceLevelNonCritical(L, T, rate2, rate1, S, Sc),4) + " , ");
		System.out.print(slSim + " , ");
		System.out.print(slAprox + " , ");
		System.out.println(F.round( 100 * (slSim - slAprox)/ slSim, 2));
	}
}
