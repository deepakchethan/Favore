const mongoose = require('mongoose');
const config = require('../config/database');
var Schema = mongoose.Schema;
var PostSchema = new Schema({

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
  isImage:{
    type:Boolean
  },
  imagePath:{
    type:String
  },
  id:{
    type:Schema.Types.ObjectId
  },
  postText:{
    type:String,
    require: true
  },
  posterId:{
    type:Number
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
PostSchema.methods.favored = function(){
  this.favors++;
}

PostSchema.methods.computeAge = function(){
  var temp = new Date((new Date).getTime() - this.date.getTime());
  // 3600 to convert to seconds
  // 1000 to convert milli sec to sec
  // Returns in minutes
  this.age = Math.round(temp/(60*1000));

}

module.exports = mongoose.model('Post',PostSchema);
