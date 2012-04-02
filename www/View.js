var View = function(model, c) {
  var that = {};
  that.draw = function() {
      // Iterér over modellen og tegn...
      var roads = model.getRoads();
      for (var i = 0; i < roads.length; i++) {
        var r = roads[i];
        c.beginPath();
        c.moveTo((r.from.x - 442254.35659)/1000, ((r.from.y - 6049914.43018)/1000)*-1+350);
        c.lineTo((r.to.x - 442254.35659)/1000, ((r.to.y - 6049914.43018)/1000)*-1+350);
        c.lineWidth = 3;//r.width;
        c.strokeStyle = r.color;
        c.closePath();
        c.stroke();
      }
  };
  return that;
};