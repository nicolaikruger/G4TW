var View = function(model, canvas) {
    // Get the canvas context
    var c = canvas.getContext("2d");

    // Create the transformation matrix
    var t    = new Transform();
    var pan  = Vector(-19750.8500543, -183901.14393240004);
    //var pan  = Vector(0, 0);
    var zoom = 0.03;

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
            t.scale(zoom);

            var roads = model.getRoads();
            var divisor = 850;
            var offset = 425;
            for (var i = 0; i < roads.length; i++) {
                var r = roads[i];
                c.beginPath();
                var from = t.transformPoint(r.from);
                var to = t.transformPoint(r.to);

                if(i == 0) console.log(from.x, from.y)

                c.moveTo(from.x, from.y);
                c.lineTo(to.x, to.y);
                c.lineWidth = 1;//r.width;
                c.strokeStyle = r.color;
                c.closePath();
                c.stroke();
            }

            c.restore();
        },
        pan: function(vector) {
            pan = pan.add(vector);
            this.draw();
        },
        zoom: function(delta, point) {
            var point = point || Vector(canvas.width / 2, canvas.height / 2);
            if ((zoom > 0.001 || delta > 0) && (zoom < 10 || delta < 0)) {
                var f = Math.pow(2, delta * 0.2);
                zoom *= f;
                pan = pan.subtract(point).multiply(f).add(point);
                this.draw();
            }
        }
    }
};