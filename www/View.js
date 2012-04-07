var View = function(model, c) {
  var that = {};
  that.draw = function() {
      // Iterér over modellen og tegn...
      var roads = model.getRoads();
      var divisor = 850;
      var offset = 425;
      for (var i = 0; i < roads.length; i++) {
        var r = roads[i];
        c.beginPath();
        c.moveTo((r.from.x - 442254.35659)/divisor, ((r.from.y - 6049914.43018)/divisor)*-1+offset);
        c.lineTo((r.to.x - 442254.35659)/divisor, ((r.to.y - 6049914.43018)/divisor)*-1+offset);
        c.lineWidth = 1;//r.width;
        c.strokeStyle = r.color;
        c.closePath();
        c.stroke();
      }
  };
  return that;
};