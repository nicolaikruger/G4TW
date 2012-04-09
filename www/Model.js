var Model = (function() {
	// The roads in the model.
	var roads = [];

    var level = 0;

    // Get the canvas and the context
    var canvas = document.getElementById("canvas");

    function performRequest(url, callback) {
        // Initiate a XMLHttpRequest
        var req = new XMLHttpRequest();

        req.onreadystatechange = callback;

        req.open("GET", url, true);
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
            // parse XML til noget vi forstår...
            var roadIterator = xml.evaluate("//r", xml, null, XPathResult.ANY_TYPE, null);
            var i = xml.evaluate("//i", xml, null, XPathResult.ANY_TYPE, null);
            var s = xml.evaluate("//s", xml, null, XPathResult.ANY_TYPE, null);
            var fx = xml.evaluate("//fx", xml, null, XPathResult.ANY_TYPE, null);
            var fy = xml.evaluate("//fy", xml, null, XPathResult.ANY_TYPE, null);
            var tx = xml.evaluate("//tx", xml, null, XPathResult.ANY_TYPE, null);
            var ty = xml.evaluate("//ty", xml, null, XPathResult.ANY_TYPE, null);
            //var l = xml.evaluate("//l", xml, null, XPathResult.ANY_TYPE, null);
            var road = roadIterator.iterateNext();

            console.log(xml)

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
                var spd = toNumber(s);
                var red = Math.min(spd * 2, 255).toString(16);
                var c = "#" + (red < 16 ? 0 + red : red) + "2d4a"; // courtesy BBC
                var w = 1 + spd * 0.05;

                roads[id] = {speed: spd, from: Vector(x1,y1), to: Vector(x2,y2), color: c, width: w};

                road = roadIterator.iterateNext();
            }
        },
        // Create a method to retreive the roads from the Model
        getRoads: function() {
            return roads;
        },
        setFilterLevel: function(newLevel) {
            console.log(newLevel);

            function findPos(obj) {
                var curleft = curtop = 0;
                if (obj.offsetParent) {
                    do {
                        curleft += obj.offsetLeft;
                        curtop += obj.offsetTop;
                    } while (obj = obj.offsetParent)

                    return Vector(curleft, curtop);
                }
            }
            var v1 = findPos(canvas);
            var newV1 = Vector(v1.x, v1.y * -1)
            var v2 = Vector(newV1.x + 550, newV1.y - 500)
            var it = new Transform().invert();
            var tv1 = it.transformPoint(newV1);
            var tv2 = it.transformPoint(v2);

            if (level != newLevel) {
                // Set the level of the model.
                level = newLevel;

                // Create url
                var url = "xml?x1=" +tv1.x+ "&x2=" +tv2.x+ "&y1=" +tv1.y+ "&y2=" +tv2.y+ "&filter=" + level;

                // Define callback
                function callback(e) {
                    // Clear the model
                    roads = [];

                    // Initiate variables
                    var xml;

                    // Set the filter
                    var filter = 1;

                    // Get the request-event
                    var req = e.currentTarget;

                    // Function to be called when result arrives
                    if (req.readyState==4 && (req.status==0 || req.status==200)) {
                        // Get the DOMParser and parse the response-string
                        var parser = new DOMParser();
                        xml = parser.parseFromString(String(req.response), "text/xml");

                        // Add roads to the model
                        Model.addRoads(xml);

                        // Initiate the view
                        View.draw();
                    }
                }

                // Execute request
                performRequest(url, callback);
            }
        }
    }
}());