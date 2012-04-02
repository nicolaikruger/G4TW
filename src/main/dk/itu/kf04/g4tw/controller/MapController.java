package dk.itu.kf04.g4tw.controller;

import dk.itu.kf04.g4tw.model.MapModel;

/**
 * Created with IntelliJ IDEA.
 * User: Krüger-Pony  **mwuahahah**
 * Date: 01-04-12
 * Time: 12:15
 * To change this template use File | Settings | File Templates.
 */
public class MapController {

	MapModel map = new MapModel();

	public String getXML(double xMin, double yMin, double xMax, double yMax, int... type)
	{
		return map.getXML(xMin, yMin, xMax, yMax, type);
	}
}
