/*
  Schema file for user comments
*/
const mongoose = require('mongoose');
const config = require('../config/database');
const autoincrement = require('simple-mongoose-autoincrement');
var Schema = mongoose.Schema;

var CommentSchema = new Schema({
  id:{type:Schema.Types.ObjectId},
  postId:{type:Number,require:true},
  commentText:{type:String,require:true},
  // parent is null if it is the top level comment
  parent:{type:Number,require:true},
  commenterName:{type:String,require:true},
  childrenCount:{type:Number, default:0},
  // Posts will stay in a small area, so liked by is enough
  likedBy:{type:[Number],default:[]},
  dislikedBy:{type:[Number],default:[]},
});
// This method can be called to toggle if the comment has subcomment or not
CommentSchema.methods.childrenIncrement = function(){
  this.childrenCount++;
}

CommentSchema.methods.childrenDecrement = function(){
  this.childrenCount--;
}

CommentSchema.methods.likeButtonClicked = function(id){
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

CommentSchema.methods.dislikeButtonClicked = function(id){
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
module.exports = mongoose.model('Comment',CommentSchema);
