var Controller = (function() {
    var isLeftMouseDown, startPoint;
    var canvas = document.getElementById('canvas');

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
            view.pan(v.reverse());
        }
    };
    // Zoom
    canvas.onmousewheel = function(e, d) {
        var coord = getCoordinates(e);
        view.zoom(e.wheelDelta/120, coord);
    };

    // Return empty object
    return {};
}());