var Model = (function() {
	// The roads in the model.
	var redRoads = [];
    var blueRoads = [];
    var greenRoads = [];
    var blackRoads = [];

    var level = 0;

    // Get the canvas and the context
    var canvas = document.getElementById("canvas");

    function performRequest(url, callback) {
        // Initiate a XMLHttpRequest
        var req = new XMLHttpRequest();

        req.onreadystatechange = callback;
        req.open("GET", url, callback);
        req.send(null);
    }

    // Add a number of roads from the given XML-string
    return {
        HIGHWAY:        1,
        EXPRESSWAY:     2,
        PRIMARY_ROAD:   4,
        SECONDARY_ROAD: 8,
        MINOR_ROAD:    16,
        PATH:          32,
        SEAWAY:        64,
        LOCATION:     128,

        addRoads: function(xml) {
            //Clears the road arrays --> Just in case ;)
            redRoads = [];
            blueRoads = [];
            greenRoads = [];
            blackRoads = [];

            // parse XML til noget vi forstår...
            var roadIterator = xml.evaluate("//r", xml, null, XPathResult.ANY_TYPE, null);
            var i = xml.evaluate("//i", xml, null, XPathResult.ANY_TYPE, null);
            var t = xml.evaluate("//t", xml, null, XPathResult.ANY_TYPE, null);
            var fx = xml.evaluate("//fx", xml, null, XPathResult.ANY_TYPE, null);
            var fy = xml.evaluate("//fy", xml, null, XPathResult.ANY_TYPE, null);
            var tx = xml.evaluate("//tx", xml, null, XPathResult.ANY_TYPE, null);
            var ty = xml.evaluate("//ty", xml, null, XPathResult.ANY_TYPE, null);
            //var l = xml.evaluate("//l", xml, null, XPathResult.ANY_TYPE, null);
            var road = roadIterator.iterateNext();

            while (road) {
                function toNumber(e) {
                    return Number(e.iterateNext().childNodes[0].nodeValue);
                }

                var id = toNumber(i);
                var x1 = toNumber(fx);
                var y1 = toNumber(fy);
                var x2 = toNumber(tx);
                var y2 = toNumber(ty);
                //var l = toNumber(l);
                var type = toNumber(t);

                switch (type) {
                    case 1:
                    case 2:
                        redRoads.push({from:Vector(x1, y1), to:Vector(x2, y2)});
                        break;
                    case 4:
                    case 8:
                        blueRoads.push({from:Vector(x1, y1), to:Vector(x2, y2)});
                        break;
                    case 16:
                    case 32:
                        greenRoads.push({from:Vector(x1, y1), to:Vector(x2, y2)});
                        break;
                    case 64:
                    case 128:
                        blackRoads.push({from:Vector(x1, y1), to:Vector(x2, y2)});
                        break;
                }

                road = roadIterator.iterateNext();
            }
        },
        // Create a method to retreive the roads from the Model
        getRoads: function(color) {
            switch (color) {
                case "red":
                    return redRoads;
                    break;
                case "blue":
                    return blueRoads;
                    break;
                case "green":
                    return greenRoads;
                    break;
                case "black":
                    return blackRoads;
                    break;
            }
            return roads;
        },
        setFilterLevel: function(newLevel) {
            console.log(newLevel);

            if (true) {
                // Shows the loading.gif
                var loader = document.getElementById("loading");
                loader.style.display = "block";
                // Set the level of the model.
                level = newLevel;

                // Define callback
                function callback(e) {

                    // Initiate variables
                    var xml;

                    // Get the request-event
                    var req = e.currentTarget;

                    // Function to be called when result arrives
                    if (req.readyState == 4 && (req.status == 0 || req.status == 200)) {

                        // Get the DOMParser and parse the response-string
                        var parser = new DOMParser();
                        xml = parser.parseFromString(String(req.response), "text/xml");

                        // Add roads to the model
                        Model.addRoads(xml);

                        // Initiate the view
                        View.draw();

                        // Hide the loader.gif
                        loader.style.display = "none";
                    }
                }
                // Creates a vector from the findPos function.
                // The vector will contain vectors for x and y values.
                var tv = View.findPos(canvas);

                // Creates 2 new vectors from the previous vector.
                // This is the start and end coordinates for the window.
                var tv1 = tv.x;
                var tv2 = tv.y;

                // Create url
                var url = "xml?x1=" +tv1.x+ "&x2=" +tv2.x+ "&y1=" +tv1.y+ "&y2=" +tv2.y+ "&filter=" + level;
                console.log(url);

                // Execute request
                performRequest(url, callback);
            }
        }
    }
}());