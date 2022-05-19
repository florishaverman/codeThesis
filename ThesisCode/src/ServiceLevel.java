import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

/**
 * This class allows to calculate the exact non critical service level,
 * the approximation of the critical service level. 
 * This class also contains a simulation to calculate the service level of both classes
 * @author Floris Haverman 
 *
 *	(Interesting class)
 */
public class ServiceLevel {
	public static void main(String[] args) {
		
		int S, Sc, rate1, rate2;
		double T, L, Bc, Bn;
		boolean case1 = false;
		
		Random r =  new Random(1234);
		
		// set parameters for the modle
		Sc = 3;// Critical level
		S = 5; // Base stock level
		rate1 = 1;//Rate class 1
		rate2 = 4; //Rate class 2
		T= 0.1; //Demand lead time
		L = 0.5; // Supply lead time		

	
		System.out.println(F.round(getSimServiceLevelCritical(L, T, rate1, rate2, S, Sc, r, case1),4));

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
	
	/**
	 * Calculated the non critical service level
	 * @param L The system parameter for supply lead time
	 * @param T The system parameter for demand lead time
	 * @param rate1 The rate of class 1 customers
	 * @param rate2 The rate of class 2 customers
	 * @param S The order up to level
	 * @param Sc The critical level
	 * @return the non critical service level
	 */
	public static double getServiceLevelNonCritical(double L, double T, double rate1, double rate2, int S, int Sc) {
		double serviceLevel = 0;
		
		for (int i = 0; i < S - Sc; i++) {
			
			double temp = F.pdfPoission(i, (rate1 * L + rate2 * (L - T)));
//			System.out.println("For i "+ i + " the value is " + temp);
		
			serviceLevel += temp;
		}
		
		
		return serviceLevel;
	}
	
	/**
	 * Calculates the approximate service level of the critical class
	 * @param L The system parameter for supply lead time
	 * @param T The system parameter for demand lead time
	 * @param rate1 The rate of class 1 customers
	 * @param rate2 The rate of class 2 customers
	 * @param S The order up to level
	 * @param Sc The critical level
	 * @param case1 A boolean to indicate if class 1 is the critical class (true) of not (false)
	 * @return The service level of the critical class
	 */
	public static double getAproxServiceLevelCritical(double L, double T, double rate1, double rate2, int S, int Sc, boolean case1) {
		//Parameters
		//n is the number of partitions
		int n = 10000; 
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
	
	/**
	 * 
	 * @param L The system parameter for supply lead time
	 * @param T The system parameter for demand lead time
	 * @param rate1 The rate of class 1 customers
	 * @param rate2 The rate of class 2 customers
	 * @param S The order up to level
	 * @param Sc The critical level
	 * @param r A Random object used to create randomness
	 * @param case1 A boolean to indicate if class 1 is the critical class (true) of not (false)
	 * @return The service level of the critical class
	 */
	public static double getSimServiceLevelCritical(double L, double T, double rate1, double rate2, int S, int Sc, Random r, boolean case1) {
		
		/*
		 * The different events all have a number
		 * 
		 * 1: arrival of critical demand
		 * 2: arrival of non-critical demand
		 * 3: Due date of non-critical demand
		 * 4: arrival of replenishment
		 */
		
		//Used to store all the upcoming events
		TreeMap<Double, Integer> events = new TreeMap<>();
		
		//The number of time units to simulate.
		double timeHorizon = 1E+5; 
		
		//The there are high rates a smaller time horizon is considered, as more demand occurs in less time a shorter simulation suffices.
		if(rate1 > 10 && rate2 > 10 ) timeHorizon = timeHorizon/10;
		if(Math.abs(L-T)<0.0005 ) T += 0.001;

		
		//Initilization
		//Start with S stock and no back orders
		int criticalBackorders= 0;
		int nonCriticalBackorders= 0;
		int stock = S;
		
		//Performance calculation variables
		int totCritical = 0;
		int filledCritical = 0;
		
		int totNonCritical = 0;
		int filledNonCritical = 0;
		
		
		
		double t = 0;
		
		//Set the first critical and non critical demand
		events.put(t + F.getExponetialRandom(r, rate1), 1);
		events.put(t + F.getExponetialRandom(r, rate2), 2);

		boolean reset = false; // A boolean to keep track of if we are in the start up phase
		
		while (t < timeHorizon){
			//Reset the order count after a the start up fase
			if (t> 100 && !reset) {
				totCritical = 0;
				filledCritical = 0;
				
				totNonCritical = 0;
				filledNonCritical = 0;
				reset = true;
			}
			//Get the next event
			Entry<Double, Integer> event = events.pollFirstEntry();
			//Set the clock to the time of the event
			t = event.getKey();
			
			//Check which event happens
			switch(event.getValue()) {
				case 1: // order without DLT arrives (class 1)
					//If the order is critical (case1) it is satisfied if there is enough stock, and backordered otherwise
					if (case1) {
						totCritical++;
						if (stock == 0) {
							criticalBackorders ++;
						}else {
							filledCritical++;
							stock--;
						}
						
					}else { //If class 1 is the non critical class the order is satisfied if the stock level is above the critical level 
						totNonCritical++;
						if (stock <= Sc) {
							nonCriticalBackorders ++;
						}else { // And backordered if there the stock is lower than the critical level
							filledNonCritical++;
							stock--;
						}
					}
					//The next demand is simulated with an exponential random variable
					events.put(t + F.getExponetialRandom(r, rate1), 1);
					events.put(t + L, 4); //The replenishment order is scheduled.

					break;
				case 2: //Order with DLT arrives (class 2)
					
					events.put(t + T, 3); // Set the due date of the the demand
					//The next demand is simulated with an exponential random variable
					events.put(t + F.getExponetialRandom(r, rate2), 2);
					events.put(t + L, 4);//The replenishment order is scheduled.
					break;
				case 3: //Order with DLT needs to be filled (class 2)
					if (case1) { //If class 2 is not critical the stock level needs to be above the critical level for it to be satisfied. 
						totNonCritical++;
						if (stock > Sc) {
							filledNonCritical++;
							stock--;
						}else { //Otherwise it is backordered
							nonCriticalBackorders++;
						}
					}else { //If class 2 is critical the order is filled if there is stock
						totCritical++;
						if (stock > 0) {
							filledCritical++;
							stock--;
						}else { //If there is no stock it is backordered
							criticalBackorders++;
						}
					}
					
					break;
				case 4: //arrival of replenishment 
					//If there are critical backorders they are filled first
					if (criticalBackorders  > 0) {
						criticalBackorders--;
					}
					else if(stock < Sc) { 
						// If there are no critical backorders and the stock level is below the critical level
						// The stock level is increased
						stock++;
					}else if(nonCriticalBackorders > 0) {
						//If the stock level is at (or above) the critical level and there exist backorders are filled
						nonCriticalBackorders--;
					}else {
						//If there are no backorders the stock level is increased again.
						stock++;
					}
					break;
			}

		}
		
		//The service level is calculated
//		System.out.println("The service level for the non critical class is " + F.round( (double) filledNonCritical/totNonCritical, 5) );
//		System.out.println("The service level for the  critical class is " + F.round((double) filledCritical/totCritical, 5) );
		return F.round((double) filledCritical/totCritical, 4);
	}
	
	/**
	 * 
	 * @param L The system parameter for supply lead time
	 * @param T The system parameter for demand lead time
	 * @param rate1 The rate of class 1 customers
	 * @param rate2 The rate of class 2 customers
	 * @param S The order up to level
	 * @param Sc The critical level
	 * @param r A Random object used to create randomness
	 * @param case1 A boolean to indicate if class 1 is the critical class (true) of not (false)
	 * @param printBoth Has no function other then differentiating the two methods for which an extra parameter is needed
	 * @return The service level of the critical class
	 */	
public static double[] getSimServiceLevelCritical(double L, double T, double rate1, double rate2, int S, int Sc, Random r, boolean case1, boolean printBoth) {
		
		/*
		 * The different events all have a number
		 * 
		 * 1: arrival of critical demand
		 * 2: arrival of non-critical demand
		 * 3: Due date of non-critical demand
		 * 4: arrival of replenishment
		 */
		
		//Used to store all the upcoming events
		TreeMap<Double, Integer> events = new TreeMap<>();
		
		//The number of time units to simulate.
		double timeHorizon = 1E+5; 
		
		//The there are high rates a smaller time horizon is considered, as more demand occurs in less time a shorter simulation suffices.
		if(rate1 > 10 && rate2 > 10 ) timeHorizon = timeHorizon/10;
		if(Math.abs(L-T)<0.0005 ) T += 0.001;
		
		//Initilization
		//Start with S stock and no back orders
		int criticalBackorders= 0;
		int nonCriticalBackorders= 0;
		int stock = S;
		
		//Performance calculation variables
		int totCritical = 0;
		int filledCritical = 0;
		
		int totNonCritical = 0;
		int filledNonCritical = 0;
		
		
		
		double t = 0;
		
		//Set the first critical and non critical demand
		events.put(t + F.getExponetialRandom(r, rate1), 1);
		events.put(t + F.getExponetialRandom(r, rate2), 2);

		boolean reset = false; // A boolean to keep track of if we are in the start up phase
		
		while (t < timeHorizon){
			//Reset the order count after a the start up fase
			if (t> 100 && !reset) {
				totCritical = 0;
				filledCritical = 0;
				
				totNonCritical = 0;
				filledNonCritical = 0;
				reset = true;
			}
			//Get the next event
			Entry<Double, Integer> event = events.pollFirstEntry();
			//Set the clock to the time of the event
			t = event.getKey();
			
			//Check which event happens
			switch(event.getValue()) {
				case 1: // order without DLT arrives (class 1)
					//If the order is critical (case1) it is satisfied if there is enough stock, and backordered otherwise
					if (case1) {
						totCritical++;
						if (stock == 0) {
							criticalBackorders ++;
						}else {
							filledCritical++;
							stock--;
						}
						
					}else { //If class 1 is the non critical class the order is satisfied if the stock level is above the critical level 
						totNonCritical++;
						if (stock <= Sc) {
							nonCriticalBackorders ++;
						}else { // And backordered if there the stock is lower than the critical level
							filledNonCritical++;
							stock--;
						}
					}
					//The next demand is simulated with an exponential random variable
					events.put(t + F.getExponetialRandom(r, rate1), 1);
					events.put(t + L, 4); //The replenishment order is scheduled.

					break;
				case 2: //Order with DLT arrives (class 2)
					
					events.put(t + T, 3); // Set the due date of the the demand
					//The next demand is simulated with an exponential random variable
					events.put(t + F.getExponetialRandom(r, rate2), 2);
					events.put(t + L, 4);//The replenishment order is scheduled.
					break;
				case 3: //Order with DLT needs to be filled (class 2)
					if (case1) { //If class 2 is not critical the stock level needs to be above the critical level for it to be satisfied. 
						totNonCritical++;
						if (stock > Sc) {
							filledNonCritical++;
							stock--;
						}else { //Otherwise it is backordered
							nonCriticalBackorders++;
						}
					}else { //If class 2 is critical the order is filled if there is stock
						totCritical++;
						if (stock > 0) {
							filledCritical++;
							stock--;
						}else { //If there is no stock it is backordered
							criticalBackorders++;
						}
					}
					
					break;
				case 4: //arrival of replenishment 
					//If there are critical backorders they are filled first
					if (criticalBackorders  > 0) {
						criticalBackorders--;
					}
					else if(stock < Sc) { 
						// If there are no critical backorders and the stock level is below the critical level
						// The stock level is increased
						stock++;
					}else if(nonCriticalBackorders > 0) {
						//If the stock level is at (or above) the critical level and there exist backorders are filled
						nonCriticalBackorders--;
					}else {
						//If there are no backorders the stock level is increased again.
						stock++;
					}
					break;
			}

		}
		
		//The service level is calculated
//		System.out.println("The service level for the non critical class is " + F.round( (double) filledNonCritical/totNonCritical, 5) );
//		System.out.println("The service level for the  critical class is " + F.round((double) filledCritical/totCritical, 5) );
		return new double[] {F.round((double) filledCritical/totCritical, 4), F.round((double) filledNonCritical/totNonCritical, 4)} ;
	}
	
	
	
	
		
}
