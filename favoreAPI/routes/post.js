var mongoose = require('mongoose');
var passport = require('passport');
var config = require('../config/database');
require('../config/passport')(passport);
var express = require('express');
var jwt = require('jsonwebtoken');
var router = express.Router();
var User = require("../models/user");
var Post = require('../models/post');

// Helper function for postimg, to upload image
function uploadImage(req,res,path){
  // Images available at imagepath/postId.jpg
  var img = req.files.file;
  if (!req.files){
    res.json({success:false,msg:"No image to upload!"});
    return false;
  }
  else {
  img.mv("public"+path,function(err){
    if (err){
      res.json({success:false,msg:"Failed to upload the file",error:err});
      return false;
    }
    else return true;
  });
}}

router.post('/postimg/',function(req,res,next){
  var path = "/images/"+Date.now()+"-"+req.files.file.name;
  var stat = uploadImage(req,res,path);
  if(stat==false){
    res.json({success:false,msg:"Timeout mate!",error:err});
    return;
  }
  var newPost = new Post({
    location:{type:"Point",coordinates:[req.body.x,req.body.y]},
    posterId:req.body.posterId,
    isImage:true,
    imagePath:path,
    originalPoster: req.body.poster,
    poster: req.body.poster,
    posterProfile: req.body.posterProfile,
  });
  newPost.save(function(err,post){
    if (err){
	     res.json({success:false,msg:"Unable to save the post"});
	     return;
    } else {
        User.findOne({id:req.body.posterId},function(err,usr){
          if (err){
              res.json({success:false,msg:"Some kind of error"});
	            return;
          } if (usr == null){
            res.json({success:false,msg:"User not found"});
            return;
          } else {
            var usr_posts = usr.posts;
            usr_posts.push(post.id);
            User.update({id:usr.id},{$set:{posts:usr_posts}},function(err,out){
              if (err){
                  res.json({success:false,msg:"Some kind of error"});
                  return;
              }
              else res.json({success:true, msg:"Successfully inserted a post mate!", post:suc});
            })
          }
        });
    }
  });
});

router.post('/posttext/',function(req,res,next){
    // Sending the post
    var x = Number(req.body.x);
    var y = Number(req.body.y);
    var newPost = new Post({
      postText:req.body.postText,
      location:{type:"Point", coordinates:[x,y]},
      posterId:req.body.posterId,
      originalPosterName: req.body.originalPosterName,
      posterName: req.body.posterName,
      posterProfile: req.body.posterProfile
    });
    newPost.save(function(err,post){
    if (err){
      res.json({success:false, 'err':err});
    }
    else{
        User.findOne({id:req.body.posterId},function(err,usr){
          if (err){
            res.json({success:false,msg:"Some kind of error"});
          } if (usr == null){
            res.json({success:false,msg:"User not found"})
          } else{
            var usr_posts = usr.posts;
            usr_posts.push(post.id);
            User.update({id:usr.id},{$set:{posts:usr_posts}},function(err,out){
              if (err) res.json({success:false,msg:"Some kind of error"});
              else res.json({success:true, msg:"Successfully inserted a post mate!", post:suc});
            });
          }
        });
    }
});
});


router.get("/favore/",function(req,res,next){
  var post_id = req.query.post_id;
  var poster_id = req.query.poster_id;
  var lon = req.query.lon;
  var lat = req.query.lat;
  // Find the particular post
  Post.findOne({id:post_id},function(err,pst){
    if (err){
      res.json({success:"False",msg:"Some kind of error"})
    }
    else{
      // Now find the user
      User.find({id:poster_id},function(err,usr){
        if (err){
          res.json({success:"False",msg:"Some kind of error"})
        }
        else if (pst == null || usr == null){
          res.json({success:"False",msg:"No such post"})
        }
        else {
          // Lets use the slowly increasing function
          var lspan = (60/(1+(24*pst.favors)));
          var post = new Post({
           postText:pst.postText,
           id:pst.id,
           location:pst.location,
           favors:pst.favors+1,
           datePosted:pst.date,
           lifespan:pst.lifespan+lspan,
           poster:usr.displayName,
           originalPoster: pst.poster,
           posterProfile: usr.profileImg,
          });
          // Update the original Post
          Post.update({"_id":post_id},post,function(err,result){
            if (err){
              res.json({success:false,msg:"some kind of error"})
             }else{
              res.json({success:true,res:result})
             }
          });
          // Insert the new guys post with new location and new poster address
          var post = new Post({
            postText:pst.postText,
            id:pst.id,
            location:{type:"Point",coordinates:[lat,lon]},
            favors:pst.favors+1,
            date:pst.date,
            poster:usr.dname,
            lifespan:pst.lifespan+lspan,
            originalPoster: pst.poster,
            posterProfile: usr.profileImg
          });
          // Now insert this new one
          post.save(function(err,res){
            if (err){
              res.json({'success':false,'err':'could not insert the favore post'});
              return;
            } else{
              res.json({'success':true,'msg':"Everything happened without a problem"});
            }
          })
        }
    });
    }
  });
});

router.get('/removePost/:post_id',function(req,res,next){
  /*
    usage:localhost:3000/<post+id>
    Removes the post and returns the ack
  */
  var post_id = req.params.post_id;
  Post.findOne({id:post_id},function(err,post){
    if (err){
      res.json({success:false,msg:"Some kind of error!"});
    }
    else if (post==null){
      res.json({success:false,msg:"No such post"});
    }
    else{
      var usr_id = post.posterId;
      User.findOne({id:usr_id},function(err,user){
        if (err){
          res.json({success:false,msg:"Some kind of error!"});
        }
        else if (post==null){
          res.json({success:false,msg:"No such user"});
        }
        else{
          var post_list = user.posts;
          post_list.splice(post_list.indexOf(post_id),1);
          User.update({id:user.id},{$set:{posts:post_list}},function(err,out){
            if (err){
              res.json({success:false,msg:"Some kind of error!"});
            }else{
              Post.remove({id:post_id},function(err,msg){
                if (err) throw err;
                else{
                  res.json({success:true,msg:msg});
                }
              })
            }
          })
        }
      });
    }
  });
});

module.exports = router;
