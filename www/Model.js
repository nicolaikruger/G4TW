var Model = function(xml) {
	// Instantiate new object to be returned (ensures encapsulation)
	var that = {};
	// The roads in the model.
	var roads = [];

	// parse XML til noget vi forstår...
	var roadIterator = xml.evaluate("//r", xml, null, XPathResult.ANY_TYPE, null);
	var fx = xml.evaluate("//fx", xml, null, XPathResult.ANY_TYPE, null);
	var ty = xml.evaluate("//ty", xml, null, XPathResult.ANY_TYPE, null);
	var tx = xml.evaluate("//tx", xml, null, XPathResult.ANY_TYPE, null);
	var ty = xml.evaluate("//ty", xml, null, XPathResult.ANY_TYPE, null);
	var s = xml.evaluate("//s", xml, null, XPathResult.ANY_TYPE, null);
	var l = xml.evaluate("//l", xml, null, XPathResult.ANY_TYPE, null);
	var road = roadIterator.iterateNext();
	var i = 0;

	console.log(roadIterator);

	while (road) {
		var f = road.getElementsByTagName("fNode")[0];

		function toNumber(e, name) {
			return Number(e.getElementsByTagName(name)[0].childNodes[0].nodeValue);
		}

		var t = road.getElementsByTagName("tNode")[0];
		var x1 = toNumber(road, "fx");
		var y1 = toNumber(road, "fy");
		var x2 = toNumber(road, "tx");
		var y2 = toNumber(road, "ty");
		var l = toNumber(road, "l");
		var spd = toNumber(road, "speed");
		var red = Math.min(spd * 2, 255).toString(16);
		var c = "#" + (red < 16 ? 0 + red : red) + "2d4a"; // courtesy BBC
		var w = 1 + spd * 0.05;

		roads.push({speed: spd, from: {x:x1,y:y1}, to: {x:x2,y:y2}, color: c, width: w});
		road = roadIterator.iterateNext();
	}

	// Create a method to retreive the roads from the Model
	that.getRoads = function() {
		return roads;
	};

	// Return
	return that;
};