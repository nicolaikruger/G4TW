var View = (function() {
    // Get the canvas
    var canvas = document.getElementById('canvas');
    canvas.width = window.innerWidth;
    canvas.height = window.innerHeight;

    var drawRed = true;
    var drawBlue = true;
    var drawGreen = true;
    var drawBlack = true;
    var drawPath = true;

    // Get the canvas context
    var c = canvas.getContext("2d");

    // Create the transformation matrix
    var t    = new Transform();
    var pan  = new Vector(-300.856257136847, 6562.1620226675);
    var zoom = 0.001;

    // Create returning object
    return {
        // Adds an event-listener to the canvas
        addEventListener: function(type, f) {
            canvas.addEventListener(type, f);
        },

        // Change the "draw" booleans
        changeDrawState: function(e) {
            switch(e) {
                case "red":
                    drawRed = !drawRed;
                    break;
                case "blue":
                    drawBlue = !drawBlue;
                    break;
                case "green":
                    drawGreen = !drawGreen;
                    break;
                case "black":
                    drawBlack = !drawBlack;
                    break;
                case "path":
                    drawPath = !drawPath;
                    break;
            }
            View.draw();
        },

        // Iterate the model and draw the roads
        draw: function() {
            // Clear the canvas
            canvas.width = canvas.width;

            // Translate
            c.save();
            t.reset();
            t.translate(pan);
            t.scale(zoom, -zoom);

            if(drawBlack)
                drawFromArray(Model.getRoads("black"), "#000000", 2);

            if(drawGreen)
                drawFromArray(Model.getRoads("green"), "#ADD8E6", 1);

            if (drawBlue)
                drawFromArray(Model.getRoads("blue"), "#FFFF00", 3);

            if(drawRed)
                drawFromArray(Model.getRoads("red"), "#FFA500", 4);

            if(drawPath)
                drawFromArray(Model.getRoads("path"),"#FF0000", 6);

            c.restore();

        },
        pan: function(vector) {
            pan = pan.add(vector);
            this.draw();
        },
        resize: function() {
            canvas.width = window.innerWidth * 0.69;
            canvas.height = window.innerHeight - 2;
            View.draw();
        },
        zoom: function(delta, point) {
            var zoomPoint = point || new Vector(canvas.width / 2, canvas.height / 2);
            if ((zoom > 0.0001 || delta > 0) && (zoom < 3 || delta < 0)) {
                var f = Math.pow(2, delta * 0.5);
                zoom *= f;
                pan = pan.subtract(zoomPoint).multiply(f).add(zoomPoint);
                View.draw();
            }
        },
        findPos: function() {
            // Finds the coordinates of the canvas on the screen,
            // and creates two vectors to define the boundaries of the canvas.
            // v1 is the start point, v2 is the end point (start point + canvas dimensions).
            var v1 = new Vector(0,0);
            var v2 = new Vector(v1.x + canvas.width, v1.y + canvas.height);
//            console.log("V1: " + v1 + " x: " + v1.x + " y: " + v1.y + "  V2: " + v2 + " x: " + v2.x + " y: " + v2.y);

            // Sets up the transformation matrix.
            // The transformation matrix can translate map coordinates to screen coordinates.
            t.reset();
            t.translate(pan);
            t.scale(zoom, -zoom);
            // Creates an inverted transformation matrix.
            // The inverted transformation matrix can translate coordinates back to map coordinates.
            var it = t.invert();
            // Transforms the vectors to map vectors.
            var tv1 = it.transformPoint(v1);
            var tv2 = it.transformPoint(v2);

            // Returns a vector containing the two vectors.
            return new Vector(tv1, tv2);
        },
        getZoom: function() {
            return zoom;
        }
    };

    function drawFromArray(roads, color, width)
    {
        c.beginPath();
        for (var i = 1; i < roads.length; i++) {
            // Get the road
            var r = roads[i];

            if (r == undefined) continue;


            var from = t.transformPoint(r.from);
            var to = t.transformPoint(r.to);

            c.moveTo(from.x, from.y);
            c.lineTo(to.x, to.y);
        }

        c.lineWidth = width;
        c.strokeStyle = color;
        c.closePath();
        c.stroke();
    }
}());
