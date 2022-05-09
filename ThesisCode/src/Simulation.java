import java.util.Random;

public class Simulation {
	public static void main(String[] args) {
		System.out.println("Hello world");
		
		//Demand lead time
		double T = 0.1;
		
		// Supply lead time
		double L = 0.5;
		
		//Rate class 1 and class 2
		double rate1= 1;
		double rate2= 4;
		
		// Base stock level
		int S =5;
		
		// Critical level
		int Sc = 3;
		Random r = new Random();
		for (int i = 0; i < 100; i ++) {
			System.out.println(F.getPoissonRandom(r, rate2));
		}
		
	}
	
	public static void runSimulation() {
		
	}
	

}
