import java.util.Random;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This class is used to obtain the simulated and approximated critical and exact non-critical service level.
 * It also includes the optimization algorithm to optimize the parameters S and S_c. 
 * @author Floris Haverman
 * 
 * (interesting class)
 *
 */
public class Extension {
	/**
	 * This is the main method of the class. 
	 * In this does not have to be used as all methods can be called from outside this class,
	 * so this is mainly for testing.
	 * 
	 * Left at some random testing settings.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Hello world");
		int S, Sc;
		double T, L, Bc, Bn, p, rate1, rate2;
		boolean case1 = false;
		
		Random r =  new Random(1234);
		
		// set parameters for the modle
		Sc = 3;// Critical level
		S = 14; // Base stock level
		rate1 = 10;//Rate class 1
		rate2 = 4; //Rate class 2
		T= 0.1; //Demand lead time
		L = 0.5; // Supply lead time
		p = 0.5; //probability a class to demand has dlt of T.
		
		//object used to pass on system parameters. 
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("L", L);
		parameters.put("T", T);
		parameters.put("rate1", rate1);
		parameters.put("rate2", rate2);
		parameters.put("S", S);
		parameters.put("Sc", Sc);
		parameters.put("p", p);

		parameters.put("r", r);
		parameters.put("case1", true);
		
		//update the rates to match original case
		double rate1new = rate1 + (1-p) * rate2;
		double rate2new = p* rate2;
		parameters.put("rate1", rate1new);
		parameters.put("rate2", rate2new);
		
//		System.out.println(Extension.getServiceLevelNonCritical(parameters));
//		F.round(Extension.getSimServiceLevelCritical(L, T, rate1, rate2, S, Sc, r, case1, p),4);
//		System.out.println(F.round(Extension.getSimServiceLevelCritical(L, T, rate2, rate1, S, Sc, r, case1, p),4));

		System.out.println(ServiceLevel.getAproxServiceLevelCritical(L, T, rate1new, rate2new, S, Sc, case1));

		System.out.println(Extension.getAproxServiceLevelCritical(L, T, rate1, rate2, S, Sc, case1,p));

	}
	
	
	/**
	 * This method is used to calculate the exact non-critical service level for the exended model. 
	 * @param L The system parameter for supply lead time
	 * @param T The system parameter for demand lead time
	 * @param rate1 The rate of class 1 customers
	 * @param rate2 The rate of class 2 customers
	 * @param S The order up to level
	 * @param Sc The critical level
	 * @param p The probabilty class 2 has DLT of T.
	 * @return the non critical service level
	 */
	public static double  getServiceLevelNonCritical(double L, double T, double rate1, double rate2, int S, int Sc, double p) {
		double serviceLevel = 0;
		
		//update the rates to match original case
		double rate1new = rate1 + (1-p) * rate2;
		double rate2new = p* rate2;
		
		
		for (int i = 0; i < S - Sc; i++) {
			double temp = F.pdfPoission(i, rate1new * L +  rate2new * (L - T) );		
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
	 * @param p The probabilty class 2 has DLT of T.
	 * @return The service level of the critical class
	 */
	public static double getAproxServiceLevelCritical(double L, double T, double rate1, double rate2, int S, int Sc, boolean case1, double p) {
		//Parameters
		//n is the number of partitions to numerically calculate each integral
		int n = 10000; 
		double serviceLevel = 0;
		
		
		//The third part is just the non-critical service level
		for (int i = 0; i < S - Sc; i++) {
			double temp = F.pdfPoission(i, (rate1 + (1-p) * rate2) * L +  p * rate2 * (L - T) );		
			serviceLevel += temp;
		}
		
		//Part one and Part two are the first and second integral needed to calculate the approximation.
		double partone = IntegralsExtension.IntSimpson(L, T, rate1, rate2, S, Sc, 0, (L - T), n, true, case1, p) ;
		double parttwo = IntegralsExtension.IntSimpson(L, T, rate1, rate2, S, Sc, (L - T), L,  n, false, case1, p);

		//return the approximation of the critical service level.
		return serviceLevel + partone
					+ parttwo;
	}

	/**
	 * This methods calculates the simulated critical service level
	 * 
	 * Note that, we use in initialization period to reduce the effect of the initialization.
	 *  
	 * @param L The system parameter for supply lead time
	 * @param T The system parameter for demand lead time
	 * @param rate1 The rate of class 1 customers
	 * @param rate2 The rate of class 2 customers
	 * @param S The order up to level
	 * @param Sc The critical level
	 * @param r A Random object used to create randomness
	 * @param case1 A boolean to indicate if class 1 is the critical class (true) of not (false)
	 * @param p The probabilty class 2 has DLT of T.
	 * @return The service level of the critical class
	 */	
	public static double getSimServiceLevelCritical(double L, double T, double rate1, double rate2, int S, int Sc, Random r, boolean case1, double p) {
		
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
		
		//If there are high rates, a smaller time horizon is considered, as more demand occurs in less time a shorter simulation suffices. Helps to improve run time.
		if(rate1 > 10 && rate2 > 10 ) timeHorizon = timeHorizon/10;
		if(Math.abs(L-T)<0.0005 ) T += 0.001; // This is done due to an error is L = T.

		
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
			//Reset the order count after a the start up period
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
					
					if (r.nextDouble()<= p) {
						events.put(t + T, 3); // Set the due date of the the demand
					}else {
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
					}
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
	public static double[] runSimulationBrute(double L, double T, double rate1, double rate2, double Bc, double Bn, Random r, boolean case1, boolean minimiseSc, double p) {
		//Get Smin to limit the feasible region
		int Smin = Extension.getSmin(L, T, rate1, rate2, Bc, Bn,  p);
		
		//Start searching for the small values of S and increase until you find parameters that yield the required service level
		for (int i= Smin; i < 100; i++) {
			
			if (minimiseSc) { //Search for the smallest S with also the smallest Sc, thus maximizing non critical service level
				for (int j = 0; j <= i - Smin ; j++) {
//					System.out.println("start simulation");
					double slCrit = Extension.getSimServiceLevelCritical(L, T, rate1, rate2, i, j, r, case1, p);
					double slNonCrit = Extension.getServiceLevelNonCritical(L, T, rate1, rate2, i, j, p);

//					System.out.println("end simulation with i " + i + " and j "+ j);

//					System.out.println("Sl for crit "+ sl[0] + " sl for non crit "+ sl[1] + "with S "+ i + " and Sc "+ j);
					if (slCrit >= Bc && slNonCrit >= Bn) {
						return new double [] {i,j};
					}
				}
			} else { //Search for the smallest S with the largest Sc, thus maximizing the critical service level
				for (int j = i - 1; j > 0; j--) {
					double  slCrit = Extension.getSimServiceLevelCritical(L, T, rate1, rate2, i, j, r, case1, p);					double slNonCrit = Extension.getServiceLevelNonCritical(L, T, rate1, rate2, i, j, p);
					double 	slNonCrit1 = Extension.getServiceLevelNonCritical(L, T, rate1, rate2, i, j, p);

//					System.out.println("Sl for crit "+ sl[0] + " sl for non crit "+ sl[1] + "with S "+ i + " and Sc "+ j);
					if (slCrit >= Bc && slNonCrit1 >= Bn) {
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
	public static double[] getOptimalForAprox(double L, double T, double rate1, double rate2, double Bc, double Bn, Random r, boolean case1, boolean minimiseSc, double p) {
		int Smin = Extension.getSmin(L, T, rate1, rate2, Bc, Bn,p);
		
		for (int i= Smin; i < 50; i++) {
			if (minimiseSc) {
				for (int j = 0; j <= i - Smin ; j++) {
					double  slCrit = Extension.getAproxServiceLevelCritical(L, T, rate1, rate2, i, j, case1, p);
					double  slNonCrit = Extension.getServiceLevelNonCritical(L, T, rate1, rate2, i, j, p);
					double[] sl = new double[] {slCrit, slNonCrit};
//					System.out.println("Sl for crit "+ sl[0] + " sl for non crit "+ sl[1] + " with S "+ i + " and Sc "+ j);
					if (sl[0] >= Bc && sl[1] >= Bn) {
						return new double [] {i,j};
					}
				}
			}else {
				for (int j = i - 1; j >= 0; j--) {
					double  slCrit = Extension.getAproxServiceLevelCritical(L, T, rate1, rate2, i, j, case1, p);
					double  slNonCrit = Extension.getServiceLevelNonCritical(L, T, rate1, rate2, i, j, p);
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
	public static double getOptimalForNoRat(double L, double T, double rate1, double rate2, double Bc, double Bn, Random r, boolean case1, double p) {
		
		//In this case the service levels of both classes are the same, so we can use the exact one which is fastest and exact. 
		//Find the smallest S such that the service level requirements are met
		for (int i= Extension.getSmin(L, T, rate1, rate2, Bc, Bn, p); i < 100; i++) {
			//Note the 0 in the parameters. This means that the S_c is restricted to 0, hence no rationing.
			double sl =  Extension.getServiceLevelNonCritical(L, T, rate1, rate2, i, 0, p);
			if (sl > Bc) return i;
		}
		
		return 0;
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
	public static int getSmin(double L, double T, double rate1, double rate2, double Bc, double Bn, double p) {
		
		for (int Smin= 1; Smin < 100; Smin++) {
			if (Extension.getServiceLevelNonCritical(L, T, rate1, rate2, Smin, 0, p) >= Bn) {
				return Smin;
			}
		}
		return 1;
	}
}
