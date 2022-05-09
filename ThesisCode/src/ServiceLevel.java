import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

public class ServiceLevel {
	public static void main(String[] args) {
		
		//Demand lead time
		double T = 0.1;
		
		// Supply lead time
		double L = 0.5;
		
		//Rate class 1 and class 2
		int rate1= 8;
		int rate2= 4;
		
		// Base stock level
		int S =12;
		
		// Critical level
		int Sc = 3;
		
		System.out.println("Hello world");
		
		

		
//		System.out.println("The service level is");
//		System.out.println(ServiceLevel.getServiceLevelNonCritical(L, T, rate1, rate2, S, Sc));
		
//		System.out.println("The service level  for the critical class is");

//		System.out.println(ServiceLevel.getAproxServiceLevelCritical(L, T, rate1, rate2, S, Sc));

		Random r = new Random(1234);
		for (int i = 0; i < 100; i++) {
			//System.out.println(F.getExponetialRandom(r, 4));
		}
		
		ServiceLevel.getSimServiceLevelCritical(L, T, rate1, rate2, S, Sc, r);
		
		
		/*
		System.out.println();
		System.out.println("Start of the list");

		for (rate1 = 1; rate1 < 13; rate1++) {
			S = rate1 + 4;
			System.out.print("The service level for the non-critical and critical class with rate 1: " +rate1+ ", rate 2: " +rate2+ ", S: " +S + ", Sc: " +Sc + "  ");
			System.out.print(F.round(ServiceLevel.getServiceLevelNonCritical(L, T, rate1, rate2, S, Sc),4));
			System.out.print(" , ");			
			System.out.println(F.round( ServiceLevel.getAproxServiceLevelCritical(L, T, rate1, rate2, S, Sc), 4));

		}
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
	
	public static double getSimServiceLevelCritical(double L, double T, int rate1, int rate2, int S, int Sc, Random r) {
		
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
		int timeHorizon = 100000;
		
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
					totCritical++;
					if (stock == 0) {
						criticalBackorders ++;
					}else {
						filledCritical++;
						stock--;
					}
					events.put(t + F.getExponetialRandom(r, rate1), 1);
					events.put(t + L, 4);
					break;
				case 2: //non critical order arrival
					events.put(t + F.getExponetialRandom(r, rate2), 2);
					events.put(t + L, 4);
					events.put(t + T, 3);
					break;
				case 3: //non critical order is due
					totNonCritical++;
					if (stock > Sc) {
						filledNonCritical++;
						stock--;
					}else {
						nonCriticalBackorders++;
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
