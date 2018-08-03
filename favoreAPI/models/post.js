const mongoose = require('mongoose');
const config = require('../config/database');
const autoincrement = require('simple-mongoose-autoincrement')
var Schema = mongoose.Schema;
var PostSchema = new Schema({
  originalPosterName:{ type:String, require: true },
  posterName:{ type:String,require: true },
  posterProfile:{ type:String, require: true},
  location: {
     type: { type:String, default:"Point"},
     coordinates:{ type: [Number], index: "2dsphere"}
  },
  isImage: { type:Boolean, default:false},
  imagePath:{ type:String, default:"" },
  id:{ type:Schema.Types.ObjectId },
  postText:{ type:String, require: true },
  posterId:{ type:Number, require: true },
  favors:{ type:Number, default:0 },
  // At each hour, we calculate the age and compare with lifespan to decide if we want to delete a post
  lifespan:{type:Number, default: 24*60*60},
  datePosted:{ type:Date, default:Date.now},
  likedBy:{type:Number, default:[]},
  favoredBy:{type:Number,default:[]},
  dislikedBy:{type:Number,default:[]}
});

PostSchema.methods.favored = function(){
  this.favors++;
}

PostSchema.methods.favoreButtonClicked = function(id){
  // favored already, so will unfavore it
  if (this.favoredBy.indexOf(id) > -1){
      this.favoredBy = this.favoredBy.splice(this.favoredBy.indexOf(id),1);
  }else{
    this.favoredBy.push(id);
  }
}

PostSchema.methods.likeButtonClicked = function(id){
  // disliked, now if you like it. we remove it form disliked list
  if (this.dislikedBy.indexOf(id)>-1){
    this.dislikedBy = this.dislikedBy.splice(this.dislikedBy.indexOf(id),1);
  }
  // Liked already, so will unlike it
  if (this.likedBy.indexOf(id) > -1){
      this.likedBy = this.likedBy.splice(this.likedBy.indexOf(id),1);
  }else{
    this.likedBy.push(id);
  }
}

PostSchema.methods.dislikeButtonClicked = function(id){
  if (this.likedBy.indexOf(id)>-1){
    this.likedBy = this.likedBy.splice(this.likedBy.indexOf(id),1);
  }
  // Liked already, so will unlike it
  if (this.dislikedBy.indexOf(id) > -1){
      this.dislikedBy = this.dislikedBy.splice(this.dislikedBy.indexOf(id),1);
  }else{
    this.dislikedBy.push(id);
  }
}


mongoose.plugin(autoincrement,{field:'id'});
module.exports = mongoose.model('Post',PostSchema);
