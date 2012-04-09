var Controller = (function() {
    var isLeftMouseDown, startPoint;
    var canvas = document.getElementById('canvas');

    Model.setFilterLevel(Model.HIGHWAY + Model.SEAWAY);

    var getCoordinates = function(e) {
        var x = 0;
        var y = 0;
        if (!e) e = window.event;
        if (e.pageX || e.pageY) 	{
            x = e.pageX;
            y = e.pageY;
        }
        else if (e.clientX || e.clientY) 	{
        x = e.clientX + document.body.scrollLeft
          + document.documentElement.scrollLeft;
        y = e.clientY + document.body.scrollTop
          + document.documentElement.scrollTop;
        }

        // posx and posy contain the mouse position relative to the document
        // Do something with this information
        return Vector(x - 400, y - 50).round(3);
    };

    canvas.onmousedown = function(e) { isLeftMouseDown = true; startPoint = getCoordinates(e); };
    canvas.onmouseup   =  function(e) { isLeftMouseDown = false; };
    canvas.onmousemove = function(e) {
        if (isLeftMouseDown) {
            var coord = getCoordinates(e);
            var v = startPoint.subtract(coord);
            startPoint = startPoint.subtract(v);
            View.pan(v.reverse());
        }
    };
    // Zoom
    canvas.onmousewheel = function(e, d) {
        var coord = getCoordinates(e);
        View.zoom(e.wheelDelta/120, coord);

        getLevel(View.getZoom());
    };

    var getLevel = function(zoom) {
        console.log(zoom);
        // Set default value
        var filter = Model.HIGHWAY + Model.SEAWAY;
        switch (filter) {
            case filter > 100:
        }
        if (zoom > 0.002) filter += Model.PRIMARY_ROAD + Model.EXPRESSWAY;
        if (zoom > 0.004) filter += Model.SECONDARY_ROAD;
        if (zoom > 0.01) filter += Model.MINOR_ROAD;

        Model.setFilterLevel(filter);
    }

    // Return empty object
    return {};
}());