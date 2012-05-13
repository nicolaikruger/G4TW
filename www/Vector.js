var Vector = function(x, y) {

  return {
    x: x || 0,
    y: y || 0,

    add: function(v)      { return Vector(x + v.x, y + v.y); },
    divide: function(d)   { return Vector(x / d, y / d); },
    // Equals even with very minor differences
    // (if they are equal down to 6th decimal).
    equals: function(v)   { return x.toFixed(9) == v.x.toFixed(9) && y.toFixed(9) == v.y.toFixed(9); },
    length: function()    { return Math.sqrt(x * x + y * y); },
    multiply: function(s) { return Vector(x * s, y * s); },
    reverse: function()   { return Vector(-x, -y); },
    round: function(dec)  {
      if (typeof dec == 'number') {
        var log = Math.pow(10, dec);
        return Vector(Math.round(x * log) / log, Math.round(y * log) / log);
      } else {
        return Vector(Math.round(x), Math.round(y));
      }
    },
    subtract: function(v) { return Vector(x - v.x, y - v.y); },
    toString: function() {
      return "(" + x + "," + y + ")";
    }
  }

};