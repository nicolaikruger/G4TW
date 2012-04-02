var View = function(model, c) {
  var that = {};
  that.draw = function() {
      // Iterér over modellen og tegn...
      var roads = model.getRoads();
      for (var i = 0; i < roads.length; i++) {
        var r = roads[i];
        c.beginPath();
        c.moveTo(r.from.x, r.from.y);
        c.lineTo(r.to.x, r.to.y);
        c.lineWidth = r.width;
        c.strokeStyle = r.color;
        c.closePath();
        c.stroke();
      }
  };
  return that;
};