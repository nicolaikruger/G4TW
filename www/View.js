var View = (function() {
    // Get the canvas
    var canvas = document.getElementById('canvas');

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

            var roads = Model.getRoads();
            var divisor = 850;
            var offset = 425;
            var bool = true;
            for (var i = 1; i < roads.length; i++) {
                // Get the road
                var r = roads[i];

                if (r == undefined) continue;

                c.beginPath();
                var from = t.transformPoint(r.from);
                var to = t.transformPoint(r.to);

                c.moveTo(from.x, from.y);
                c.lineTo(to.x, to.y);
                c.lineWidth = 1;//r.width;
                c.strokeStyle = r.color;
                c.closePath();
                c.stroke();
            }

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
        }
    }
}());