var View = (function() {
    // Get the canvas
    var canvas = document.getElementById('canvas');

    // Get the canvas context
    var c = canvas.getContext("2d");

    // Create the transformation matrix
    var t    = new Transform();
    var pan  = Vector(-370.856257136847, 6462.1620226675);
    //var pan  = Vector(0, 0);
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
            for (var i = 1; i < roads.length; i++) {
                // Get the road
                var r = roads[i];

                if (r == undefined) continue;

                c.beginPath();
                var from = t.transformPoint(r.from);
                var to = t.transformPoint(r.to);

                if(i == 0) console.log(zoom, from.x, from.y)

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
        },
        zoom: function(delta, point) {
            var point = point || Vector(canvas.width / 2, canvas.height / 2);
            if ((zoom > 0.001 || delta > 0) && (zoom < 3 || delta < 0)) {
                var f = Math.pow(2, delta * 0.2);
                zoom *= f;
                console.log(pan.x, pan.y, point.x, point.y)
                pan = pan.subtract(point).multiply(f).add(point);
                this.draw();
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
            t.reset();
            t.translate(pan);
            t.scale(zoom, -zoom);
            var it = t.invert();
            var tv1 = it.transformPoint(v1);
            console.log("v1: " + v1.x, v1.y, tv1.x, tv1.y);
            var tv2 = it.transformPoint(v2);
            console.log("v2: " + v2.x, v2.y, tv2.x, tv2.y);

            return Vector(tv1, tv2);
        }
    }
}());