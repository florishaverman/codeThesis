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
		int rate1= 9;
		int rate2= 4;
		
		// Base stock level
		int S =rate1+4;
		
		// Critical level
		int Sc = 3;
		
		System.out.println("Hello world");
		
		boolean case1 = false;
		


		
//		System.out.println("The service level is");
//		System.out.println(ServiceLevel.getServiceLevelNonCritical(L, T, rate1, rate2, S, Sc));
		
//		System.out.println("The service level  for the critical class is");

//		System.out.println(ServiceLevel.getAproxServiceLevelCritical(L, T, rate1, rate2, S, Sc));

		Random r = new Random(1234);
		for (int i = 0; i < 100; i++) {
			//System.out.println(F.getExponetialRandom(r, 4));
		}
		
//		ServiceLevel.getSimServiceLevelCritical(L, T, rate1, rate2, S, Sc, r);
//		System.out.println( F.round(ServiceLevel.getSimServiceLevelCritical(L, T, rate1, rate2, S, Sc, r, case1),4));
		
		int ratec = 4;
		int raten = 1;
		
		S = 5;
		Sc = 2;
		rate1 = 2;
		rate2 = 2;
		
		System.out.println("This is the pdf poisson" + F.pdfPoission(0, 2.25));
		System.out.println("This is the pdf poisson" + F.pdfPoission(0, 2.5));


		
//		System.out.println(getServiceLevelNonCritical(2, 0.5, 5, 10, 31, 1));
		System.out.println(getAproxServiceLevelCritical(2, 0.5, 5, 10, 32, 11,true));
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println(getAproxServiceLevelCritical(2, 0.5, 5, 10, 32, 10,true));

			
		/*
//		L = 1;
//		T= 0.5;
		System.out.println(F.round(ServiceLevel.getServiceLevelNonCritical(L, T, rate2, rate1, S, Sc),4));
		System.out.println(F.round(ServiceLevel.getAproxServiceLevelCritical(L, T, ratec, raten, S, Sc, case1),4));
		System.out.println(F.round(ServiceLevel.getAproxServiceLevelCritical(L, T, raten, ratec, S, Sc, case1),4));
		System.out.println( F.round(ServiceLevel.getSimServiceLevelCritical(L, T, rate2, rate1, S, Sc, r, case1),4));
		
		Table.createLine(L, T, ratec, raten, S, Sc, r, case1);
		*/
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
			
			double temp = F.pdfPoission(i, (rate1 * L + rate2 * (L - T)));
//			System.out.println("For i "+ i + " the value is " + temp);
		
			serviceLevel += temp;
		}
		
		
		return serviceLevel;
	}
	
	
	public static double getAproxServiceLevelCritical(double L, double T, int rate1, int rate2, int S, int Sc, boolean case1) {
		//Parameters
		//n is the number of partitions
		int n = 1000; //TODO: increase to higher number
		
		double serviceLevel = 0;
		
		for (int i = 0; i < S - Sc; i++) {
		
			serviceLevel += F.pdfPoission(i, (rate1 * L + rate2 * (L - T)));
		}
		
		double partone = Function.IntSimpson(L, T, rate1, rate2, S, Sc, 0, (L - T), n, true, case1) ;
		double parttwo = Function.IntSimpson(L, T, rate1, rate2, S, Sc, (L - T), L,  n, false, case1);
//		System.out.println("service level "+ serviceLevel );
//		System.out.println("this is the first one "+ partone );
//		System.out.println("this is the second one "+ parttwo);
//		System.out.println("total "+ (partone + parttwo + serviceLevel));
		return serviceLevel + partone
					+ parttwo;
	}
	
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
				case 1: // order  without DLT arrives
					if (case1) {
						totCritical++;
						if (stock == 0) {
							criticalBackorders ++;
						}else {
							filledCritical++;
							stock--;
						}
						
					}else {
						totNonCritical++;
						if (stock <= Sc) {
							nonCriticalBackorders ++;
						}else {
							filledNonCritical++;
							stock--;
						}
					}
					events.put(t + F.getExponetialRandom(r, rate1), 1);
					events.put(t + L, 4);

					break;
				case 2: //Order with DLT arrives
					
					events.put(t + T, 3);
					events.put(t + F.getExponetialRandom(r, rate2), 2);
					events.put(t + L, 4);
					break;
				case 3: //Order with DLT needs to be filled
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
	
public static double[] getSimServiceLevelCritical(double L, double T, int rate1, int rate2, int S, int Sc, Random r, boolean case1, boolean printBoth) {
		
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
				case 1: // order  without DLT arrives
					if (case1) {
						totCritical++;
						if (stock == 0) {
							criticalBackorders ++;
						}else {
							filledCritical++;
							stock--;
						}
						
					}else {
						totNonCritical++;
						if (stock <= Sc) {
							nonCriticalBackorders ++;
						}else {
							filledNonCritical++;
							stock--;
						}
					}
					events.put(t + F.getExponetialRandom(r, rate1), 1);
					events.put(t + L, 4);

					break;
				case 2: //Order with DLT arrives
					
					events.put(t + T, 3);
					events.put(t + F.getExponetialRandom(r, rate2), 2);
					events.put(t + L, 4);
					break;
				case 3: //Order with DLT needs to be filled
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
		return new double[] {F.round((double) filledCritical/totCritical, 4), F.round((double) filledNonCritical/totNonCritical, 4)} ;
	}
	
	
	
	
		
}
