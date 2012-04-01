package dk.itu.kf04.g4tw.RoadKill;

import dk.itu.kf04.g4tw.model.map.Road;
import dk.itu.kf04.g4tw.util.DynamicArray;

public class FindMax
{
	public static double getMax(DynamicArray<Road> a) {
		double tmpMax = 0;
		double tmpL;
		for (int i = 0; i<a.length(); i++) {
			Road r = a.get(i);
			tmpL = r.getLength();
			if (tmpL > tmpMax) { tmpMax = tmpL; }
		}
		return tmpMax;
	}
}