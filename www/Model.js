var Model = (function () {
    // The roads in the model.
    var roads = [];

    var level = 0;

    function performRequest(url, callback) {
        // Initiate a XMLHttpRequest
        var req = new XMLHttpRequest();

        req.onreadystatechange = callback;
        req.open("GET", url, callback);
        req.send(null);
    }


    // Add a number of roads from the given XML-string
    return {
        HIGHWAY:1,
        EXPRESSWAY:2,
        PRIMARY_ROAD:4,
        SECONDARY_ROAD:8,
        MINOR_ROAD:16,
        PATH:32,
        SEAWAY:64,
        LOCATION:128,

        addRoads:function (xml) {
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
                var color;
                var w

                switch (type) {
                    case 1:
                    case 2:
                        color = "#FF0000";
                        w = 3;
                        break;
                    case 4:
                    case 8:
                        color = "#0000FF";
                        w = 1.5;
                        break;
                    case 16:
                    case 32:
                        color = "#00FF00";
                        w = 1;
                        break;
                    case 64:
                    case 128:
                        color = "#000000";
                        w = 2;
                        break;
                }

                roads[id] = {from:Vector(x1, y1), to:Vector(x2, y2), color:color, width:w};

                road = roadIterator.iterateNext();
            }
        },
        // Create a method to retreive the roads from the Model
        getRoads:function () {
            return roads;
        },
        setFilterLevel:function (newLevel) {
            console.log(newLevel)
            if (level != newLevel) {
                var loader = document.getElementById("loading");
                loader.style.display = "block";
                // Set the level of the model.
                level = newLevel;

                // Create url
                var url = "xml?x1=0&x2=999999999&y1=0&y2=999999999&filter=" + level;

                // Define callback
                function callback(e) {

                    // Initiate variables
                    var xml;
                    // Get the canvas and the context
                    var canvas = document.getElementById("canvas");
                    // Set the filter
                    var filter = 1;

                    // Get the request-event
                    var req = e.currentTarget;

                    // Function to be called when result arrives
                    if (req.readyState == 4 && (req.status == 0 || req.status == 200)) {
                        // Clear the model
                        roads = [];

                        // Get the DOMParser and parse the response-string
                        var parser = new DOMParser();
                        xml = parser.parseFromString(String(req.response), "text/xml");

                        // Add roads to the model
                        Model.addRoads(xml);

                        // Initiate the view
                        View.draw();
                        loader.style.display = "none";
                    }
                }

                // Execute request
                performRequest(url, callback);
            }
        }
    }
}());