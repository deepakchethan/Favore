const mongoose = require('mongoose');
var Schema = mongoose.Schema;

const GeoSchema = new Schema();


var PostSchema = new Schema({
  postid:{
    type:Number,
  }
  text:{
    type:String,
    require: true
  },

  location: {
    type:{
      type:String,
      default:"Point"
    },
    coordinates:{
      type: [Number],
      index: "2dsphere"
    }
  },

  favors:{
    type:Number,
    default:0
  },
  age:{
    type:Number,
    default: 0
  },
  date:{
    type:Date,
    default:Date.now
  }
});

PostSchema.methods.computeAge = function(){
  var temp = Date.now().getTime() - this.date.getTime();
  this.age = Math.round(temp/(1000*60*60*24));
}

module.exports = mongoose.model('Post',PostSchema);
