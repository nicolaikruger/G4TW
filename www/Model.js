var Model = (function() {
	// The roads in the model.
    var redRoads = [];
    var blueRoads = [];
    var greenRoads = [];
    var blackRoads = [];
    var path = [];

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

    function findViewDifference(startpoint, endpoint) {
        var pandifference = new Vector(endpoint.x.subtract(startpoint.x), endpoint.y.subtract(startpoint.y));
        pandifference.reverse();
        console.log(""+pandifference)
        return pandifference;
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

        /**
         * Adds a number of roads to the given array, by parsing the xml input.
         * @param xml
         * @param array  The array to insert roads into
         */
        addRoads: function(xml, array) {
            var roadIterator = xml.evaluate("//r", xml, null, XPathResult.ANY_TYPE, null);
            var i = xml.evaluate("//i", xml, null, XPathResult.ANY_TYPE, null);
            var t = xml.evaluate("//t", xml, null, XPathResult.ANY_TYPE, null);
            var fx = xml.evaluate("//fx", xml, null, XPathResult.ANY_TYPE, null);
            var fy = xml.evaluate("//fy", xml, null, XPathResult.ANY_TYPE, null);
            var tx = xml.evaluate("//tx", xml, null, XPathResult.ANY_TYPE, null);
            var ty = xml.evaluate("//ty", xml, null, XPathResult.ANY_TYPE, null);
            //var l = xml.evaluate("//l", xml, null, XPathResult.ANY_TYPE, null);
            var road = roadIterator.iterateNext();

            // Iterate over possible matches
            while (road) {
                /**
                 * Tries to parse a given input to a number.
                 * @param e  The element to parse.
                 */
                function toNumber(e) {
                    return Number(e.iterateNext().childNodes[0].nodeValue);
                }

                // Retrieve values
                var id = toNumber(i);
                var x1 = toNumber(fx);
                var y1 = toNumber(fy);
                var x2 = toNumber(tx);
                var y2 = toNumber(ty);

                // Add the road to the array
                var obj = {from:Vector(x1, y1), to:Vector(x2, y2)};
                // Hashing the id:
                var hash = id % 200000;
                array[hash] = obj;
//                array.push(obj);

                road = roadIterator.iterateNext();
            }
        },
        /**
         * Clears the model.
         */
        clearRoads: function() {
            //Clears the road arrays --> Just in case ;)
            redRoads = [];
            blueRoads = [];
            greenRoads = [];
            blackRoads = [];
        },

        /**
         * Clears the path array
         */
        clearPath: function() {
            path = [];
        },

        /**
         * Create a method to retrieve the roads from the Model
         * @param color  The type of roads to receive.
         */
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
                case "path":
                    return path;
                    break;
            }
        },
        /**
         * Requests for a set of roads within the rectangle defined by v1 and v2. The request
         * is split up for each
         */
        requestRoads: function(v1, v2, filter) {
          /**
           * Retrieves the right array from the given road type
           * @param level  The type of the array to retrieve
           */
          function getArrayFromFilter(type) {
            switch (type) {
                case 1:  case 2:
                    return redRoads; break;
                case 4:  case 8:
                    return blueRoads; break;
                case 16: case 32:
                    return greenRoads; break;
                case 64: case 128:
                    return blackRoads; break;
            }
          }

          // Shows the loading.gif
          var loader = document.getElementById("loading");
          loader.style.display = "block";

          // Creates the url
          var url = "xml?x1=" +v1.x+ "&x2=" +v2.x+ "&y1=" +v1.y+ "&y2=" +v2.y;

          // Create one request per filter-level
          for (var i = 1; i < filter; i *= 2) if ((i & filter) == i) {
          // We wrap the code itself inside a function to avoid future tampering with
          // the local variable n - which we needs!
            (function() {
              // Store i before it increments
              var n = i;

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
                  Model.addRoads(xml, getArrayFromFilter(n));

                  // Initiate the view
                  View.draw();

                  // Hide the loader.gif
                  loader.style.display = "none";
                }
              }

              // Execute request with the callback
              performRequest(url + "&filter=" + n, callback);
            }());
          }
        },
        setFilterLevel: function(newLevel, viewDefinition) {
            if (level != newLevel)
                var runRegular = true;
            // Set the level of the model.
            level = newLevel;

            // Creates the needed vectors.
            var tv1, tv2, diff;

            // Creates a vector with PI. This vector is used for the first call of method
            // to get the first regular request.
            var piVector = new Vector(Math.PI, Math.PI);

            if ((viewDefinition.x.x.equals(piVector)) || (runRegular == true)) {
                this.regularRequest(level);
            } else {
                // Creates the needed squares.
                var square1;
                var square2;

                // Sets up the viewDefinition for ease of use:
                var oldView = viewDefinition.x;
                var newView = viewDefinition.y;

                // Finds a vector representing the difference in two views after a pan
                var difference = findViewDifference(viewDefinition.x, viewDefinition.y);
                console.log(""+difference);

                // Saves the actual difference in a variable.
                // This is due to "difference" being a vector with two vectors as coordinates.
                diff = difference.x;
                // Finds delta x and delta y.
                var x = diff.x; var y = diff.y;
                // Calculates the width and height of the old view.
                var oldViewWidth = oldView.y.x - oldView.x.x; var oldViewHeight = oldView.x.y - oldView.y.y;

                if ((x > oldViewWidth) || (y > oldViewHeight) || (!difference.x.equals(difference.y))) {
                    // If the view pans out of either the x or y, or the view zooms,
                    // then a regular request will be run.
                    this.regularRequest(level);
                } else {
                    // Creates squares defined by vectors. The actual squares can be seen
                    // in our report. The squares are the whitespace.
                    if (x >= 0 && y >= 0) {
                        square1 = new Vector(newView.x, new Vector(newView.y.x, oldView.x.y));
                        square2 = new Vector(new Vector(oldView.y.x, oldView.x.y), newView.y);
                    } else if (x >= 0 && y < 0) {
                        square1 = new Vector(new Vector(newView.x.x, oldView.y.y), newView.y);
                        square2 = new Vector(new Vector(oldView.y.x, newView.x.y), new Vector(newView.y.x, oldView.y.y));
                    } else if (x < 0 && y >= 0) {
                        square1 = new Vector(newView.x, new Vector(newView.y.x, oldView.x.y));
                        square2 = new Vector(new Vector(newView.x.x, oldView.x.y), new Vector(oldView.x.x, newView.y.y));
                    } else {
                        square1 = new Vector(new Vector(newView.x.x, oldView.y.y), newView.y);
                        square2 = new Vector(newView.x, new Vector(oldView.x.x, oldView.y.y));
                    }

                    tv1 = square1.x;
                    tv2 = square1.y;
                    // Perform the request
                    this.requestRoads(tv1, tv2, level);

                    tv1 = square2.x;
                    tv2 = square2.y;
                    this.requestRoads(tv1, tv2, level);
                }
            }
        },
        regularRequest: function(newLevel) {
            console.log("Making regular request");
            level = newLevel;

            this.clearRoads();
            // Creates a vector from the findPos function.
            // The vector will contain vectors for x and y values.
            tv = View.findPos(canvas);

            // Creates 2 new vectors from the previous vector.
            // This is the start and end coordinates for the window.
            tv1 = tv.x;
            tv2 = tv.y;

            this.requestRoads(tv1, tv2, level);
        }
    }
}());