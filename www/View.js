var View = (function() {
    // Get the canvas
    var canvas = document.getElementById('canvas');
    canvas.width = window.innerWidth;
    canvas.height = window.innerHeight;

    // Get the canvas context
    var c = canvas.getContext("2d");

    // Create the transformation matrix
    var t    = new Transform();
    var pan  = Vector(-370.856257136847, 6462.1620226675);
    var zoom = 0.001;

    // Create returning object
    return {
        // Adds an event-listener to the canvas
        addEventListener: function(type, f) {
            canvas.addEventListener(type, f);
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

            drawFromArray(Model.getRoads("red"), "#FF0000", 3);
            drawFromArray(Model.getRoads("blue"), "#0000FF", 1.5);
            drawFromArray(Model.getRoads("green"), "#00FF00", 1);
            drawFromArray(Model.getRoads("black"), "#000000", 2);

            c.restore();

        },
        getZoom: function() {
            return zoom;
        },
        pan: function(vector) {
            pan = pan.add(vector);
            this.draw();
        },
        resize: function() {
            canvas.width = window.innerWidth;
            canvas.height = window.innerHeight;
            View.draw();
        },
        zoom: function(delta, point) {
            var point = point || Vector(canvas.width / 2, canvas.height / 2);
            if ((zoom > 0.0001 || delta > 0) && (zoom < 3 || delta < 0)) {
                var f = Math.pow(2, delta * 0.5);
                zoom *= f;
                pan = pan.subtract(point).multiply(f).add(point);
                View.draw();
            }
        },
        findPos: function(obj) {
            var curleft = curtop = 0;
            if (obj.offsetParent) {
                do {
                    curleft += obj.offsetLeft;
                    curtop += obj.offsetTop;
                } while (obj = obj.offsetParent)
            }

            var v1 = Vector(curleft, curtop);
            var v2 = Vector(v1.x + canvas.width, v1.y + canvas.height);
            console.log("V1: " + v1 + " x: " + v1.x + " y: " + v1.y + "  V2: " + v2 + " x: " + v2.x + " y: " + v2.y);

            t.reset();
            t.translate(pan);
            t.scale(zoom, -zoom);
            var it = t.invert();
            var tv1 = it.transformPoint(v1);
            var tv2 = it.transformPoint(v2);

            return Vector(tv1, tv2);
        }
    }

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
