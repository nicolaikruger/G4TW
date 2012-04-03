var Model = function(xml) {
	// Instantiate new object to be returned (ensures encapsulation)
	var that = {};
	// The roads in the model.
	var roads = [];

	// parse XML til noget vi forstår...
	var roadIterator = xml.evaluate("//r", xml, null, XPathResult.ANY_TYPE, null);
    var s = xml.evaluate("//s", xml, null, XPathResult.ANY_TYPE, null);
    var fx = xml.evaluate("//fx", xml, null, XPathResult.ANY_TYPE, null);
	var fy = xml.evaluate("//fy", xml, null, XPathResult.ANY_TYPE, null);
	var tx = xml.evaluate("//tx", xml, null, XPathResult.ANY_TYPE, null);
	var ty = xml.evaluate("//ty", xml, null, XPathResult.ANY_TYPE, null);
	//var l = xml.evaluate("//l", xml, null, XPathResult.ANY_TYPE, null);
	var road = roadIterator.iterateNext();

	console.log(road);

	while (road) {

		function toNumber(e) {
            return Number(e.iterateNext().childNodes[0].nodeValue);
        }

		var x1 = toNumber(fx);
		var y1 = toNumber(fy);
		var x2 = toNumber(tx);
		var y2 = toNumber(ty);
		//var l = toNumber(l);
		var spd = toNumber(s);
		var red = Math.min(spd * 2, 255).toString(16);
		var c = "#" + (red < 16 ? 0 + red : red) + "2d4a"; // courtesy BBC
		var w = 1 + spd * 0.05;

		roads.push({speed: spd, from: {x:x1,y:y1}, to: {x:x2,y:y2}, color: c, width: w});

		road = roadIterator.iterateNext();
	}
    console.log(roads)

	// Create a method to retreive the roads from the Model
	that.getRoads = function() {
		return roads;
	};

	// Return
	return that;
};