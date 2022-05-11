import java.util.Random;
import java.util.TreeMap;
import java.util.Map.Entry;


/**
 * This class is used to temporarily store a backup version of a method if a structural change is made.
 * @author Floris Haverman 
 *
 *	(Not an interesting class)
 */
public class Backup {
	
	
	/**  METHODS THAT ARE NOT USED*/ 
	

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

}
