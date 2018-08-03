var mongoose = require('mongoose');
var passport = require('passport');
var config = require('../config/database');
require('../config/passport')(passport);
var express = require('express');
var jwt = require('jsonwebtoken');
var router = express.Router();
var User = require("../models/user");
var Post = require('../models/post');
var Comment = require('../models/comment');

router.post('/writeComment/',function(req,res,next){
   // PARENT IS NULL FOR FIRST GENERATION COMMENT
   // THEY are equal to the parent comment id, if they are children
  var comment = new Comment({
    parent:req.body.parent,
    postId:req.body.postId,
    commentText:req.body.commentText,
    commenterId:req.body.commenterId,
    commenterName:req.body.commmterName,
  });
  comment.save(function(err,suc){
    if(err){
      res.json({'success':false,'err':err});
    }else{
      if (req.body.parent == null){
          res.json({'success':true,'msg':"Successfully posted the comment"});
      }
      Comment.findOne({id:req.body.parent},function(err,parcom){
        if (err){
          res.json({'success':false,'err':err});
          return;
        }
        parcom.childrenIncrement();
        res.json({'success':true,'msg':"Successfully posted the comment"});
      })
    }
  });
});

 // YOU CAN"T DELETE THE COMMENT, IT WON"T STAY FOR LONG THOUGH

router.post('/getComments/',function(req,res,next){
  //Must return comments at various levels
  Comment.find({postId:req.body.postId, parent:req.body.parent},function(err,coms){
    if(err){
      res.json({'success':false,'err':err});
    }else{
      res.json({'success':true,'comments':coms});
    }
  });
})


router.post('/likeComment/',function(req,res,next){
  // Update the liked list liked list
  var todo = req.body.todo;
  var id = req.body.posterId;
  Comment.findOne({id:req.body.id},function(err,com){
    if (err){
      res.json({'success':false, 'err':err});
    }else{
      com.likeButtonClicked(id);
      Comment.findByIdAndUpdate(req.body.id,com,{new:true},function(err,new_com){
        if (err){
          res.json({'success':false,'err':err});
        } else {
          res.json({'success':true,'msg':"Liked the post man"});
        }
      })
    }
  });
});

router.post('/dislike/',function(req,res,next){
  var todo = req.body.todo;
  var id = req.body.posterId;
  Comment.findOne({id:req.body.id},function(err,com){
    if (err){
      res.json({'success':false,'err':err});
    }else{
      com.dislikeButtonClicked(id);
      Comment.findByIdAndUpdate(req.body.id,com,{new:true},function(err,new_com){
        if (err){
          res.json({'success':false,'err':err});
        } else{
          res.json({'success':true,'msg':"Disliked the post man!"});
        }
      })
    }
  });
});



module.exports = router;
