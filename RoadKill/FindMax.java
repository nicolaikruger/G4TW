public class FindMax
{
	public static double getMax(DynamicArray<Road> a) {
		double tmpMax = 0;
		double tmpL = 0;
		for (int i = 0; i<a.length(); i++) {
			Road r = a.get(i);
			tmpL = r.getLength();
			if (tmpL > tmpMax) { tmpMax = tmpL; }
		}
		return tmpMax;
	}
}