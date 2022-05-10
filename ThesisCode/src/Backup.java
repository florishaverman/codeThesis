import java.util.Random;
import java.util.TreeMap;
import java.util.Map.Entry;

public class Backup {
	
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
		
		S = 8;
		for (rate1 = 2; rate1 < 9; rate1++) {
			case1 = true;
			Sc = rate1 - 1;
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
	
	/** redundent */
public static double getSimServiceLevelCritical(double L, double T, int rate1, int rate2, int S, int Sc, Random r, boolean case1) {
		
		/*
		 * The different events all have a number
		 * 
		 * 1: arrival of critical demand
		 * 2: arrival of non-critical demand
		 * 3: Due date of non-critical demand
		 * 4: arrival of replenishment
		 */
		
		
		TreeMap<Double, Integer> events = new TreeMap<>();
		//Simulate T time units.
		double timeHorizon = 1E+5;
		
		//Initilization
		int criticalBackorders= 0;
		int nonCriticalBackorders= 0;
		int stock = S;
		
		int totCritical = 0;
		int filledCritical = 0;
		
		int totNonCritical = 0;
		int filledNonCritical = 0;
		
		
		double t = 0;
		events.put(t + F.getExponetialRandom(r, rate1), 1);
		events.put(t + F.getExponetialRandom(r, rate2), 2);

		boolean reset = false; 
		
		while (t < timeHorizon){
			if (t> 100 && !reset) {
				totCritical = 0;
				filledCritical = 0;
				
				totNonCritical = 0;
				filledNonCritical = 0;
				reset = true;
			}
			Entry<Double, Integer> event = events.pollFirstEntry();
			t = event.getKey();
			switch(event.getValue()) {
				case 1: //critical order arrival
					if (case1) {
						totCritical++;
						if (stock == 0) {
							criticalBackorders ++;
						}else {
							filledCritical++;
							stock--;
						}
						
					}else {
						events.put(t + T, 3);
					}
					events.put(t + F.getExponetialRandom(r, rate1), 1);
					events.put(t + L, 4);

					break;
				case 2: //non critical order arrival
					if (case1) {
						events.put(t + T, 3);
					}else {
						totNonCritical++;
						if (stock > Sc) {
							filledNonCritical++;
							stock--;
						}else {
							nonCriticalBackorders++;
						}
					}
					events.put(t + F.getExponetialRandom(r, rate2), 2);
					events.put(t + L, 4);
					break;
				case 3: //(non )critical order is due
					if (case1) {
						totNonCritical++;
						if (stock > Sc) {
							filledNonCritical++;
							stock--;
						}else {
							nonCriticalBackorders++;
						}
					}else {
						totCritical++;
						if (stock > 0) {
							filledCritical++;
							stock--;
						}else {
							criticalBackorders++;
						}
					}
					
					break;
				case 4: //arrival of replenishment 
					if (criticalBackorders  > 0) {
						criticalBackorders--;
					}else if(stock < Sc) {
						stock++;
					}else if(nonCriticalBackorders > 0) {
						nonCriticalBackorders--;
					}else {
						stock++;
					}
					break;
			}
//			System.out.println(event.getKey() + " , " + event.getValue() + " , " + stock+ " , " + totCritical+ " , " + filledCritical + " , " +criticalBackorders + " , " + nonCriticalBackorders );

		}
		
		
//		System.out.println("The service level for the non critical class is " + F.round( (double) filledNonCritical/totNonCritical, 5) );
//		System.out.println("The service level for the  critical class is " + F.round((double) filledCritical/totCritical, 5) );
		return F.round((double) filledCritical/totCritical, 4);
	}

}
